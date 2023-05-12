package com.imams.boardminton.engine.data

import com.imams.boardminton.engine.data.model.Game
import com.imams.boardminton.engine.data.model.MatchRule
import com.imams.boardminton.engine.data.model.MatchScore
import com.imams.boardminton.engine.data.model.MatchType
import com.imams.boardminton.engine.data.model.Player
import com.imams.boardminton.engine.data.model.Score
import com.imams.boardminton.engine.data.model.Side
import com.imams.boardminton.engine.data.model.Team
import com.imams.boardminton.engine.data.model.Winner

class MatchEngine(
    private val matchType: MatchType,
    private var teamA: Team,
    private var teamB: Team,
    private var prevGames: MutableList<Game> = mutableListOf(),
) {

    private val rule: MatchRule = MatchRule()

    private var gameIndex: Int = 1

    private var gameEngine: GameEngine = GameEngine(gameIndex)

    private val currentGame by lazy { gameEngine.asGame() }

    private val prevGameWinner by lazy {
        if (prevGames.isNotEmpty()) prevGames.last().winner else Winner.None
    }

    private var winner: Winner = Winner.None

    /**
     * Create Match [MatchType.Single] with player name [a1] vs [b1]
     */
    constructor(a1: String, b1: String) : this(
        MatchType.Single,
        Team(Player(a1), Player(a1)),
        Team(Player(b1), Player(b1))
    ) {
        gameIndex = 1
    }

    /**
     * Create Match [MatchType.Double] with player name [a1]-[a2] vs [b1]-[b2]
     */
    constructor(a1: String, a2: String, b1: String, b2: String) : this(
        MatchType.Double,
        Team(Player(a1), Player(a2)),
        Team(Player(b1), Player(b2)),
    ) {
        gameIndex = 1
    }

    constructor(match: MatchScore): this(match.type, match.teamA, match.teamA, match.games,) {
        gameIndex = match.currentGame.index
    }

    private fun createNewGame(gameIndex: Int) {
        if (gameIndex >= rule.maxGame) return
        this.gameIndex = gameIndex
        gameEngine = GameEngine(this.gameIndex, Score(), Score())
    }
    
    /**
     *
     */
    fun pointTo(side: Side) {
        gameEngine.pointTo(side)
        executeMatchRules()
    }

    /**
     *
     */
    fun minTo(side: Side) {
        gameEngine.minPoint(side)
        executeMatchRules()
    }

    fun serveTo(side: Side) {
        gameEngine.serveTo(side)
    }

    /**
     *
     */
    private fun executeMatchRules() {
        if (currentGame.winner != Winner.None) {
            addPreviousGame()
        }
        trySetWinner()
    }

    private fun trySetWinner() {
        if (gameIndex < 1) return
        if (prevGameWinner == Winner.None) return
        if (prevGameWinner == currentGame.winner) winner = currentGame.winner
    }

    /**
     *
     */
    private fun addPreviousGame() {
        prevGames.add(gameIndex - 1, currentGame)
        createNewGame(gameIndex +1)
    }

    // region getter
    fun getMatchScore() = MatchScore(
        type = matchType,
        currentGame = currentGame,
        games = prevGames,
        teamA = teamA,
        teamB = teamB,
        winner = winner,
    )

    // endregion

    private fun printLog(msg: String) {
        println("MatchEngine: $msg")
    }

}

