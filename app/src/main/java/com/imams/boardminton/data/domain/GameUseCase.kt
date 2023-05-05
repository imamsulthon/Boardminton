package com.imams.boardminton.data.domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.imams.boardminton.data.GameHistory
import com.imams.boardminton.data.GameScore
import javax.inject.Inject

class GameUseCase @Inject constructor(
    private var matchType: MatchType,
): UseCase {

    private val _gameScore by lazy {
        if (matchType == MatchType.SINGLE) {
            mutableStateOf(GameScore().createSingles())
        } else {
            mutableStateOf(GameScore().createDoubles())
        }
    }

    private var _history = mutableListOf<GameHistory>()

    override fun get(): MutableState<GameScore> {
        return _gameScore
    }

    override fun get2(): GameScore {
        return _gameScore.value
    }

    override fun addA() {
        _gameScore.value.addA()
    }

    override fun addB() {
        _gameScore.value.addB()
    }

    override fun minA() {
        _gameScore.value.minA()
    }

    override fun minB() {
        _gameScore.value.minB()
    }

    override fun swapServer() {
        _gameScore.value.swapServer()
    }

    override fun reset() {
        _gameScore.value.reset()
    }

    override fun swapSide() {
        _gameScore.value.swapSide()
        val h = _history
        h.forEach {g ->
            g.scoresA = g.scoresB.also { g.scoresB = g.scoresA }
        }
    }

    override fun createSingleMatch(playerA: String, playerB: String) {
        matchType = MatchType.SINGLE
        _gameScore.value.run {
            createSingles(playerA, playerB)
        }
    }

    override fun createDoubleMatch(
        playerA1: String,
        playerA2: String,
        playerB1: String,
        playerB2: String
    ) {
        matchType = MatchType.DOUBLE
        _gameScore.value.run {
            createDoubles(playerA1, playerA2, playerB1, playerB2)
        }
    }

    override fun setHistory(history: GameHistory) {
        _history.add(history)
    }

    override fun getHistory(): MutableList<GameHistory> {
        return _history
    }

}

enum class MatchType {
    SINGLE, DOUBLE
}