package pl.battleships.javaspringship.exception;

public class InvalidParamException extends RuntimeException {

    public InvalidParamException(String message) {
        super(message);
    }
}