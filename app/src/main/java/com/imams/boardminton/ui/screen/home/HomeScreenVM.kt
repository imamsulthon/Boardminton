package com.imams.boardminton.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.mapper.MatchRepoMapper.toVp
import com.imams.boardminton.domain.model.MatchUIState
import com.imams.data.match.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenVM @Inject constructor(
    private val repository: MatchRepository,
): ViewModel() {

    private var _matchUiState = MutableStateFlow<MatchUIState?>(null)
    val latestMatch: StateFlow<MatchUIState?> = _matchUiState.asStateFlow()

    fun getLatestMatch() {
        viewModelScope.launch {
            repository.getLatestMatch().collectLatest {
                log("latest $it")
                if (it == null) {
                    _matchUiState.update { null }
                } else {
                    _matchUiState.update { state ->
                        state?.copy(match = it.toVp()) ?: MatchUIState(match = it.toVp())
                    }
                }
            }
        }
    }

    private fun log(msg: String) = println("HomeScreenVm: $msg")

}