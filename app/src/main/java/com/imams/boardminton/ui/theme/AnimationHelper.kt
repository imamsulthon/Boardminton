package com.imams.boardminton.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

@ExperimentalAnimationApi
fun scoreUpDownAnimation(increase: Boolean, duration: Int = 500): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height ->
        if (increase) -height/2 else height
    } + fadeIn(animationSpec = tween(durationMillis = duration)) with slideOutVertically(
        animationSpec = tween(durationMillis = duration)) { height ->
        if (increase) 0 else -height/2
    } + fadeOut(animationSpec = tween(durationMillis = duration))
}