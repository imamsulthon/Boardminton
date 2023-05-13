package com.imams.boardminton.engine.implementation

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
    private var previousGames: MutableList<Game> = mutableListOf(),
) {

    private val rule: MatchRule = MatchRule()

    private var gameIndex: Int = 1

    private var gameEngine: GameEngine = GameEngine(gameIndex)

    private val currentGame by lazy { gameEngine.asGame() }

    private val previousGameWinner by lazy {
        if (previousGames.isNotEmpty()) previousGames.last().winner else Winner.None
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
        printLog("cons Single")
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
        printLog("cons Double")
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

    fun updatePlayers(matchType: MatchType, a1: String, a2: String = "", b1: String, b2: String = "") {
        teamA = Team(Player(a1), Player(a2))
        teamB = Team(Player(b1), Player(b2))
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

    fun swapServer() {
        gameEngine.revertServer()
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
        if (previousGameWinner == Winner.None) return
        if (previousGameWinner == currentGame.winner) winner = currentGame.winner
    }

    /**
     *
     */
    private fun addPreviousGame() {
        previousGames.add(gameIndex - 1, currentGame)
        createNewGame(gameIndex +1)
    }

    // region getter
    fun getMatchScore() = MatchScore(
        type = matchType,
        currentGame = currentGame,
        games = previousGames,
        teamA = teamA,
        teamB = teamB,
        winner = winner,
    )

    // endregion

    private fun printLog(msg: String) {
        println("MatchEngine: $msg")
    }

}

