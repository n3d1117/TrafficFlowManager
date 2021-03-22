package it.ned.TrafficFlowManager;

import it.ned.TrafficFlowManager.utils.CSVExtractor;
import it.ned.TrafficFlowManager.utils.FileZipper;
import it.ned.TrafficFlowManager.utils.GeoServerHelper;
import it.ned.TrafficFlowManager.utils.SHPExtractor;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "uploadLayerServlet", value = "/upload")
public class UploadLayerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write("please do a POST request instead");
        resp.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        try {
            /*String user = req.getParameter("username");*/

            heavyLifting();

            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("success", true)
                    .build();
            resp.getWriter().print(jsonResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("success", false)
                    .add("error", e.getLocalizedMessage())
                    .build();
            resp.getWriter().print(jsonResponse.toString());
        }
    }

    public void destroy() {
    }

    private void heavyLifting() throws Exception {

        String roadsJson = "/Users/ned/IdeaProjects/TomcatServletDemo/roadsNew.json";
        String roadsDensityJson = "/Users/ned/IdeaProjects/TomcatServletDemo/roadsDensityNew.json";
        String layerId = "traffic_200320211950";
        String outputName = "/Users/ned/IdeaProjects/TomcatServletDemo/" + layerId;

        new File(outputName).mkdir();

        System.out.println("[Main] Starting...");

        String outputShp = outputName + "/" + layerId + ".shp";
        String outputCsv = outputName + "/" + layerId + ".csv";
        String outputZip = outputName + "/" + layerId + ".zip";
        String outputDbf = outputName + "/" + layerId + ".dbf";
        String outputFix = outputName + "/" + layerId + ".fix";
        String outputPrj = outputName + "/" + layerId + ".prj";
        String outputShx = outputName + "/" + layerId + ".shx";

        // Parse both json files and produce a CSV file
        CSVExtractor.extract(roadsJson, roadsDensityJson, outputCsv);

        // Read the CSV file and produce a Shapefile
        SHPExtractor.extract(outputCsv, outputShp);

        // Zip {.dbf, .fix, .prj, .shp, .shx} into output.zip
        List<String> filesToZip = Arrays.asList(outputDbf, outputFix, outputPrj, outputShx, outputShp);
        FileZipper.zipFiles(filesToZip, outputZip);

        // Publish Shapefile as .zip
        // NOTE: specified workspace MUST exist already on the server!!!
        Properties properties = getProperties();
        String endpoint = properties.getProperty("geoServerUrl");
        String user = properties.getProperty("geoServerUser");
        String pass = properties.getProperty("geoServerPass");
        String workspace = properties.getProperty("geoServerWorkspace");
        String datastore = "ds_" + layerId;
        GeoServerHelper geoServerHelper = new GeoServerHelper(user, pass, endpoint, workspace);
        geoServerHelper.publishShp(datastore, outputZip);

        // Apply correct layer style
        // NOTE: specified style MUST exist already on the server!!!
        String styleName = properties.getProperty("geoServerStyleName");
        geoServerHelper.setLayerStyle(layerId, styleName);

        System.out.println("[Main] Done!");
    }

    private Properties getProperties() throws IOException {
        Properties prop = new Properties();
        FileInputStream file = new FileInputStream("/Users/ned/IdeaProjects/TomcatServletDemo/src/main/resources/config.properties");
        prop.load(file);
        return prop;
    }
}