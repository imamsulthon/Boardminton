package com.imams.boardminton.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.imams.boardminton.data.domain.UseCase
import com.imams.boardminton.data.toList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScoreBoardVM @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    private val _game = mutableStateOf(useCase.get2())
    val game = _game

    private val _scoreA = mutableStateOf(game.value.pointA)
    val scoreA = _scoreA

    private val _scoreB = mutableStateOf(game.value.pointB)
    val scoreB = _scoreB

    init {
        useCase.createDoubleMatch("Donald Kadafi", "Imam Sulthon", "Joe Biden", "Tai Lako")
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
        }
    }

    fun plusB() {
        useCase.run {
            addB()
            _scoreB.value = get().value.pointB
            _game.value = get().value
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

    private fun printLog(msg: String) = println("ScoreBoardVM $msg")

}