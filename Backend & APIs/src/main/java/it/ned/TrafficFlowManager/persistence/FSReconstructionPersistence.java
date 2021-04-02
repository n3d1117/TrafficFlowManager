package it.ned.TrafficFlowManager.persistence;

import it.ned.TrafficFlowManager.utils.ConfigProperties;

import javax.json.*;
import java.io.*;

public class FSReconstructionPersistence implements ReconstructionPersistenceInterface {

    private final String jsonDatabasePath;

    public FSReconstructionPersistence() throws IOException {
        jsonDatabasePath = ConfigProperties.getProperties().getProperty("db");

        if (!new File(jsonDatabasePath).exists()) {
            System.out.println("[DB] First write, creating json db...");
            PrintWriter writer = new PrintWriter(jsonDatabasePath, "UTF-8");
            writer.println("[]");
            writer.close();
        }
    }

    @Override
    public void addEntry(JsonObject metadata) throws IOException {
        System.out.println("[DB] Adding entry...");
        try (InputStream inputStream = new FileInputStream(jsonDatabasePath)) {

            JsonReader reader = Json.createReader(inputStream);
            JsonArray mainArray = reader.readArray();

            JsonArrayBuilder builder = Json.createArrayBuilder();
            JsonArray newArray;
            if (mainArray.contains(metadata)) {
                newArray = mainArray;
            } else {
                for (JsonValue existingValue: mainArray)
                    builder.add(existingValue);
                builder.add(metadata);
                newArray = builder.build();
            }

            try (FileWriter fileWriter = new FileWriter(jsonDatabasePath)) {
                fileWriter.write(newArray.toString());
                fileWriter.flush();
            }

            System.out.println("[DB] Done!");
        }
    }

    @Override
    public JsonArray allLayers() throws IOException {
        System.out.println("[DB] Retrieving all layers...");
        try (InputStream inputStream = new FileInputStream(jsonDatabasePath)) {
            return Json.createReader(inputStream).readArray();
        }
    }
}
