// fill in your values
String elk_ip = '' // <IP-of-your-ELK>
String elk_reconfigure_port = '' // <reconfigure-port-to-your-ELK>

// do not change anything below here
if (elk_ip.isEmpty() || elk_reconfigure_port.isEmpty()) {
    return
}

env = System.getenv()
String project_name = env['NAMESPACE']

String github_url = 'github.developer.allianz.io'
String github_org = 'AgileDeliveryPlatform'
String tools_repo_name = 'logging-config-tools'
String openshift_secret_name = 'elk-key'
String credentialsId = project_name + '-' + openshift_secret_name
String config_path = "logging"
String relative_config_path = "../${config_path}"

Boolean logging_auth_required = true

String prefix = 'ADP Logging'
String prefix_id = prefix.toLowerCase().replace(' ', '_')

listView(prefix) {
    description('Administrative Jobs to reconfigure and restart Logging')
    jobs {
        regex("${prefix_id}_.+")
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}


job("${prefix_id}_execute_curator") {
    displayName(prefix + ' - execute curator')

    label('ansible')

    parameters {
        stringParam('CURATOR_PERIOD', '5', 'Log retention period in days.')
    }

    scm {
        git {
            remote {
                url(env['CONFIGURATION_REPOSITORY_URL'])
                credentials('git-token-credentials')
            }
            branch(env['CONFIGURATION_REPOSITORY_BRANCH'])
        }
    }

    wrappers {
        preBuildCleanup()
        sshAgent(credentialsId)
        credentialsBinding {
            usernamePassword('git_username', 'git_password', 'git-token-credentials')
        }
    }

    steps {
        shell(
"""rm --recursive --force ${tools_repo_name}
git clone https://\${git_username}:\${git_password}@${github_url}/${github_org}/${tools_repo_name}""")
        shell("./${tools_repo_name}/run.sh execute-curator ${elk_ip} ${elk_reconfigure_port} ${relative_config_path} \${CURATOR_PERIOD}")
    }
}

job("${prefix_id}_reconfigure_curator") {
    displayName("${prefix} - reconfigure curator")

    label('ansible')

    parameters {
        stringParam('CURATOR_PERIOD', '30', 'Log retention period in days. By default the curator will be executed every day at 23:30 removing logs older than 30 days but this period can be changed.')
    }

    scm {
        git {
            remote {
                url(env['CONFIGURATION_REPOSITORY_URL'])
                credentials('git-token-credentials')
            }
            branch(env['CONFIGURATION_REPOSITORY_BRANCH'])
            extensions {
                pathRestriction {
                    includedRegions("${config_path}/curator/.*")
                    excludedRegions('')
                }
            }
        }
    }

    triggers {
        githubPush()
    }

    wrappers {
        preBuildCleanup()
        sshAgent(credentialsId)
        credentialsBinding {
            usernamePassword('git_username', 'git_password', 'git-token-credentials')
        }
    }

    steps {
        shell(
"""rm --recursive --force ${tools_repo_name}
git clone https://\${git_username}:\${git_password}@${github_url}/${github_org}/${tools_repo_name}""")
        shell("./${tools_repo_name}/run.sh reconfigure-curator ${elk_ip} ${elk_reconfigure_port} ${relative_config_path} \${CURATOR_PERIOD}")
    }
}


['Logstash', 'Elasticsearch', 'Kibana', 'Elastalert', 'Opendistro', 'APM-Server'].each {
    def service_display_name = it
    def service_name = service_display_name.toLowerCase()

    def id = "${prefix_id}_rebuild_and_restart_${service_name}"
    def display_name = "${prefix} - rebuild configuration and restart ${service_display_name}"
    if (service_display_name == 'Opendistro') {
        id = "${prefix_id}_reload_${service_name}"
        display_name = "${prefix} - reload ${service_display_name} configuration"
    }
    job(id) {
        displayName(display_name)

        label('ansible')

        scm {
            git {
                remote {
                    url(env['CONFIGURATION_REPOSITORY_URL'])
                    credentials('git-token-credentials')
                }
                branch(env['CONFIGURATION_REPOSITORY_BRANCH'])
            }
        }

        wrappers {
            preBuildCleanup()
            sshAgent(credentialsId)
            credentialsBinding {
                usernamePassword('git_username', 'git_password', 'git-token-credentials')
                if (logging_auth_required) {
                    usernamePassword('LOGGING_USERNAME', 'LOGGING_PASSWORD', project_name + '-logging')
                }
            }
        }

        steps {
            shell(
"""rm --recursive --force ${tools_repo_name}
git clone https://\${git_username}:\${git_password}@${github_url}/${github_org}/${tools_repo_name}""")
            shell("./${tools_repo_name}/run.sh reconfigure-${service_name} ${elk_ip} ${elk_reconfigure_port} ${relative_config_path}")
        }
    }
}


job("${prefix_id}_restart_logstash") {
    displayName(prefix + ' - restart Logstash')

    label('ansible')

    scm {
        git {
            remote {
                github("${github_org}/${tools_repo_name}", 'https', github_url)
                credentials('git-token-credentials')
            }
            branch('master')
        }
    }

    wrappers {
        sshAgent(credentialsId)
    }

    steps {
        shell("./run.sh restart-logstash ${elk_ip} ${elk_reconfigure_port} ${relative_config_path}")
    }
}


job("${prefix_id}_restart_logging") {
    displayName(prefix + ' - restart Logging')

    label('ansible')

    scm {
        git {
            remote {
                github("${github_org}/${tools_repo_name}", 'https', github_url)
                credentials('git-token-credentials')
            }
            branch('master')
        }
    }

    wrappers {
        sshAgent(credentialsId)
    }

    steps {
        shell("./run.sh restart-vm ${elk_ip} ${elk_reconfigure_port} ${relative_config_path}")
    }
}
