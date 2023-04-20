package com.imams.boardminton.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.ui.viewmodel.CountTimerViewModel
import com.imams.boardminton.ui.viewmodel.ScoreBoardVM
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable
fun ScoreBoardScreen(
    counterVm: CountTimerViewModel = hiltViewModel(),
    scoreVm: ScoreBoardVM = hiltViewModel(),
) {
    val playerA1 by remember { scoreVm.playerA1 }
    val playerA2 by remember { scoreVm.playerA2 }
    val scoreA by remember { scoreVm.scoreA }
    val scoreB by remember { scoreVm.scoreB }
    val turnA by remember { scoreVm.turnA }
    val turnB by remember { scoreVm.turnB }
    val lastPointA by remember { scoreVm.lastPointA }
    val lastPointB by remember { scoreVm.lastPointB }
    val gameOver by remember { scoreVm.gameOver }
    val timer by counterVm.time.observeAsState()

    printLog("gameOver $gameOver")

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .padding(12.dp),
    ) {
        val (timeLapseView, scoreView1, playerDetail) = createRefs()

        TimeCounterView(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .constrainAs(timeLapseView) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
            timeString = timer ?: "00:00:00",
            play = {},
            pause = {},
            stop = {}
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 12.dp)
                .constrainAs(scoreView1) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(timeLapseView.bottom)
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {

            OutlinedButton(
                onClick = { scoreVm.minA() },
                modifier = Modifier
                    .wrapContentWidth(unbounded = false)
                    .padding(end = 6.dp)
                    .wrapContentHeight()
            ) { Text(text = "-") }

            BaseScore(score = scoreA, onTurn = turnA, lastPoint = lastPointA, callback = { _, _ ->
                run {
                    scoreVm.plusA()
                    scoreVm.turnA()
                }
            })

            Spacer(modifier = Modifier.size(6.dp))

            BaseScore(score = scoreB, onTurn = turnB, lastPoint = lastPointB, callback = { _, _ ->
                run {
                    scoreVm.plusB()
                    scoreVm.turnB()
                }
            })

            OutlinedButton(
                onClick = {
                    scoreVm.minB()
                    printLog("buttonClick $scoreB")
                },
                modifier = Modifier
                    .wrapContentWidth(unbounded = false)
                    .wrapContentHeight()
                    .padding(start = 6.dp)
            ) {
                Text(text = "-", modifier = Modifier.padding(2.dp))
            }

        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .constrainAs(playerDetail) {
                top.linkTo(scoreView1.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }, verticalArrangement = Arrangement.Top
        )
        {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val (left, right) = createRefs()
                PlayerNameBoard(
                    modifier = Modifier
                        .wrapContentWidth()
                        .constrainAs(left) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        },
                    player1 = scoreVm.playerA1.value,
                    player2 = scoreVm.playerA2.value,
                    alignment = Alignment.Start
                )
                PlayerNameBoard(
                    modifier = Modifier
                        .wrapContentWidth()
                        .constrainAs(right) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        },
                    player1 = scoreVm.playerB1.value,
                    player2 = scoreVm.playerB2.value,
                    alignment = Alignment.End
                )
            }

            AnimatedVisibility(
                visible = gameOver,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Game Over", fontSize = 24.sp)
            }
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