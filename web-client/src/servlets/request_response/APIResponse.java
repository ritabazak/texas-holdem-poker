package servlets.request_response;

public class APIResponse<T> {
    public static APIResponse<EmptyObject> newEmptyResponse() {
        return new APIResponse<>(new EmptyObject());
    }

    public final boolean success;
    public final String error;
    public final T data;

    public APIResponse(String error) {
        this.success = false;
        this.error = error;
        data = null;
    }

    public APIResponse(T data) {
        success = true;
        error = null;
        this.data = data;
    }
}