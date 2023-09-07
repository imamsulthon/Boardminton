package com.imams.boardminton.ui.screen.timer

import com.imams.boardminton.ui.screen.timer.Util.pad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class MatchTimerGenerator {

    private var timeDuration: Duration = Duration.ZERO
    private lateinit var timer: Timer
    private var _gameDuration = mutableListOf<GameDuration>()
    private val _tcUiState = MutableStateFlow(TimeCounterUiState())

    fun getUiState(): StateFlow<TimeCounterUiState> {
        return _tcUiState.asStateFlow()
    }

    fun currentTimerInSeconds(pause: Boolean = true): Long {
        return timeDuration.inWholeSeconds.also { if (pause) timer.cancel() }
    }

    fun start(init: Long?) {
        init?.let { timeDuration = timeDuration.plus(it.seconds) }
        runTimer()
    }

    private fun runTimer() {
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            timeDuration = timeDuration.plus(1.seconds)
            updateTimeStates()
        }
    }

    private fun updateTimeStates() {
        timeDuration.toComponents { hours, minutes, seconds, _ ->
            _tcUiState.update {
                it.copy(
                    counter = Util.formatTime(seconds.pad(), minutes.pad(), hours.pad())
                )
            }
        }
    }

    fun pause() {
        timer.cancel()
    }

    fun restart() {
        timer.cancel()
        timeDuration = Duration.ZERO
        runTimer()
    }

    fun stop() {
        timer.cancel()
        timeDuration = Duration.ZERO
        updateTimeStates()
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