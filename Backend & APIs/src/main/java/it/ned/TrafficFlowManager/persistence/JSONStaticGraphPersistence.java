package it.ned.TrafficFlowManager.persistence;

import it.ned.TrafficFlowManager.utils.ConfigProperties;

import javax.json.*;
import java.io.*;

public class JSONStaticGraphPersistence implements StaticGraphPersistenceInterface {

    private final String staticGraphFolder;

    public JSONStaticGraphPersistence() throws IOException {
        staticGraphFolder = ConfigProperties.getProperties().getProperty("staticGraphsFolder");
        new File(staticGraphFolder).mkdir();
    }

    @Override
    public void saveStaticGraph(String staticGraphName, JsonValue json) throws IOException {
        System.out.println("[SDB] Saving static graph " + staticGraphName);
        String filename = staticGraphFolder + "/" + staticGraphName + ".json";
        try (FileWriter file = new FileWriter(filename)) {
            file.write(json.toString());
            file.flush();
        }
    }

    @Override
    public JsonValue getStaticGraph(String staticGraphName) throws FileNotFoundException {
        System.out.println("[SDB] retrieving static graph " + staticGraphName);
        String filename = staticGraphFolder + "/" + staticGraphName + ".json";
        InputStream inputStream = new FileInputStream(filename);
        JsonReader reader = Json.createReader(inputStream);
        return reader.readValue();
    }
}
