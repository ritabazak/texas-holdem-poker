package servlets;

import com.google.gson.Gson;
import managers.EngineManager;
import servlets.request_response.APIResponse;
import servlets.request_response.EmptyObject;
import servlets.request_response.room.BetRequest;
import servlets.request_response.room.ChatRequest;
import servlets.request_response.room.RoomResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RoomServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        EngineManager manager = EngineManager.getEngineFromContext(getServletContext());
        APIResponse<EmptyObject> res;

        String username = (String)request.getSession().getAttribute("USERNAME");
        int gameId = Integer.valueOf(request.getParameter("id"));

        Gson gson = new Gson();
        BetRequest betRequest;
        ChatRequest chatRequest;

        try {
            switch (request.getParameter("method")) {
                case "JOIN":
                    manager.joinGame(gameId, username);
                    break;
                case "LEAVE":
                    manager.leaveGame(gameId, username);
                    break;
                case "READY":
                    manager.setPlayerReady(gameId, username, true);
                    break;
                case "UNREADY":
                    manager.setPlayerReady(gameId, username, false);
                    break;
                case "BUYIN":
                    manager.buyIn(gameId, username);
                    break;
                case "FOLD":
                    manager.fold(gameId);
                    break;
                case "CHECK":
                    manager.check(gameId);
                    break;
                case "CALL":
                    manager.call(gameId);
                    break;
                case "BET":
                    betRequest = gson.fromJson(request.getReader(), BetRequest.class);
                    manager.bet(gameId, betRequest.amount);
                    break;
                case "RAISE":
                    betRequest = gson.fromJson(request.getReader(), BetRequest.class);
                    manager.raise(gameId, betRequest.amount);
                    break;
                case "ADD_MESSAGE":
                    chatRequest = gson.fromJson(request.getReader(), ChatRequest.class);
                    manager.addMessage(gameId, username, chatRequest.message);
                    break;
            }

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        int gameId = Integer.valueOf(request.getParameter("id"));
        String username = (String)request.getSession().getAttribute("USERNAME");

        EngineManager manager = EngineManager.getEngineFromContext(getServletContext());
        APIResponse<RoomResponse> res = new APIResponse<>(new RoomResponse(
                manager.getGameInfo(gameId),
                manager.getHandInfo(gameId, username),
                username
        ));

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            out.print(gson.toJson(res));
            out.flush();
        }
    }
}
