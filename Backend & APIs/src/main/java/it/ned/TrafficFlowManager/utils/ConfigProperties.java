package it.ned.TrafficFlowManager.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigProperties {

    /**
     * Parse and return the .properties file as a Properties object
     * @return the Properties object
     * @throws IOException if a .properties file is not found
     */
    public static Properties getProperties() throws IOException {
        Properties prop = new Properties();
        // FileInputStream file = new FileInputStream("/Users/ned/Documents/unifi/BIG_DATA_ARCHITECTURES/TrafficFlowManager/src/main/resources/config.properties");
        FileInputStream file = new FileInputStream("/home/ned/TrafficFlowManager/TrafficFlowManager.properties");
        prop.load(file);
        return prop;
    }
}
