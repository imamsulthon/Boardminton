package com.imams.boardminton.ui.screen.timer

import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.imams.boardminton.ui.screen.timer.Utility.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class CountTimerViewModel @Inject constructor(
    // todo
): ViewModel() {

    private var countDownTimer: CountDownTimer? = null

    private val _time = MutableLiveData(Utility.TIME_COUNTDOWN.formatTime())
    val time: LiveData<String> = _time

    private val _progress = MutableLiveData(1.00F)
    val progress: LiveData<Float> = _progress

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _celebrate = SingleLiveEvent<Boolean>()

    val celebrate : LiveData<Boolean> get() =  _celebrate

    init {
        handleCountDownTimer()
    }

    //region Public methods
    private fun handleCountDownTimer() {
        if (isPlaying.value == true) {
            pauseTimer()
            _celebrate.postValue(false)
        } else {
            startTimer()
        }
    }
    // endregion

    //region Private methods
    private fun pauseTimer() {
        countDownTimer?.cancel()
        handleTimerValues(false, Utility.TIME_COUNTDOWN.formatTime(), 1.0F, false)

    }

    private fun startTimer() {
        _isPlaying.value = true

        countDownTimer = object : CountDownTimer(Utility.TIME_COUNTDOWN, 1000) {
            override fun onTick(millisRemaining: Long) {
                val progressValue = millisRemaining.toFloat() / Utility.TIME_COUNTDOWN
                handleTimerValues(true, millisRemaining.formatTime(), progressValue, false)
                _celebrate.postValue(false)
            }

            override fun onFinish() {
                pauseTimer()
                _celebrate.postValue(true)
            }
        }.start()
    }

    private fun handleTimerValues(isPlaying: Boolean, text: String, progress: Float, celebrate: Boolean) {
        _isPlaying.value = isPlaying
        _time.value = text
        _progress.value = progress
        _celebrate.postValue(celebrate)
    }
    //endregion

}

object Utility {

    //time to countdown - 1hr - 60secs
    const val TIME_COUNTDOWN: Long = 3600000L
    private const val TIME_FORMAT = "%02d:%02d:%02d"


    //convert time to milli seconds
    fun Long.formatTime(): String = String.format(
        TIME_FORMAT,
        TimeUnit.MILLISECONDS.toHours(this),
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )

}

class SingleLiveEvent  <T>: MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Log.e("message","Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer<T> { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }

}