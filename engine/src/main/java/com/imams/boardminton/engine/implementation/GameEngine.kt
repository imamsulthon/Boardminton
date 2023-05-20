package com.imams.boardminton.engine.implementation

import com.imams.boardminton.engine.data.model.Game
import com.imams.boardminton.engine.data.model.GameRule
import com.imams.boardminton.engine.data.model.OnServe
import com.imams.boardminton.engine.data.model.Score
import com.imams.boardminton.engine.data.model.Side
import com.imams.boardminton.engine.data.model.Winner

class GameEngine constructor(
    private val index: Int = 1,
    private val scoreA: Score = Score(),
    private val scoreB: Score = Score(),
) {

    private val rules: GameRule = GameRule()
    private var onServe: OnServe = OnServe.NONE
    private var winner: Winner = Winner.None
    var hasReachInterval = false
        private set

    constructor(game: Game) : this(game.index, game.scoreA, game.scoreB) {
        onServe = game.onServe
        winner = game.winner
    }

    /**
     * Add 1 point of a score [side] based on the game rules relative to its opponent.
     *
     * [scoreA] relative to [scoreB] should be only added dependent to each other.
     *
     * Check [addPointWithRule] and this method will return a value if its score already reach win point
     */
    fun pointTo(side: Side) {
        when (side) {
            Side.A -> scoreA.addPointWithRule(scoreB) { isWin ->
                onServe = OnServe.A
                if (isWin) winner = Winner.A
                trySetGameHasReachInterval()
            }
            Side.B -> scoreB.addPointWithRule(scoreA) { isWin ->
                onServe = OnServe.B
                if (isWin) winner = Winner.B
                trySetGameHasReachInterval()
            }
        }
    }

    /**
     * Minus 1 point of a score [side] based on the game rules relative to its opponent.
     */
    fun minPoint(side: Side) {
        when (side) {
            Side.A -> scoreA.minPointWithRule(scoreB) {
                winner = Winner.None
                tryUnsetGameInterval()
            }
            Side.B -> scoreB.minPointWithRule(scoreA) {
                winner = Winner.None
                tryUnsetGameInterval()
            }
        }
    }


    /**
     * Set the side whose on serve.
     */
    fun serveTo(side: Side) {
        when (side) {
            Side.A -> scoreA.onServe = true.also {
                scoreB.onServe = false
                onServe = OnServe.A
            }
            Side.B -> scoreB.onServe = true.also {
                scoreA.onServe = false
                onServe = OnServe.B
            }
        }
    }

    /**
     * Revert/swap the side whose on serve.
     */
    fun revertServer() {
        scoreA.onServe = scoreB.onServe.also { scoreB.onServe = scoreA.onServe }
        onServe = when (onServe) {
            OnServe.A -> OnServe.B
            else -> OnServe.A
        }
    }

    /**
     * Set [hasReachInterval] because one of score point has reached [GameRule.intervalPoint] of [rules] {11}
     */
    private fun trySetGameHasReachInterval() {
        if (hasReachInterval) return
        if (scoreA.point == rules.intervalPoint || scoreB.point == rules.intervalPoint) {
            hasReachInterval = true
        }
    }

    /**
     * Unroll [hasReachInterval] because both point below [GameRule.intervalPoint] 11
     */
    private fun tryUnsetGameInterval() {
        if (hasReachInterval && scoreA.point < 11 && scoreB.point < 11) hasReachInterval = false
    }

    /**
     * reset all parameters of [scoreA] and [scoreB], including its lastPoint, onServe
     */
    fun reset() {
        scoreA.reset()
        scoreB.reset()
        onServe = OnServe.NONE
        winner = Winner.None
        hasReachInterval = false
    }

    /**
     * Getter of this [GameEngine]. Use this method only as single source of value
     */
    fun asGame() = Game(index, scoreA, scoreB, onServe, winner)

    /**
     * Add 1 point of a [Score], following rules relative to its [opponent]
     */
    private fun Score.addPointWithRule(
        opponent: Score,
        added: ((Boolean) -> Unit)? = null,
    ) {
        if (point == rules.advPoint) return //  check if point not reach adv point
        if (point.isNotDeuce(opponent.point)) this.add()
            .run {
                opponent.onServe = false // revert server
                lastPoint = point.lastPoint(opponent.point) // check last point
                isWin = point.isEndGame(opponent.point) // set is win or not
                added?.invoke(isWin)
            }
    }

    /**
     * Minus 1 point of a [Score], following rules relative to its [opponent]
     */
    private fun Score.minPointWithRule(opponent: Score, block: () -> Unit) {
        if (point == 0) return
        min()
        this.lastPoint(opponent)
        isWin = false
        block.invoke()
    }

    /**
     * Checking if [this] score point is left 1 point from maximum win point
     */
    private fun Score.lastPoint(opponent: Score) {
        this.lastPoint = this.point.lastPoint(opponent.point)
    }

    /**
     * A game is on deuce if the [scoreA] or [scoreB] relative to its opponent is
     * 1 point in difference while both score reached in range 20 to 30 at the same time
     */
    private fun Int.isNotDeuce(opponent: Int): Boolean {
        return !this.isEndGame(opponent)
    }

    /**
     * Game is end if [this] Score reach point in range between [rules] maximum point [21] to advance point [30].
     * ```
     * Score A = 21 vs [opponent] Score B = 19. A Win
     * Score A = 25 vs [opponent] Score B = 23. A Win
     * ```
     */
    private fun Int.isEndGame(opponent: Int): Boolean {
        return this in rules.maxPoint..rules.advPoint && this - opponent > 1
    }

    /**
     * Check last point of A/B [this] relative to its [opponent]
     */
    private fun Int.lastPoint(opponent: Int): Boolean {
        return this in rules.maxPoint - 1 until rules.maxPoint && this - opponent > 0
    }

}
