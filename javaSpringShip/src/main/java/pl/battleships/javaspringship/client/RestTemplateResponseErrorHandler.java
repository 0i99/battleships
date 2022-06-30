package pl.battleships.javaspringship.client;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import pl.battleships.core.exception.GameOverException;
import pl.battleships.core.exception.InvalidMoveException;
import pl.battleships.core.exception.NoGameFoundException;

import java.io.IOException;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        switch (response.getStatusCode()) {
            case NOT_FOUND:
                throw new NoGameFoundException();
            case GONE:
                throw new GameOverException();
            case FORBIDDEN:
                throw new InvalidMoveException();
        }
    }
}
