package com.imams.boardminton.ui.screen.create.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.mapper.UseCaseMapper.toModel
import com.imams.boardminton.domain.mapper.UseCaseMapper.toState
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.boardminton.ui.screen.create.player.CreateTeamState
import com.imams.data.player.repository.PlayerRepository
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
class TeamDetailVM @Inject constructor(
    private val teamRepository: TeamRepository,
    private val playerRepository: PlayerRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTeamState())
    val uiState: StateFlow<CreateTeamState> = _uiState.asStateFlow()
    private val _player1State = MutableStateFlow(CreatePlayerState())
    val player1State: StateFlow<CreatePlayerState> = _player1State.asStateFlow()
    private val _player2State = MutableStateFlow(CreatePlayerState())
    val player2State: StateFlow<CreatePlayerState> = _player2State.asStateFlow()

    fun getTeam(id: Int) {
        log("getTeam $id")
        viewModelScope.launch {
            teamRepository.getTeam(id).collectLatest { obj ->
                obj?.let { t ->
                    val state = t.toState()
                    _uiState.update { state }
                    fetchPlayerDetail(state)
                }
            }
        }
    }

    private fun fetchPlayerDetail(state: CreateTeamState) {
        fetchPlayer1(state.playerId1)
        fetchPlayer2(state.playerId2)
    }

    private fun fetchPlayer1(id: Int) {
        viewModelScope.launch {
            delay(500)
            log("getPlayers $id")
            playerRepository.getPlayer(id).collectLatest {
                _player1State.update { _ -> it.toState() }
            }
        }
    }

    private fun fetchPlayer2(id: Int) {
        viewModelScope.launch {
            delay(500)
            log("getPlayers $id")
            playerRepository.getPlayer(id).collectLatest {
                _player2State.update { _ -> it.toState() }
            }
        }
    }

    fun onRefreshTeamPlayer(callBack: () -> Unit) {
        viewModelScope.launch {
            val team = _uiState.value.copy(
                playerName1 = _player1State.value.fullName,
                playerName2 = _player2State.value.fullName,
                profilePhotoUri1 = _player1State.value.photoProfileUri,
                profilePhotoUri2 = _player2State.value.photoProfileUri
            )
            teamRepository.updateTeam(team.toModel(true))
            delay(500)
            callBack.invoke()
        }
    }

    private fun log(m: String) = println("TeamDetailVM: $m")

}