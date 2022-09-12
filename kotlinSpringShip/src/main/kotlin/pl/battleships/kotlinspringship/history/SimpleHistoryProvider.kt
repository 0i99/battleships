package pl.battleships.kotlinspringship.history

import pl.battleships.core.api.HistoryProvider
import pl.battleships.core.model.Board
import pl.battleships.core.model.Position

class SimpleHistoryProvider : HistoryProvider {
    private val games: HashMap<String, Board> = hashMapOf()
    private val shots: HashMap<String, MutableList<Position>> = hashMapOf()

    override fun addGame(gameId: String, board: Board): Long {
        games.put(gameId, board)
        return 0
    }

    override fun opponentShotForGame(gameId: String, position: Position): Long {
        shots.computeIfAbsent(gameId) { it -> mutableListOf<Position>() }
        shots.get(gameId)?.add(position)
        return 0
    }

    override fun getAllShots(gameId: String): MutableList<Position> {
        return shots.get(gameId).orEmpty().toMutableList()
    }
}