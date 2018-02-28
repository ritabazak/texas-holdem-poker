package exceptions;

public class UsernameAlreadyTakenException extends Exception {
    public UsernameAlreadyTakenException() {
        super("USERNAME_ALREADY_TAKEN");
    }
}