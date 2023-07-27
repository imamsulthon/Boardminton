package com.imams.boardminton.ui.screen.player

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.R
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.ui.component.EmptyContent
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.boardminton.ui.screen.create.player.GenderField
import com.imams.boardminton.ui.screen.create.player.HandPlays
import kotlinx.coroutines.launch

@Composable
fun PlayerAndTeamsList(
    viewModel: RegisteredPlayersVM = hiltViewModel(),
    addNewPlayer: () -> Unit,
    onEditPlayer: (id: Int) -> Unit,
) {
    ContentWrapper(
        playerList = {
            PlayerList(
                viewModel = viewModel,
                addNewPlayer = addNewPlayer::invoke,
                onItemClick = { onEditPlayer.invoke(it.id) },
            )},
        teamList = {
            TeamList(
                viewModel = viewModel,
                addNewTeam = { },
                onItemClick = { },
            ) },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContentWrapper(
    playerList: @Composable () -> Unit,
    teamList: @Composable () -> Unit,
) {
    val tabData = listOf(
        "Players" to Icons.Filled.Home,
        "Teams" to Icons.Filled.AccountBox,
    )
    val pagerState = rememberPagerState(pageCount = { tabData.size })
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Column {
        TabRow(selectedTabIndex = tabIndex) {
            tabData.forEachIndexed { index, pair ->
                Tab(
                    modifier = Modifier.padding(10.dp),
                    selected = tabIndex == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                ) {
                    when (index) {
                        0 -> Text(text = pair.first, Modifier.padding(5.dp))
                        else -> Text(text = pair.first, Modifier.padding(5.dp))
                    }
                }
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { index ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (index) {
                    0 -> playerList()
                    1 -> teamList()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlayerList(
    viewModel: RegisteredPlayersVM,
    addNewPlayer: () -> Unit,
    onItemClick: (CreatePlayerState) -> Unit,
) {

    val list by viewModel.savePlayers.collectAsState()

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
            SmallFloatingActionButton(onClick = { addNewPlayer.invoke() }) {
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        },
    ) { padding ->
        if (list.isEmpty()) {
            EmptyContent(message = "No Player registered")
        } else {
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
                                viewModel.removePlayer(it)
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
                                PlayerItem(it)
                            }
                        })
                }
            }
            if (openFilterDialog) {
                ModalBottomSheet(
                    onDismissRequest = { openFilterDialog = false },
                    sheetState = bottomSheetState,
                ) {
                    FilterSheet(
                        filter = FilterPlayer(),
                        onApply = {
                            openFilterDialog = false
                            viewModel.setFilter(it)
                        },
                        onCancel = { openFilterDialog = false }
                    )
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
private fun PlayerItem(
    item: CreatePlayerState,
) {
    ListItem(
        leadingContent = {
            when (item.gender.toLowerCase(Locale.current)) {
                "man" -> Icon(
                    painter = painterResource(id = R.drawable.ic_player_man),
                    contentDescription = "player_man"
                )

                "woman" -> Icon(
                    painter = painterResource(id = R.drawable.ic_player_woman),
                    contentDescription = "player_man"
                )

                else -> Icon(Icons.Outlined.Person, contentDescription = "player_man")
            }
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
            Text(
                text = "ID: ${item.id}, Height: ${item.height} cm / Weight: ${item.weight} kg",
                fontSize = 10.sp
            )
        }
    )
}

@Composable
fun ListBottomBar(
    onFilter: () -> Unit,
    onSort: () -> Unit,
    enableFilter: Boolean = true,
    enableSort: Boolean = true,
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

@Composable
private fun FilterSheet(
    filter: FilterPlayer,
    onApply: (FilterPlayer) -> Unit,
    onCancel: () -> Unit,
) {
    var sGender by remember { mutableStateOf(filter.gender) }
    var sHandPlay by remember { mutableStateOf(filter.handPlay) }
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 10.dp)
    ) {
        Text(text = "Filter by:")
        GenderField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            onSelected = { sGender = it }, initialSelection = sGender
        )
        HandPlays(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            initialSelection = sHandPlay, onSelected = { sHandPlay = it }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 4.dp),
                onClick = { onCancel.invoke() }) {
                Text(text = "Cancel")
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = { onApply.invoke(filter.copy(gender = sGender, handPlay = sHandPlay)) }
            ) { Text(text = "Apply") }
        }
    }
}

@Composable
fun SortSheet(
    filter: SortPlayer,
    onApply: (SortPlayer) -> Unit,
    onCancel: () -> Unit,
) {
    val sortData = listOf(Sort.Ascending, Sort.Descending)
    var init by remember { mutableStateOf(filter) }
    var sortId: Sort? by remember { mutableStateOf(null) }
    var sortName: Sort? by remember { mutableStateOf(null) }
    var sortHeight: Sort? by remember { mutableStateOf(null) }
    var sortWeight: Sort? by remember { mutableStateOf(null) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Sort by:")
        SortField(label = "ID", options = sortData, initialSelection = sortId?.name.orEmpty(),
            onSelected = {
                init = SortPlayer.Id(it)
                sortId = init.asc
                sortName = null
                sortWeight = null
                sortHeight = null
            }
        )
        SortField(label = "Name", options = sortData, initialSelection = sortName?.name.orEmpty(),
            onSelected = {
                init = SortPlayer.Name(it)
                sortName = init.asc
                sortId = null
                sortWeight = null
                sortHeight = null
            }
        )
        SortField(label = "Height", options = sortData, initialSelection = sortHeight?.name.orEmpty(),
            onSelected = {
                init = SortPlayer.Height(it)
                sortHeight = init.asc
                sortId = null
                sortName = null
                sortWeight = null
            }
        )
        SortField(label = "Weight", options = sortData, initialSelection = sortWeight?.name.orEmpty(),
            onSelected = {
                init = SortPlayer.Weight(it)
                sortWeight = init.asc
                sortId = null
                sortName = null
                sortHeight = null
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortField(
    label: String,
    options: List<Sort>,
    initialSelection: String = "",
    onSelected: (Sort) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$label:")
        Row(Modifier.selectableGroup()) {
            options.forEach { text ->
                InputChip(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    selected = text.name.equals(initialSelection, true),
                    onClick = { onSelected.invoke(text) },
                    label = {
                        Text(
                            text = text.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                )
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
    list.add(CreatePlayerState(3, Athlete.Taufik_Hidayat, "", "Right", "Man"))
    list.add(CreatePlayerState(4, "Anthony", "Ginting", "Left", "Woman"))
    list.add(CreatePlayerState(5, "Carolina", "Marin", "Left", "Woman"))
    list.add(CreatePlayerState(6, "Susi", "Susanti", "Left", "Woman"))
}