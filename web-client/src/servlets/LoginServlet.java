package servlets;

import com.google.gson.Gson;
import managers.EngineManager;
import servlets.request_response.login.LoginRequest;
import servlets.request_response.EmptyObject;
import servlets.request_response.APIResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        Gson gson = new Gson();
        LoginRequest req = gson.fromJson(request.getReader(), LoginRequest.class);
        APIResponse<EmptyObject> res;

        EngineManager manager = EngineManager.getEngineFromContext(getServletContext());

        try {
            manager.addPlayer(req.name, req.type);
            request.getSession().setAttribute("USERNAME", req.name);
            res = APIResponse.newEmptyResponse();
        }
        catch (Exception e){
            response.setStatus(500);
            res = new APIResponse<>(e.getMessage());
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(gson.toJson(res));
            out.flush();
        }
    }
}
