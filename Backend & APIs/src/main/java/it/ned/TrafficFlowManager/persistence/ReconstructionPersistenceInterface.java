package it.ned.TrafficFlowManager.persistence;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.IOException;

public interface ReconstructionPersistenceInterface {
    void addEntry(JsonObject metadata, String layerName) throws IOException;
    void saveReconstructionAsJson(JsonValue json, String layerName) throws IOException;
    JsonArray allLayersClustered() throws IOException;
    JsonArray layersForFluxName(String fluxName) throws IOException;
    void changeColorMapForFluxName(String fluxName, String colorMap) throws IOException;
    void deleteFlux(String fluxName) throws IOException;
    void deleteLayer(String layerName) throws IOException;
}
