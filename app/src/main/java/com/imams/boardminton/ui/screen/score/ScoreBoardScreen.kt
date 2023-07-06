package com.imams.boardminton.ui.screen.score

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import com.imams.boardminton.R
import com.imams.boardminton.data.toJson
import com.imams.boardminton.domain.mapper.isSingle
import com.imams.boardminton.domain.mapper.none
import com.imams.boardminton.domain.model.Court
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.domain.model.MatchUIState
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.ui.component.ButtonPointLeft
import com.imams.boardminton.ui.component.ButtonPointRight
import com.imams.boardminton.ui.component.GameFinishDialogContent
import com.imams.boardminton.ui.component.MainNameBoardView
import com.imams.boardminton.ui.component.MyCourtMatch
import com.imams.boardminton.ui.component.TimeCounterView
import com.imams.boardminton.ui.screen.timer.TimeCounterUiState

@Composable
fun ScoreBoardScreen(
    id: Int? = null,
    single: Boolean,
    players: String,
    onEdit: (Boolean, String) -> Unit,
    scoreVm: ScoreBoardVM = hiltViewModel(),
    savedStateHandle: SavedStateHandle?,
    onBackPressed: () -> Unit,
) {

    BackHandler(true) {
        printLog("onBackHandler")
        scoreVm.updateGame(onBackPressed)
    }

    LaunchedEffect(Unit) {
        scoreVm.setupPlayer(id, players, single)
    }

    val uiState by scoreVm.matchUIState.collectAsState()
    val timerUiState by scoreVm.tcUiState.collectAsState()
    val winnerState by scoreVm.winnerState.collectAsState()

    if (savedStateHandle != null) {
        val editResult by savedStateHandle.getLiveData<String>("players").observeAsState()
        LaunchedEffect(editResult) {
            editResult?.let {
                scoreVm.updatePlayers(it, single)
                savedStateHandle.remove<String>("players")
            }
        }
    }

    if (winnerState.show) {
        Dialog(
            onDismissRequest = { scoreVm.onGameEndDialog(false, winnerState.type) },
            content = {
                GameFinishDialogContent(winnerState, onDone = scoreVm::onGameEndDialog)
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )
    }

    ScoreBoardScreen(
        uiState = uiState,
        timerUiState = timerUiState,
        scoreVm = scoreVm,
        toEditPlayers = { isSingle, a, b ->
            onEdit.invoke(isSingle, listOf(a.player1.name, a.player2.name, b.player1.name, b.player2.name).toJson())
        }
    )

}

@Composable
private fun ScoreBoardScreen(
    uiState: MatchUIState,
    timerUiState: TimeCounterUiState,
    scoreVm: ScoreBoardVM,
    toEditPlayers: (Boolean, TeamViewParam, TeamViewParam) -> Unit,
) {
    @Composable
    fun mainBoard() = MainNameBoardView(
        modifier = Modifier
            .widthIn(max = 400.dp, min = 250.dp)
            .fillMaxWidth(),
        team1 = uiState.match.teamA,
        team2 = uiState.match.teamB,
        scoreA = uiState.match.currentGame.scoreA.point,
        scoreB = uiState.match.currentGame.scoreB.point,
        histories = uiState.match.games,
        single = uiState.match.matchType.isSingle(),
    )

    @Composable
    fun scoreBoard() = UmpireBoard(
        modifier = Modifier
            .widthIn(max = 450.dp)
            .heightIn(max = 350.dp),
        board = uiState.scoreByCourt,
        plus = {
            when (it) {
                ISide.A -> scoreVm.pointTo(Court.Left)
                ISide.B -> scoreVm.pointTo(Court.Right)
            }
        }
    )

    @Composable
    fun courtView() = MyCourtMatch(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        court = uiState.scoreByCourt,
        type = uiState.match.matchType,
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
    ) {
        val (topRef, contentRef, bottomRef) = createRefs()

        TopView(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(topRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
            timer = timerUiState.counter,
            onSwap = { scoreVm.swapCourt() },
            onEdit = {
                toEditPlayers.invoke(
                    uiState.match.matchType.isSingle(),
                    uiState.match.teamA,
                    uiState.match.teamB,
                )
            },
            onReset = { scoreVm.resetGame() }
        )

        ContentView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .constrainAs(contentRef) {
                    top.linkTo(topRef.bottom)
                    bottom.linkTo(bottomRef.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                },
            board = { scoreBoard() },
            main = { mainBoard() },
            courtView = { courtView() }
        )

        BottomView(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(bottomRef) {
                    top.linkTo(contentRef.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
            aPlus = { scoreVm.pointTo(Court.Left) },
            aMin = { scoreVm.minusPoint(Court.Left) },
            bPlus = { scoreVm.pointTo(Court.Right) },
            bMin = { scoreVm.minusPoint(Court.Right) },
            swap = { scoreVm.swapServe() },
            enabled = uiState.match.currentGame.winner.none()
        )
    }
}

@Composable
private fun TopView(
    modifier: Modifier,
    timer: String?,
    onSwap: () -> Unit,
    onEdit: () -> Unit,
    onReset: () -> Unit,
) {
    ConstraintLayout(modifier) {
        val (startV, endV) = createRefs()
        TimeCounterView(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(startV) {
                    start.linkTo(parent.start)
                    top.linkTo(endV.top)
                    bottom.linkTo(endV.bottom)
                },
            timeString = timer ?: "00:00:00",
            play = {},
            pause = {},
            stop = {}
        )

        Row(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(endV) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            OutlinedButton(
                onClick = { onSwap.invoke() },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 2.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp),
                    painter = painterResource(id = R.drawable.ic_swap_3),
                    contentDescription = "swap_icon"
                )
            }
            OutlinedButton(
                onClick = { onReset.invoke() },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 2.dp)
            ) {
                Icon(
                    Icons.Outlined.Refresh,
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp),
                    contentDescription = "reset_icon"
                )
            }
            OutlinedButton(
                onClick = { onEdit.invoke() },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 2.dp)
            ) {
                Icon(
                    Icons.Outlined.Edit,
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp),
                    contentDescription = "edit_icon"
                )
            }
        }
    }
}

