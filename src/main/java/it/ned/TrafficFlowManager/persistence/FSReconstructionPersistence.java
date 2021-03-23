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
    public void addEntry(JsonObject metadata) {
        System.out.println("[DB] Adding entry...");
        try (InputStream inputStream = new FileInputStream(jsonDatabasePath)) {

            JsonReader reader = Json.createReader(inputStream);
            JsonArray mainArray = reader.readArray();

            JsonArrayBuilder builder = Json.createArrayBuilder();
            for (JsonValue existingValue: mainArray)
                builder.add(existingValue);
            builder.add(metadata);
            JsonArray newArray = builder.build();

            FileWriter fileWriter = new FileWriter(jsonDatabasePath);
            fileWriter.write(newArray.toString());
            fileWriter.flush();
            fileWriter.close();

            System.out.println("[DB] Done!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
