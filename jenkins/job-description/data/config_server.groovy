package data


class config_server {

	def config_BizDevOps = [
      server: [
		    proxyUrl: 'http://svr001-e1-svr.zone2.proxy.allianz:8090'
	    ],
      job: [
        libraries: 'agile_JSL1111',
        agent: 'maven',
        withTriggers: false,
        artifactDaysToKeep: 1,
        artifactNumToKeep: 1,
        daysToKeep: 3,
        numToKeep: 10
      ],
      git: [
        server: 'github.developer.allianz.io',
        credentialsId: 'git-token-credentials',
        protocol: 'https',
        serverDirect: 'https://github.developer.allianz.io',
      ],
      ghe: [
        //commitMsgPattern: '/[A-Z]{2,10}-[0-9]+:.*/',
        commitMsgPattern: '.*',
        apiUrl: 'https://github.developer.allianz.io/api/v3',
	bizDevOpsGitGroup: 'JEQP',
	bizDevOpsRepoName: 'BDO_Test_167'
      ],
      jira: [
        url: 'https://jmp-dev04.allianz.net',
        credentialsId: 'adp-tools-jmp-team-user-at-jira2'
      ]
	
	]

    def config_rollator = [
      server: [ 
        stage: 'dev',
        proxyUrl: 'http://de001-surf.zone2.proxy.allianz:8080'
      ],
      job: [
        libraries: 'agile_JSL@develop',
        agent: 'master',
        withTriggers: false
      ],
      git: [
        server: 'rollator.dhcp.allianz:8082',
        repo: '/sef/abs.git',
        branch: 'test',
        credentialsId: 'gitlab_usrpwd',
        protocol: 'http',
        serverDirect: 'https://github.developer.allianz.io',
      ],
      ghe: [
        apiUrl: 'https://github.developer.allianz.io/api/v3',
      ],
      tosca: [
        ciclientUrl: 'https://artifactorytest.cc.azd.cloud.allianz/artifactory/agile_scale_gradle_local/Tosca/ToscaCIJavaClient.jar',
        vendor: 'AZD'
      ],
      abs: [
        rpc_url: 'https://artifactorytest.cc.azd.cloud.allianz/artifactory/agile_scale_gradle/RPC/',
        gitRepoABS: '/absd-dev/absd.git',
        buildMap: [
          build1: ['common,client'],
          build2: []
        ]
      ],
      maven: [
        settingsID: 'svld-maven-settings'
      ],
      testcasedb: [
        credentialsId: 'testcasedb_usrpwd',
        dbconnection: 'jdbc:postgresql://dev-testjob-controller-db.chw3iayacbs4.eu-central-1.rds.amazonaws.com:5432/testjob_controller_db',
        applicationId: '1', //ABSd
        company: 'azd',
      ]
    ]

    def config_absdbuildtest = [
      server: [ 
        stage: 'test',
        proxyUrl: 'http://de001-surf.zone2.proxy.allianz:8080'
      ],
      job: [
        libraries: ['agile_JSL@develop'],
        agent: 'maven-3.6-jdk-11',
        withTriggers: true
      ],
      git: [
        server: 'ghe.adp.allianz',
        credentialsId: 'tu-absd-build-dev',
        protocol: 'https',
        serverDirect: 'https://github.developer.allianz.io',
      ],
      ghe: [
        //commitMsgPattern: '/[A-Z]{2,10}-[0-9]+:.*/',
        commitMsgPattern: '.*',
        apiUrl: 'https://github.developer.allianz.io/api/v3',
        owner: 'absd-dev',
        repo: 'absd'
      ],
      tools: [
        gitRepo: '/absd-dev/tools.git'
      ],
      tosca: [
        ciClientUrl: 'https://artifactorytest.cc.azd.cloud.allianz/artifactory/agile_scale_maven_local/de/allianz/sef/toscaCIJavaClient/ToscaCIJavaClient.jar',
        vendor: 'AZD',
        parallelExecutions: '4',
        dexUrl: 'https://tosca-dex-dev.srv.allianz',
        aoServicePort: '5006',
        pathToYaml: 'tools/tosca-test.yaml',
        parallelEntityKey: 'tosca_unique_id',
        sendResultsToHerold: 'true'
      ],
      abs: [
        rpc_url: 'https://artifactorytest.cc.azd.cloud.allianz/artifactory/agile_scale_gradle/RPC/',
        gitRepoABS: '/absd-dev/absd.git',
        buildMap: [
          build1: ['common,client'],
          build2: []
        ]
      ],
      maven: [
        settingsID: 'global-maven-settings'
      ],
      testcasedb: [
        credentialsId: 'testcasedb_usrpwd',
        dbConnection: 'jdbc:postgresql://dev-testjob-controller-db.chw3iayacbs4.eu-central-1.rds.amazonaws.com:5432/testjob_controller_db',
        sqlConverterUrl: 'https://artifactorytest.cc.azd.cloud.allianz/artifactory/agile_scale_maven_local/de/allianz/sef/sqltoyaml/sqlyaml-sqlyaml.jar',
        applicationId: '1', //ABSd
        company: 'azd',
      ],
      soapui: [ 
        testCaseBranch: 'develop',
        certificateBranch: 'master',
        gitRepoTestsShort: '/absd-dev/absd-test-interface-soapui',
        gitRepoTests: '/absd-dev/absd-test-interface-soapui.git',
        gitRepoCertificates: '/absd-dev/absd-test-interface-certificates.git',
        parallelExecutions: '4',
        parallelEntityKey: 'team',
        sendResultsToHerold: 'true',
        pathToYaml: 'tools/',
        yamlName: 'soapui-test.yaml',
        rootElement: "soapui_path",
        parallelNodeLabel: "maven-3.6-jdk-8"
      ],
      junit: [
        absBranch: 'develop',
        pathToYaml: 'tools/',
        yamlName: 'junit-test.yaml',
        parallelEntityKey: 'team',
        parallelExecutions: 4,
        parallelNodeLabel: "maven-3.6-jdk-11"
      ],
      herold: [ 
        junitResultUrl: 'https://dev1.heroldweb.allianz.de/api/v1/junitResult/',
        mavenLogUrl: 'https://dev1.heroldweb.allianz.de/api/v1/mavenLog/',
        proxyUrl: "http://surf.cc.azd.cloud.allianz:8080"
      ]
    ]

