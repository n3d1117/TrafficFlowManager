import java.io.*;
import java.net.URL;

public class GeoServerHelper {

    // Uploads shp to the data store, creating it if necessary
    // Doc: https://docs.geoserver.org/latest/en/api/#/latest/en/api/1.0.0/datastores.yaml
    public static void publishShp(String endpoint, String workspace, String datastore,
                                  String user, String pass, String zipFile) throws IOException {
        System.out.println("[GeoServerHelper] Publishing shapefile to datastore " + datastore + "...");
        String datastoreEndpoint = endpoint + "/workspaces/" + workspace + "/datastores/" + datastore;
        URL url = new URL(datastoreEndpoint + "/file.shp");
        Integer response = HTTPHelper.doRequest(url, "PUT", "application/zip", user, pass, zipFile);
        System.out.println("[GeoServerHelper] Got response: " + response.toString());
    }
}