package it.ned.TrafficFlowManager.persistence;

import javax.json.JsonValue;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface StaticGraphPersistenceInterface {
    void saveStaticGraph(String staticGraphName, JsonValue json) throws IOException;
    JsonValue getStaticGraph(String staticGraphName) throws FileNotFoundException;
}