    def config_absdbuild = [
      server: [
        stage: 'prod',
        proxyUrl: 'http://de001-surf.zone2.proxy.allianz:8080'
      ],
      job: [
        libraries: ['agile_JSL@master'],
        agent: 'maven-3.6-jdk-11',
        withTriggers: true
      ],
      git: [
        server: 'ghe.adp.allianz',
        credentialsId: 'tu-absd-build-dev',
        protocol: 'https',
        serverDirect: 'https://github.developer.allianz.io',
      ],
      ghe: [
        //commitMsgPattern: '/[A-Z]{2,10}-[0-9]+:.*/',
        commitMsgPattern: '.*',
        apiUrl: 'https://github.developer.allianz.io/api/v3',
        owner: 'absd-dev',
        repo: 'absd'
      ],
      tosca: [
        ciClientUrl: 'https://artifactorytest.cc.azd.cloud.allianz/artifactory/agile_scale_maven_local/de/allianz/sef/toscaCIJavaClient/ToscaCIJavaClient.jar',
        vendor: 'AZD',
        parallelExecutions: '10',
        dexUrl: 'https://tosca-dex-prod.srv.allianz',
        aoServicePort: '5006',
        pathToYaml: 'tools/tosca-test.yaml',
        parallelEntityKey: 'tosca_unique_id',
        sendResultsToHerold: 'true'
      ],
      junit: [
        absBranch: 'master',
        pathToYaml: 'tools/',
        yamlName: 'junit-test.yaml',
        parallelEntityKey: 'team',
        parallelExecutions: 4,
        parallelNodeLabel: "maven-3.6-jdk-11"
      ],
      tools: [
        gitRepo: '/absd-dev/tools.git'
      ],
      abs: [
        rpc_url: 'https://artifactorytest.cc.azd.cloud.allianz/artifactory/agile_scale_gradle/RPC/',
        gitRepoABS: '/absd-dev/absd.git',
        buildMap: [
          build1: ['common,client'],
          build2: []
        ]
      ],
      maven: [
        settingsID: 'global-maven-settings'
      ],
      testcasedb: [
        credentialsId: 'testcasedb_usrpwd',
        dbConnection: 'jdbc:postgresql://azdsefcontroldb-prod.chlrhmknfwxq.eu-central-1.rds.amazonaws.com:5432/azdsefcontroldb',
        sqlConverterUrl: 'https://artifactorytest.cc.azd.cloud.allianz/artifactory/agile_scale_maven_local/de/allianz/sef/sqltoyaml/sqlyaml-sqlyaml.jar',
        applicationId: '1', //ABSd
        company: 'azd',
      ],
      soapui: [
        gitRepoTestsShort: '/absd-dev/absd-test-interface-soapui',
        testCaseBranch: 'master',
        certificateBranch: 'master',
        gitRepoTests: '/absd-dev/absd-test-interface-soapui.git',
        gitRepoCertificates: '/absd-dev/absd-test-interface-certificates.git',
        parallelExecutions: '4',
        parallelEntityKey: 'team',
        sendResultsToHerold: 'true',
        pathToYaml: 'tools/',
        yamlName: 'soapui-test.yaml',
        rootElement: "soapui_path",
        parallelNodeLabel: "maven-3.6-jdk-8"
      ],
      herold: [
        junitResultUrl: 'https://dev1.heroldweb.allianz.de/api/v1/junitResult/',
        mavenLogUrl: 'https://dev1.heroldweb.allianz.de/api/v1/mavenLog/',
        proxyUrl: "http://surf.cc.azd.cloud.allianz:8080"
      ]
    ]

  def get(String url) {
    def cfg = null
    switch(url) {
      case "http://rollator.dhcp.allianz:8081/":
        cfg = config_rollator
        break;
      case "https://absdbuildtest-jenkins.apps.crpcc.azd.cloud.allianz/":
        cfg = config_absdbuildtest
        break;
      case "https://absdbuild-jenkins.apps.crpcc.azd.cloud.allianz/":
        cfg = config_absdbuild
        break;
	  case "https://jenkins-adp-tools-jmp-team.apps.tools.adp.allianz/":
		return config_BizDevOps;
		break;
      default:
        return config_BizDevOps;
        break;
    }
    return cfg
  }
}
