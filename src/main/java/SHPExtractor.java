
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SHPExtractor {
    public static void main(String[] args) throws IOException {

        // Input file
        File inputFile = new File("output.csv");
        if (!inputFile.exists())
            System.exit(1);

        // Create Feature type
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Traffic Reconstruction");
        builder.setCRS(DefaultGeographicCRS.WGS84); // Coordinate reference system

        builder.add("Segment", LineString.class);

        // Metadata
        builder.add("segment_id", String.class);
        builder.add("road_id", String.class);
        builder.add("start_lat", Double.class);
        builder.add("start_long", Double.class);
        builder.add("end_lat", Double.class);
        builder.add("end_long", Double.class);
        builder.add("lanes", Integer.class);
        builder.add("fipili", Boolean.class);
        builder.add("tr_value", Double.class);
        builder.add("tr_label", String.class);

        // We create a Feature collection into which we will put each Feature created from a record in the input csv
        List<SimpleFeature> collection = new ArrayList<>();

        // GeometryFactory will be used to create the geometry attribute of each feature (LineString for each segment)
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

        // This builder will be used to build features for each segment
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(builder.buildFeatureType());

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {

            // First line of the data file is the header
            String line = reader.readLine();
            System.out.println("Header: " + line);

            for (line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.trim().length() > 0) { // skip blank lines
                    String[] tokens = line.split(",");

                    // Extract metadata
                    String segmentId = tokens[0];
                    String roadId = tokens[1];
                    double startLat = Double.parseDouble(tokens[2]);
                    double startLong = Double.parseDouble(tokens[3]);
                    double endLat = Double.parseDouble(tokens[4]);
                    double endLong = Double.parseDouble(tokens[5]);
                    Integer lanes = Integer.parseInt(tokens[6]);
                    Boolean fipili = Boolean.parseBoolean(tokens[7]);
                    Double trafficValue = Double.parseDouble(tokens[8]);
                    String trafficLabel = tokens[9];

                    // Create LineString geometry type
                    Coordinate[] coords = {new Coordinate(startLong, startLat), new Coordinate(endLong, endLat)};
                    LineString lineString = geometryFactory.createLineString(coords);

                    // Add line string
                    featureBuilder.add(lineString);

                    // Add metadata
                    featureBuilder.add(segmentId);
                    featureBuilder.add(roadId);
                    featureBuilder.add(startLat);
                    featureBuilder.add(startLong);
                    featureBuilder.add(endLat);
                    featureBuilder.add(endLong);
                    featureBuilder.add(lanes);
                    featureBuilder.add(fipili);
                    featureBuilder.add(trafficValue);
                    featureBuilder.add(trafficLabel);

                    // Build feature and add to the collection
                    SimpleFeature feature = featureBuilder.buildFeature(null);
                    collection.add(feature);
                }
            }
        }

        // Get an output file name and write the shapefile
        File outputFile = new File("tfm_sample.shp");
        WriteShapeFile writer = new WriteShapeFile(outputFile);
        writer.writeFeatures(DataUtilities.collection(collection));
    }
}
