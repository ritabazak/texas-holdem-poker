package servlets;

import com.google.gson.Gson;
import managers.EngineManager;
import servlets.request_response.lobby.LobbyResponse;
import servlets.request_response.APIResponse;
import servlets.request_response.EmptyObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LobbyServlet")
public class LobbyServlet extends HttpServlet {

    class AddGameRequest {
        private final String xmlContent;

        AddGameRequest(String xmlContent) {
            this.xmlContent = xmlContent;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        String username = (String)request.getSession().getAttribute("USERNAME");

        EngineManager manager = EngineManager.getEngineFromContext(getServletContext());
        APIResponse<LobbyResponse> res = new APIResponse<>(new LobbyResponse(
                manager.getPlayers(),
                manager.getGames(),
                username
        ));

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            out.print(gson.toJson(res));
            out.flush();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        Gson gson = new Gson();
        EngineManager manager = EngineManager.getEngineFromContext(getServletContext());
        AddGameRequest req = gson.fromJson(request.getReader(), AddGameRequest.class);
        APIResponse<EmptyObject> res;

        try {
            manager.addGame(req.xmlContent, (String)request.getSession().getAttribute("USERNAME"));
            res = APIResponse.newEmptyResponse();
        }
        catch (Exception e){
            res = new APIResponse<>(e.getMessage());
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(gson.toJson(res));
            out.flush();
        }

    }
}