package com.imams.boardminton.data.domain

import androidx.compose.runtime.MutableState
import com.imams.boardminton.data.GameHistory
import com.imams.boardminton.data.GameScore

interface UseCase {
    fun get(): MutableState<GameScore>

    fun get2(): GameScore

    fun addA()

    fun addB()

    fun minA()

    fun minB()

    fun swapServer()

    fun reset()

    fun swapSide()

    fun createSingleMatch(playerA: String, playerB: String)

    fun createDoubleMatch(playerA1: String, playerA2: String, playerB1: String, playerB2: String)

    fun setHistory(history: GameHistory)
    fun getHistory(): MutableList<GameHistory>

}