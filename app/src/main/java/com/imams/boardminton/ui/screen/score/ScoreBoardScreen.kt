package com.imams.boardminton.ui.screen.score

import android.content.res.Configuration
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.R
import com.imams.boardminton.domain.mapper.isSingle
import com.imams.boardminton.domain.model.Court
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.ui.component.ButtonPointLeft
import com.imams.boardminton.ui.component.ButtonPointRight
import com.imams.boardminton.ui.component.MainNameBoardView
import com.imams.boardminton.ui.component.TimeCounterView
import com.imams.boardminton.ui.screen.destinations.EditPlayersScreenDestination
import com.imams.boardminton.ui.screen.timer.CounterTimerVM
import com.imams.boardminton.ui.screen.toEditPlayers
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@Destination
@Composable
fun ScoreBoardScreen(
    players: String,
    single: Boolean,
    counterVm: CounterTimerVM = hiltViewModel(),
    scoreVm: ScoreBoardVM = hiltViewModel(),
    navigator: DestinationsNavigator?,
    resultRecipient: ResultRecipient<EditPlayersScreenDestination, String>?,
) {

    // todo should use Side Effect?
    scoreVm.setupPlayer(players, single)

    val uiState by scoreVm.matchUIState.collectAsState()
    val timeCounterUiState by counterVm.tcUiState.collectAsState()
    val finishMatch by remember { mutableStateOf(false) }

    resultRecipient?.onNavResult { result ->
        if (result is NavResult.Value) scoreVm.updatePlayers(result.value, single)
    }

//    if (anyWinner.show) {
//        Dialog(
//            onDismissRequest = { scoreVm.setGameEnd(false) },
//            content = {
//                GameFinishDialogContent(anyWinner.index, anyWinner.by,
//                    onDone = {
//                        if (it) scoreVm.onNewGame() else scoreVm.setGameEnd(false)
//                    }
//                )
//            },
//            properties = DialogProperties(
//                dismissOnBackPress = false,
//                dismissOnClickOutside = false
//            )
//        )
//    }

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

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
            timer = timeCounterUiState.counter,
            onSwap = { scoreVm.swapCourt() },
            onEdit = {
                navigator?.toEditPlayers(
                    single,
                    team1 = uiState.match.teamA,
                    team2 = uiState.match.teamB
                )
            },
            onReset = { scoreVm.reset() }
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
            main = { mainBoard() }
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
            enabled = !finishMatch
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
) {
    val config = LocalConfiguration.current
    when (config.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PortraitContent(
                modifier = modifier,
                mainBoard = { main() },
                scoreBoard = { board() }
            )
        }
        else -> {
            LandscapeContent(
                modifier = modifier,
                mainBoard = { main() },
                scoreBoard = { board() }
            )
        }
    }

}

@Composable
private fun LandscapeContent(
    modifier: Modifier,
    mainBoard: @Composable () -> Unit,
    scoreBoard: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        scoreBoard()
        Divider(
            color = Color.Black, modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 20.dp)
                .width(2.dp)
        )
        mainBoard()
    }
}

@Composable
private fun PortraitContent(
    modifier: Modifier,
    mainBoard: @Composable () -> Unit,
    scoreBoard: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        mainBoard()
        LineDivider()
        scoreBoard()
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
private fun LineDivider(padding: Dp = 20.dp) = Divider(
    modifier = Modifier.padding(vertical = padding),
    color = Color.Black,
    thickness = 2.dp
)

private fun printLog(msg: String) {
    println("ScoreBoardScreen $msg")
}

@Preview(device = Devices.NEXUS_6)
@Composable
private fun ScoreBoardScreenV() {
    ScoreBoardScreen("listOf()", false, navigator = null, resultRecipient = null)
}