package com.imams.boardminton.ui.screen.timer

import androidx.lifecycle.ViewModel
import com.imams.boardminton.ui.screen.timer.Util.formatTime
import com.imams.boardminton.ui.screen.timer.Util.pad
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CounterTimerVM @Inject constructor(): ViewModel() {

    private var timeDuration: Duration = Duration.ZERO
    private lateinit var timer: Timer

    private var _gameDuration = mutableListOf<GameDuration>()

    private val _tcUiState = MutableStateFlow(TimeCounterUiState())
    val tcUiState: StateFlow<TimeCounterUiState> get() = _tcUiState

    fun start(init: Duration? = null) {
        init?.let { timeDuration.plus(it) }
        start()
    }

    private fun start() {
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            timeDuration = timeDuration.plus(1.seconds)
            updateTimeStates()
        }
    }

    private fun updateTimeStates() {
        timeDuration.toComponents { hours, minutes, seconds, _ ->
            _tcUiState.update {
                it.copy(
                    counter = formatTime(seconds.pad(), minutes.pad(), hours.pad())
                )
            }
        }
    }

    fun pause() {
        timer.cancel()
    }

    fun stop() {
        pause()
        timeDuration = Duration.ZERO
        updateTimeStates()
    }

    fun restart() {
        timer.cancel()
        timeDuration = Duration.ZERO
        start()
    }

    fun restart(onGame: Int) {
        val currentGameDuration = _gameDuration.find { it.index == onGame } ?: GameDuration(onGame)
        _gameDuration.add(currentGameDuration.apply {
            inWholeSeconds = timeDuration.inWholeSeconds
            inLabel = _tcUiState.value.counter
        })
        _tcUiState.update { it.copy(gameDuration = _gameDuration) }
    }

}

data class TimeCounterUiState(
    val counter: String = "00:00:00",
    val gameDuration: List<GameDuration> = listOf()
)

data class GameDuration(
    val index: Int = 1,
    var inWholeSeconds: Long = 0,
    var inLabel: String = "00:00:00"
)

internal object Util {

    fun formatTime(seconds: String, minutes: String, hours: String): String {
        return "$hours:$minutes:$seconds"
    }

    fun Long.pad(): String = this.toInt().pad()

    fun Int.pad(): String {
        return this.toString().padStart(2, '0')
    }

}