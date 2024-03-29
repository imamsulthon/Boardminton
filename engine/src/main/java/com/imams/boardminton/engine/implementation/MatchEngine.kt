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
    private var id: Int? = 0,
    private val matchType: MatchType,
    private var teamA: Team,
    private var teamB: Team,
    private var previousGames: MutableList<Game> = mutableListOf(),
) {

    private val rule: MatchRule = MatchRule()
    private var gameIndex: Int = 1
    private var gameEngine: GameEngine
    private var shuttleCockCount = 0

    private val previousGameWinner by lazy {
        if (previousGames.isNotEmpty()) previousGames.last().winner else Winner.None
    }

    /**
     * Value for Match Winner
     */
    private var winner: Winner = Winner.None

    /**
     * Create Match [MatchType.Single] with player name [a1] vs [b1]
     */
    constructor(a1: String, b1: String) : this(
        null,
        MatchType.Single,
        Team(Player(a1), Player("")),
        Team(Player(b1), Player(""))
    ) {
        gameIndex = 1
    }

    /**
     * Create Match [MatchType.Double] with player name [a1]-[a2] vs [b1]-[b2]
     */
    constructor(a1: String, a2: String, b1: String, b2: String) : this(
        null,
        MatchType.Double,
        Team(Player(a1), Player(a2)),
        Team(Player(b1), Player(b2)),
    ) {
        gameIndex = 1
    }

    /**
     * Create Match with existing [match]
     */
    constructor(match: MatchScore): this(match.id, match.type, match.teamA, match.teamB, match.games) {
        gameIndex = match.currentGame.index
        gameEngine = GameEngine(gameIndex,
            match.currentGame.scoreA, match.currentGame.scoreB,
            match.currentGame.onServe, match.currentGame.winner
        )
        winner = match.winner
        shuttleCockCount = match.shuttleCockCount
    }

    init {
        gameEngine = GameEngine(gameIndex)
    }

    fun updatePlayers(matchType: MatchType, a1: String, a2: String = "", b1: String, b2: String = "") {
        teamA = Team(Player(a1), if (matchType == MatchType.Double) Player(a2) else Player(""))
        teamB = Team(Player(b1), if (matchType == MatchType.Double) Player(b2) else Player(""))
    }
    
    /**
     * Add 1 point to [side] on current [gameEngine]
     */
    fun pointTo(side: Side) {
        gameEngine.pointTo(side)
        executeMatchRules()
    }

    /**
     * Minus 1 point to [side] on current [gameEngine]
     */
    fun minTo(side: Side) {
        gameEngine.minPoint(side)
        executeMatchRules()
    }

    /**
     * Set the side whose on serve in the current game
     */
    fun serveTo(side: Side) {
        gameEngine.serveTo(side)
    }

    /**
     * Revert/swap the side whose on serve in the current game.
     */
    fun swapServer() {
        gameEngine.revertServer()
    }

    /**
     * reset game engine
     */
    fun resetGame() {
        gameEngine.reset()
        executeMatchRules()
    }

    fun addShuttleCock() {
        shuttleCockCount++
    }

    /**
     * Create new game engine
     */
    fun createNewGame(onIndex: Int) {
        if (onIndex > rule.maxGame) return
        // add current game as history
        previousGames.add(gameEngine.asGame())
        if (previousGameWinner == Winner.None && previousGameWinner == gameEngine.asGame().winner) return
        // create new game engine
        gameIndex = onIndex
        gameEngine = GameEngine(gameIndex, Score(), Score())
    }

    /**
     *
     */
    private fun executeMatchRules() {
        trySetWinnerByMatch()
    }

    /**
     * try set Winner of match
     */
    private fun trySetWinnerByMatch() {
        if (gameIndex < 2) return
        if (previousGameWinner == Winner.None) return
        val current = gameEngine.asGame().winner
        winner = when {
            current == Winner.None -> Winner.None
            gameIndex == 2 -> {
                if (current == previousGameWinner) current else Winner.None
            }
            gameIndex == 3 -> current
            else -> Winner.None
        }
    }

    // region getter
    fun getMatchScore() = MatchScore(
        id = id ?: 0,
        type = matchType,
        currentGame = gameEngine.asGame(),
        games = previousGames,
        teamA = teamA,
        teamB = teamB,
        winner = winner,
        shuttleCockCount = shuttleCockCount,
    )
    // endregion

}

