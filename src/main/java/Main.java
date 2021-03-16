import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Parse arguments
        // Example args: roadsNew.json roadsDensityNew.json traffic_16032021
        String roadsJson = args[0];
        String roadsDensityJson = args[1];
        String outputFolder = args[2];

        // Create output folder if needed
        new File(outputFolder).mkdir();

        try {

            System.out.println("[Main] Starting...");

            String outputShp = outputFolder + "/" + outputFolder + ".shp";
            String outputCsv = outputFolder + "/" + outputFolder + ".csv";
            String outputZip = outputFolder + "/" + outputFolder + ".zip";
            String outputDbf = outputFolder + "/" + outputFolder + ".dbf";
            String outputFix = outputFolder + "/" + outputFolder + ".fix";
            String outputPrj = outputFolder + "/" + outputFolder + ".prj";
            String outputShx = outputFolder + "/" + outputFolder + ".shx";

            // Parse both json files and produce a CSV file
            CSVExtractor.extract(roadsJson, roadsDensityJson, outputCsv);

            // Read the CSV file and produce a Shapefile
            SHPExtractor.extract(outputCsv, outputShp);

            // Zip {.dbf, .fix, .prj, .shp, .shx} into output.zip
            List<String> filesToZip = Arrays.asList(outputDbf, outputFix, outputPrj, outputShx, outputShp);
            FileZipper.zipFiles(filesToZip, outputZip);

            // Publish Shapefile
            // NOTE: specified workspace MUST already exist on the server
            String endpoint = "http://localhost:8080/geoserver/rest";
            String user = "admin";
            String pass = "geoserver";
            String workspace = "traffic";
            String datastore = "ds_" + outputFolder;
            GeoServerHelper.publishShp(endpoint, workspace, datastore, user, pass, outputZip);

            // Apply layer style
            // NOTE: specified style MUST already exist on the server

            System.out.println("[Main] Done!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}