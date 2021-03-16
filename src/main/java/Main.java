import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Parse arguments
        // Example args: roadsNew.json roadsDensityNew.json output.shp
        String roadsJson = args[0];
        String roadsDensityJson = args[1];
        String outputShp = args[2];

        try {

            System.out.println("[Main] Starting...");

            String csv = "output.csv";

            // Parse both json files and produce a CSV file
            CSVExtractor.extract(roadsJson, roadsDensityJson, csv);

            // Read the CSV file and produce a Shapefile
            SHPExtractor.extract(csv, outputShp);

            // Zip {.dbf, .fix, .prj, .shp, .shx} into output.zip
            String outputFileName = outputShp.substring(0, outputShp.length() - 4);
            List<String> filesToZip = Arrays.asList(
                    outputFileName + ".dbf",
                    outputFileName + ".fix",
                    outputFileName + ".prj",
                    outputFileName + ".shp",
                    outputFileName + ".shx"
            );
            FileZipper.zipFiles(filesToZip, outputFileName + ".zip");

            System.out.println("[Main] Done!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
