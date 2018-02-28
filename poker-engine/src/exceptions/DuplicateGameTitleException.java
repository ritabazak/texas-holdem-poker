package exceptions;

public class DuplicateGameTitleException extends Exception {
    public DuplicateGameTitleException() {
        super("DUPLICATE_GAME_TITLE");
    }
}
