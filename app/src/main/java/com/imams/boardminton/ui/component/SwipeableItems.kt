package com.imams.boardminton.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.swipeable
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

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
    onItemSwiped: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    content: @Composable () -> Unit,
    actionContent: @Composable (RowScope.() -> Unit)
) {
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)) {
        ActionRow(modifier = Modifier.align(Alignment.CenterEnd)) {
            actionContent(this)
        }
        ForegroundListItem(
            index,
            onItemSwiped = { onItemSwiped.invoke(index) },
            content
        )
    }
}


private fun printLog(m: String) {
    println("SwipeToOptional $m")
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ForegroundListItem(
    index: Int,
    onItemSwiped: (Int) -> Unit,
    content: @Composable () -> Unit,
) {
    val swipeState = androidx.compose.material.rememberSwipeableState(
        initialValue = SwipeState.VISIBLE,
        confirmStateChange = {
            if (it == SwipeState.SWIPED) {
                onItemSwiped.invoke(index)
            }
            true
        }
    )
    val swipeAnchors =
        mapOf(0f to SwipeState.VISIBLE, -1000f to SwipeState.SWIPED, -500f to SwipeState.MIDDLE)

    Row(
        modifier = Modifier
            .swipeable(
                state = swipeState,
                anchors = swipeAnchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
            .offset { IntOffset(swipeState.offset.value.roundToInt(), 0) }
            .background(MaterialTheme.colorScheme.primary)
            ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }

}

private const val MIN_DRAG = 5

@Composable
private fun ActionRow(
    modifier: Modifier,
    actionContent: @Composable (RowScope.() -> Unit)
) {
    Row(
        modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.End,
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
        Button(onClick = {
            printLog("BackGroundAction onDelete")
            onDelete.invoke()
        }) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = null
            )
        }
        IconButton(onClick = {
            printLog("BackGroundAction onEdit")
            onEdit.invoke()
        }) {
            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = null
            )
        }
    }
}

