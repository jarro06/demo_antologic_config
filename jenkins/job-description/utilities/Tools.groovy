package utilities

class Tools {

	static def mergeConfig( map, config ) {
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
				map.replace(it.key, mergeMap(it.value, map.config))
			} else if (it.key != "parameters" && it.key != "triggers" && it.key != "steps" && it.key != "config" && it.value.getClass() == java.util.ArrayList ) {
				it.value.eachWithIndex { wert, index ->
					it.value[index] = mergeMap(wert, map.config)
				}
			}
		}
		return map
	} 

	static def mergeMap( map1, map2 ) {
			map2.each { cfg ->
				if (map1.containsKey(cfg.key))
				   map1[cfg.key] = cfg.value + map1[cfg.key]
				else
					map1.put(cfg.key, cfg.value)
			}
/*		    map1.eachWithIndex { it, i -> 
			if (it.value.getClass() == java.util.LinkedHashMap ) {
			    println("map")
			//	map1.replace(it.key, mergeMap(it.value, map2[it]))
			} else if (it.value.getClass() == java.util.ArrayList ) {
				println("List")
			//	it.value.eachWithIndex { wert, index ->
			//		it.value[index] = mergeMap(wert, map2[it])
			//	}
			}
		}*/
		return map1
	} 

	static String formatMap( map ) {
/*	    def map1 = mergeMap([:] , map )
		map1.each { element1 ->
            element1.value.each { element ->
                if (element.key == "trigger" || element.key == "parameter")  {
		            element.value = "{}"
				}
            }
		}*/
		return map.inspect()
	}
	

	static def writeJob(Map head, scripts) {
		def job = pipelineJob("${head.name}");

		if (head.containsKey("trigger")) {
			job.triggers(head.trigger)
		}
		
		if (head.containsKey("parameters")) {
			job.parameters(head.parameters)
		}

		job.with {
		  definition {
			cps { 
			  script(scripts)
			}
		  } 
		}  
	}

}