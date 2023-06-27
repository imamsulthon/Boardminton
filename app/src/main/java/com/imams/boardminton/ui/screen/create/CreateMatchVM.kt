package com.imams.boardminton.ui.screen.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.data.toList
import com.imams.boardminton.domain.impl.CreatePlayerUseCase
import com.imams.boardminton.domain.mapper.MatchRepoMapper.toJson
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.domain.model.ITeam
import com.imams.boardminton.domain.model.PlayerViewParam
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.ui.component.printLog
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.data.match.model.Match
import com.imams.data.match.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMatchVM @Inject constructor(
    private val useCase: CreatePlayerUseCase,
    private val repository: MatchRepository,
) : ViewModel() {

    private val _playerA1 = mutableStateOf("")
    val playerA1 = _playerA1
    private val _playerA2 = mutableStateOf("")
    val playerA2 = _playerA2
    private val _playerB1 = mutableStateOf("")
    val playerB1 = _playerB1
    private val _playerB2 = mutableStateOf("")
    val playerB2 = _playerB2

    private val _savePlayers = mutableStateListOf<CreatePlayerState>()
    private val _savePlayersFlow = MutableStateFlow(_savePlayers)
    val savePlayers: StateFlow<List<CreatePlayerState>> = _savePlayersFlow

    init {
        initOptionalPlayers()
    }

    fun setupPlayers(isSingle: Boolean, json: String) {
        val data = json.toList()
        if (isSingle) setupPlayers(a1 = data[0], b1 = data[0])
        else setupPlayers(data[0], data[1], data[2], data[3])
    }

    private fun setupPlayers(a1: String = "", a2: String = "", b1: String = "", b2: String = "") {
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

    fun updatePlayerName(iTeam: ITeam, data: CreatePlayerState) {
        updatePlayerName(iTeam, "${data.firstName} ${data.lastName}")
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

    private fun initOptionalPlayers() {
        viewModelScope.launch {
            viewModelScope.launch {
                useCase.getAllPlayers().collectLatest {
                    _savePlayers.clear()
                    _savePlayers.addAll(it)
                }
            }
        }
    }

    fun saveInputPlayer(single: Boolean) {
        viewModelScope.launch {
            repository.saveMatch(
                Match(
                    type = if (single) "single" else "double",
                    teamA = TeamViewParam(PlayerViewParam(playerA1.value), PlayerViewParam(playerA2.value), false).toJson(),
                    teamB = TeamViewParam(PlayerViewParam(playerB1.value), PlayerViewParam(playerB2.value), false).toJson(),
                    currentGame = GameViewParam().toJson(),
                    games = listOf<GameViewParam>().toJson(),
                    winner = "none",
                    lastUpdate = System.currentTimeMillis().toString(),
            ))
        }
    }

    fun randomPlayers(single: Boolean) {
        val optionals = _savePlayersFlow.value.toList().shuffled().take(4)
        printLog("defaultPlayers size ${optionals.size}")
        if (single) {
            optionals.forEachIndexed { index, player ->
                when (index) {
                    0 -> _playerA1.value = player.fullName
                    1 -> _playerB1.value = player.fullName
                }
            }
            _playerA2.value = ""
            _playerB2.value = ""
        } else {
            optionals.forEachIndexed { index, player ->
                when (index) {
                    0 -> _playerA1.value = player.fullName
                    1 -> _playerA2.value = player.fullName
                    2 -> _playerB1.value = player.fullName
                    3 -> _playerB2.value = player.fullName
                }
            }
        }
    }

}
