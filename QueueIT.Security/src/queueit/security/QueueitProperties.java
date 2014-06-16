/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package queueit.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author mala_000
 */
public class QueueitProperties {
    
    private static Map<String, Properties> properties = new HashMap<String, Properties>(); 
    
    public static Properties getProperties(String id) throws IOException {
        if (!properties.containsKey(id)) {
        
            InputStream configFile = null;
        
            try {
                // Load the properties
                Properties props = new Properties();
                ClassLoader classLoader = QueueitProperties.class.getClassLoader();     
                configFile = classLoader.getResourceAsStream(id);
                if (configFile == null) {
                    throw new IOException("Config file '" + id + "' was not loaded");
                }

                props.load(configFile);
                properties.put(id, props);
            } 
            finally {
                if (configFile != null) {
                    try {
                        configFile.close();
                    } catch(Exception ex) {
                       //ignore
                    }
                }         
            }
        }
        
        return properties.get(id);
    }
}
