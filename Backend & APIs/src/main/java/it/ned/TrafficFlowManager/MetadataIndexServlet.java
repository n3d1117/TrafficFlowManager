package it.ned.TrafficFlowManager;

import it.ned.TrafficFlowManager.persistence.JSONReconstructionPersistence;
import it.ned.TrafficFlowManager.persistence.ReconstructionPersistenceInterface;

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
        ReconstructionPersistenceInterface db = new JSONReconstructionPersistence();
        if (fluxName == null) {
            result = db.allLayersClustered();
        } else {
            result = db.layersForFluxName(fluxName);
        }
        response.getWriter().write(result.toString());
        response.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        String action = req.getParameter("action");

        ReconstructionPersistenceInterface db = new JSONReconstructionPersistence();

        switch (action) {
            case "change_color_map": {
                String fluxName = req.getParameter("id");
                String newColorMap = req.getParameter("valore");
                db.changeColorMapForFluxName(fluxName, newColorMap);
                resp.setStatus(HttpServletResponse.SC_OK);
                break;
            }
            case "delete_metadata": {
                String fluxName = req.getParameter("id");
                db.deleteFlux(fluxName);
                resp.setStatus(HttpServletResponse.SC_OK);
                break;
            }
            case "delete_data":
                String layerName = req.getParameter("id");
                db.deleteLayer(layerName);
                resp.setStatus(HttpServletResponse.SC_OK);
                break;
            default:
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                break;
        }

        resp.getWriter().close();
    }
}
