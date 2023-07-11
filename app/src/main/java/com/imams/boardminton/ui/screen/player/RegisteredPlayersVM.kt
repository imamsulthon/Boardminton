package com.imams.boardminton.ui.screen.player

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.mapper.UseCaseMapper.toModel
import com.imams.boardminton.domain.mapper.UseCaseMapper.toState
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.data.player.model.Player
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

    private val _tempData = mutableStateListOf<Player>()
    private val _savePlayers = mutableStateListOf<CreatePlayerState>()
    private val _savePlayersFlow = MutableStateFlow(_savePlayers)
    val savePlayers: StateFlow<List<CreatePlayerState>> = _savePlayersFlow

    init {
        checkSavedPlayers()
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
                printLog("gender ${params.gender}")
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
        }
    }

    fun removePlayer(item: CreatePlayerState) {
        viewModelScope.launch {
            repository.removePlayer(item.toModel(true))
        }
    }

    private fun printLog(msg: String) {
        println("RegisteredPlayer VM $msg")
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
}

enum class Sort {
    Ascending, Descending
}