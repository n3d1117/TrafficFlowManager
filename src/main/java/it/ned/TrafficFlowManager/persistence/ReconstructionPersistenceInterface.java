package it.ned.TrafficFlowManager.persistence;

import javax.json.JsonObject;

public interface ReconstructionPersistenceInterface {
    void addEntry(JsonObject metadata);
    // TODO: list all entries
}
