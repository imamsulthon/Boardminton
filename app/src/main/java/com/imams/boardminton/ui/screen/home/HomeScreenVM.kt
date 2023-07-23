package com.imams.boardminton.ui.screen.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.mapper.MatchRepoMapper.toVp
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.data.match.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenVM @Inject constructor(
    private val repository: MatchRepository,
): ViewModel() {

    private val _onGoingMatches = mutableStateListOf<MatchViewParam>()
    private val _onGoingMatchesFlow = MutableStateFlow(_onGoingMatches)
    val onGoingMatches: StateFlow<List<MatchViewParam>> = _onGoingMatchesFlow

    fun getLatestMatch(max: Int = 3) {
        viewModelScope.launch {
            repository.getOnGoingMatches().collectLatest {
                _onGoingMatches.clear()
                val list = it?.map { item -> item.toVp() }
                    ?.sortedByDescending { item -> item.lastUpdate }
                    ?.take(max) ?: emptyList()
                _onGoingMatches.addAll(list)
            }
        }
    }

    private fun log(msg: String) = println("HomeScreenVm: $msg")

}