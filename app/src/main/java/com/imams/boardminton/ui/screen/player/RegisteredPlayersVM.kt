package com.imams.boardminton.ui.screen.player

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.impl.UseCaseMapper.toModel
import com.imams.boardminton.domain.impl.UseCaseMapper.toState
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.data.player.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisteredPlayersVM @Inject constructor(
    private val repository: PlayerRepository,
): ViewModel() {

    private val _savePlayers = mutableStateListOf<CreatePlayerState>()
    private val _savePlayersFlow = MutableStateFlow(_savePlayers)
    val savePlayers: StateFlow<List<CreatePlayerState>> = _savePlayersFlow

    init {
        checkSavedPlayers()
    }

    private fun checkSavedPlayers() {
        viewModelScope.launch {
            repository.getAllPlayers().collectLatest {
                printLog("checkSavedPlayer")
                _savePlayers.clear()
                _savePlayers.addAll(it.map { p -> p.toState() })
            }
        }
    }

    fun removePlayer(item: CreatePlayerState) {
        viewModelScope.launch {
            repository.removePlayer(item.toModel())
            printLog("removePlayer")
        }
    }

    private fun printLog(msg: String) {
        println("RegisteredPlayer VM $msg")
    }

}