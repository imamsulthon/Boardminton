package com.imams.boardminton.ui.screen.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.data.toList
import com.imams.boardminton.domain.impl.CreatePlayerUseCase
import com.imams.boardminton.domain.impl.CreateTeamUseCase
import com.imams.boardminton.domain.mapper.MatchRepoMapper.toJson
import com.imams.boardminton.domain.mapper.UseCaseMapper.toState
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.domain.model.ITeam
import com.imams.boardminton.domain.model.PlayerViewParam
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.engine.data.model.Winner
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.boardminton.ui.screen.create.team.CreateTeamState
import com.imams.data.match.model.Match
import com.imams.data.match.repository.MatchRepository
import com.imams.data.team.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMatchVM @Inject constructor(
    private val createPlayerUseCase: CreatePlayerUseCase,
    private val repository: MatchRepository,
    private val createTeamUseCase: CreateTeamUseCase,
    private val teamRepository: TeamRepository,
) : ViewModel() {

    private val _playerA1 = mutableStateOf("")
    val playerA1 = _playerA1
    private val _playerA2 = mutableStateOf("")
    val playerA2 = _playerA2
    private val _playerB1 = mutableStateOf("")
    val playerB1 = _playerB1
    private val _playerB2 = mutableStateOf("")
    val playerB2 = _playerB2
    private val _umpire = mutableStateOf("")
    val umpire = _umpire

    private val _savePlayers = mutableStateListOf<CreatePlayerState>()
    private val _savePlayersFlow = MutableStateFlow(_savePlayers)
    val savePlayers: StateFlow<List<CreatePlayerState>> = _savePlayersFlow

    private val _saveTeams = mutableStateListOf<CreateTeamState>()
    private val _saveTeamsFlow = MutableStateFlow(_saveTeams)
    val saveTeams: StateFlow<List<CreateTeamState>> = _saveTeamsFlow

    init {
        initOptionalPlayers()
        initOptionalTeams()
    }

    fun setupPlayers(isSingle: Boolean, json: String) {
        val data = json.toList()
        if (isSingle) setupPlayers(a1 = data[0], b1 = data[2])
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

    fun umpireName(name: String) {
        _umpire.update(name)
    }

    fun updatePlayerName(iTeam: ITeam, data: CreatePlayerState) {
        updatePlayerName(iTeam, "${data.firstName} ${data.lastName}")
    }

    fun updatePlayerName(iSide: ISide, data: CreateTeamState) {
        when (iSide) {
            ISide.A -> {
                updatePlayerName(ITeam.A1, data.playerName1)
                updatePlayerName(ITeam.A2, data.playerName2)
            }
            ISide.B -> {
                updatePlayerName(ITeam.B1, data.playerName1)
                updatePlayerName(ITeam.B2, data.playerName2)
            }
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
            createPlayerUseCase.getAllPlayers().collectLatest {
                _savePlayers.clear()
                _savePlayers.addAll(it)
            }
        }
    }

    private fun initOptionalTeams() {
        viewModelScope.launch {
            teamRepository.getAllTeams().collectLatest {
                _saveTeams.clear()
                _saveTeams.addAll(it.map { t -> t.toState() })
            }
        }
    }

    fun saveInputPlayer(single: Boolean, callback: (String, Int) -> Unit) {
        viewModelScope.launch {
            val teamA = TeamViewParam(
                PlayerViewParam(playerA1.value),
                PlayerViewParam(playerA2.value),
                false
            )
            val teamB = TeamViewParam(
                PlayerViewParam(playerB1.value),
                PlayerViewParam(playerB2.value),
                false
            )
            when (val save = repository.saveMatch(
                Match(
                    type = if (single) "single" else "double",
                    teamA = teamA.toJson(),
                    teamB = teamB.toJson(),
                    currentGame = GameViewParam().toJson(),
                    games = listOf<GameViewParam>().toJson(),
                    winner = Winner.None.name,
                    lastUpdate = System.currentTimeMillis().toString(),
                    matchDuration = 0L,
                    shuttleCockUsed = 0,
                    umpireName = umpire.value
                )
            ).toInt()
            ) {
                0 -> {
                    callback.invoke(if (single) "single" else "double", save)
                }
                else -> {
                    if (single) {
                        callback.invoke("single", save)
                    } else {
                        saveTeam(teamA, teamB) {
                            callback.invoke("double", save)
                        }
                    }
                }
            }
        }
    }

    private fun saveTeam(teamA: TeamViewParam, teamB: TeamViewParam, callback: () -> Unit) {
        viewModelScope.launch {
            log("saveTeam A: $teamA B: $teamB")
            createTeamUseCase.createTeam(teamA)
            createTeamUseCase.createTeam(teamB)
            callback.invoke()
        }
    }

    private fun log(m: String) = println("TeamCreateMatch: $m")

    fun randomPlayers(single: Boolean) {
        val optionals = _savePlayersFlow.value.toList().shuffled().take(4)
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
