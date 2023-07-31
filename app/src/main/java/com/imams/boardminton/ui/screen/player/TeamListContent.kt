package com.imams.boardminton.ui.screen.player

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imams.boardminton.ui.component.EmptyContent
import com.imams.boardminton.ui.screen.create.player.CreateTeamState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TeamList(
    viewModel: RegisteredPlayersVM,
    addNewTeam: () -> Unit,
    onItemClick: (CreateTeamState) -> Unit,
) {

    val list by viewModel.saveTeams.collectAsState()

    var openFilterDialog by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded)
    var openSortDialog by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded2 by remember { mutableStateOf(false) }
    val bottomSheetState2 = rememberModalBottomSheetState(skipPartiallyExpanded2)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            ListBottomBar(
                onFilter = { openFilterDialog = true },
                onSort = { openSortDialog = true },
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = { addNewTeam.invoke() }) {
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        },
    ) { padding ->
        if (list.isEmpty()) {
            EmptyContent(message = "No Team registered")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(padding)
            ) {
                items(items = list, key = { listItem: CreateTeamState -> listItem.id }) {
                    val dismissState = rememberDismissState(
                        confirmValueChange = { dismissState ->
                            if (dismissState == DismissValue.DismissedToStart || dismissState == DismissValue.DismissedToEnd) {
                                viewModel.removeTeams(it)
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
                                onClick = {
                                    onItemClick.invoke(it)
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
                                TeamItem(it)
                            }
                        })
                }
            }
            if (openFilterDialog) {
                ModalBottomSheet(
                    onDismissRequest = { openFilterDialog = false },
                    sheetState = bottomSheetState,
                ) {

                }
            }
            if (openSortDialog) {
                ModalBottomSheet(
                    onDismissRequest = { openSortDialog = false },
                    sheetState = bottomSheetState2,
                ) {
                    SortSheet(
                        filter = SortPlayer.Id(Sort.Ascending),
                        onApply = {
                            openSortDialog = false
                            viewModel.setSorting(it)
                        },
                        onCancel = { openSortDialog = false })
                }
            }
        }
    }
}

@Composable
fun TeamItem(
    item: CreateTeamState
) {
    ListItem(
        leadingContent = {
            Text(text = item.rank.toString())
        },
        trailingContent = {
            Text(text = item.id.toString())
        },
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        overlineContent = {
            Text(text = item.type)
        },
        headlineContent = {
            val label = item.alias
            Text(label, fontWeight = FontWeight.Bold)
        },
        supportingContent = {
            Text(
                text = "${item.playerName1} (${item.playerId1}) & ${item.playerName2} (${item.playerId2})",
                fontSize = 10.sp
            )
            Text(
                text = "Play; ${item.play}. Win; ${item.win}. Lose; ${item.lose}.",
                fontSize = 10.sp
            )
        }
    )
}