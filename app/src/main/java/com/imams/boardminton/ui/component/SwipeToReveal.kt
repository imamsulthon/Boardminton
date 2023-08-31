package com.imams.boardminton.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun SwipeToRevealContainer(
    modifier: Modifier = Modifier,
    swipeToRevealParameters: SwipeToRevealParameters,
    isRevealed: Boolean,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    rowContent: @Composable () -> Unit,
    actionContent: @Composable (RowScope.() -> Unit)
) {

    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardTransition")

    val offsetTransition by transition.animateFloat(
        label = "offsetTransition",
        transitionSpec = { tween(durationMillis = swipeToRevealParameters.swipeToRevealAnimationDurationMs) },
        targetValueByState = { if (isRevealed) swipeToRevealParameters.cardOffset.value else 0f },
    )

    val alphaTransition by transition.animateFloat(
        label = "alphaTransition",
        transitionSpec = { tween(durationMillis = swipeToRevealParameters.swipeToRevealAnimationDurationMs) },
        targetValueByState = { if (isRevealed) 1f else 0f },
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        ActionRow(
            alpha = alphaTransition,
            actionContent = actionContent,
        )
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .offset { IntOffset(offsetTransition.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        when {
                            dragAmount >= MIN_DRAG -> onExpand()
                            dragAmount < -MIN_DRAG -> onCollapse()
                        }
                    }
                },
        ) {
            rowContent()
        }
    }
}

private const val MIN_DRAG = 5

@Composable
private fun ActionRow(
    alpha: Float,
    actionContent: @Composable (RowScope.() -> Unit)
) {
    Row(
        Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .alpha(alpha = alpha)
    ) {
        actionContent(this)
    }
}

data class SwipeToRevealParameters(
    val swipeToRevealAnimationDurationMs: Int,
    val cardOffset: Dp,
)