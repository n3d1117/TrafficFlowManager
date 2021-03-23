package it.ned.TrafficFlowManager.persistence;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;

public interface ReconstructionPersistenceInterface {
    void addEntry(JsonObject metadata) throws IOException;
    JsonArray allLayers() throws IOException;
}
