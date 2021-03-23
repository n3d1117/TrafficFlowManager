package it.ned.TrafficFlowManager;

import it.ned.TrafficFlowManager.persistence.FSReconstructionPersistence;

import javax.json.JsonArray;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LayerIndexServlet", value = "/api/layers")
public class LayerIndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        JsonArray layers = new FSReconstructionPersistence().allLayers();
        response.getWriter().write(layers.toString());
        response.getWriter().close();
    }
}
