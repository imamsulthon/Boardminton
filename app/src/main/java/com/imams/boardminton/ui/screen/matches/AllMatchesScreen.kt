package com.imams.boardminton.ui.screen.matches

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.data.prettifyDate
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.boardminton.domain.model.ScoreViewParam
import com.imams.boardminton.ui.component.EmptyContent
import com.imams.boardminton.ui.utils.getLabel

@Composable
fun AllMatchesScreen(
    viewModel: AllMatchesVM = hiltViewModel(),
    onSelect: ((String, Int) -> Unit)? = null,
) {

    val list by viewModel.savePlayers.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchData()
    }

    if (list.isNotEmpty()) {
        AllMatchContent(
            list = list,
            onRemove = viewModel::remove,
            onItemClick = {
                onSelect?.invoke(it.matchType.name.lowercase(), it.id)
            }
        )
    } else {
        EmptyContent(message = "No Match Found")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllMatchContent(
    list: List<MatchViewParam>,
    onRemove: (MatchViewParam) -> Unit,
    onItemClick: ((MatchViewParam) -> Unit)? = null,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                playerSize = list.size,
                onBackPressed = {},
                onActionClick = {},
            )
        },
        bottomBar = {
            BottomBar(
                onFilter = { }, onSort = { },
                enableFilter = false, enableSort = false
            )
        }
    ) { p ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(p)
        ) {
            items(
                items = list,
                key = { listItem: MatchViewParam -> listItem.id }
            ) {
                val dismissState = rememberDismissState(
                    confirmValueChange = { dismissState ->
                        if (dismissState == DismissValue.DismissedToStart || dismissState == DismissValue.DismissedToEnd) {
                            onRemove(it)
                        }
                        true
                    }
                )
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    background = {
                        val color by animateColorAsState(
                            targetValue = when (dismissState.targetValue) {
                                DismissValue.Default -> Color.Transparent
                                DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.outline
                                DismissValue.DismissedToStart -> MaterialTheme.colorScheme.outline
                            }
                        )
                        val scale by animateFloatAsState(
                            if (dismissState.targetValue == DismissValue.Default) 0.60f else 1f
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
                                onItemClick?.invoke(it)
                            },
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 5.dp,
                                bottomEnd = 5.dp,
                                bottomStart = 5.dp
                            ),
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 10.dp),
                        ) {
                            MatchItem(item = it, onClick = { onItemClick?.invoke(it) })
                        }
                    })
            }
        }
    }

}

@Composable
fun MatchItem(
    item: MatchViewParam,
    onClick: (MatchViewParam) -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable {
            onClick.invoke(item)
        },
        leadingContent = {
            Text(text = item.id.toString())
        },
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        headlineContent = {
            ItemContent(
                type = item.matchType.name,
                teamA = item.teamA.getLabel(),
                teamB = item.teamB.getLabel(),
                scoreA = item.currentGame.scoreA.point,
                scoreB = item.currentGame.scoreB.point,
                games = item.games,
            )
        },
        supportingContent = {
            Text(text = "${item.lastUpdate.prettifyDate()} in ${item.matchDurations}'s", fontSize = 10.sp)
        },
        trailingContent = {
            Text(text = "Winner\n${item.winner.name}")
        }
    )
}

@Composable
private fun ItemContent(
    type: String,
    teamA: String,
    teamB: String,
    scoreA: Int,
    scoreB: Int,
    games: MutableList<GameViewParam>,
) {
    Column {
        LazyRow(
            horizontalArrangement = Arrangement.Start,
        ) {
            item {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(end = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "$type match", fontWeight = FontWeight.Bold)
                    Text(text = teamA)
                    Text(text = teamB)
                }
            }
            items(items = games) {
                GameComponent(index = it.index, scoreA = it.scoreA.point, scoreB = it.scoreB.point)
            }
            if (games.size >= 3) return@LazyRow
            item {
                GameComponent(index = if (games.isEmpty()) 1 else games.size + 1, scoreA = scoreA, scoreB = scoreB)
            }
        }
    }
}

@Composable
internal fun GameComponent(
    index: Int,
    scoreA: Int,
    scoreB: Int,
) {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(end = 10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "G$index", fontWeight = FontWeight.Bold)
        Text(text = scoreA.toString())
        Text(text = scoreB.toString())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    playerSize: Int,
    onBackPressed: () -> Unit,
    onActionClick: () -> Unit
) {
    TopAppBar(
        title = {
            val label = if (playerSize > 0) "All Matches ($playerSize)" else "Registered Player"
            Text(text = label)
        },
        navigationIcon = {
            IconButton(onClick = { onBackPressed.invoke() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {}
    )
}

@Composable
private fun BottomBar(
    onFilter: () -> Unit,
    onSort: () -> Unit,
    enableFilter: Boolean = false,
    enableSort: Boolean = false,
) {
    BottomAppBar(tonalElevation = 1.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            OutlinedButton(
                enabled = enableSort,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 5.dp),
                onClick = { onSort.invoke() }) {
                Text(text = "Sort")
            }
            OutlinedButton(enabled = enableFilter,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 5.dp),
                onClick = { onFilter.invoke() }) {
                Text(text = "Filter")
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AllMatchesScreenPrev() {
    ItemContent(type = "Single", teamA = "Imam Sulthon", teamB = "Iqbal Kamal",
        games = mutableListOf(
            GameViewParam(1, ScoreViewParam(12), ScoreViewParam(21)),
            GameViewParam(2, ScoreViewParam(21), ScoreViewParam(19)),
        ),
        scoreB = 10, scoreA = 21
    )
}