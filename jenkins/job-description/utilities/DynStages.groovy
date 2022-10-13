package utilities



class DynStages {
    
	static String get(stage, String orig_abstand, data) {
		def ret = ""
		def abstand = orig_abstand

	    def jslgit = new de.allianz.Pipeline.JSLGit()		
		def jslmaven = new de.allianz.Pipeline.JSLMaven()
		def jslgeneral = new de.allianz.Pipeline.JSLGeneral()
		def jsltosca = new de.allianz.Pipeline.JSLTosca()
		def jslabs = new de.allianz.application.JSLABS()
		def jslp2 = new de.allianz.Pipeline.JSLP2()
		def jslartifactory = new de.allianz.Pipeline.JSLArtifactory()
		def jslgradle = new de.allianz.Pipeline.JSLGradle()
		def jslsoapui = new de.allianz.Pipeline.JSLSoapUI()
		
		if ( stage.catchError ) {
			ret += abstand + "catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {\n"
			abstand += "\t"
		}
		stage.steps.each {step ->
			
			String[] splitstep = step.split('\\.')
			if (splitstep.size() > 1) {
				switch (splitstep[0]) {
				    case "abs":
						ret += jslabs.format(splitstep[1], stage.config)
						break;
					case "git":
						ret += jslgit.format(splitstep[1], stage.config)
						break;
					case "maven":
						ret += jslmaven.format(splitstep[1], stage.config)
						break;
					case "general":
						ret += jslgeneral.format(splitstep[1], stage.config)
						break;
					case "tosca":
						ret += jsltosca.format(splitstep[1], stage.config)
						break;
					case "p2":
						ret += jslp2.format(splitstep[1], stage.config)
						break;
					case "artifactory":
						ret += jslartifactory.format(splitstep[1], stage.config)
						break;
					case "gradle":
						ret += jslgradle.format(splitstep[1], stage.config)
						break;
					case "soapui":
						ret += jslsoapui.format(splitstep[1], stage.config)
						break;	
					default:
						ret += "echo '${splitstep[0]} class not implemented in DynStages'\n"
						break;
				}
			} else {
				ret += "echo '${step} not implemented'\n"
			}
			
/*			switch(step) { 
				case "maven.build":
					ret += abstand + "configFileProvider([configFile(fileId: 'global-maven-settings', variable: 'SETTINGS')]) {\n"
					ret += abstand + '\tsh "mvn -f pom.xml -s ${SETTINGS} -Dstyle.color=always -DskipTests --log-file log.txt package"\n'
					ret += abstand + "}\n"
					break;
				case "maven.testunit":
					ret += abstand + "configFileProvider([configFile(fileId: 'global-maven-settings', variable: 'SETTINGS')]) {\n"
					ret += abstand + '\tsh "mvn -f pom.xml -s ${SETTINGS} -Dstyle.color=always test"\n'
					ret += abstand + "}\n"				
					break;
			} 		
			*/
		}
		
		if ( stage.catchError ) {
			abstand = orig_abstand
			ret += abstand + "}\n"
		}
		return ret
    }
}
