package pl.battleships.javaspringship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.battleships.api.model.Position;
import pl.battleships.api.model.Ship;
import pl.battleships.api.model.ShotResult;
import pl.battleships.javaspringship.exception.InvalidParamException;
import pl.battleships.javaspringship.model.BoardReadyToPlay;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class ShotServiceImpl implements ShotService {

    private final Map<String, List<Position>> shots = new HashMap<>();

    @Override
    public ShotResult shot(BoardReadyToPlay game, Position position) {
        log.info("Got shot at ({},{}) for game {}", position.getX(), position.getY(), game.getGameId());
        if (Math.max(position.getX(), position.getY()) >= game.getBoard().getSize()) {
            log.error("Invalid shot ({},{}) out off the board", position.getX(), position.getY());
            throw new InvalidParamException("Please shot on board");
        }
        var shotResult = ShotResult.MISSED;
        for (Ship ship : game.getShips()) {
            var shipPosition = ship.getLocaction().stream().filter(p -> p.getX().equals(position.getX()) && p.getY().equals(position.getY())).findAny();
            if (shipPosition.isPresent()) {
                shipPosition.get().setHit(Boolean.TRUE);
                shotResult = ShotResult.HIT;
                log.info("Position ({},{}) hit", position.getX(), position.getY());
            } else {
                game.getBoard().missedShot(position.getX(), position.getY());
            }
            if (ship.getLocaction().stream().allMatch(Position::getHit)) {
                ship.destroyed(Boolean.TRUE);
                shotResult = ShotResult.DESTROYED;
                log.info("Ship '{}' destroyed", ship.getType().name());
            }
        }
        log.info("Game board:\n{}", game.getBoard());

        saveShot(game.getGameId(), position, shotResult);
        //fixme: send shot to stream ex. kafka

        return shotResult;
    }

    @Override
    public List<Position> getAllShots(String gameId) {
        return Optional.ofNullable(shots.get(gameId)).orElse(Collections.emptyList());
    }

    private void saveShot(String gameId, Position position, ShotResult shotResult) {
        if (!shots.containsKey(gameId)) {
            shots.put(gameId, new ArrayList<>());
        }
        position.setHit(!shotResult.equals(ShotResult.MISSED));
        shots.get(gameId).add(position);
    }


}
