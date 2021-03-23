package it.ned.TrafficFlowManager.persistence;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ReconstructionPersistenceInterface {
    void addEntry(JsonObject metadata);
    JsonArray allLayers() throws IOException;
}
