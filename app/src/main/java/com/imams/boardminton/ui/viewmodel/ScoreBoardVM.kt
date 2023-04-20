package com.imams.boardminton.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.imams.boardminton.data.domain.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScoreBoardVM @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    private val _playerA1 = mutableStateOf("Player 1")
    val playerA1 = _playerA1

    private val _playerA2 = mutableStateOf("Player Player A 2")
    val playerA2 = _playerA2

    private val _playerB1 = mutableStateOf("Player B")
    val playerB1 = _playerB1

    private val _playerB2 = mutableStateOf("Full Name of Player 1")
    val playerB2 = _playerB2

    private val _scoreA = mutableStateOf(0)
    val scoreA = _scoreA

    private val _scoreB = mutableStateOf(0)
    val scoreB = _scoreB

    private val _turnA = mutableStateOf(false)
    val turnA = _turnA

    private val _turnB = mutableStateOf(false)
    val turnB = _turnB

    val lastPointA = mutableStateOf(false)
    val lastPointB = mutableStateOf(false)

    private val _gameOver = mutableStateOf(false)
    val gameOver = _gameOver

    init {
        turnA.value = true
    }

    fun plusA() {
        if (_scoreA.value.notDeuce(_scoreB.value)) {
            _scoreA.value += 1
        }
        _scoreA.value.revert(_scoreB.value, lastPointA, lastPointB)
        _gameOver.value = _scoreA.value.game(_scoreB.value)
    }

    fun plusB() {
        if (_scoreB.value.notDeuce(_scoreA.value)) {
            _scoreB.value += 1
        }
        _scoreB.value.revert(_scoreA.value, lastPointB, lastPointA)
        _gameOver.value = _scoreB.value.game(_scoreA.value)
    }

    fun minA() {
        if (_scoreA.value > 0) _scoreA.value -= 1
        _scoreA.value.revert(_scoreB.value, lastPointA, lastPointB)
    }

    fun minB() {
        if (_scoreB.value > 0) _scoreB.value -= 1
        _scoreB.value.revert(_scoreA.value, lastPointB, lastPointA)
    }

    private fun Int.revert(opposite: Int, lastPoint: MutableState<Boolean>, lastPointO: MutableState<Boolean>) {
        lastPoint.value = this.lastPoint(opposite)
        lastPointO.value = false
    }

    fun turnA() {
        _turnA.value = true
        _turnB.value = false
    }

    fun turnB() {
        _turnB.value = true
        _turnA.value = false
    }

    private fun Int.notDeuce(opposite: Int) = !this.game(opposite)

    private fun Int.game(opposite: Int) = this in 21..30 && this - opposite > 1

    private fun Int.lastPoint(opposite: Int) = this in 20..29 && this - opposite > 0

}