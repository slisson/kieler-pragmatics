package de.cau.cs.kieler.kiml.config.text

import com.google.common.collect.Lists
import de.cau.cs.kieler.core.kgraph.KNode
import de.cau.cs.kieler.kiml.LayoutMetaDataService
import de.cau.cs.kieler.kiml.LayoutOptionData
import de.cau.cs.kieler.kiml.config.VolatileLayoutConfig
import java.util.List
import org.eclipse.emf.ecore.resource.Resource

class LayoutConfigTransformer {

    public static def List<VolatileLayoutConfig> from(Resource resource, Pair<String, Number>... additionalOptions) {

        val List<VolatileLayoutConfig> volatileConfigs = Lists.newLinkedList
        val dataService = LayoutMetaDataService.getInstance();

        // these are no actual KNodes, we just use them as containers
        // for the layout options that are specified in the textual config 
        resource.contents.filter(typeof(KNode)).forEach [ root |
            
            // iterate through all configs
            root.data.forEach [ cfg |
                val currentConfig = new VolatileLayoutConfig
                volatileConfigs += currentConfig
               
                // add all textually specified layout options
                cfg.persistentEntries.forEach [ entry |
                    val optData = dataService.getOptionDataBySuffix(entry.key) as LayoutOptionData
                    
                    // if valid, parse its value
                    if (optData != null) {
                        val value = optData.parseValue(entry.value)
                        if (value != null) {
                            currentConfig.setValue(optData, value)
                        }
                    }
                ]
                
                // add options that are added by additional elements, e.g. scales
                additionalOptions.forEach [ opt | 
                  val optData = dataService.getOptionDataBySuffix(opt.key) as LayoutOptionData
                    
                    // if valid, parse its value
                    if (optData != null) {
                        val value = optData.parseValue(opt.value.toString)
                        if (value != null) {
                            currentConfig.setValue(optData, value)
                        }
                    }
                ]
            ]
        ]

        return volatileConfigs
    }

}