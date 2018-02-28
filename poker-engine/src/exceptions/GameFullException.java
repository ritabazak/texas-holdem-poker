package exceptions;

public class GameFullException extends Exception {
    public GameFullException() {
        super("GAME_FULL");
    }
}
