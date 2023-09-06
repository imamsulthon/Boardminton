package com.imams.boardminton.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissItem(
    directions: Set<DismissDirection> = setOf(DismissDirection.EndToStart),
    onSwipeDismiss: () -> Unit,
    onItemClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val dismissState = rememberDismissState(
        confirmValueChange = { dismissState ->
            if (dismissState == DismissValue.DismissedToStart || dismissState == DismissValue.DismissedToEnd) {
                onSwipeDismiss.invoke()
            }
            true
        }
    )
    SwipeToDismiss(
        state = dismissState,
        directions = directions,
        background = {
            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    DismissValue.Default -> Color.Transparent
                    DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.outlineVariant
                    DismissValue.DismissedToStart -> MaterialTheme.colorScheme.outline
                }, label = ""
            )
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f, label = ""
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Icon",
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = {
            Card(
                onClick = {
                    onItemClick.invoke()
                },
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 5.dp,
                    bottomEnd = 5.dp,
                    bottomStart = 5.dp
                ),
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
            ) {
                content()
            }
        }
    )
}

enum class SwipeState {
    SWIPED, VISIBLE, MIDDLE
}

@Composable
fun SwipeToOptional(
    index: Int,
    onItemFullSwipe: (Int) -> Unit,
    onItemClick: () -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    content: @Composable () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onItemClick.invoke() },
        contentAlignment = Alignment.Center,
    ) {
        ForegroundListItem(
            index,
            onFullSwiped = { onItemFullSwipe.invoke(index) },
            onHalfSwiped = { visible = true },
            onFullVisible = { visible = false },
            content
        )
        val transitionState = remember { MutableTransitionState(visible).apply { targetState = !visible } }
        val transition = updateTransition(transitionState, "cardTransition")
        val alphaTransition by transition.animateFloat(
            label = "alphaTransition",
            transitionSpec = { tween(durationMillis = 500) },
            targetValueByState = { if (it) 1f else 0f },
        )
        AnimatedVisibility(visible = visible,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            ActionRow(modifier = Modifier.align(Alignment.CenterEnd), alphaTransition) {
                BackgroundListItem(
                    this,
                    onDelete = { onDelete.invoke(index) },
                    onEdit = { onEdit.invoke(index) },
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForegroundListItem(
    index: Int,
    onFullSwiped: (Int) -> Unit,
    onHalfSwiped: (Int) -> Unit,
    onFullVisible: (Int) -> Unit,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val swipeAnchors = DraggableAnchors {
        SwipeState.VISIBLE at 0f
        SwipeState.MIDDLE at -500f
        SwipeState.SWIPED at -1000f
    }
    val swipeState = remember {
        AnchoredDraggableState(
            initialValue = SwipeState.VISIBLE,
            anchors = swipeAnchors,
            confirmValueChange = {
                when (it) {
                    SwipeState.VISIBLE -> onFullVisible.invoke(index)
                    SwipeState.MIDDLE -> onHalfSwiped.invoke(index)
                    SwipeState.SWIPED -> onFullSwiped.invoke(index)
                }
                true
            },
            animationSpec = tween(500),
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            positionalThreshold = { distance: Float -> distance * 0.5f },
        )
    }

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .anchoredDraggable(state = swipeState, orientation = Orientation.Horizontal)
            .offset { IntOffset(swipeState.requireOffset().toInt(), 0) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
private fun ActionRow(
    modifier: Modifier,
    alpha: Float,
    actionContent: @Composable (RowScope.() -> Unit)
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .alpha(alpha),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        actionContent(this)
    }
}

@Composable
fun BackgroundListItem(
    rowScope: RowScope,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    rowScope.apply {
        IconButton(onClick = onDelete::invoke) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }
        IconButton(onClick = onEdit::invoke) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        }
    }
}

