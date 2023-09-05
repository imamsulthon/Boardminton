package com.imams.boardminton.ui.screen.create.team

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.R
import com.imams.boardminton.ui.component.ProfileImage
import com.imams.boardminton.ui.component.RowInfoData
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.boardminton.ui.utils.horizontalGradientBackground

@Composable
fun TeamDetailScreen(
    viewModel: TeamDetailVM = hiltViewModel(),
    teamId: Int,
    onEdit: (Int) -> Unit,
    onClickPlayer: (Int) -> Unit,
    onBackPressed: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getTeam(teamId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val p1State by viewModel.player1State.collectAsState()
    val p2State by viewModel.player2State.collectAsState()
    val context = LocalContext.current

    Content(
        teamState = uiState,
        player1State = p1State,
        player2State = p2State,
        onEdit = { onEdit.invoke(teamId) },
        onRefresh = {
            viewModel.onRefreshTeamPlayer(
                callBack = { Toast.makeText(context, "Team Updated", Toast.LENGTH_LONG).show() }
            )
        },
        onClickPlayer = onClickPlayer::invoke,
        onBackPressed = onBackPressed::invoke,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    teamState: CreateTeamState,
    player1State: CreatePlayerState,
    player2State: CreatePlayerState,
    onEdit: () -> Unit,
    onRefresh: () -> Unit,
    onBackPressed: () -> Unit,
    onClickPlayer: (Int) -> Unit,
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState(0)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBarView(
                teamState = teamState,
                scrollBehavior = scrollBehavior,
                onEdit = onEdit::invoke,
                onRefresh = onRefresh::invoke,
                onBackPressed = onBackPressed::invoke
            )
        }
    ) { p ->
        Box(modifier = Modifier
            .padding(p)
            .fillMaxSize()
            .semantics { testTag = "Team Detail Screen" }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                PlayerDetail(
                    teamState = teamState,
                    player1State = player1State, player2State = player2State,
                    onClickPlayer = onClickPlayer::invoke
                )
                BottomScrollingContent(teamState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarView(
    teamState: CreateTeamState,
    scrollBehavior: TopAppBarScrollBehavior,
    onEdit: () -> Unit,
    onRefresh: () -> Unit,
    onBackPressed: () -> Unit,
) {
    MediumTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = teamState.alias, color = MaterialTheme.colorScheme.onSurface) },
        navigationIcon = {
            IconButton(onClick = onBackPressed::invoke) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            Row {
                IconButton(onClick = onEdit::invoke, enabled = false) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 2.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = onRefresh::invoke) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 2.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

        },
    )
}

@Composable
fun BottomScrollingContent(
    state: CreateTeamState,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
            .fillMaxSize()
    ) {
        SectionTeamData(state = state)
    }
}

@Composable
private fun SectionTeamData(
    modifier: Modifier = Modifier,
    state: CreateTeamState,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Team Data & Stats",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
            Column {
                RowInfoData(label = "Id", content = state.id.toString())
                RowInfoData(label = "Rank", content = state.rank.toString())
                RowInfoData(label = "Play", content = state.play.toString())
                RowInfoData(label = "Win", content = state.win.toString())
                RowInfoData(label = "Lose", content = state.lose.toString())
            }
        }
    }
}

@Composable
private fun PlayerDetail(
    modifier: Modifier = Modifier,
    teamState: CreateTeamState,
    player1State: CreatePlayerState,
    player2State: CreatePlayerState,
    onClickPlayer: (Int) -> Unit,
) {
    val gradient = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
    )
    ElevatedCard(
        modifier = modifier.padding(top = 12.dp),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .horizontalGradientBackground(gradient)
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp, bottom = 24.dp)
        ) {
            val (p1, p2, r) = createRefs()
            Row(
                modifier = Modifier
                    .clickable { onClickPlayer.invoke(player1State.id) }
                    .constrainAs(p1) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(r.start)
                        width = Dimension.fillToConstraints
                    }
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imgDefault = if (player1State.gender.equals("man", true))
                    R.drawable.ic_player_man_color else R.drawable.ic_player_woman_color
                ProfileImage(
                    imgUriPath = player1State.photoProfileUri, imgDefault = imgDefault, size = 64.dp,
                )
                Text(
                    text = player1State.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            Row(
                modifier = Modifier
                    .clickable { onClickPlayer.invoke(player2State.id) }
                    .constrainAs(p2) {
                        start.linkTo(parent.start)
                        top.linkTo(p1.bottom)
                        end.linkTo(r.start)
                        width = Dimension.fillToConstraints
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imgDefault = if (player2State.gender.equals("man", true))
                    R.drawable.ic_player_man_color else R.drawable.ic_player_woman_color
                ProfileImage(
                    imgUriPath = player2State.photoProfileUri, imgDefault = imgDefault, size = 64.dp,
                )
                Text(
                    text = player2State.fullName, maxLines = 2,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            Row(
                modifier = Modifier
                    .constrainAs(r) {
                        top.linkTo(p1.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(p2.bottom)
                    }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Rank\n${teamState.rank}", textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TeamDetailPreview() {
    val c = CreatePlayerState(
        id = 10,
        firstName = "Imam", lastName = "Sulthon",
        weight = 56, height = 168,
        gender = "man", handPlay = "Left",
    )
    val c2 = c.copy(firstName = "Ratna", lastName = "Yunita")
    Content(
        teamState = CreateTeamState(rank = 1, playerName1 = c.fullName, playerName2 = c2.fullName),
        player1State = c,
        player2State = c2,
        onEdit = { /*TODO*/ },
        onRefresh = { /*TODO*/ },
        onClickPlayer = {}, onBackPressed = {}
    )
}