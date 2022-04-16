package pl.battleships.core.exception;

public class GameOverException extends RuntimeException {

    public GameOverException() {
        super();
    }

    public GameOverException(String message) {
        super(message);
    }
}
