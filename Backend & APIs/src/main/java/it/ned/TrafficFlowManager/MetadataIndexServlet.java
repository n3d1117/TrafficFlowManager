package it.ned.TrafficFlowManager;

import it.ned.TrafficFlowManager.persistence.FSReconstructionPersistence;

import javax.json.JsonArray;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "MetadataIndexServlet", value = "/api/metadata")
public class MetadataIndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String fluxName = request.getParameter("fluxName");

        JsonArray result;
        if (fluxName == null) {
            result = new FSReconstructionPersistence().allLayersClustered();
        } else {
            result = new FSReconstructionPersistence().layersForFluxName(fluxName);
        }
        response.getWriter().write(result.toString());
        response.getWriter().close();
    }
}
