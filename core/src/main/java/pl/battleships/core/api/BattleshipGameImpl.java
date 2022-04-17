package pl.battleships.core.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.battleships.core.exception.DuplicatedGameException;
import pl.battleships.core.exception.GameOverException;
import pl.battleships.core.exception.InvalidParamException;
import pl.battleships.core.exception.NoGameFoundException;
import pl.battleships.core.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static pl.battleships.core.model.Ship.Type.*;

@Slf4j
@RequiredArgsConstructor
public class BattleshipGameImpl implements BattleshipGame {

    private static final Integer MIN_BOARD_SIZE = 10;
    private static final Random random = new Random();
    public static final List<Ship.Type> defaultShipList = List.of(Carrier, Battleship, Cruiser, Submarine, Destroyer, Destroyer);
    private final HistoryProvider historyProvider;

    @Override
    public Board joinTheGame(String gameId, int size) {
        log.info("Joining to the game {}", gameId);

        if (historyProvider.findGame(gameId).isPresent()) {
            log.warn("Already in the game {}", historyProvider.getGame(gameId));
            throw new DuplicatedGameException();
        }
        var board = prepareBoard(gameId, size);
        historyProvider.addGame(gameId, board);
        log.info("Joined to the game with id: {} ", gameId);
        return board;
    }

    @Override
    public List<Ship> findShips(String gameId, boolean destroyed) {
        return historyProvider.findGame(gameId)
                .orElseThrow(NoGameFoundException::new)
                .getShips().stream().filter(p -> p.isDestroyed() == destroyed).collect(Collectors.toList());
    }

    @Override
    public ShotResult opponentShot(String gameId, Position position) {
        var board = historyProvider.findGame(gameId);
        if (board.isEmpty()) {
            throw new NoGameFoundException();
        }
        if (board.get().getStatus().equals(GameStatus.OVER)) {
            log.info("Game {} is already over", gameId);
            throw new GameOverException("Game " + gameId + " is already over");
        }
        return opponentShot(board.get(), position);
    }

    private ShotResult opponentShot(Board game, Position position) {
        log.info("Got shot at ({},{}) for game {}", position.getX(), position.getY(), game.getGameId());
        if (Math.max(position.getX(), position.getY()) >= game.getBoard().getSize()) {
            log.error("Invalid shot ({},{}) out off the board", position.getX(), position.getY());
            throw new InvalidParamException("Please shot on board");
        }
        var shotResult = ShotResult.MISSED;
        for (Ship ship : game.getShips()) {
            var shipPosition = ship.getLocation().stream().filter(p -> p.getX().equals(position.getX()) && p.getY().equals(position.getY())).findAny();
            if (shipPosition.isPresent()) {
                shipPosition.get().setHit(Boolean.TRUE);
                shotResult = ShotResult.HIT;
                log.info("Position ({},{}) hit", position.getX(), position.getY());
            } else {
                game.getBoard().missedShot(position.getX(), position.getY());
            }
            if (ship.getLocation().stream().allMatch(Position::isHit)) {
                ship.setDestroyed(Boolean.TRUE);
                shotResult = ShotResult.DESTROYED;
                log.info("Ship '{}' destroyed", ship.getType());
            }
        }
        if (GameStatus.OVER.equals(game.getGameStatus())) {
            shotResult = ShotResult.ALL_DESTROYED;
            log.info("All of ships are destroyed");
        }
        log.info("Game board:\n{}", game.getUpdatedBoard());

        position.setHit(!shotResult.equals(ShotResult.MISSED));

        //put shot in repository
        historyProvider.addShotForGame(game.getGameId(), position);

        return shotResult;
    }

    private Board prepareBoard(String gameId, int size) {
        if (size < MIN_BOARD_SIZE) {
            throw new InvalidParamException("Game should have at least size " + MIN_BOARD_SIZE + "x" + MIN_BOARD_SIZE);
        }
        TwoDimensionalBoard board = new TwoDimensionalBoard(size);
        List<Ship> ships = defaultShipList.stream().map(i ->
                locateShipOnBoard(Dockyard.INSTANCE.build(i), board)
        ).collect(Collectors.toList());

        return Board.builder().board(board).ships(ships).gameId(gameId).status(GameStatus.RUNNING).build();
    }


    private Ship locateShipOnBoard(Ship ship, TwoDimensionalBoard board) {
        log.info("Setting ship with size {}", ship.getType());

        List<Position> freePlacesToLocateShip = getFreePlaces(board);
        int partOfShip = ship.getType();
        while (partOfShip > 0 && freePlacesToLocateShip.size() > 0) {
            Position position = freePlacesToLocateShip.get(random.nextInt(freePlacesToLocateShip.size()));
            ship.getLocation().add(position);
            board.setOnBoard(position.getX(), position.getY(), ship.getType());
            freePlacesToLocateShip = getFreePlacesForPosition(position, board);
            partOfShip--;
        }
        log.info("Board with ships:\n{}", board);
        return ship;
    }

    private List<Position> getFreePlaces(TwoDimensionalBoard board) {
        List<Position> available = new ArrayList<>();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getPositions()[i][j] == null) {
                    available.add(Position.builder().x(i).y(j).hit(Boolean.FALSE).build());
                }
            }
        }
        return available;
    }

    private List<Position> getFreePlacesForPosition(Position position, TwoDimensionalBoard board) {
        List<Position> available = new ArrayList<>();
        for (int i = Math.max(1, position.getX()) - 1; i <= Math.min(position.getX(), board.getSize() - 2) + 1; i++) {
            for (int j = Math.max(1, position.getY()) - 1; j <= Math.min(position.getY(), board.getSize() - 2) + 1; j++) {
                if (board.getPositions()[i][j] == null) {
                    available.add(Position.builder().x(i).y(j).hit(Boolean.FALSE).build());
                }
            }
        }
        log.info("Available positions for ({},{}) : {}", position.getX(), position.getY(), available.stream().map(i -> "(" + i.getX() + "," + i.getY() + ")").collect(Collectors.joining(",")));
        return available;
    }

}
