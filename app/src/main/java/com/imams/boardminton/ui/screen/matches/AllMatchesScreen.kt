package com.imams.boardminton.ui.screen.matches

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.imams.boardminton.ui.component.FancyIndicator
import com.imams.boardminton.ui.screen.player.Sort
import com.imams.boardminton.ui.utils.getLabel
import kotlinx.coroutines.launch

@Composable
fun AllMatchesScreen(
    viewModel: AllMatchesVM = hiltViewModel(),
    onSelect: ((String, Int) -> Unit)? = null,
) {

    val allMatches by viewModel.allMatches.collectAsState()
    val onGoingMatches by viewModel.onGoingMatches.collectAsState()
    val finishMatches by viewModel.finishMatches.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchData()
    }

    ContentWrapper(
        onGoingMatches = {
            MatchListContent(
                list = onGoingMatches,
                onRemove = viewModel::remove,
                onItemClick = {
                    onSelect?.invoke(it.matchType.name.lowercase(), it.id)
                },
                onFilter = { viewModel.filter(FilterOn.OnGoing(it)) },
                onSort = { viewModel.sortOn(SortOn.OnGoing(it)) },
            )
        },
        finishedMatches = {
            MatchListContent(
                list = finishMatches,
                onRemove = viewModel::remove,
                onItemClick = {
                    onSelect?.invoke(it.matchType.name.lowercase(), it.id)
                },
                onFilter = { viewModel.filter(FilterOn.Finished(it)) },
                onSort = { viewModel.sortOn(SortOn.Finished(it)) },
            )
        },
        allMatches = {
            MatchListContent(
                list = allMatches,
                onRemove = viewModel::remove,
                onItemClick = {
                    onSelect?.invoke(it.matchType.name.lowercase(), it.id)
                },
                onFilter = { viewModel.filter(FilterOn.AllMatch(it)) },
                onSort = { viewModel.sortOn(SortOn.AllMatch(it)) },
            )
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ContentWrapper(
    onGoingMatches: @Composable () -> Unit,
    finishedMatches: @Composable () -> Unit,
    allMatches: @Composable () -> Unit,
) {
    val tabData = listOf(
        "On Going" to Icons.Filled.Home,
        "Finished" to Icons.Filled.AccountBox,
        "All" to Icons.Filled.AccountBox,
    )
    val pagerState = rememberPagerState(pageCount = { tabData.size })
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        FancyIndicator(MaterialTheme.colorScheme.primary, Modifier.tabIndicatorOffset(tabPositions[tabIndex]))
    }
    Column {
        TabRow(modifier = Modifier.padding(10.dp),
            selectedTabIndex = tabIndex, indicator = indicator
        ) {
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
        ) { index ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (index) {
                    0 -> onGoingMatches()
                    1 -> finishedMatches()
                    2 -> allMatches()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MatchListContent(
    list: List<MatchViewParam>,
    onRemove: (MatchViewParam) -> Unit,
    onItemClick: ((MatchViewParam) -> Unit)? = null,
    onFilter: (FilterMatch) -> Unit,
    onSort: (SortMatch) -> Unit,
) {
    var openFilterDialog by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded)
    var openSortDialog by rememberSaveable { mutableStateOf(false) }
    val skipPartially2 by remember { mutableStateOf(false) }
    val btmSheetState2 = rememberModalBottomSheetState(skipPartially2)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                onFilter = { openFilterDialog = true }, onSort = { openSortDialog = true },
                enableFilter = list.isNotEmpty(), enableSort = list.isNotEmpty()
            )
        }
    ) { p ->
        if (list.isEmpty()) {
            EmptyContent(message = "No Matches")
        } else {
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
            if (openSortDialog) {
                ModalBottomSheet(
                    onDismissRequest = { openSortDialog = false },
                    sheetState = btmSheetState2,
                ) {
                    SortSheet(
                        filter = SortMatch.Id( Sort.Ascending ),
                        onApply = {
                            openSortDialog = false
                            onSort.invoke(it)
                        },
                        onCancel = { openSortDialog = false })
                }
            }
            if (openFilterDialog) {
                ModalBottomSheet(
                    onDismissRequest = { openFilterDialog = false },
                    sheetState = bottomSheetState) {
                    FilterSheet(
                        filter = FilterMatch(null),
                        onApply = {
                            openFilterDialog = false
                            onFilter.invoke(it)
                        },
                        onCancel = { openFilterDialog = false }
                    )
                }
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
            Text(
                text = "${item.lastUpdate.prettifyDate()} in ${item.matchDurations}'s",
                fontSize = 10.sp
            )
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
                GameComponent(
                    index = if (games.isEmpty()) 1 else games.size + 1,
                    scoreA = scoreA,
                    scoreB = scoreB
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSheet(
    filter: FilterMatch,
    onApply: (FilterMatch) -> Unit,
    onCancel: () -> Unit,
) {
    val optionals = mutableListOf("All", "Single", "Double")
    var selected : String? by remember { mutableStateOf(filter.type) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Filter by:")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Match Type:")
            Row(Modifier.selectableGroup()) {
                optionals.forEach { text ->
                    InputChip(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        selected = text.equals(selected, true),
                        onClick = { selected = text },
                        label = {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    )
                }
            }
        }
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
                onClick = { onApply.invoke(FilterMatch(selected)) }
            ) { Text(text = "Apply") }
        }
    }
}

@Composable
private fun SortSheet(
    filter: SortMatch,
    onApply: (SortMatch) -> Unit,
    onCancel: () -> Unit,
) {
    val sortData = listOf(Sort.Ascending, Sort.Descending)
    var init by remember { mutableStateOf(filter) }
    var sortId: Sort? by remember { mutableStateOf(null) }
    var sortLastUpdate: Sort? by remember { mutableStateOf(null) }
    var sortDuration: Sort? by remember { mutableStateOf(null) }
    var sortGameCount: Sort? by remember { mutableStateOf(null) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Sort by:", fontWeight = FontWeight.SemiBold)
        SortField(label = "ID",
            options = sortData,
            initialSelection = sortId?.name.orEmpty(),
            onSelected = {
                init = SortMatch.Id(it)
                sortId = init.asc
                sortLastUpdate = null
                sortGameCount = null
                sortDuration = null
            }
        )
        SortField(label = "Last Update",
            options = sortData,
            initialSelection = sortLastUpdate?.name.orEmpty(),
            onSelected = {
                init = SortMatch.LastUpdate(it)
                sortLastUpdate = init.asc
                sortId = null
                sortGameCount = null
                sortDuration = null
            }
        )
        SortField(label = "Duration",
            options = sortData,
            initialSelection = sortDuration?.name.orEmpty(),
            onSelected = {
                init = SortMatch.Duration(it)
                sortDuration = init.asc
                sortId = null
                sortLastUpdate = null
                sortGameCount = null
            }
        )
        SortField(label = "Game Count",
            options = sortData,
            initialSelection = sortGameCount?.name.orEmpty(),
            onSelected = {
                init = SortMatch.GameCount(it)
                sortGameCount = init.asc
                sortId = null
                sortLastUpdate = null
                sortDuration = null
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AllMatchesScreenPrev() {
    ItemContent(
        type = "Single", teamA = "Imam Sulthon", teamB = "Iqbal Kamal",
        games = mutableListOf(
            GameViewParam(1, ScoreViewParam(12), ScoreViewParam(21)),
            GameViewParam(2, ScoreViewParam(21), ScoreViewParam(19)),
        ),
        scoreB = 10, scoreA = 21
    )
}