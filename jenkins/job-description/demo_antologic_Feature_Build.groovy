    
def config = [:]

def srvconf = new data.config_server()
def srv_config = srvconf.get("\${JENKINS_URL}")
def job_config = [
    job: [
        name: "demo_antologic_Feature_Build"
    ],
    git: [ 
        branch: "develop"
    ]
]


def gConfig = utilities.Tools.mergeMap(job_config, srv_config )


def scripts = """
def lib = library identifier: 'BizDevOps_JSL@develop', retriever: modernSCM(
  [\$class: 'GitSCMSource',
   remote: 'https://github.developer.allianz.io/JEQP/BizDevOps-JSL.git',
   credentialsId: 'git-token-credentials']) 
 
def config = ${utilities.Tools.formatMap(gConfig)}

def jslGeneral    = lib.de.allianz.bdo.pipeline.JSLGeneral.new()
def jslGit        = lib.de.allianz.bdo.pipeline.JSLGit.new()
def jslMaven      = lib.de.allianz.bdo.pipeline.JSLMaven.new()
def jslGhe        = lib.de.allianz.bdo.pipeline.JSLGhe.new()

def manual_commit_sha

// for questions about this job ask mario akermann/tobias pfeifer from team pipeline

pipeline {
    agent { label "\${config.job.agent}" }

    stages {
    stage('Prepare') {
      steps {
        echo "prepare checkout"
        script {
          jslGeneral.clean()
          // for all build via pull-request, the branch name is not filled
          if(branch_name.isEmpty()) {
            // build pull-request
            jslAbs.buildPullRequest(config, pr_number, commit_sha)
          // for all manual builds, the branch name is filled
          } else {
            // this is the part for the manual parameters - its documented on confluence
            def profiles = [:]
            if(common.toBoolean()) {
              echo "add common to buildMap"
              profiles['common'] = ["common"]
            }
            if(common_client.toBoolean()) {
              echo "add common,client to buildMap"
              profiles['common_client'] = ["common,client"]
            }
            if(common_web.toBoolean()) {
              profiles['common_web'] = ["common,web"]
              echo "add common, web to buildMap"
            }
            if(common_test.toBoolean()) {
              profiles['common_test'] = ["common,test"]
              echo "add common, test to buildMap"
            }
            if(client.toBoolean()) {
              profiles['client'] = "client"
              echo "add client to buildMap"
            }
            if(client_test.toBoolean()) {
              profiles['client_test'] = ["client,test"]
              echo "add client,test to buildMap"
            }
            // build branch
            jslAbs.buildBranch(config, branch_name, profiles, run_unit_tests)
          }
        }
      }
    }
    }
}


"""
        
def job = pipelineJob("${gConfig.job.name}");

job.with {

    parameters {
    stringParam('branch_name','', 'name of the branch to build')
    booleanParam('common', false)
    booleanParam('common_client', false)
    booleanParam('common_web', false)
    booleanParam('common_test', false)
    booleanParam('client', false)
    booleanParam('client_test', false)
    booleanParam('run_unit_tests', false)
    }

    properties {
        triggers {
            genericTrigger {
                genericVariables {
                    genericVariable {
                        key("action")
                        value("\$.action")
                        expressionType("JSONPath") //Optional, defaults to JSONPath
                        regexpFilter("") //Optional, defaults to empty string
                        defaultValue("") //Optional, defaults to empty string
                    }
                    genericVariable {
                        key("commit_sha")
                        value("\$.pull_request.head.sha")
                        expressionType("JSONPath") //Optional, defaults to JSONPath
                        regexpFilter("") //Optional, defaults to empty string
                        defaultValue("") //Optional, defaults to empty string
                    }
                    genericVariable {
                        key("pr_number")
                        value("\$.number")
                        expressionType("JSONPath") //Optional, defaults to JSONPath
                        regexpFilter("") //Optional, defaults to empty string
                        defaultValue("") //Optional, defaults to empty string
                    }
                    genericVariable {
                        key("pr_title")
                        value("\$.pull_request.title")
                        expressionType("JSONPath") //Optional, defaults to JSONPath
                        regexpFilter("") //Optional, defaults to empty string
                        defaultValue("") //Optional, defaults to empty string
                    }
                    genericVariable {
                        key("commits_url")
                        value("\$.pull_request.commits_url")
                        expressionType("JSONPath") //Optional, defaults to JSONPath
                        regexpFilter("") //Optional, defaults to empty string
                        defaultValue("") //Optional, defaults to empty string
                    }
                }
                //                genericRequestVariables {
                //                    genericRequestVariable {
                //                        key("requestParameterName")
                //                        regexpFilter("")
                //                    }
                //                }
                //                genericHeaderVariables {
                //                    genericHeaderVariable {
                //                        key("requestHeaderName")
                //                        regexpFilter("")
                //                    }
                //                }
                token('QWpifKQNwMYbOPo6FjptoYQ5Jm65Ue8A')
                printContributedVariables(true)
                printPostContent(true)
                silentResponse(false)
                regexpFilterText("\$action")
                regexpFilterExpression("^(opened|reopened|synchronize)\$")
            }
        }
    }

    definition {
        cps { 
            script(scripts)
        }
    } 
}  
