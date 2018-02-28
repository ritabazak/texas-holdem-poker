package exceptions;

public class GameAlreadyInProgressException extends Exception {
    public GameAlreadyInProgressException() {
        super("GAME_ALREADY_IN_PROGRESS");
    }
}
