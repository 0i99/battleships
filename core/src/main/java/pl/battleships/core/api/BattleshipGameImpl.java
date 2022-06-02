package pl.battleships.core.api;

import com.github.javafaker.Faker;
import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import pl.battleships.core.exception.*;
import pl.battleships.core.model.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static pl.battleships.core.model.Ship.Type.*;

@Slf4j
public class BattleshipGameImpl implements BattleshipGame {

    private static final Integer MIN_BOARD_SIZE = 10;
    public static final List<Ship.Type> defaultShipList = List.of(Carrier, Battleship, Cruiser, Submarine, Destroyer, Destroyer);
    private static final Random random = new Random();
    private final Map<String, Semaphore> semaphores = new HashMap<>();
    private final Map<String, Board> boards = new HashMap<>();
    private static final String MDC_PLAYER_MARKER = "PLAYER";
    private static final String MDC_GAME_MARKER = "GAME";
    private final String player;

    private final HistoryProvider historyProvider;
    private final ShotHandler shotHandler;
    private final ExecutorService executorService;
    private final ListeningExecutorService listeningExecutorService;

    public BattleshipGameImpl(HistoryProvider historyProvider, ShotHandler shotHandler) {
        this.historyProvider = historyProvider;
        this.shotHandler = shotHandler;
        this.executorService = Executors.newSingleThreadExecutor();
        this.listeningExecutorService = MoreExecutors.listeningDecorator(this.executorService);
        this.player = new Faker().funnyName().name();
    }

    @Override
    public Board start(String gameId, int size, boolean firstShotIsYours) {
        try {
            MDC.put(MDC_PLAYER_MARKER, getMdcMarker());
            MDC.put(MDC_GAME_MARKER,gameId);
            log.info("Joining to the game {}", gameId);
            if (boards.containsKey(gameId)) {
                log.warn("Already in the game {}", boards.get(gameId));
                throw new DuplicatedGameException();
            }
            var board = prepareBoard(gameId, size);
            boards.put(gameId, board);
            if (firstShotIsYours) {
                shot(gameId);
            }
            historyProvider.addGame(gameId, board);
            log.info("Joined to the game with id: {} , board:\n{}", gameId, board.getUpdatedBoard());
            return board;
        } finally {
            MDC.clear();
        }
    }

    @Override
    public ShotResult opponentShot(String gameId, Position position) {
        try {
            MDC.put(MDC_PLAYER_MARKER, getMdcMarker());
            MDC.put(MDC_GAME_MARKER,gameId);
            var board = Optional.ofNullable(boards.get(gameId)).orElseThrow(NoGameFoundException::new);
            if (board.getStatus().equals(GameStatus.OVER)) {
                log.info("Game {} is already over", gameId);
                throw new GameOverException("Game " + gameId + " is already over");
            }
            return opponentShot(board, position);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public List<Ship> findShips(String gameId, boolean destroyed) {
        try {
            MDC.put(MDC_PLAYER_MARKER, getMdcMarker());
            MDC.put(MDC_GAME_MARKER,gameId);
            return Optional.ofNullable(boards.get(gameId))
                    .orElseThrow(NoGameFoundException::new)
                    .getShips().stream().filter(p -> p.isDestroyed() == destroyed).collect(Collectors.toList());
        } finally {
            MDC.clear();
        }

    }

    private ShotResult opponentShot(Board game, Position position) {
        if (isMyMove(game.getGameId())) {
            log.warn("Invalid move in the game, semaphore acquired - my move!");
            throw new InvalidMoveException();
        }
        log.info("Got shot at ({},{})", position.getX(), position.getY());
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
        position.setHit(!shotResult.equals(ShotResult.MISSED));

        //put shot in repository
        historyProvider.opponentShotForGame(game.getGameId(), position);

        if (ShotResult.MISSED.equals(shotResult)) {
            log.info("Missed at ({},{})",position.getX(), position.getY());
            shot(game.getGameId());
        }

        log.info("Game board:\n{}", game.getUpdatedBoard());
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
        log.trace("Board with ships:\n{}", board);
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
        log.trace("Available positions for ({},{}) : {}", position.getX(), position.getY(), available.stream().map(i -> "(" + i.getX() + "," + i.getY() + ")").collect(Collectors.joining(",")));
        return available;
    }

    @Override
    public boolean isMyMove(String gameId) {
        return semaphores.containsKey(gameId) && semaphores.get(gameId).availablePermits() == 0;
    }

    private boolean lockMove(String gameId) {
        log.info("MY MOVE - acquiring 'move' semaphore");
        semaphores.computeIfAbsent(gameId, s -> new Semaphore(1));
        if (!semaphores.get(gameId).tryAcquire()) {
            log.info("Problem acquiring move semaphore");
            return false;
        }
        return true;
    }

    private void unlockMove(String gameId) {
        log.info("YOUR MOVE - releasing 'move' semaphore");
        semaphores.get(gameId).release();
    }

    private Position getPositionToShot(String gameId) {
        int size = boards.get(gameId).getBoard().getSize();
        return Position.builder().x(random.nextInt(size)).y(random.nextInt(size)).build();
    }

    private ListenableFuture<ShotResult> shotTask(String gameId) {
        return listeningExecutorService.submit(() -> {
            try {
                MDC.put(MDC_PLAYER_MARKER, getMdcMarker());
                MDC.put(MDC_GAME_MARKER,gameId);
                TimeUnit.MILLISECONDS.sleep(200);
                Position position = getPositionToShot(gameId);
                log.info("Making shot to {}", position);
                return shotHandler.shotToOpponent(gameId, position);
            } finally {
                MDC.clear();
            }
        });
    }

    private void shotAndHandleReponse(String gameId) {
        if (isMyMove(gameId)) {


            Futures.addCallback(shotTask(gameId), new FutureCallback<>() {
                @Override
                public void onSuccess(ShotResult result) {
                    try {
                        MDC.put(MDC_PLAYER_MARKER, getMdcMarker());
                        MDC.put(MDC_GAME_MARKER,gameId);
                        log.info("Shot result '{}'", result);
                        if (result.equals(ShotResult.MISSED)) {
                            unlockMove(gameId);
                        } else {
                            log.info("Shot status '{}', so making another shot", result);
                            shotAndHandleReponse(gameId);
                        }
                    } finally {
                        MDC.clear();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    try {
                        MDC.put(MDC_PLAYER_MARKER, getMdcMarker());
                        MDC.put(MDC_GAME_MARKER,gameId);
                        log.error("Error while shooting", t);
                        if (t instanceof InvalidMoveException) {
                            log.warn("Invalid move");
                            unlockMove(gameId);
                        } else {
                            log.info("Problem while shooting, so making another shot");
                            shotAndHandleReponse(gameId);
                        }
                    } finally {
                        MDC.clear();
                    }
                }
            }, listeningExecutorService);

        }
    }

    protected void shot(String gameId) {
        lockMove(gameId);
        shotAndHandleReponse(gameId);
    }

    private String getMdcMarker(){
        return player;
    }
}
