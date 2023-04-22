package com.imams.boardminton.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.R
import com.imams.boardminton.ui.component.BaseScore
import com.imams.boardminton.ui.component.MainNameBoardView
import com.imams.boardminton.ui.component.PlayerNameBoard
import com.imams.boardminton.ui.component.TimeCounterView
import com.imams.boardminton.ui.viewmodel.CountTimerViewModel
import com.imams.boardminton.ui.viewmodel.ScoreBoardVM
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ScoreBoardScreen(
    counterVm: CountTimerViewModel = hiltViewModel(),
    scoreVm: ScoreBoardVM = hiltViewModel(),
) {
    val game by remember { scoreVm.game }
    val scoreA by remember { scoreVm.scoreA }
    val scoreB by remember { scoreVm.scoreB }
    val timer by counterVm.time.observeAsState()

    printLog("game A ${game.pointA} B ${game.pointB}")

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .padding(12.dp),
    ) {
        val (topView, scoreView, playerDetail) = createRefs()

        val modifierTop = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .constrainAs(topView) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
        TopView(
            modifier = modifierTop, timer = timer,
            onSwap = {},
            onReset = {}
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 12.dp)
                .constrainAs(scoreView) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(topView.bottom)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                BaseScore(
                    score = scoreA,
                    onTurn = game.onTurnA,
                    winner = game.gameEnd,
                    lastPoint = game.lastPointA,
                    callback = { _, _ ->
                        run {
                            scoreVm.plusA()
                        }
                    })

                Spacer(modifier = Modifier.size(6.dp))

                BaseScore(
                    score = scoreB,
                    onTurn = game.onTurnB,
                    lastPoint = game.lastPointB,
                    winner = game.gameEnd,
                    callback = { _, _ ->
                        run { scoreVm.plusB() }
                    })
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .constrainAs(playerDetail) {
                    top.linkTo(scoreView.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 12.dp)
            ) {
                val (left, right) = createRefs()
                PlayerNameBoard(
                    modifier = Modifier
                        .wrapContentWidth()
                        .constrainAs(left) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        },
                    teamPlayer = game.teamA,
                    alignment = Alignment.Start
                )
                PlayerNameBoard(
                    modifier = Modifier
                        .wrapContentWidth()
                        .constrainAs(right) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        },
                    teamPlayer = game.teamB,
                    alignment = Alignment.End
                )
            }

            MainNameBoardView(
                team1 = game.teamA,
                team2 = game.teamB,
                scoreA = scoreA,
                scoreB = scoreB
            )

            AnimatedVisibility(
                visible = game.gameEnd,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Game End", fontSize = 24.sp)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ButtonPointLeft(
                    onClickPlus = { scoreVm.plusA() },
                    onClickMin = { scoreVm.minA() }
                )

                OutlinedButton(
                    onClick = { scoreVm.swapServe() },
                    modifier = Modifier.widthIn(min = 40.dp, max = 60.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cock),
                        contentDescription = "swap_server"
                    )
                }

                ButtonPointRight(
                    onClickPlus = { scoreVm.plusB() },
                    onClickMin = { scoreVm.minB() }
                )
            }
        }
    }

}

@Composable
private fun TopView(
    modifier: Modifier,
    timer: String?,
    onSwap: () -> Unit,
    onReset: () -> Unit,
) {
    ConstraintLayout(modifier) {
        val (startV, endV) = createRefs()
        TimeCounterView(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
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
                .wrapContentWidth()
                .wrapContentHeight()
                .constrainAs(endV) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            OutlinedButton(
                onClick = { onSwap.invoke() },
                modifier = Modifier
                    .widthIn(min = 50.dp, max = 100.dp)
                    .wrapContentHeight()
                    .padding(horizontal = 2.dp)
            ) {
                Text(text = "<->", fontSize = 11.sp, modifier = Modifier.padding(1.dp))
            }
            OutlinedButton(
                onClick = { onReset.invoke() },
                modifier = Modifier
                    .widthIn(min = 50.dp, max = 100.dp)
                    .wrapContentHeight()
                    .padding(horizontal = 2.dp)
            ) {
                Text(text = "@", fontSize = 11.sp, modifier = Modifier.padding(1.dp))
            }
        }
    }
}

@Composable
fun ButtonPointLeft(
    onClickPlus: () -> Unit,
    onClickMin: () -> Unit,
) {
    Row {
        Button(
            onClick = { onClickPlus.invoke() },
            modifier = Modifier.widthIn(min = 60.dp, max = 120.dp)
        ) {
            Text(text = "+1")
        }

        OutlinedButton(
            onClick = { onClickMin.invoke() },
            modifier = Modifier
                .widthIn(min = 40.dp, max = 80.dp)
                .padding(horizontal = 4.dp)
                .wrapContentHeight()
        ) { Text(text = "-1") }

    }
}

@Composable
fun ButtonPointRight(
    onClickPlus: () -> Unit,
    onClickMin: () -> Unit,
) {
    Row {
        OutlinedButton(
            onClick = { onClickMin.invoke() },
            modifier = Modifier
                .widthIn(min = 40.dp, max = 80.dp)
                .wrapContentHeight()
                .padding(horizontal = 4.dp)
        ) {
            Text(text = "-1", modifier = Modifier.padding(2.dp))
        }

        Button(
            onClick = { onClickPlus.invoke() },
            modifier = Modifier.widthIn(min = 60.dp, max = 120.dp)
        ) {
            Text(text = "+1")
        }
    }
}

private fun printLog(msg: String) {
    println("ScoreBoardScreen $msg")
}

@Preview(device = Devices.NEXUS_6)
@Composable
fun ScoreBoardScreenV() {
    ScoreBoardScreen()
}