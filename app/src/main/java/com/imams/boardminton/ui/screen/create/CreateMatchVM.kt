package com.imams.boardminton.ui.screen.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.domain.model.ITeam
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

    fun setupPlayers(a1: String = "", a2: String = "", b1: String = "", b2: String = "") {
        _playerA1.value = a1
        _playerA2.value = a2
        _playerB1.value = b1
        _playerB2.value = b2
    }

    fun updatePlayerName(iTeam: ITeam, v: String) {
        when (iTeam) {
            ITeam.A1 -> _playerA1.update(v)
            ITeam.A2 -> _playerA2.update(v)
            ITeam.B1 -> _playerB1.update(v)
            ITeam.B2 -> _playerB2.update(v)
        }
    }

    fun swapSingleMatch() {
        _playerA1.swap(_playerB1)
    }

    fun swapDoubleMatch() {
        _playerA1.swap(_playerB1)
        _playerA2.swap(_playerB2)
    }

    fun swapPlayerByTeam(side: ISide) {
        when (side) {
            ISide.A -> _playerA1.swap(_playerA2)
            ISide.B -> _playerB1.swap(_playerB2)
        }
    }

    fun importPlayer(target: ITeam) {
        when (target) {
            ITeam.A1 -> _playerA1.importRes()
            ITeam.A2 -> _playerA2.importRes()
            ITeam.B1 -> _playerB1.importRes()
            ITeam.B2 -> _playerB2.importRes()
        }
    }

    fun onClearPlayers() {
        _playerA1.value = ""
        _playerA2.value = ""
        _playerB1.value = ""
        _playerB2.value = ""
    }

    private fun MutableState<String>.swap(with: MutableState<String>) {
        this.value = with.value.also { with.value = this.value }
    }

    private fun MutableState<String>.update(with: String) {
        this.value = with
    }

    private fun MutableState<String>.importRes() {
        this.value = ""
    }

    // todo delete
    fun defaultPlayers(single: Boolean) {
        _playerA1.value = Athlete.Imam_Sulthon
        _playerB1.value = Athlete.Kim_Astrup
        if (single) {
            _playerA2.value = ""
            _playerB2.value = ""
            return
        }
        _playerA2.value = Athlete.Anthony
        _playerB2.value = Athlete.Anders_Skaarup
    }

}
