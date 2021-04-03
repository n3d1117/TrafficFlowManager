package it.ned.TrafficFlowManager.persistence;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;

public interface ReconstructionPersistenceInterface {
    void addEntry(JsonObject metadata, String layerName) throws IOException;
    JsonArray allLayersClustered() throws IOException;
    JsonArray layersForFluxName(String fluxName) throws IOException;
}
