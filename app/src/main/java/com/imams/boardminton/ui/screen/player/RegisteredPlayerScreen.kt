package com.imams.boardminton.ui.screen.player

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.imams.boardminton.R
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.data.asDateTime
import com.imams.boardminton.data.epochToAge
import com.imams.boardminton.ui.component.EmptyContent
import com.imams.boardminton.ui.component.FancyIndicator
import com.imams.boardminton.ui.component.SwipeToOptional
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.boardminton.ui.screen.create.player.GenderField
import com.imams.boardminton.ui.screen.create.player.HandPlays
import com.imams.boardminton.ui.utils.bottomDialogPadding
import kotlinx.coroutines.launch

@Composable
fun PlayerAndTeamsList(
    viewModel: RegisteredPlayersVM = hiltViewModel(),
    addNewPlayer: () -> Unit,
    onEditPlayer: (id: Int) -> Unit,
    onDetailPlayer: (Int) -> Unit,
    onEditTeam: (id: Int) -> Unit,
    addNewTeam: () -> Unit,
) {
    ContentWrapper(
        playerList = {
            PlayerList(
                viewModel = viewModel,
                addNewPlayer = addNewPlayer::invoke,
                onItemClick = { onDetailPlayer.invoke(it.id) },
                onEditPlayer = { onEditPlayer.invoke(it.id) }
            )
        },
        teamList = {
            TeamList(
                viewModel = viewModel,
                addNewTeam = addNewTeam::invoke,
                onItemClick = { },
            )
        },
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
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        FancyIndicator(MaterialTheme.colorScheme.primary, Modifier.tabIndicatorOffset(tabPositions[tabIndex]))
    }
    Column {
        TabRow(modifier = Modifier, selectedTabIndex = tabIndex, indicator = indicator) {
            tabData.forEachIndexed { index, pair ->
                Tab(
                    modifier = Modifier.padding(15.dp),
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
    onEditPlayer: (CreatePlayerState) -> Unit,
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
                itemsIndexed(
                    items = list,
                    itemContent = { index, data ->
                        SwipeToOptional(
                            index = index,
                            onItemClick = { onItemClick.invoke(data) },
                            onItemFullSwipe = { viewModel.removePlayer(data) },
                            onEdit = { onEditPlayer.invoke(data) },
                            onDelete = { viewModel.removePlayer(data) },
                            content = { PlayerItem(data) },
                        )
                    }
                )
            }
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

private fun printLog(m: String) {
    println("SwipeToOptional Page $m")
}

@Composable
private fun ProfileImage(
    imgUriPath: String? = null,
    @DrawableRes imgDefault: Int,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(if (imgUriPath.isNullOrEmpty())  imgDefault else Uri.parse(imgUriPath))
            .crossfade(true)
            .error(imgDefault)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
    )
}

// todo chane List Item to other layout component
@Composable
private fun PlayerItem(
    item: CreatePlayerState,
) {
    ListItem(
        leadingContent = {
            val imgRes = if (item.gender.equals("man", true))
                R.drawable.ic_player_man else R.drawable.ic_player_woman
            ProfileImage(imgUriPath = item.photoProfileUri, imgDefault = imgRes)
        },
        trailingContent = {
            Text(text = "Hand Play\n${item.handPlay}")
        },
        overlineContent = {
            Text(text = "Player ID: ${item.id}")
        },
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        headlineContent = {
            val label = "${item.firstName} ${item.lastName}"
            Text(label, fontWeight = FontWeight.Bold)
        },
        supportingContent = {
            Text(
                text = "Height: ${item.height} cm / Weight: ${item.weight} kg" +
                        "\nDoB: ${item.dob.toString().asDateTime("dd MMM yyyy")} Age: ${item.dob.epochToAge()}",
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
        modifier = Modifier.bottomDialogPadding()
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
private fun SortSheet(
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
    var sortAge: Sort? by remember { mutableStateOf(null) }
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
                init = SortPlayer.Id(it)
                sortId = init.asc
                sortName = null
                sortWeight = null
                sortHeight = null
                sortAge = null
            }
        )
        SortField(label = "Name", options = sortData, initialSelection = sortName?.name.orEmpty(),
            onSelected = {
                init = SortPlayer.Name(it)
                sortName = init.asc
                sortId = null
                sortWeight = null
                sortHeight = null
                sortAge = null
            }
        )
        SortField(label = "Height", options = sortData, initialSelection = sortHeight?.name.orEmpty(),
            onSelected = {
                init = SortPlayer.Height(it)
                sortHeight = init.asc
                sortId = null
                sortName = null
                sortWeight = null
                sortAge = null
            }
        )
        SortField(label = "Weight", options = sortData, initialSelection = sortWeight?.name.orEmpty(),
            onSelected = {
                init = SortPlayer.Weight(it)
                sortWeight = init.asc
                sortId = null
                sortName = null
                sortHeight = null
                sortAge = null
            }
        )
        SortField(label = "Age", options = sortData, initialSelection = sortAge?.name.orEmpty(),
            onSelected = {
                init = SortPlayer.Age(it)
                sortAge = init.asc
                sortWeight = null
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
fun SortField(
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