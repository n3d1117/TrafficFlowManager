package it.ned.TrafficFlowManager.persistence;

import it.ned.TrafficFlowManager.utils.ConfigProperties;

import javax.json.*;
import javax.json.stream.JsonCollectors;
import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JSONReconstructionPersistence implements ReconstructionPersistenceInterface {

    private final String jsonDatabasePath;

    public JSONReconstructionPersistence() throws IOException {
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

            // Append layer name to metadata object
            JsonObject newMetadata = appendKeyValueToObject(metadata, "layerName", layerName);

            // Append object and build response array
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

            // Write response
            try (FileWriter fileWriter = new FileWriter(jsonDatabasePath)) {
                fileWriter.write(newArray.toString());
                fileWriter.flush();
            }

            System.out.println("[DB] Done!");
        }
    }

    @Override
    public JsonArray allLayersClustered() throws IOException {
        System.out.println("[DB] Retrieving all layers clustered...");
        try (InputStream inputStream = new FileInputStream(jsonDatabasePath)) {

            JsonArray array = Json.createReader(inputStream).readArray();
            JsonBuilderFactory factory = Json.createBuilderFactory(null);
            JsonArrayBuilder builder = Json.createArrayBuilder();

            Set<String> set = new HashSet<>();
            array.forEach(item -> {
                String fluxName = item.asJsonObject().getString("fluxName");
                if (set.add(fluxName)) {
                    long instances = array
                            .stream()
                            .filter(jsonValue -> jsonValue.asJsonObject().getString("fluxName").equals(fluxName))
                            .count();
                    builder.add(factory.createObjectBuilder()
                            .add("fluxName", fluxName)
                            .add("locality", item.asJsonObject().getString("locality"))
                            .add("organization", item.asJsonObject().getString("organization"))
                            .add("scenarioID", item.asJsonObject().getString("scenarioID"))
                            .add("colorMap", item.asJsonObject().getString("colorMap"))
                            .add("instances", instances)
                            .add("locality", item.asJsonObject().getString("locality"))
                            .add("metricName", item.asJsonObject().getString("metricName"))
                            .add("unitOfMeasure", item.asJsonObject().getString("unitOfMeasure"))
                            .add("staticGraphName", item.asJsonObject().getString("staticGraphName"))
                    );
                }

            });

            return builder.build();
        }
    }

    @Override
    public JsonArray layersForFluxName(String fluxName) throws IOException {
        System.out.println("[DB] Retrieving all layers for fluxName " + fluxName + "...");
        try (InputStream inputStream = new FileInputStream(jsonDatabasePath)) {
            JsonArray array = Json.createReader(inputStream).readArray();
            return array
                    .stream()
                    .filter(jsonValue -> jsonValue.asJsonObject().getString("fluxName").equals(fluxName))
                    .collect(JsonCollectors.toJsonArray());
        }
    }

    @Override
    public void changeColorMapForFluxName(String fluxName, String newColorMap) throws IOException {
        System.out.println("[DB] Changing color map to " + newColorMap + " for fluxName " + fluxName + "...");

        try (InputStream inputStream = new FileInputStream(jsonDatabasePath)) {
            JsonArray array = Json.createReader(inputStream).readArray();

            // Substitute value
            JsonArrayBuilder builder = Json.createArrayBuilder();
            for (JsonValue value: array) {
                if (value.asJsonObject().getString("fluxName").equals(fluxName)) {
                    builder.add(substituteValueToObject(value.asJsonObject(), "colorMap", newColorMap));
                } else {
                    builder.add(value);
                }
            }

            // Write changes to file
            try (FileWriter fileWriter = new FileWriter(jsonDatabasePath)) {
                fileWriter.write(builder.build().toString());
                fileWriter.flush();
            }
        }
    }

    private JsonObject appendKeyValueToObject(JsonObject obj, String key, String value) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Map.Entry<String, JsonValue> entry : obj.entrySet())
            builder.add(entry.getKey(), entry.getValue());
        builder.add(key,value);
        return builder.build();
    }

    private JsonObject substituteValueToObject(JsonObject obj, String key, String newValue) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Map.Entry<String, JsonValue> entry : obj.entrySet()) {
            if (entry.getKey().equals(key))
                builder.add(key, newValue);
            else
                builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }
}
