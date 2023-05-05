package com.imams.boardminton.ui.screen.score

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.imams.boardminton.data.GameHistory
import com.imams.boardminton.data.GameScore
import com.imams.boardminton.data.domain.UseCase
import com.imams.boardminton.data.toList
import com.imams.boardminton.ui.getLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScoreBoardVM @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    private var gameIndex = 1

    private val _players = mutableStateOf("")
    val players = _players

    private val _game = mutableStateOf(useCase.get2())
    val game = _game

    private val _scoreA = mutableStateOf(game.value.pointA)
    val scoreA = _scoreA

    private val _scoreB = mutableStateOf(game.value.pointB)
    val scoreB = _scoreB

    private val _histories: MutableState<List<GameHistory>> = mutableStateOf(listOf())
    val histories = _histories

    private val _anyWinner = mutableStateOf(WinnerState(gameIndex, show = false, by = ""))
    val anyWinner = _anyWinner

    val finishMatch = mutableStateOf(false)

    fun updatePlayers(data: String) {
        _players.value = data
    }

    fun setupPlayer(json: String, single: Boolean) {
        val data = json.toList()
        printLog("setupPlayer $single $data")
        if (single) {
            useCase.createSingleMatch(data[0], data[1])
        } else {
            useCase.createDoubleMatch(data[0], data[1], data[2], data[3])
        }
    }

    fun plusA() {
        useCase.run {
            addA()
            _scoreA.value = get().value.pointA
            _game.value = get().value
            _game.value.checkWinner()
        }
    }

    fun plusB() {
        useCase.run {
            addB()
            _scoreB.value = get().value.pointB
            _game.value = get().value
            _game.value.checkWinner()
        }
    }

    fun minA() {
        useCase.run {
            minA()
            _scoreA.value = get().value.pointA
            _game.value = get().value
        }
    }

    fun minB() {
        useCase.run {
            minB()
            _scoreB.value = get().value.pointB
            _game.value = get().value
        }
    }

    fun swapServe() {
        useCase.run {
            swapServer()
            _game.value = get().value
        }
    }

    fun reset() {
        useCase.run {
            reset()
            _scoreA.value = get().value.pointA
            _scoreB.value = get().value.pointB
            _game.value = get().value
        }
    }

    fun swapSide() {
        useCase.run {
            swapSide()
            _scoreA.value = get().value.pointA
            _scoreB.value = get().value.pointB
            _game.value = get().value
            _histories.value = getHistory()
        }
    }

    fun setGameEnd(show: Boolean) {
        _anyWinner.value = _anyWinner.value.copy(show = show)
    }

    private fun GameScore.checkWinner() {
        if (gameEnd) {
            _anyWinner.value = _anyWinner.value.copy(
                isWin = true, show = true,
                by = if (server() == "A") teamA.getLabel() else teamB.getLabel()
            )
        }
    }

    fun onNewGame() {
        useCase.run {
            setHistory(GameHistory(gameIndex, scoreA.value, scoreB.value))
            resetWinner()
            reset()
            _scoreA.value = get().value.pointA
            _scoreB.value = get().value.pointB
            _game.value = get().value
            _histories.value = getHistory()
            if (gameIndex > 3) finishMatch.value = true
        }
    }

    private fun resetWinner() {
        gameIndex += 1
        _anyWinner.value = _anyWinner.value.copy(index = gameIndex, show = false, isWin = false, by = "")
    }

    private fun printLog(msg: String) = println("ScoreBoardVM $msg")

}

data class WinnerState(
    val index: Int,
    val isWin: Boolean = false,
    val show: Boolean = true,
    val by: String,
)