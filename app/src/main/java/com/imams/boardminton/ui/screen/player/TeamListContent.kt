package com.imams.boardminton.ui.screen.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.imams.boardminton.R
import com.imams.boardminton.domain.model.Sort
import com.imams.boardminton.ui.component.EmptyContent
import com.imams.boardminton.ui.component.ProfileImage
import com.imams.boardminton.ui.component.SortField
import com.imams.boardminton.ui.component.SwipeToOptional
import com.imams.boardminton.ui.screen.create.player.CreateTeamState
import com.imams.boardminton.ui.utils.bottomDialogPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TeamList(
    viewModel: RegisteredPlayersVM,
    addNewTeam: () -> Unit,
    onItemClick: (CreateTeamState) -> Unit,
    onEditTeam: (CreateTeamState) -> Unit,
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
                itemsIndexed(
                    items = list,
                    itemContent = { index, item ->
                        SwipeToOptional(
                            index = index,
                            onItemFullSwipe = { viewModel.removeTeams(item) },
                            onItemClick = { onItemClick.invoke(item) },
                            onDelete = { viewModel.removeTeams(item) },
                            onEdit = { onEditTeam.invoke(item) },
                            content = { TeamItem(item) }
                        )
                    }
                )
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
    Card {
        ConstraintLayout(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            val (vS, vC, vE) = createRefs()
            val compPad = 2.dp
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(vS) {
                        top.linkTo(vC.top)
                        bottom.linkTo(vC.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(compPad),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imgRes = R.drawable.ic_player_man
                ProfileImage(modifier = Modifier.padding(end = 4.dp),
                    imgUriPath = item.profilePhotoUri1, imgDefault = imgRes, size = 32.dp,
                )
                ProfileImage(modifier = Modifier.padding(end = 4.dp),
                    imgUriPath = item.profilePhotoUri2, imgDefault = imgRes, size = 32.dp
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(vC) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(vS.end)
                        end.linkTo(vE.start)
                        width = Dimension.fillToConstraints
                    }
                    .padding(compPad),
                horizontalAlignment = Alignment.Start
            ) {
                Text(item.alias, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${item.playerName1} (${item.playerId1}) & ${item.playerName2} (${item.playerId2})\n" +
                            "Play; ${item.play}. Win; ${item.win}. Lose; ${item.lose}.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                modifier = Modifier.wrapContentSize().padding(compPad).constrainAs(vE) {
                        end.linkTo(parent.end)
                        top.linkTo(vC.top)
                        bottom.linkTo(vC.bottom)
                    },
                text = item.id.toString()
            )
        }
    }
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
        modifier = Modifier
            .fillMaxWidth()
            .bottomDialogPadding(),
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
        Spacer(modifier = Modifier.padding(vertical = 5.dp))
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    TeamItem(item = CreateTeamState(playerName1 = "Imam Sulthon", playerName2 = "Ratna Yunita"))
}