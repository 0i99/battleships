package pl.battleships.javaspringship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.battleships.api.model.Game;
import pl.battleships.api.model.Position;
import pl.battleships.api.model.Ship;
import pl.battleships.api.model.ShotResult;
import pl.battleships.javaspringship.exception.DuplicatedGameException;
import pl.battleships.javaspringship.exception.InvalidParamException;
import pl.battleships.javaspringship.exception.NoGameFoundException;
import pl.battleships.javaspringship.model.Board;
import pl.battleships.javaspringship.model.BoardReadyToPlay;

import java.util.*;
import java.util.stream.Collectors;

import static pl.battleships.api.model.Ship.TypeEnum.*;

@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    /**
     * Minimum game board size 10x10
     */
    private static final Integer MIN_BOARD_SIZE = 10;
    private static final Random random = new Random();
    private Map<String, BoardReadyToPlay> games = new HashMap<>();
    public static final List<Ship.TypeEnum> defaultShipList = List.of(Carrier, Battleship, Cruiser, Submarine, Destroyer, Destroyer);

    private final ShotService shotService;

    @Override
    public BoardReadyToPlay joinTheGame(Game game) {
        log.info("Joining to the game {}", game);
        if (games.containsKey(game.getId())) {
            log.warn("Already in the game {}", games.get(game.getId()));
            throw new DuplicatedGameException();
        }
        var board = prepareBoard(game);
        games.put(game.getId(), board);
        log.info("Joined to the game with id: {} ", game.getId());
        return board;
    }

    @Override
    public List<Ship> findShips(String gameId, boolean destroyed) {
        return Optional.ofNullable(games.get(gameId))
                .orElseThrow(NoGameFoundException::new)
                .getShips().stream().filter(p -> p.getDestroyed().equals(destroyed)).collect(Collectors.toList());
    }

    @Override
    public ShotResult shot(String gameId, Position position) {
        var game = Optional.ofNullable(games.get(gameId)).orElseThrow(NoGameFoundException::new);
        return shotService.shot(game, position);
    }

    private BoardReadyToPlay prepareBoard(Game game) {
        if (game.getSize() < MIN_BOARD_SIZE) {
            throw new InvalidParamException("Game should have at least size " + MIN_BOARD_SIZE + "x" + MIN_BOARD_SIZE);
        }
        Board board = new Board(game.getSize());
        List<Ship> ships = defaultShipList.stream().map(i ->
                locateShipOnBoard(Dockyard.INSTANCE.build(i), board)
        ).collect(Collectors.toList());

        return BoardReadyToPlay.builder().board(board).ships(ships).gameId(game.getId()).build();
    }


    private Ship locateShipOnBoard(Ship ship, Board board) {
        log.info("Setting ship with size {}", ship.getType().getValue());

        List<Position> freePlacesToLocateShip = getFreePlaces(board);
        int partOfShip = ship.getType().getValue();
        while (partOfShip > 0) {
            Position position = freePlacesToLocateShip.get(random.nextInt(freePlacesToLocateShip.size()));
            ship.getLocaction().add(position);
            board.setOnBoard(position.getX(), position.getY(), ship.getType().getValue());
            freePlacesToLocateShip = getFreePlacesForPosition(position, board);
            partOfShip--;
        }
        log.info("Board with ships:\n{}", board);
        return ship;
    }

    private List<Position> getFreePlaces(Board board) {
        List<Position> available = new ArrayList<>();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getPositions()[i][j] == null) {
                    available.add(new Position().x(i).y(j).hit(Boolean.FALSE));
                }
            }
        }
        return available;
    }

    private List<Position> getFreePlacesForPosition(Position position, Board board) {
        List<Position> available = new ArrayList<>();
        for (int i = Math.max(1, position.getX()) - 1; i <= Math.min(position.getX(), board.getSize() - 2) + 1; i++) {
            for (int j = Math.max(1, position.getY()) - 1; j <= Math.min(position.getY(), board.getSize() - 2) + 1; j++) {
                if (board.getPositions()[i][j] == null) {
                    available.add(new Position().x(i).y(j).hit(Boolean.FALSE));
                }
            }
        }
        log.info("Available positions for ({},{}) : {}", position.getX(), position.getY(), available.stream().map(i -> "(" + i.getX() + "," + i.getY() + ")").collect(Collectors.joining(",")));
        return available;
    }

}
