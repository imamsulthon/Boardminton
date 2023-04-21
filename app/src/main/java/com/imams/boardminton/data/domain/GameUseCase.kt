package com.imams.boardminton.data.domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.imams.boardminton.data.GameScore
import javax.inject.Inject

class GameUseCase @Inject constructor(
    private var matchType: MatchType,
): UseCase {

    private val _gameScore by lazy {
        if (matchType == MatchType.SINGLE) {
            printLog("by Lazy single")
            mutableStateOf(GameScore().createSingles())
        } else {
            printLog("by Lazy double")
            mutableStateOf(GameScore().createDoubles())
        }
    }

    override fun get(): MutableState<GameScore> {
        return _gameScore
    }

    override fun get2(): GameScore {
        return _gameScore.value
    }

    override fun addA() {
        _gameScore.value.run { addA() }
    }

    override fun addB() {
        _gameScore.value.run { addB() }
    }

    override fun minA() {
        _gameScore.value.run { minA() }
    }

    override fun minB() {
        _gameScore.value.minB()
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

    private fun printLog(msg: String = "No Message") = println("ScoreBoardUseCase $msg")

}

enum class MatchType {
    SINGLE, DOUBLE
}