package servlets;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

class LoginRequest {
    public final String name;
    public final String type;

    public LoginRequest(String name, String type) {
        this.name = name;
        this.type = type;
    }
}

class LoginResponse {
    public final boolean success;
    public final String error;

    public LoginResponse(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public LoginResponse(boolean success) {
        this(success, "");
    }
}

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        Gson gson = new Gson();
        LoginRequest req = gson.fromJson(request.getReader(), LoginRequest.class);
        LoginResponse loginResponse = new LoginResponse(true);

        if (req.name.equals("")) {
            response.setStatus(500);
            loginResponse = new LoginResponse(false, "NAME_EMPTY");
        }
        else if (req.name.equals("Amit")) {
            response.setStatus(500);
            loginResponse = new LoginResponse(false, "NAME_TAKEN");
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(gson.toJson(loginResponse));
            out.flush();
        }
    }
}
