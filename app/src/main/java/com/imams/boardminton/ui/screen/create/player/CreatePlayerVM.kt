package com.imams.boardminton.ui.screen.create.player

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.impl.CreatePlayerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlayerVM @Inject constructor(
    private val useCase: CreatePlayerUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(CreatePlayerState())
    val uiState: StateFlow<CreatePlayerState> = _uiState.asStateFlow()

    private val _savePlayers = mutableStateListOf<CreatePlayerState>()
    private val _savePlayersFlow = MutableStateFlow(_savePlayers)
    val savePlayers: StateFlow<List<CreatePlayerState>> = _savePlayersFlow

    init {
        checkSavedPlayers()
    }

    fun execute(event: CreatePlayerEvent) {
        when (event) {
            is CreatePlayerEvent.FirstName -> _uiState.update { it.copy(firstName = event.name) }
            is CreatePlayerEvent.LastName -> _uiState.update { it.copy(lastName = event.name) }
            is CreatePlayerEvent.HandPlay -> _uiState.update { it.copy(handPlay = event.value) }
            is CreatePlayerEvent.Gender -> _uiState.update { it.copy(gender = event.value) }
            is CreatePlayerEvent.Height -> _uiState.update { it.copy(height = event.value) }
            is CreatePlayerEvent.Weight -> _uiState.update { it.copy(weight = event.value) }
            is CreatePlayerEvent.Clear -> _uiState.update { CreatePlayerState() }
        }
    }

    private var isInit = false
    private fun printLog(msg: String) = println("CreatePlayer $msg")
    fun setupWith(id: Int) {
        printLog("SetupWith init $id")
        // todo should use savedInstance state
        if (isInit) return
        viewModelScope.launch {
            useCase.getPlayer(id).collectLatest { player ->
                printLog("SetupWith player $player")
                isInit = true
                _uiState.update { player }
            }
        }
    }

    fun savePlayer(callback: (() -> Unit)? = null, data: CreatePlayerState = uiState.value) {
        viewModelScope.launch {
            useCase.createPlayer(data)
            delay(500)
            callback?.invoke()
            _uiState.update { CreatePlayerState() }
        }
    }

    fun updatePlayer(callback: (() -> Unit)? = null, data: CreatePlayerState = uiState.value) {
        viewModelScope.launch {
            useCase.updatePlayer(data)
            delay(500)
            callback?.invoke()
            _uiState.update { CreatePlayerState() }
        }
    }

    private fun checkSavedPlayers() {
        viewModelScope.launch {
            useCase.getAllPlayers().collectLatest {
                _savePlayers.clear()
                _savePlayers.addAll(it)
            }
        }
    }

}

data class CreatePlayerState(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val handPlay: String = "",
    val gender: String = "",
    val height: Int = 0,
    val weight: Int = 0,
) {
    val fullName = "$firstName $lastName"
}

sealed class CreatePlayerEvent {
    data class FirstName(val name: String): CreatePlayerEvent()
    data class LastName(val name: String): CreatePlayerEvent()
    data class Height(val value: Int): CreatePlayerEvent()
    data class Weight(val value: Int): CreatePlayerEvent()
    data class HandPlay(val value: String): CreatePlayerEvent()
    data class Gender(val value: String): CreatePlayerEvent()
    object Clear: CreatePlayerEvent()
}