package servlets.request_response.login;

public class LoginRequest {
    public final String name;
    public final String type;

    public LoginRequest(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
