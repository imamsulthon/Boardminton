package com.imams.boardminton.data.domain

import androidx.compose.runtime.mutableStateOf
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

    val gameScore = _gameScore

    override fun addA() = _gameScore.value.addA()

    override fun addB() = _gameScore.value.addB()

    override fun minA() = _gameScore.value.minA()

    override fun minB() = _gameScore.value.minB()

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

}

enum class MatchType {
    SINGLE, DOUBLE
}