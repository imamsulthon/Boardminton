package com.imams.boardminton.ui.screen.player

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.mapper.UseCaseMapper.toModel
import com.imams.boardminton.domain.mapper.UseCaseMapper.toState
import com.imams.boardminton.domain.model.Sort
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.boardminton.ui.screen.create.team.CreateTeamState
import com.imams.data.player.model.Player
import com.imams.data.player.repository.PlayerRepository
import com.imams.data.team.model.Team
import com.imams.data.team.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisteredPlayersVM @Inject constructor(
    private val repository: PlayerRepository,
    private val teamRepository: TeamRepository,
): ViewModel() {

    private val _tempData = mutableStateListOf<Player>()
    private val _savePlayers = mutableStateListOf<CreatePlayerState>()
    private val _savePlayersFlow = MutableStateFlow(_savePlayers)
    val savePlayers: StateFlow<List<CreatePlayerState>> = _savePlayersFlow

    private val _tempData2 = mutableStateListOf<Team>()
    private val _saveTeams = mutableStateListOf<CreateTeamState>()
    private val _saveTeamsFlow = MutableStateFlow(_saveTeams)
    val saveTeams: StateFlow<List<CreateTeamState>> = _saveTeamsFlow

    init {
        checkSavedPlayers()
        checkTeams()
    }

    private fun checkSavedPlayers() {
        viewModelScope.launch {
            repository.getAllPlayers().collectLatest {
                _tempData.clear()
                _tempData.addAll(it)
                _tempData.proceed()
            }
        }
    }

    private fun List<Player>.proceed() {
        val data = this.toMutableList()
        _savePlayers.clear()
        _savePlayers.addAll(data.map { p -> p.toState() })
    }

    fun setFilter(params: FilterPlayer) {
        var data = _tempData.toList()
        when (params.gender) {
            "Man" -> {
                data = data.filter { it.gender == "Man" }
            }
            "Woman" -> {
                data = data.filter { it.gender == "Woman" }
            }
            else -> {
            }
        }
        when (params.handPlay) {
            "Left" -> {
                data = data.filter { it.handPlay == "Left" }
            }
            "Right" -> {
                data = data.filter { it.handPlay == "Right" }
            }
        }
        _savePlayers.clear()
        _savePlayers.addAll(data.map { it.toState() })
    }

    fun setSorting(params: SortPlayer) {
        when (params) {
            is SortPlayer.Id -> {
                if (params.asc == Sort.Ascending)_savePlayers.sortBy { it.id }
                else _savePlayers.sortByDescending { it.id }
            }
            is SortPlayer.Name -> {
                if (params.asc == Sort.Ascending) _savePlayers.sortBy { it.firstName }
                else _savePlayers.sortByDescending { it.firstName }
            }
            is SortPlayer.Weight -> {
                if (params.asc == Sort.Ascending) _savePlayers.sortBy { it.weight }
                else _savePlayers.sortByDescending { it.weight }
            }
            is SortPlayer.Height -> {
                if (params.asc == Sort.Ascending) _savePlayers.sortBy { it.height }
                else _savePlayers.sortByDescending { it.height }
            }
            is SortPlayer.Age -> {
                if (params.asc == Sort.Ascending) _savePlayers.sortBy { it.dob }
                else _savePlayers.sortByDescending { it.dob }
            }
            else -> {}
        }
    }

    fun setSorting(params: SortTeam) {
        when (params) {
            is SortTeam.Id -> {
                if (params.asc == Sort.Ascending)_saveTeams.sortBy { it.id }
                else _saveTeams.sortByDescending { it.id }
            }
            is SortTeam.Name -> {
                if (params.asc == Sort.Ascending) _saveTeams.sortBy { it.playerName1 }
                 else _saveTeams.sortByDescending { it.playerName1 }
            }
            is SortTeam.Rank -> {
                if (params.asc == Sort.Ascending) _saveTeams.sortBy { it.rank }
                else _saveTeams.sortByDescending { it.rank }
            }
            is SortTeam.Play -> {
                if (params.asc == Sort.Ascending) _saveTeams.sortBy { it.play }
                else _saveTeams.sortByDescending { it.play }
            }
            is SortTeam.Win -> {
                if (params.asc == Sort.Ascending) _saveTeams.sortBy { it.win }
                else _saveTeams.sortByDescending { it.win }
            }
            is SortTeam.Lose -> {
                if (params.asc == Sort.Ascending) _saveTeams.sortBy { it.lose }
                else _saveTeams.sortByDescending { it.lose }
            }
            else -> {}
        }
    }

    fun removePlayer(item: CreatePlayerState) {
        viewModelScope.launch {
            repository.removePlayer(item.toModel(true))
        }
    }

    private fun checkTeams() {
        viewModelScope.launch {
            teamRepository.getAllTeams().collectLatest {
                _tempData2.clear()
                _tempData2.addAll(it)
                _tempData2.proceed2()
            }
        }
    }
    private fun List<Team>.proceed2() {
        val data = this.toMutableList()
        _saveTeams.clear()
        _saveTeams.addAll(data.map { t -> t.toState() })
    }

    fun removeTeams(item: CreateTeamState) {
        viewModelScope.launch {
            teamRepository.removeTeam(item.toModel(true))
        }
    }

}

data class FilterPlayer(
    val gender: String = "",
    val handPlay: String = "",
)

sealed class SortPlayer(val asc: Sort) {
    data class Id(val sort: Sort): SortPlayer(sort)
    data class Name(val sort: Sort): SortPlayer(sort)
    data class Height(val sort: Sort): SortPlayer(sort)
    data class Weight(val sort: Sort): SortPlayer(sort)
    data class Age(val sort: Sort): SortPlayer(sort)
}

sealed class SortTeam(val asc: Sort) {
    data class Id(val sort: Sort): SortTeam(sort)
    data class Name(val sort: Sort): SortTeam(sort)
    data class Rank(val sort: Sort): SortTeam(sort)
    data class Play(val sort: Sort): SortTeam(sort)
    data class Win(val sort: Sort): SortTeam(sort)
    data class Lose(val sort: Sort): SortTeam(sort)
}