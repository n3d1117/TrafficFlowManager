package it.ned.TrafficFlowManager;

import it.ned.TrafficFlowManager.utils.ConfigProperties;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "JsonIndexServlet", value = "/api/json")
public class JsonIndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String reconstructionsFolder = ConfigProperties.getProperties().getProperty("reconstructionsFolder");
        String layerName = request.getParameter("layerName");
        if (layerName != null) {
            response.setHeader("Content-Disposition", "filename=\"" + layerName + "\"");
            String filename = reconstructionsFolder + "/" + layerName + ".json";
            InputStream inputStream = new FileInputStream(filename);
            JsonReader reader = Json.createReader(inputStream);
            JsonValue jsonValue = reader.readValue();
            response.getWriter().write(jsonValue.toString());
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.getWriter().close();
    }
}
