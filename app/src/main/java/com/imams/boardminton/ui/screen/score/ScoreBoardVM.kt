package com.imams.boardminton.ui.screen.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.data.GameScore
import com.imams.boardminton.data.toList
import com.imams.boardminton.domain.impl.BoardEvent
import com.imams.boardminton.domain.impl.MatchBoardUseCase
import com.imams.boardminton.domain.model.CourtSide
import com.imams.boardminton.domain.model.MatchUIState
import com.imams.boardminton.engine.data.model.MatchType
import com.imams.boardminton.engine.data.model.Side
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreBoardVM @Inject constructor(
    private val useCase: MatchBoardUseCase
) : ViewModel() {

    private var alreadySetup = false

    private val _matchUiState = MutableStateFlow(MatchUIState())
    val matchUIState: StateFlow<MatchUIState> = _matchUiState.asStateFlow()

    private var courtSide = CourtSide(
        left = Side.A,
        right = Side.B,
    )

    fun setupPlayer(json: String, single: Boolean) {
        val data = json.toList()
        printLog("setupPlayer $single $data")
        if (alreadySetup) return
        _matchUiState.update {
            if (single) {
                useCase.create(data[0], data[1])
            } else {
                useCase.create(data[0], data[1], data[2], data[3])
            }
            it.copy(match = useCase.get())
        }
        alreadySetup = true
    }

    fun updatePlayers(json: String, single: Boolean) {
        viewModelScope.launch {
            val data = json.toList()
            printLog("updatePlayer $single $data")
            val t = if (single) MatchType.Single else MatchType.Double
            _matchUiState.update {
                useCase.updatePlayers(t, data[0], data[1],data[2],data[3])
                it.copy(match = useCase.get())
            }
        }
    }

    fun plusA() {
        _matchUiState.update {
            useCase.execute(BoardEvent.PointTo(Side.A))
            it.copy(match = useCase.get())
        }
    }

    fun plusB() {
        _matchUiState.update {
            useCase.execute(BoardEvent.PointTo(Side.B))
            it.copy(match = useCase.get())
        }
    }

    fun minA() {
        _matchUiState.update {
            useCase.execute(BoardEvent.MinTo(Side.A))
            it.copy(match = useCase.get())
        }
    }

    fun minB() {
        _matchUiState.update {
            useCase.execute(BoardEvent.MinTo(Side.B))
            it.copy(match = useCase.get())
        }
    }

    fun swapServe() {
        _matchUiState.update {
            useCase.execute(BoardEvent.SwapServer)
            it.copy(match = useCase.get())
        }
    }

    fun reset() {

    }

    fun swapSide() {

    }

    fun setGameEnd(show: Boolean) {
//        _anyWinner.value = _anyWinner.value.copy(show = show)
    }

    private fun GameScore.checkWinner() {

    }

    fun onNewGame() {

    }

    private fun resetWinner() {
//        gameIndex += 1
//        _anyWinner.value = _anyWinner.value.copy(index = gameIndex, show = false, isWin = false, by = "")
    }

    private fun printLog(msg: String) = println("ScoreBoardVM $msg")

}

data class WinnerState(
    val index: Int,
    val isWin: Boolean = false,
    val show: Boolean = true,
    val by: String,
)