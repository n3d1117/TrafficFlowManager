import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Parse arguments
        // Example args: roadsNew.json roadsDensityNew.json traffic_16032021
        String roadsJson = args[0]; // path to roadsNew.json
        String roadsDensityJson = args[1]; // path to roadsDensityNew.json
        String outputName = args[2]; // output folder name: this will be the name of the layer too

        // Create output folder if needed
        new File(outputName).mkdir();

        try {

            System.out.println("[Main] Starting...");

            String outputShp = outputName + "/" + outputName + ".shp";
            String outputCsv = outputName + "/" + outputName + ".csv";
            String outputZip = outputName + "/" + outputName + ".zip";
            String outputDbf = outputName + "/" + outputName + ".dbf";
            String outputFix = outputName + "/" + outputName + ".fix";
            String outputPrj = outputName + "/" + outputName + ".prj";
            String outputShx = outputName + "/" + outputName + ".shx";

            // Parse both json files and produce a CSV file
            CSVExtractor.extract(roadsJson, roadsDensityJson, outputCsv);

            // Read the CSV file and produce a Shapefile
            SHPExtractor.extract(outputCsv, outputShp);

            // Zip {.dbf, .fix, .prj, .shp, .shx} into output.zip
            List<String> filesToZip = Arrays.asList(outputDbf, outputFix, outputPrj, outputShx, outputShp);
            FileZipper.zipFiles(filesToZip, outputZip);

            // Publish Shapefile as .zip
            // NOTE: specified workspace MUST exist already on the server!!!
            String endpoint = "http://localhost:8080/geoserver/rest";
            String user = "admin";
            String pass = "geoserver";
            String workspace = "traffic";
            String datastore = "ds_" + outputName;
            GeoServerHelper geoServerHelper = new GeoServerHelper(user, pass, endpoint, workspace);
            geoServerHelper.publishShp(datastore, outputZip);

            // Apply correct layer style
            // NOTE: specified style MUST exist already on the server!!!
            String styleName = "road_traffic_style";
            geoServerHelper.setLayerStyle(outputName, styleName);

            System.out.println("[Main] Done!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}