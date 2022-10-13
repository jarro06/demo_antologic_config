package utilities

import data.config_server
import data.jobs_Tosca
import data.jobs_ABSd
import data.jobs_SoapUI


class ReadYaml {



	def datas = [
		pipe_savi_upadte: [
			name: "git_update_mit_savi_neu2",
			view: "inDevelop",
			tools: [
				maven: "maven3.6.3",
				jdk: "jdk11"
			],			
			config: [
				git: [
					branch: "release/20.0",
					dir: "git"
				],
				savi: [
					dir: "savi"
				]
			],
			stages: [
				[name: 'Prep'					, rank:  05, use: true, type: 'stage', steps: ['git.prep', 'savi.prep']],
				[name: 'CheckOut git'		   	, rank:  10, use: true, type: 'stage', group: "checkout", steps: ['git.checkout']],
				[name: 'checkout savi'         	, rank:  20, use: true, type: 'stage', group: "checkout", steps: ['savi.checkout']],
				[name: 'create feature'        	, rank:  30, use: true, type: 'stage', steps: ['git.create_feature']],
				[name: 'update git with savi'	, rank:  40, use: true, type: 'stage', steps: ['savi.git_update']],
				[name: 'publish new feature'   	, rank:  50, use: true, type: 'stage', steps: ['git.publish']]
			]
		],
	
		pipe1: [
			name: "abs_pipe_feature_sac12",
			view: "inDevelop",
			tools: [
				maven: "maven3.6.3",
				jdk: "jdk11"
			],			
			config: [
				git: [
					branch: "feature/SAVI-1912121157"
				]
			],
			stages: [
				[name: 'CheckOut'                     , rank:  00, use: true, type: 'stage', steps: ['git.checkout']],
				[name: 'Prep'                         , rank:  10, use: false, type: 'stage', steps: ['maven.prep']],
				[name: 'Build'                        , rank:  20, use: true, type: 'stage', steps: ['maven.build']],
				[name: 'Test - Unit Tests'            , rank:  30, use: false, type: 'stage', steps: ['maven.testunit']],
				[name: 'Test - Light Integration'     , rank:  40, use: false, type: 'stage', steps: ['maven.testunitplugin']],
				[name: 'Test - Light Integration - UI', rank:  50, use: false, type: 'stage', steps: ['']],
				[name: 'Deploy'                       , rank:  60, use: false, type: 'stage', steps: ['']],
				[name: 'Test - SonarQube'             , rank:  70, use: false, type: 'stage', steps: ['Sonar.test']],
				[name: 'Test - JFrog Xray'            , rank:  80, use: false, type: 'stage', steps: ['Xray.test']],
				[name: 'Test - Integration with CISL' , rank:  90, use: false, type: 'stage', steps: ['postman.test']],
				[name: 'Test - Integration with Tosca', rank: 100, use: false, type: 'stage', steps: ['']],
				[name: 'Archive'                      , rank: 110, use: false, type: 'stage', steps: ['']]
			]
		],
		
		pipe2: [
				name: "abs_pipe_feature",
				view: "abs_build",
				config: [
					git: [ 
						branch: "develop",
						source_branch1: "\${env.gitlabBranch}",
						source_branch: '${merge_feature}',
						comment: 'merge ${merge_feature} into develop'
					]
				],
				parameters: {
					stringParam('merge_feature','', 'feature to merge')
				},		
		
				stages: [
					 [name: 'CheckOut'             			, rank:  00, use: true, catchError: false, type: 'stage', steps: ['git.checkout']],
					 [name: 'merge'							, rank:  10, use: true, catchError: false, type: 'stage', steps: ['git.merge']],
					 [name: 'read configs'         			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['']],
					 [name: 'Build'                			, rank:  30, use: true, catchError: false, type: 'stage', steps: ['maven.build']],
					 [name: 'Komponententests - prep'	  	, rank:  40, use: true, catchError: true,  type: 'stage', steps: ['']],
					 [name: 'Komponententests - Unit'     	, rank:  50, use: true, catchError: true,  type: 'stage', steps: ['']],
					 [name: 'Komponententests - Unit-plugin', rank:  60, use: true, catchError: true,  type: 'stage', steps: ['']],
				 	 [name: 'generate docs'	      			, rank:  70, use: true, catchError: true,  type: 'stage', steps: ['']],
					 [name: 'publish and delete'			, rank:  80, use: true, catchError: false,  type: 'stage', steps: ['git.publish']]
				]
			],
		
		pipe3: [
				name: "abs_pipe_develop",
				view: "abs_pipeline",
				tools: [
					maven: "maven3.6.3",
					jdk: "jdk11"
				],
				config: [
					git: [ 
						branch: "develop"
					],
					maven: [
						settingsID: "clean-maven-settings"
					]
				],
				stages: [
					[name: 'Preperation'					, rank:   5, use: true, catchError: false, type: 'stage', steps: ['general.clean']],
					[name: 'CheckOut'             			, rank:  10, use: true, catchError: false, type: 'stage', steps: ['git.checkout']],
					[name: 'read configs'         			, rank:  15, use: true, catchError: false, type: 'stage', steps: ['abs.mavenprep']],
					[name: 'Build'                			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['maven.build']],
					[name: 'Komponententests - Prep'	  	, rank:  30, use: true, catchError: true,  type: 'stage', steps: ['']],
					[name: 'Komponententests - Unit'     	, rank:  40, use: true, group: 'Komponententests', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Komponententests - Unit-plugin' , rank:  50, use: true, group: 'Komponententests', catchError: true,  type: 'stage', steps: ['']],
				 	[name: 'generate docs'	      			, rank:  80, use: true, catchError: true,  type: 'stage', steps: ['']],
					[name: 'Package - P2'		  			, rank:  90, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Package - SAC'		  			, rank: 100, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Package - Webservices'			, rank: 110, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Deploy - ATQ'		  			, rank: 120, use: true, catchError: true,  type: 'stage', steps: ['']],					
					[name: 'Integrationtest - Prep'	  		, rank: 130, use: true, catchError: true,  type: 'stage', steps: ['']],					
					[name: 'Integrationtest - Sonar'        , rank: 140, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - XRay' 		, rank: 150, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - CISL'    		, rank: 160, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - Tosca'	  	, rank: 170, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - Unit'     	, rank: 132, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - Unit-plugin' 	, rank: 135, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Deploy - ATE'		  			, rank: 180, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Deploy - ATA'		  			, rank: 190, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['']],					
					[name: 'Abnahmetest - Prep'	  			, rank: 200, use: true, catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - CISL'     		, rank: 210, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - Tosca'	  		, rank: 220, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - Unit'     		, rank: 204, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - Unit-plugin' 		, rank: 206, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
				]
			],

		pipe_absrelease: [
				name: "abs_pipe_release_19.5",
				view: "abs_pipeline",
				trigger: {
					cron('H 07,14,19 * * 1-5')
				},
				tools: [
					maven: "maven3.6.3",
					jdk: "jdk11"
				],
				config: [
					git: [ 
						branch: "release/19.5_2003170515"
					],
					general: [
						clean_every: 10
					]
					
				],
				stages: [
					[name: 'Preperation'					, rank:   5, use: true, catchError: false, type: 'stage', steps: ['general.clean_every']],
					[name: 'CheckOut'             			, rank:  10, use: true, catchError: false, type: 'stage', steps: ['git.checkout2']],
					[name: 'read configs'         			, rank:  15, use: true, catchError: false, type: 'stage', steps: ['abs.mavenprep']],
					[name: 'Build'                			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['maven.validate','maven.build']],
					[name: 'Komponententests - Prep'	  	, rank:  30, use: true, catchError: true,  type: 'stage', steps: ['']],
					[name: 'Komponententests - Unit'     	, rank:  40, use: true, group: 'Komponententests', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Komponententests - Unit-plugin' , rank:  50, use: true, group: 'Komponententests', catchError: true,  type: 'stage', steps: ['']],
				 	[name: 'generate docs'	      			, rank:  80, use: true, catchError: true,  type: 'stage', steps: ['']],
					[name: 'Package - P2'		  			, rank:  90, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Package - SAC'		  			, rank: 100, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Package - Webservices'			, rank: 110, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Deploy - ATQ'		  			, rank: 120, use: true, catchError: true,  type: 'stage', steps: ['']],					
					[name: 'Integrationtest - Prep'	  		, rank: 130, use: true, catchError: true,  type: 'stage', steps: ['']],					
					[name: 'Integrationtest - Sonar'        , rank: 140, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - XRay' 		, rank: 150, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - CISL'    		, rank: 160, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - Tosca'	  	, rank: 170, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - Unit'     	, rank: 132, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - Unit-plugin' 	, rank: 135, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Deploy - ATE'		  			, rank: 180, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Deploy - ATA'		  			, rank: 190, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['']],					
					[name: 'Abnahmetest - Prep'	  			, rank: 200, use: true, catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - CISL'     		, rank: 210, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - Tosca'	  		, rank: 220, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - Unit'     		, rank: 204, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - Unit-plugin' 		, rank: 206, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
				]
			],

		pipe_absreleaseTest: [
				name: "abs_pipe_release_Test",
				view: "inDevelop",
				tools: [
					maven: "maven3.6.3",
					jdk: "jdk11"
				],
				config: [
					git: [ 
						branch: "bugfix/clean_repos"
					],
					general: [
						clean_every: 10
					]
					
				],
				stages: [
					[name: 'Preperation'					, rank:   5, use: true, catchError: false, type: 'stage', steps: ['general.clean']],
					[name: 'CheckOut'             			, rank:  10, use: true, catchError: false, type: 'stage', steps: ['git.checkout2']],
					[name: 'read configs'         			, rank:  15, use: false, catchError: false, type: 'stage', steps: ['abs.mavenprep']],
					[name: 'Build'                			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['maven.build']]
				]
			],


		pipe_absrelease_clean: [
				name: "abs_pipe_release_clean",
				view: "abs_pipeline",
				trigger: {
					cron('H 06,13,18 * * 1-5')
				},
				tools: [
					maven: "maven3.6.3",
					jdk: "jdk11"
				],
				config: [
					git: [ 
						branch: "release/19.5_2003170515"
					]
				],
				stages: [
					[name: 'Preperation'					, rank:   5, use: true, catchError: false, type: 'stage', steps: ['general.clean']],
					[name: 'CheckOut'             			, rank:  10, use: true, catchError: false, type: 'stage', steps: ['git.checkout']],
					[name: 'read configs'         			, rank:  15, use: true, catchError: false, type: 'stage', steps: ['abs.mavenprep']],
					[name: 'Build'                			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['maven.build']],
					[name: 'Komponententests - Prep'	  	, rank:  30, use: true, catchError: true,  type: 'stage', steps: ['']],
					[name: 'Komponententests - Unit'     	, rank:  40, use: true, group: 'Komponententests', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Komponententests - Unit-plugin' , rank:  50, use: true, group: 'Komponententests', catchError: true,  type: 'stage', steps: ['']],
				 	[name: 'generate docs'	      			, rank:  80, use: true, catchError: true,  type: 'stage', steps: ['']],
					[name: 'Package - P2'		  			, rank:  90, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Package - SAC'		  			, rank: 100, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Package - Webservices'			, rank: 110, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Deploy - ATQ'		  			, rank: 120, use: true, catchError: true,  type: 'stage', steps: ['']],					
					[name: 'Integrationtest - Prep'	  		, rank: 130, use: true, catchError: true,  type: 'stage', steps: ['']],					
					[name: 'Integrationtest - Sonar'        , rank: 140, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - XRay' 		, rank: 150, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - CISL'    		, rank: 160, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - Tosca'	  	, rank: 170, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - Unit'     	, rank: 132, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Integrationtest - Unit-plugin' 	, rank: 135, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Deploy - ATE'		  			, rank: 180, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Deploy - ATA'		  			, rank: 190, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['']],					
					[name: 'Abnahmetest - Prep'	  			, rank: 200, use: true, catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - CISL'     		, rank: 210, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - Tosca'	  		, rank: 220, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - Unit'     		, rank: 204, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
					[name: 'Abnahmetest - Unit-plugin' 		, rank: 206, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['']],
				]
			],

			
		pipe4: [
				name: "abs_pipe_simulation",
				view: "abs_build",
				trigger: {
					cron('H 05 * * 1-5')
				},
				config: [
					git: [ 
						branch: "develop"
					]
				],
				stages: [
					[name: 'CheckOut'             			, rank:  10, use: true, catchError: false, type: 'stage', steps: ['general.wait']],
					[name: 'read configs'         			, rank:  15, use: true, catchError: false, type: 'stage', steps: ['general.wait']],
					[name: 'Build'                			, rank:  20, use: true, catchError: false, type: 'stage', steps: ['general.wait']],
					[name: 'Komponententest - prep'	  	, rank:  30, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Komponententest - Unit'     	, rank:  40, use: true, group: 'Komponententest', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Komponententest - Unit-plugin' , rank:  50, use: true, group: 'Komponententest', catchError: true,  type: 'stage', steps: ['general.wait']],
				 	[name: 'generate docs'	      			, rank:  80, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Package - P2'		  			, rank:  90, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Package - SAC'		  			, rank: 100, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Package - Webservices'			, rank: 110, use: true, group: 'Package', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Deploy - ATQ'		  			, rank: 120, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],					
					[name: 'Integrationtest - Prep'	  		, rank: 130, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],					
					[name: 'Integrationtest - Sonar'        , rank: 140, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Integrationtest - XRay' 		, rank: 150, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Integrationtest - CISL'    		, rank: 160, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Integrationtest - Tosca'	  	, rank: 170, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Integrationtest - Unit'     	, rank: 132, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Integrationtest - Unit-plugin' 	, rank: 135, use: true, group: 'Integrationtest', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Deploy - ATE'		  			, rank: 180, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Deploy - ATA'		  			, rank: 190, use: true, group: 'Deploy', catchError: true,  type: 'stage', steps: ['general.wait']],					
					[name: 'Abnahmetest - Prep'	  			, rank: 200, use: true, catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Abnahmetest - CISL'     		, rank: 210, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Abnahmetest - Tosca'	  		, rank: 220, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Abnahmetest - Unit'     		, rank: 204, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
					[name: 'Abnahmetest - Unit-plugin' 		, rank: 206, use: true, group: 'Abnahmetest', catchError: true,  type: 'stage', steps: ['general.wait']],
				]
			],
			
			
			
			
		pipe_ABS_P2: [
				name: "abs_generate_P2",
				view: "abs_additional",
				tools: [
					gradle: "gradle"
				],
				config: [
					git: [
						branch: '${branch}', 
						files: ['dependencies.common.properties','dependencies.client.properties','dependencies.web.properties','dependencies.common.test.properties','dependencies.client.test.properties','dependencies.web.test.properties']
					],
					p2: [
						jarpath: "dependencies.properties",
						reponame: "generated.p2",
						repoversion: '${abs_release}'
					],
					gradle: [
						settingsID: 'gradle_properties',
						target: 'updateSiteZip'
					],
					artifactory: [
						serverID: 'artifactorytest',
						pattern: 'build/updatesite.zip',
						target: 'agile_scale_p2_local/abs/${abs_release}/'
					]
				],
				parameters: {
					choiceParam('abs_release',['20.0','19.6','19.5','20.0','Other','Test'], 'ABS Release')
					stringParam('branch', 'release/20.0_200423', 'Git Branch')
				},		
				
				stages: [
					[name: 'Preperation'					    , rank:   5, use: true, catchError: false, type: 'stage', steps: ['git.checkoutFiles']],
					[name: 'generate Gradle Common'             , rank:  10, use: true, catchError: false, type: 'stage', steps: ['p2.generateGradle'], config: [p2: [jarpath: 'dependencies.common.properties', reponame: 'de.allianz.externallibs.common.p2']]],
					[name: 'start Gradle Common'            	, rank:  11, use: true, catchError: false, type: 'stage', steps: ['gradle.execute']],
					[name: 'Rename P2 Common'       	        , rank:  12, use: true, catchError: false, type: 'stage', steps: ['p2.rename'], config: [p2: [reponame: 'de.allianz.externallibs.common.p2']]],
					[name: 'Artifactory Test Uplaod Common'     , rank:  13, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/common/de.allianz.externallibs.common.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.common.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Common'     , rank:  14, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/common/de.allianz.externallibs.common.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.common.p2_${abs_release}.zip', serverID: 'artifactory']]],
					[name: 'generate Gradle Common Test'       	, rank:  20, use: true, catchError: false, type: 'stage', steps: ['p2.generateGradle'], config: [p2: [jarpath: 'dependencies.common.test.properties', reponame: 'de.allianz.externallibs.common.test.p2']]],
					[name: 'start Gradle Common Test'           , rank:  21, use: true, catchError: false, type: 'stage', steps: ['gradle.execute']],
					[name: 'Rename P2 Common Test'       	    , rank:  22, use: true, catchError: false, type: 'stage', steps: ['p2.rename'], config: [p2: [reponame: 'de.allianz.externallibs.common.test.p2']]],
					[name: 'Artifactory Uplaod Common Test'     , rank:  23, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/common/de.allianz.externallibs.common.test.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.common.test.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Common Test', rank:  24, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/common/de.allianz.externallibs.common.test.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.common.test.p2_${abs_release}.zip', serverID: 'artifactory']]],
					[name: 'generate Gradle Client'           	, rank:  30, use: true, catchError: false, type: 'stage', steps: ['p2.generateGradle'], config: [p2:[jarpath: 'dependencies.client.properties', reponame: 'de.allianz.externallibs.client.p2']]],
					[name: 'start Gradle Client'             	, rank:  31, use: true, catchError: false, type: 'stage', steps: ['gradle.execute']],
					[name: 'Rename P2 Client'       	        , rank:  32, use: true, catchError: false, type: 'stage', steps: ['p2.rename'], config: [p2: [reponame: 'de.allianz.externallibs.client.p2']]],
					[name: 'Artifactory Uplaod Client'          , rank:  33, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/client/de.allianz.externallibs.client.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.client.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Client'     , rank:  34, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/client/de.allianz.externallibs.client.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.client.p2_${abs_release}.zip', serverID: 'artifactory']]],
					[name: 'generate Gradle Client Test'      	, rank:  40, use: true, catchError: false, type: 'stage', steps: ['p2.generateGradle'], config: [p2:[jarpath: 'dependencies.client.test.properties', reponame: 'de.allianz.externallibs.client.test.p2']]],
					[name: 'start Gradle Client Test'         	, rank:  41, use: true, catchError: false, type: 'stage', steps: ['gradle.execute']],
					[name: 'Rename P2 Client Test'       	    , rank:  42, use: true, catchError: false, type: 'stage', steps: ['p2.rename'], config: [p2: [reponame: 'de.allianz.externallibs.client.test.p2']]],
					[name: 'Artifactory Uplaod Client Test'     , rank:  43, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/client/de.allianz.externallibs.client.test.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.client.test.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Client Test', rank:  44, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/client/de.allianz.externallibs.client.test.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.client.test.p2_${abs_release}.zip', serverID: 'artifactory']]],
    				[name: 'generate Gradle Web'       	        , rank:  50, use: true, catchError: false, type: 'stage', steps: ['p2.generateGradle'], config: [p2:[jarpath: 'dependencies.web.properties', reponame: 'de.allianz.externallibs.web.p2']]],
					[name: 'start Gradle Web'           	    , rank:  51, use: true, catchError: false, type: 'stage', steps: ['gradle.execute']],
					[name: 'Rename P2 Web'       	            , rank:  52, use: true, catchError: false, type: 'stage', steps: ['p2.rename'], config: [p2: [reponame: 'de.allianz.externallibs.web.p2']]],
					[name: 'Artifactory Uplaod Web'             , rank:  53, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/web/de.allianz.externallibs.web.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.web.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Web'        , rank:  54, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/web/de.allianz.externallibs.web.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.web.p2_${abs_release}.zip', serverID: 'artifactory']]],
					[name: 'generate Gradle Web Test'       	, rank:  60, use: true, catchError: false, type: 'stage', steps: ['p2.generateGradle'], config: [p2:[jarpath: 'dependencies.web.test.properties', reponame: 'de.allianz.externallibs.web.test.p2']]],
					[name: 'start Gradle Web Test'           	, rank:  61, use: true, catchError: false, type: 'stage', steps: ['gradle.execute']],
					[name: 'Rename P2 Web Test'       	        , rank:  62, use: true, catchError: false, type: 'stage', steps: ['p2.rename'], config: [p2: [reponame: 'de.allianz.externallibs.web.test.p2']]],
					[name: 'Artifactory Uplaod Web Test'        , rank:  63, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/web/de.allianz.externallibs.web.test.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.web.test.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Web Test'   , rank:  64, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/web/de.allianz.externallibs.web.test.p2/${abs_release}/', pattern: 'build/de.allianz.externallibs.web.test.p2_${abs_release}.zip', serverID: 'artifactory']]]
				]
			],
			
				pipe_ABS_P2_Maven: [
				name: "abs_generate_P2_maven",
				view: "abs_additional",
				tools: [
					maven: "maven3.6.3"
				],
				config: [
					git: [
						branch: '${branch}', 
						files: ['dependencies.common.properties','dependencies.client.properties','dependencies.web.properties','dependencies.common.test.properties','dependencies.client.test.properties','dependencies.web.test.properties']
					],
					p2: [
						jarpath: "dependencies.properties",
						reponame: "generated.p2",
						repoversion: '${abs_release}'
					],
					maven: [
					    target: 'p2:site',
					    settingsID: 'maven_settings_svld_noproxy'
					],
					gradle: [
						settingsID: 'gradle_properties',
						target: 'updateSiteZip'
					],
					artifactory: [
						serverID: 'artifactorytest',
						pattern: 'target/repository/updatesite.zip',
						target: 'agile_scale_p2_local/abs/${abs_release}/'
					]
				],
				parameters: {
					choiceParam('abs_release',['20.0','19.6','19.5','20.0','Other','Test'], 'ABS Release')
					stringParam('branch', 'release/20.0_200423', 'Git Branch')
				},		
				
				stages: [
					[name: 'Preperation'					    , rank:   5, use: true, catchError: false, type: 'stage', steps: ['git.checkoutFiles']],
					[name: 'generate Maven Common'       	    , rank:  10, use: true, catchError: false, type: 'stage', steps: ['p2.generateMaven'], config: [p2: [jarpath: 'dependencies.common.properties', reponame: 'de.allianz.externallibs.common.p2']]],
					[name: 'start Maven Common'           	    , rank:  11, use: true, catchError: false, type: 'stage', steps: ['maven.run']],
					[name: 'Rename P2 Common'       	        , rank:  12, use: true, catchError: false, type: 'stage', steps: ['p2.renameMaven'], config: [p2: [reponame: 'de.allianz.externallibs.common.p2']]],
					[name: 'Artifactory Test Uplaod Common'     , rank:  13, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/common/de.allianz.externallibs.common.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.common.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Common'     , rank:  14, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/common/de.allianz.externallibs.common.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.common.p2_${abs_release}.zip', serverID: 'artifactory']]],
					
					[name: 'generate Maven Common Test'        	, rank:  20, use: true, catchError: false, type: 'stage', steps: ['p2.generateMaven'], config: [p2: [jarpath: 'dependencies.common.test.properties', reponame: 'de.allianz.externallibs.common.test.p2']]],
					[name: 'start Maven Common Test'           	, rank:  21, use: true, catchError: false, type: 'stage', steps: ['maven.run']],
					[name: 'Rename P2 Common Test'       	    , rank:  22, use: true, catchError: false, type: 'stage', steps: ['p2.renameMaven'], config: [p2: [reponame: 'de.allianz.externallibs.common.test.p2']]],
					[name: 'Artifactory Uplaod Common Test'     , rank:  23, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/common/de.allianz.externallibs.common.test.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.common.test.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Common Test', rank:  24, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/common/de.allianz.externallibs.common.test.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.common.test.p2_${abs_release}.zip', serverID: 'artifactory']]],
					
					[name: 'generate Maven Client'      	    , rank:  30, use: true, catchError: false, type: 'stage', steps: ['p2.generateMaven'], config: [p2:[jarpath: 'dependencies.client.properties', reponame: 'de.allianz.externallibs.client.p2']]],
					[name: 'start Maven Client'         	    , rank:  31, use: true, catchError: false, type: 'stage', steps: ['maven.run']],
					[name: 'Rename P2 Client'       	        , rank:  32, use: true, catchError: false, type: 'stage', steps: ['p2.renameMaven'], config: [p2: [reponame: 'de.allianz.externallibs.client.p2']]],
					[name: 'Artifactory Uplaod Client'          , rank:  33, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/client/de.allianz.externallibs.client.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.client.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Client'     , rank:  34, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/client/de.allianz.externallibs.client.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.client.p2_${abs_release}.zip', serverID: 'artifactory']]],
					
					[name: 'generate Maven Client Test'      	, rank:  40, use: true, catchError: false, type: 'stage', steps: ['p2.generateMaven'], config: [p2:[jarpath: 'dependencies.client.test.properties', reponame: 'de.allianz.externallibs.client.test.p2']]],
					[name: 'start Maven Client Test'         	, rank:  41, use: true, catchError: false, type: 'stage', steps: ['maven.run']],
					[name: 'Rename P2 Client Test'       	    , rank:  42, use: true, catchError: false, type: 'stage', steps: ['p2.renameMaven'], config: [p2: [reponame: 'de.allianz.externallibs.client.test.p2']]],
					[name: 'Artifactory Uplaod Client Test'     , rank:  43, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/client/de.allianz.externallibs.client.test.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.client.test.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Client Test', rank:  44, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/client/de.allianz.externallibs.client.test.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.client.test.p2_${abs_release}.zip', serverID: 'artifactory']]],
    				
					[name: 'generate Maven Web'       	        , rank:  50, use: true, catchError: false, type: 'stage', steps: ['p2.generateMaven'], config: [p2:[jarpath: 'dependencies.web.properties', reponame: 'de.allianz.externallibs.web.p2']]],
					[name: 'start Maven Web'           	    	, rank:  51, use: true, catchError: false, type: 'stage', steps: ['maven.run']],
					[name: 'Rename P2 Web'       	            , rank:  52, use: true, catchError: false, type: 'stage', steps: ['p2.renameMaven'], config: [p2: [reponame: 'de.allianz.externallibs.web.p2']]],
					[name: 'Artifactory Uplaod Web'             , rank:  53, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/web/de.allianz.externallibs.web.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.web.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Web'        , rank:  54, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/web/de.allianz.externallibs.web.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.web.p2_${abs_release}.zip', serverID: 'artifactory']]],
					
					[name: 'generate Maven Web Test'       		, rank:  60, use: true, catchError: false, type: 'stage', steps: ['p2.generateMaven'], config: [p2:[jarpath: 'dependencies.web.test.properties', reponame: 'de.allianz.externallibs.web.test.p2']]],
					[name: 'start Maven Web Test'           	, rank:  61, use: true, catchError: false, type: 'stage', steps: ['maven.run']],
					[name: 'Rename P2 Web Test'       	        , rank:  62, use: true, catchError: false, type: 'stage', steps: ['p2.renameMaven'], config: [p2: [reponame: 'de.allianz.externallibs.web.test.p2']]],
					[name: 'Artifactory Uplaod Web Test'        , rank:  63, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'agile_scale_maven_local/de/allianz/externallibs/web/de.allianz.externallibs.web.test.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.web.test.p2_${abs_release}.zip']]],
					[name: 'Artifactory Prod Uplaod Web Test'   , rank:  64, use: true, catchError: false, type: 'stage', steps: ['artifactory.upload'], config: [artifactory: [target: 'abs-external-local/de/allianz/externallibs/web/de.allianz.externallibs.web.test.p2/${abs_release}/', pattern: 'target/repository/de.allianz.externallibs.web.test.p2_${abs_release}.zip', serverID: 'artifactory']]]
				]
			]
		]
	
	def fill_config( map, config ) {
		// wenn kein config element besteht fügt dieser Teil die übergebene config hinzu
		if (!map.containsKey("config")) {
			 map.put("config", config)  
		} else  {
			config.each { cfg ->
				if (map.config.containsKey(cfg.key))
				   map.config[cfg.key] = cfg.value + map.config[cfg.key]
				else
					map.config.put(cfg.key, cfg.value)
			}
		}
		// durchsucht die Daten Strucktur nach Hashmaps oder ArrayListen durch und ruft für deren Inhalt die selbe Funktion wieder Auf und geht daher rekursiv durch die Strucktur durch.
		map.eachWithIndex { it, i -> 
			if (it.key != "parameters" && it.key != "triggers" && it.key != "steps" && it.key != "config" && it.value.getClass() == java.util.LinkedHashMap ) {
				map.replace(it.key, fill_config(it.value, map.config))
			} else if (it.key != "parameters" && it.key != "triggers" && it.key != "steps" && it.key != "config" && it.value.getClass() == java.util.ArrayList ) {
				it.value.eachWithIndex { wert, index ->
					it.value[index] = fill_config(wert, map.config)
				}
			}
		}
		return map
	} 
	
	def findJobs = { def key, def list, def ids ->
		return list.findAll { ! (list["view"] in ids) }
	}

	def get(String jenkinsSrv = "rollator") {
		def ret = [:]

		def srvconf = new config_server()
		def config = srvconf.get(jenkinsSrv)
		def jobs_tosca = new jobs_Tosca()
		def jobs_absd = new jobs_ABSd()
		def jobs_soapui = new jobs_SoapUI()
		switch(jenkinsSrv) { 
			case "rollator": 
				ret = datas
				break;
			case "http://rollator.dhcp.allianz:8081/": 
				ret = datas
				break;
			case "http://localhost:8080/":
				ret = datas.subMap(["pipe_ABS_P2_Maven","pipe4","pipe_Tosca_register","pipe_Tosca_deregister","pipe_Tosca_listregister"])
				break;
			case "https://absdbuildtest-jenkins.apps.crpcc.azd.cloud.allianz/":
//				ret = datas.subMap()
				break;
		}

		ret = ret + jobs_tosca.getJobs(jenkinsSrv)
		ret = ret + jobs_absd.getJobs(jenkinsSrv)
		ret = ret + jobs_soapui.getJobs(jenkinsSrv)

		ret = fill_config( ret, config )
		return ret
	}
}
