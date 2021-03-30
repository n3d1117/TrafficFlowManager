package it.ned.TrafficFlowManager.utils;

public class GeoServerHelper {

    private final String user;
    private final String pass;
    private final String endpoint;
    private final String workspace;

    public GeoServerHelper(String user, String pass, String endpoint, String workspace) {
        this.user = user;
        this.pass = pass;
        this.endpoint = endpoint;
        this.workspace = workspace;
    }

    /* Uploads shp to the data store, creating it if necessary
       Doc: https://docs.geoserver.org/latest/en/api/#/latest/en/api/1.0.0/datastores.yaml

       This is the equivalent of:

        curl -v -u user:pass -XPUT -H "Content-type: application/zip"
        --data-binary @zipFile.zip
        http://localhost:8080/geoserver/rest/workspaces/workspace/datastores/datastore/file.shp

       Expected HTTP Response: 201 Created
    */
    public void publishShp(String datastore, String zipFile) throws Exception {
        System.out.println("[GeoServerHelper] Publishing shapefile to datastore " + datastore + "...");
        String datastoreEndpoint = endpoint + "/workspaces/" + workspace + "/datastores/" + datastore;
        String urlString = datastoreEndpoint + "/file.shp";
        Integer response = HTTPHelper.uploadFile(urlString, "application/zip", user, pass, zipFile);
        if (response != 201) {
            throw new Exception("Failed to publish shapefile. Response code: " + response.toString());
        }
    }

    /* Set the specified style to the specified layer
       Doc: https://docs.geoserver.org/latest/en/api/#/latest/en/api/1.0.0/layers.yaml
       This is the equivalent of:

        curl -v -u user:pass -XPUT -H "Content-type: text/xml"
        -d "<layer><defaultStyle><name>style_name</name></defaultStyle></layer>"
        http://localhost:8080/geoserver/rest/layers/workspace:layer

       Expected HTTP Response: 200 OK
    */
    public void setLayerStyle(String layerName, String styleName) throws Exception {
        System.out.println("[GeoServerHelper] Applying style " + styleName + " to layer " + layerName + "...");
        String urlString = endpoint + "/layers/" + workspace + ":" + layerName;
        String xmlData = "<layer><defaultStyle><name>" + styleName + "</name></defaultStyle></layer>";
        Integer response = HTTPHelper.uploadData(urlString, "text/xml", user, pass, xmlData);
        if (response != 200) {
            throw new Exception("Failed to set layer style. Response code: " + response.toString());
        }
    }
}