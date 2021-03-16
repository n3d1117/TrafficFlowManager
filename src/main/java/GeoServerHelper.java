import java.io.*;
import java.net.URL;

public class GeoServerHelper {

    private static final String endpoint = "http://localhost:8080/geoserver/rest/";
    private static final String user = "admin";
    private static final String pass = "geoserver";
    private static final String workspace = "tfmsample";
    private static final String datastore = "TFM%20Sample";
    private static final String datastoreEndpoint = endpoint + "workspaces/" + workspace +"/datastores/" + datastore;

    // todo delete
    public static void main(String[] args) throws IOException {
        readDatastore();
    }

    public static void readDatastore() throws IOException {
        URL url = new URL(datastoreEndpoint);
        String response = HTTPHelper.doRequest(url, true);
        System.out.println(response);
    }
}