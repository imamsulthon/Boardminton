package com.imams.boardminton.ui.screen.player

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
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
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState

@Composable
fun RegisteredPlayersScreen(
    viewModel: RegisteredPlayersVM = hiltViewModel(),
    addNewPlayer: () -> Unit,
) {
    val list by viewModel.savePlayers.collectAsState()
    RegisteredPlayersScreen(list = list,
        addNewPlayer = { addNewPlayer.invoke() },
        remove = { viewModel.removePlayer(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RegisteredPlayersScreen(
    list: List<CreatePlayerState>,
    addNewPlayer: () -> Unit,
    remove: (CreatePlayerState) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(playerSize = list.size, onActionClick = { addNewPlayer.invoke() })
        },
        bottomBar = {
            BottomBar(
                onFilter = { }, onSort = { },
                enableFilter = false, enableSort = false
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(padding)
        ) {
            items(items = list, key = { listItem: CreatePlayerState -> listItem.id }) {
                val dismissState = rememberDismissState(
                    confirmValueChange = { dismissState ->
                        if (dismissState == DismissValue.DismissedToStart || dismissState == DismissValue.DismissedToEnd) {
                            remove(it)
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
                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
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
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 5.dp,
                                bottomEnd = 5.dp,
                                bottomStart = 5.dp
                            ),
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 10.dp),
                        ) {
                            PlayerItem(item = it, onClick = { })
                        }
                    })
            }
        }
    }
}

@Composable
private fun PlayerItem(
    item: CreatePlayerState,
    onClick: (CreatePlayerState) -> Unit,
) {
    ListItem(
        leadingContent = {
            val ic = if (item.gender.isEmpty()) Icons.Outlined.Person
            else if (item.gender.equals("man", true))
                Icons.Filled.Person
            else Icons.Rounded.Person
            Icon(
                ic,
                contentDescription = "Localized description"
            )
        },
        trailingContent = {
            Text(text = item.handPlay)
        },
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        headlineContent = {
            val label = "${item.firstName} ${item.lastName}"
            Text(label, fontWeight = FontWeight.Bold)
        },
        supportingContent = {
            Text(text = "ID: ${item.id}, H: ${item.height} cm / W: ${item.weight} kg", fontSize = 10.sp)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    playerSize: Int,
    onActionClick: () -> Unit
) {
    TopAppBar(
        title = {
            val label = if (playerSize > 0) "Registered Players ($playerSize)" else "Registered Player"
            Text(text = label)
        },
        actions = {
            IconButton(onClick = { onActionClick.invoke() }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
private fun BottomBar(
    onFilter: () -> Unit,
    onSort: () -> Unit,
    enableFilter: Boolean = true,
    enableSort: Boolean = true,
) {
    BottomAppBar {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(
                enabled = enableSort,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 5.dp),
                onClick = { onSort.invoke() }) {
                Text(text = "Sort")
            }
            Button(enabled = enableFilter,
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

@Preview
@Composable
private fun ListContent() {
    val list = mutableListOf<CreatePlayerState>()
    list.add(CreatePlayerState(1, Athlete.Viktor, "", "Left", "Man"))
    list.add(CreatePlayerState(2, Athlete.Imam_Sulthon, "", "Right", "Man"))
    list.add(CreatePlayerState(3, Athlete.Taufik_Hidayat, "", "Right", "Woman"))
    list.add(CreatePlayerState(4, "Anthony", "Ginting", "Left", "Woman"))
    RegisteredPlayersScreen(list, addNewPlayer = {}, remove = {})
}