package server.actions;

public class NoDefaultActionException extends RuntimeException {
    public NoDefaultActionException() {
        super("No default action has been configured");
    }
}
