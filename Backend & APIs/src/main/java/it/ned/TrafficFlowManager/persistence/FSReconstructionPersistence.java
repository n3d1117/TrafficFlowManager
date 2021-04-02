package it.ned.TrafficFlowManager.persistence;

import it.ned.TrafficFlowManager.utils.ConfigProperties;

import javax.json.*;
import javax.json.stream.JsonCollectors;
import java.io.*;
import java.util.Map;

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
    public void addEntry(JsonObject metadata, String layerName) throws IOException {
        System.out.println("[DB] Adding entry for layer " + layerName + "...");
        try (InputStream inputStream = new FileInputStream(jsonDatabasePath)) {

            JsonObject newMetadata = appendKeyValueToObject(metadata, "layerName", layerName);

            JsonReader reader = Json.createReader(inputStream);
            JsonArray mainArray = reader.readArray();
            reader.close();

            JsonArrayBuilder builder = Json.createArrayBuilder();
            JsonArray newArray;
            if (mainArray.contains(newMetadata)) {
                newArray = mainArray;
            } else {
                for (JsonValue existingValue: mainArray)
                    builder.add(existingValue);
                builder.add(newMetadata);
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

    @Override
    public JsonArray layers(String fluxName) throws IOException {
        System.out.println("[DB] Retrieving all layers for fluxName " + fluxName + "...");
        try (InputStream inputStream = new FileInputStream(jsonDatabasePath)) {
            JsonArray array = Json.createReader(inputStream).readArray();
            return array
                    .stream()
                    .filter(jsonValue -> jsonValue.asJsonObject().getString("fluxName").equals(fluxName))
                    .collect(JsonCollectors.toJsonArray());
        }
    }

    private JsonObject appendKeyValueToObject(JsonObject obj, String key, String value) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Map.Entry<String, JsonValue> entry : obj.entrySet())
            builder.add(entry.getKey(), entry.getValue());
        builder.add(key,value);
        return builder.build();
    }
}
