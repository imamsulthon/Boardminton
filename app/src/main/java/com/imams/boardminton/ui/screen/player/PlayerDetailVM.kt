package com.imams.boardminton.ui.screen.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.impl.CreatePlayerUseCase
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerDetailVM @Inject constructor(
    private val useCase: CreatePlayerUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(CreatePlayerState())
    val uiState: StateFlow<CreatePlayerState> = _uiState.asStateFlow()

    fun getPlayer(id: Int) {
        viewModelScope.launch {
            useCase.getPlayer(id).collectLatest { obj ->
                log("Collect $obj")
                _uiState.update { obj }
            }
        }
    }
    private fun log(m: String) = println("PlayerDetailVM: $m")


}