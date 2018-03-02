package servlets.request_response.room;

public class ChatMessageResponse {
    private final String author;
    private final String message;

    public ChatMessageResponse(String author, String message) {
        this.author = author;
        this.message = message;
    }
}
