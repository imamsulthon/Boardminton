package com.imams.boardminton.ui.screen.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.data.toList
import com.imams.boardminton.domain.impl.BoardEvent
import com.imams.boardminton.domain.impl.MatchBoardUseCase
import com.imams.boardminton.domain.mapper.MatchRepoMapper.toRepo
import com.imams.boardminton.domain.mapper.any
import com.imams.boardminton.domain.mapper.gameWinnerBy
import com.imams.boardminton.domain.mapper.matchWinnerBy
import com.imams.boardminton.domain.model.Court
import com.imams.boardminton.domain.model.CourtSide
import com.imams.boardminton.domain.model.IMatchType
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.domain.model.MatchUIState
import com.imams.boardminton.domain.model.WinnerState
import com.imams.data.match.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreBoardVM @Inject constructor(
    private val useCase: MatchBoardUseCase,
    private val repository: MatchRepository,
) : ViewModel() {

    private var alreadySetup = false
    private var courtConfig = CourtSide(left = ISide.A, right = ISide.B)

    private val _matchUiState = MutableStateFlow(MatchUIState().apply { courtSide = courtConfig })
    val matchUIState: StateFlow<MatchUIState> = _matchUiState.asStateFlow()

    private val _winnerState = MutableStateFlow(WinnerState(WinnerState.Type.Game, 1, isWin = false, show = false, by = ""))
    val winnerState: StateFlow<WinnerState> = _winnerState.asStateFlow()

    init {
        observeWinner()
    }

    private fun observeWinner() {
        viewModelScope.launch {
            matchUIState.collect {
                it.match.currentGame.let { g ->
                    if (g.scoreA.point > 19 || g.scoreB.point > 19) {
                        printLog("check: Winner ${g.winner}, game $g")
                    }
                }
                when {
                    it.match.winner.any() -> {
                        _winnerState.update { state ->
                            state.copy(
                                type = WinnerState.Type.Match,
                                isWin = true,
                                show = true,
                                by = it.match.matchWinnerBy()
                            )
                        }
                    }
                    it.match.currentGame.winner.any() -> {
                        _winnerState.update { state ->
                            state.copy(
                                type = WinnerState.Type.Game,
                                index = it.match.currentGame.index,
                                isWin = true,
                                show = true,
                                by = it.match.gameWinnerBy()
                            )
                        }
                    }
                }
            }
        }
    }

    fun setupPlayer(json: String, single: Boolean) {
        if (alreadySetup) return
        val data = json.toList()
        _matchUiState.update {
            if (single) {
                useCase.create(data[0], data[2])
            } else {
                useCase.create(data[0], data[1], data[2], data[3])
            }
            it.copy(match = useCase.getMatch()).apply { setScoreByCourt(courtConfig) }
        }
        alreadySetup = true
    }

    fun updatePlayers(json: String, single: Boolean) {
        viewModelScope.launch {
            val data = json.toList()
            val t = if (single) IMatchType.Single else IMatchType.Double
            _matchUiState.update {
                useCase.updatePlayers(t, data[0], data[1], data[2], data[3])
                it.copy(match = useCase.getMatch()).apply { setScoreByCourt(courtConfig) }
            }
        }
    }

    fun pointTo(onCourt: Court) {
        viewModelScope.launch {
            val side = onCourt.asSide(courtConfig)
            _matchUiState.update {
                useCase.execute(BoardEvent.PointTo(side))
                it.copy(match = useCase.getMatch()).apply { setScoreByCourt(courtConfig) }
            }
        }
    }

    fun minusPoint(onCourt: Court) {
        viewModelScope.launch {
            val side = onCourt.asSide(courtConfig)
            _matchUiState.update {
                useCase.execute(BoardEvent.MinTo(side))
                it.copy(match = useCase.getMatch()).apply { setScoreByCourt(courtConfig) }
            }
        }
    }

    fun swapServe() {
        viewModelScope.launch {
            _matchUiState.update {
                useCase.execute(BoardEvent.SwapServer)
                it.copy(match = useCase.getMatch()).apply { setScoreByCourt(courtConfig) }
            }
        }
    }

    fun swapCourt() {
        viewModelScope.launch {
            _matchUiState.update {
                courtConfig.swap()
                it.apply { this.setScoreByCourt(courtConfig) }
            }
        }
    }

    fun resetGame() {
        useCase.execute(BoardEvent.ResetGame)
        _matchUiState.update {
            it.copy(match = useCase.getMatch()).apply { setScoreByCourt(courtConfig) }
        }
    }

    fun onGameEndDialog(finishIt: Boolean, type: WinnerState.Type) {
        if (type == WinnerState.Type.Match) {
            onEndMatch()
            _winnerState.update { it.copy(show = false) }
        } else if (finishIt) {
            onNewGame()
            _winnerState.update { it.copy(show = false) }
        } else {
            _winnerState.update { it.copy(show = false) }
        }
    }

    private fun onEndMatch() {
        // todo
    }

    private fun onNewGame() {
        _matchUiState.update {
            useCase.execute(BoardEvent.OnNewGame(it.match.currentGame.index + 1))
            it.copy(match = useCase.getMatch()).apply { this.setScoreByCourt(courtConfig) }
        }
    }

    private fun Court.asSide(config: CourtSide) = when (this) {
        Court.Left -> config.left
        Court.Right -> config.right
    }

    fun saveGame(callback: () -> Unit) {
        viewModelScope.launch {
            repository.updateMatch(matchUIState.value.match.toRepo())
            callback.invoke()
        }
    }

    private fun printLog(msg: String) = println("ScoreBoardVM $msg")

}