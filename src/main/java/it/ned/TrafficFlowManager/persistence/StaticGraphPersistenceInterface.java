package it.ned.TrafficFlowManager.persistence;

import javax.json.JsonArray;
import javax.json.JsonValue;
import java.io.FileNotFoundException;

public interface StaticGraphPersistenceInterface {
    void saveStaticGraph(String staticGraphName, JsonValue json);
    JsonValue getStaticGraph(String staticGraphName) throws FileNotFoundException;
}
