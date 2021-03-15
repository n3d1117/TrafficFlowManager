public class Main {

    public static void main(String[] args) {

        // Parse arguments
        // Example args: roadsNew.json roadsDensityNew.json output.csv output.shp
        String roadsJson = args[0];
        String roadsDensityJson = args[1];
        String outputCsv = args[2];
        String outputShp = args[3];

        try {

            // Parse both json files and produce a CSV file
            CSVExtractor.extract(roadsJson, roadsDensityJson, outputCsv);

            // Read the CSV file and produce a Shapefile
            SHPExtractor.extract(outputCsv, outputShp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
