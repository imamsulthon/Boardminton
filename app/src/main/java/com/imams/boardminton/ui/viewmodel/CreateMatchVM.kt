package com.imams.boardminton.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.imams.boardminton.data.ITeam
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateMatchVM @Inject constructor() : ViewModel() {

    private val _playerA1 = mutableStateOf("")
    val playerA1 = _playerA1
    private val _playerA2 = mutableStateOf("")
    val playerA2 = _playerA2

    private val _playerB1 = mutableStateOf("")
    val playerB1 = _playerB1

    private val _playerB2 = mutableStateOf("")
    val playerB2 = _playerB2

    fun setA1(name: String) {
        _playerA1.value = name
    }

    fun setA2(name: String) {
        _playerA2.value = name
    }

    fun setB1(name: String) {
        _playerB1.value = name
    }

    fun setB2(name: String) {
        _playerB2.value = name
    }

    fun swapSingleMatch() {
        val a1 = _playerA1.value
        val b1 = _playerB1.value
        _playerA1.value = b1
        _playerB1.value = a1
    }

    fun swapDoubleMatch() {
        val a1 = _playerA1.value
        val a2 = _playerA2.value
        val b1 = _playerB1.value
        val b2 = _playerB2.value

        _playerA1.value = b1
        _playerA2.value = b2
        _playerB1.value = a1
        _playerB2.value = a2
    }

    fun swapTeamA() {
        val a1 = _playerA1.value
        val a2 = _playerA2.value
        _playerA1.value = a2
        _playerA2.value = a1
    }

    fun swapTeamB() {
        val b1 = _playerB1.value
        val b2 = _playerB2.value
        _playerB1.value = b2
        _playerB2.value = b1
    }

    fun importPlayer(target: ITeam) {
        when (target) {
            ITeam.A1 -> {

            }
            ITeam.A2 -> {

            }
            ITeam.B1 -> {

            }
            ITeam.B2 -> {

            }
        }
    }

    fun onClearPlayers() {
        _playerA1.value = ""
        _playerA2.value = ""
        _playerB1.value = ""
        _playerB2.value = ""
    }

}
