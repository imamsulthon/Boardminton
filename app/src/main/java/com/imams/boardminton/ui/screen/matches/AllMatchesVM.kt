package com.imams.boardminton.ui.screen.matches

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.mapper.MatchRepoMapper.toRepo
import com.imams.boardminton.domain.mapper.MatchRepoMapper.toVp
import com.imams.boardminton.domain.model.IMatchType
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.data.match.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllMatchesVM @Inject constructor(
    private var repository: MatchRepository,
): ViewModel() {

    private val _savePlayers = mutableStateListOf<MatchViewParam>()
    private val _savePlayersFlow = MutableStateFlow(_savePlayers)
    val savePlayers: StateFlow<List<MatchViewParam>> = _savePlayersFlow

    fun fetchData() {
        viewModelScope.launch {
            repository.getAllMatches().collectLatest {
                val res = it.map { match -> match.toVp() }.sortedByDescending { m -> m.id }
                _savePlayers.clear()
                _savePlayers.addAll(res)
            }
        }
    }

    // todo: add sort from viewModel
    fun sort() {
        _savePlayers.sortedBy {
            it.id
        }
    }

    // todo: add filter from viewModel
    fun filter() {
        _savePlayers.filter {
            it.matchType == IMatchType.Double
        }
    }

    fun remove(item: MatchViewParam) {
        viewModelScope.launch {
            repository.deleteMatch(item.toRepo())
        }
    }

}