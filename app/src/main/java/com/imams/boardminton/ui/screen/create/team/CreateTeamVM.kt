package com.imams.boardminton.ui.screen.create.team

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.impl.CreatePlayerUseCase
import com.imams.boardminton.domain.impl.CreateTeamUseCase
import com.imams.boardminton.domain.mapper.UseCaseMapper.toModel
import com.imams.boardminton.domain.mapper.UseCaseMapper.toState
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.boardminton.ui.screen.create.player.CreateTeamEvent
import com.imams.boardminton.ui.screen.create.player.CreateTeamState
import com.imams.data.team.repository.TeamRepository
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
class CreateTeamVM @Inject constructor(
    private val createTeamUseCase: CreateTeamUseCase,
    private val createPlayerUseCase: CreatePlayerUseCase,
    private val teamRepository: TeamRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(CreateTeamState())
    val uiState: StateFlow<CreateTeamState> = _uiState.asStateFlow()

    // dialog pop up show all teams registered
    private val _saveTeams = mutableStateListOf<CreateTeamState>()
    private val _saveTeamsFlow = MutableStateFlow(_saveTeams)
    val saveTeams: StateFlow<List<CreateTeamState>> = _saveTeamsFlow

    // dialog pop show players optional
    private val _savePlayers = mutableStateListOf<CreatePlayerState>()
    private val _savePlayersFlow = MutableStateFlow(_savePlayers)
    val savePlayers: StateFlow<List<CreatePlayerState>> = _savePlayersFlow

    init {
        checkSavedPlayers()
        checkSavedTeams()
    }

    fun execute(event: CreateTeamEvent) {
        when (event) {
            is CreateTeamEvent.Player1 -> {
                _uiState.update {
                    it.copy(playerName1 = event.data.fullName, playerId1 = event.data.id,
                        profilePhotoUri1 = event.data.photoProfileUri)
                }
            }
            is CreateTeamEvent.Player2 -> {
                _uiState.update {
                    it.copy(playerName2 = event.data.fullName, playerId2 = event.data.id,
                        profilePhotoUri2 = event.data.photoProfileUri)
                }
            }
            is CreateTeamEvent.Swap -> {
                _uiState.update {
                    it.copy(
                        playerName1 = it.playerName2, playerId1 = it.playerId2, profilePhotoUri1 = it.profilePhotoUri1,
                        playerName2 = it.playerName1, playerId2 = it.playerId1, profilePhotoUri2 = it.profilePhotoUri2,
                    )
                }
            }
            is CreateTeamEvent.Clear -> _uiState.update { CreateTeamState() }
        }
    }

    fun saveTeams(callback: () -> Unit) {
        viewModelScope.launch {
            createTeamUseCase.createTeam(_uiState.value.toModel())
            delay(500)
            callback.invoke()
        }
    }

    private fun checkSavedPlayers() {
        viewModelScope.launch {
            createPlayerUseCase.getAllPlayers().collectLatest {
                _savePlayers.clear()
                _savePlayers.addAll(it)
            }
        }
    }

    private fun checkSavedTeams() {
        viewModelScope.launch {
            teamRepository.getAllTeams().collectLatest {
                _saveTeams.clear()
                _saveTeams.addAll(it.map { t -> t.toState() })
            }
        }
    }

}