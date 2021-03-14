import javax.json.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        JsonArray roadsArray = getRoads("src/main/resources/roadsNew.json");
        JsonObject roadsDensityObject = getRoadsDensity("src/main/resources/roadsDensityNew.json");

        try (PrintWriter writer = new PrintWriter("output.csv")) {

            StringBuilder sb = new StringBuilder();
            sb.append("segment_id,road_id,start_lat,start_long,end_lat,end_long,lanes,fipili,traffic_value,traffic_label");
            sb.append('\n');

            for (JsonValue road: roadsArray) {
                JsonObject roadObject = road.asJsonObject();

                String roadId = roadObject.getString("road");
                JsonObject roadDensity = roadsDensityObject.getJsonObject(roadId).getJsonArray("data").getJsonObject(0);

                for (JsonValue segment: roadObject.getJsonArray("segments")) {
                    JsonObject segmentObj = segment.asJsonObject();

                    String segmentId = segmentObj.getString("id");
                    String startLat = segmentObj.getJsonObject("start").getString("lat");
                    String startLon = segmentObj.getJsonObject("start").getString("long");
                    String endLat = segmentObj.getJsonObject("end").getString("lat");
                    String endLon = segmentObj.getJsonObject("end").getString("long");
                    Integer lanes = Integer.parseInt(segmentObj.getString("lanes"));
                    Integer fipili = segmentObj.getInt("FIPILI");
                    String trafficValue = roadDensity.getString(segmentId);
                    String trafficLabel = extractTrafficLabel(trafficValue, lanes, fipili);

                    sb.append(segmentId).append(",");
                    sb.append(roadId).append(",");
                    sb.append(startLat).append(",");
                    sb.append(startLon).append(",");
                    sb.append(endLat).append(",");
                    sb.append(endLon).append(",");
                    sb.append(lanes).append(",");
                    sb.append(fipili).append(",");
                    sb.append(trafficValue).append(",");
                    sb.append(trafficLabel);
                    sb.append("\n");
                }
            }
            writer.write(sb.toString());
        }
    }

    // Extract traffic label from traffic value
    private static String extractTrafficLabel(String trafficValue, Integer lanes, Integer fipili) {
        double doubleValue = Double.parseDouble(trafficValue.replace(',', '.'));

        double green = 0.3;
        double yellow = 0.6;
        double orange = 0.9;

        if (lanes == 2) {
            green = 0.6;
            yellow = 1.2;
            orange = 1.8;
        }
        if (fipili == 1) {
            green=0.25;
            yellow=0.5;
            orange=0.75;
        }
        if (lanes == 3) {
            green = 0.9;
            yellow = 1.5;
            orange = 2.0;
        }
        if (lanes == 4) {
            green = 1.2;
            yellow = 1.6;
            orange = 2.0;
        }
        if (lanes == 5) {
            green = 1.6;
            yellow = 2.0;
            orange = 2.4;
        }
        if (lanes == 6) {
            green = 2.0;
            yellow = 2.4;
            orange = 2.8;
        }

        if (doubleValue <= green) {
            return "green";
        } else if (doubleValue <= yellow) {
            return "yellow";
        } else if (doubleValue <= orange) {
            return "orange";
        } else {
            return "red";
        }
    }

    // Parse roadsNew.json
    // Order is Array -> String -> Array
    private static JsonArray getRoads(String input) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(input);
        JsonReader reader = Json.createReader(inputStream);
        String mainArrayString = reader.readArray().getJsonString(0).getString();
        reader = Json.createReader(new StringReader(mainArrayString));
        JsonArray roadsArray = reader.readArray();
        reader.close();
        return roadsArray;
    }

    // Parse roadsDensityNew.json
    // Order is Array -> String -> Object
    private static JsonObject getRoadsDensity(String input) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(input);
        JsonReader reader = Json.createReader(inputStream);
        String mainArrayString = reader.readArray().getJsonString(0).getString();
        reader = Json.createReader(new StringReader(mainArrayString));
        JsonObject roadsDensityObject = reader.readObject();
        reader.close();
        return roadsDensityObject;
    }

}
