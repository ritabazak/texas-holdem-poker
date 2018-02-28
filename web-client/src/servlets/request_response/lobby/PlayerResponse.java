package servlets.request_response.lobby;

public class PlayerResponse {
    private final int id;
    private final String name;
    private final String type;

    public PlayerResponse(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
