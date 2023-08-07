package com.imams.boardminton.ui.screen.score

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.data.toList
import com.imams.boardminton.domain.impl.BoardEvent
import com.imams.boardminton.domain.impl.MatchBoardUseCase
import com.imams.boardminton.domain.mapper.MatchRepoMapper.toRepo
import com.imams.boardminton.domain.mapper.MatchRepoMapper.toVp
import com.imams.boardminton.domain.mapper.any
import com.imams.boardminton.domain.mapper.gameWinnerBy
import com.imams.boardminton.domain.mapper.matchWinnerBy
import com.imams.boardminton.domain.model.Court
import com.imams.boardminton.domain.model.CourtSide
import com.imams.boardminton.domain.model.IMatchType
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.domain.model.MatchUIState
import com.imams.boardminton.domain.model.WinnerState
import com.imams.boardminton.ui.screen.timer.MatchTimerGenerator
import com.imams.boardminton.ui.screen.timer.TimeCounterUiState
import com.imams.data.match.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreBoardVM @Inject constructor(
    private val useCase: MatchBoardUseCase,
    private val repository: MatchRepository,
) : ViewModel(), DefaultLifecycleObserver {

    private var alreadySetup = false
    private var courtConfig = CourtSide(left = ISide.A, right = ISide.B)

    private val _matchUiState = MutableStateFlow(MatchUIState(matchDuration = 0L).apply { courtSide = courtConfig })
    val matchUIState: StateFlow<MatchUIState> = _matchUiState.asStateFlow()

    private val _winnerState = MutableStateFlow(WinnerState(WinnerState.Type.Game, 1, isWin = false, show = false, by = ""))
    val winnerState: StateFlow<WinnerState> = _winnerState.asStateFlow()

    private val timeGenerator by lazy { MatchTimerGenerator() }
    val tcUiState : StateFlow<TimeCounterUiState> get() = timeGenerator.getUiState()

    init {
        observeWinner()
    }

    private fun observeWinner() {
        viewModelScope.launch {
            _matchUiState.collect {
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

    fun setupPlayer(id: Int?, json: String, single: Boolean) {
        if (alreadySetup) return
        if (id == null) setupPlayer(json, single) else setupWithId(id)
    }

    private fun setupPlayer(json: String, single: Boolean) {
        val data = json.toList()
        _matchUiState.update {
            if (single) {
                useCase.create(data[0], data[2])
            } else {
                useCase.create(data[0], data[1], data[2], data[3])
            }
            it.copy(match = useCase.getMatch()).apply { setScoreByCourt(courtConfig) }
        }.also {
            alreadySetup = true
            generateMatchTimer(0L)
        }
    }

    private fun setupWithId(id: Int) {
        viewModelScope.launch {
            val data = repository.getMatch(id).first().toVp()
            _matchUiState.update {
                useCase.create(data)
                it.copy(match = useCase.getMatch(), matchDuration = data.matchDurations).apply {
                    setScoreByCourt(courtConfig)
                }
            }.also {
                alreadySetup = true
                generateMatchTimer(data.matchDurations)
            }
        }
    }

    private fun generateMatchTimer(init: Long? = null) {
        timeGenerator.start(init)
    }

    fun updatePlayers(json: String, single: Boolean) {
        viewModelScope.launch {
            val data = json.toList()
            val t = if (single) IMatchType.Single else IMatchType.Double
            _matchUiState.update {
                if (single) {
                    useCase.updatePlayers(t, data[0], "", data[1], "")
                } else {
                    useCase.updatePlayers(t, data[0], data[1], data[2], data[3])
                }
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

    fun addShuttleCock() {
        viewModelScope.launch {
            _matchUiState.update {
                useCase.execute(BoardEvent.AddShuttleCock)
                it.copy(match = useCase.getMatch())
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
        timeGenerator.restart()
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
        timeGenerator.pause()
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

    fun updateGame(callback: (() -> Unit)? = null) {
        val duration = timeGenerator.currentTimerInSeconds(pause = true)
        viewModelScope.launch {
            repository.updateMatch(_matchUiState.value.match.toRepo().apply { matchDuration = duration })
            callback?.invoke()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        updateGame()
        super.onDestroy(owner)
    }

}