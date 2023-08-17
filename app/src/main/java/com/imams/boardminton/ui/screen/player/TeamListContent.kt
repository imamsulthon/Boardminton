package com.imams.boardminton.ui.screen.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imams.boardminton.ui.component.EmptyContent
import com.imams.boardminton.ui.component.SwipeToDismissItem
import com.imams.boardminton.ui.screen.create.player.CreateTeamState
import com.imams.boardminton.ui.utils.bottomDialogPadding

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
                enableFilter = false
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
                    SwipeToDismissItem(
                        onSwipeDismiss = { viewModel.removeTeams(it) },
                        onItemClick = { onItemClick.invoke(it) }
                    ) {
                        TeamItem(it)
                    }
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
                    SortTeamSheet(
                        filter = SortTeam.Id(Sort.Ascending),
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

@Composable
fun SortTeamSheet(
    filter: SortTeam,
    onApply: (SortTeam) -> Unit,
    onCancel: () -> Unit,
) {
    val sortData = listOf(Sort.Ascending, Sort.Descending)
    var init by remember { mutableStateOf(filter) }
    var sortId: Sort? by remember { mutableStateOf(null) }
    var sortName: Sort? by remember { mutableStateOf(null) }
    var sortRank: Sort? by remember { mutableStateOf(null) }
    var sortPlay: Sort? by remember { mutableStateOf(null) }
    var sortWin: Sort? by remember { mutableStateOf(null) }
    var sortLose: Sort? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier.fillMaxWidth().bottomDialogPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Sort by:", fontWeight = FontWeight.SemiBold)
        SortField(label = "ID", options = sortData, initialSelection = sortId?.name.orEmpty(),
            onSelected = {
                init = SortTeam.Id(it)
                sortId = init.asc
                sortName = null
                sortPlay = null
                sortRank = null
                sortWin = null
                sortLose = null
            }
        )
        SortField(label = "Name", options = sortData, initialSelection = sortName?.name.orEmpty(),
            onSelected = {
                init = SortTeam.Name(it)
                sortName = init.asc
                sortId = null
                sortPlay = null
                sortRank = null
                sortWin = null
                sortLose = null
            }
        )
        SortField(label = "Rank", options = sortData, initialSelection = sortRank?.name.orEmpty(),
            onSelected = {
                init = SortTeam.Rank(it)
                sortId = null
                sortName = null
                sortRank = init.asc
                sortPlay = null
                sortWin = null
                sortLose = null
            }
        )
        SortField(label = "Play", options = sortData, initialSelection = sortPlay?.name.orEmpty(),
            onSelected = {
                init = SortTeam.Play(it)
                sortId = null
                sortName = null
                sortRank = null
                sortPlay = init.asc
                sortWin = null
                sortLose = null
            }
        )
        SortField(label = "Win", options = sortData, initialSelection = sortWin?.name.orEmpty(),
            onSelected = {
                init = SortTeam.Win(it)
                sortId = null
                sortName = null
                sortRank = null
                sortPlay = null
                sortWin = init.asc
                sortLose = null
            }
        )
        SortField(label = "Lose", options = sortData, initialSelection = sortLose?.name.orEmpty(),
            onSelected = {
                init = SortTeam.Lose(it)
                sortId = null
                sortName = null
                sortRank = null
                sortLose = init.asc
                sortPlay = null
                sortWin = null
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 4.dp),
                onClick = { onCancel.invoke() }
            ) { Text(text = "Cancel") }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = { onApply.invoke(init) }
            ) { Text(text = "Apply") }
        }
    }
}