@Composable
private fun ContentView(
    modifier: Modifier = Modifier,
    board: @Composable () -> Unit,
    main: @Composable () -> Unit,
    courtView: @Composable () -> Unit
) {
    val config = LocalConfiguration.current
    when (config.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PortraitContent(
                modifier = modifier,
                mainBoard = { main() },
                scoreBoard = { board() },
                courtView = { courtView() },
            )
        }
        else -> {
            LandscapeContent(
                modifier = modifier,
                mainBoard = { main() },
                scoreBoard = { board() },
                courtView = { courtView() },
            )
        }
    }

}

@Composable
private fun LandscapeContent(
    modifier: Modifier,
    mainBoard: @Composable () -> Unit,
    scoreBoard: @Composable () -> Unit,
    courtView: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        scoreBoard()
        Divider(
            color = MaterialTheme.colorScheme.onBackground, modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 20.dp)
                .width(2.dp)
        )
        Column(
            verticalArrangement = Arrangement.Top,
        ) {
            mainBoard()
            LineDivider(padding = 5.dp, thick = 1.dp)
            courtView()
        }
    }
}

@Composable
private fun PortraitContent(
    modifier: Modifier,
    mainBoard: @Composable () -> Unit,
    scoreBoard: @Composable () -> Unit,
    courtView: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        mainBoard()
        LineDivider()
        scoreBoard()
        LineDivider(thick = 1.dp)
        courtView()
    }
}

@Composable
private fun BottomView(
    aPlus: () -> Unit,
    aMin: () -> Unit,
    bPlus: () -> Unit,
    bMin: () -> Unit,
    swap: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ButtonPointLeft(
            onClickPlus = { aPlus.invoke() },
            onClickMin = { aMin.invoke() },
            enabled = enabled
        )

        OutlinedButton(
            onClick = { swap.invoke() },
            modifier = Modifier.widthIn(min = 40.dp, max = 60.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cock),
                contentDescription = "swap_server"
            )
        }

        ButtonPointRight(
            onClickPlus = { bPlus.invoke() },
            onClickMin = { bMin.invoke() },
            enabled = enabled,
        )
    }
}

@Composable
private fun LineDivider(padding: Dp = 20.dp, thick: Dp = 2.dp) = Divider(
    modifier = Modifier.padding(vertical = padding),
    color = MaterialTheme.colorScheme.onBackground,
    thickness = thick
)

private fun printLog(msg: String) {
    println("ScoreBoardScreen $msg")
}

@Preview(device = Devices.NEXUS_6)
@Composable
private fun ScoreBoardScreenV() {
    ScoreBoardScreen(null, false, "listOf()", onEdit = {_, _, ->}, savedStateHandle = null, onBackPressed = {})
}