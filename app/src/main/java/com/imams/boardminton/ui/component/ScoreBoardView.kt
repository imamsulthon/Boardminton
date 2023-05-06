package com.imams.boardminton.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import com.imams.boardminton.R
import com.imams.boardminton.data.GameScore
import com.imams.boardminton.data.ISide
import com.imams.boardminton.data.TeamPlayer
import com.imams.boardminton.ui.prettifyName
import com.imams.boardminton.ui.theme.*

@Composable
fun BaseScoreWrapper(
    index: Int = 1,
    scoreA: Int,
    scoreB: Int,
    game: GameScore,
    plus: (ISide) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        BaseScore(
            score = scoreA,
            onTurn = game.onTurnA,
            winner = game.gameEnd,
            lastPoint = game.lastPointA,
            callback = { _, _ -> plus.invoke(ISide.A) })

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Game", color = Green, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text(
                text = index.toString(),
                color = Green,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
        }

        BaseScore(
            score = scoreB,
            onTurn = game.onTurnB,
            lastPoint = game.lastPointB,
            winner = game.gameEnd,
            callback = { _, _ -> plus.invoke(ISide.B) })
    }
}

@Composable
fun BaseScore(
    modifier: Modifier = Modifier,
    score: Int,
    onTurn: Boolean,
    lastPoint: Boolean = false,
    winner: Boolean = false,
    callback: ((Int, Boolean) -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .scoreMod(onTurn, score, callback)
            .aspectRatio(0.9f)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (tvScore, ivTurn) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.ic_cock),
                contentDescription = "content_turn",
                modifier = Modifier
                    .width(18.dp)
                    .height(18.dp)
                    .constrainAs(ivTurn) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        visibility = if (onTurn) Visibility.Visible else Visibility.Invisible
                    },
            )

            Text(
                text = score.toString(), fontSize = 64.sp,
                color = if (lastPoint) {
                    if (winner) Green else Yellow
                } else if (onTurn) AppPrimaryColor else AppPrimaryColor,
                modifier = Modifier
                    .constrainAs(tvScore) {
                        top.linkTo(ivTurn.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(6.dp)
            )
        }
    }

}

private fun Modifier.scoreMod(
    onTurn: Boolean, score: Int,
    callback: ((Int, Boolean) -> Unit)?
): Modifier = this
    .heightIn(min = 80.dp, max = 180.dp)
    .widthIn(min = 80.dp, max = 180.dp)
    .border(
        width = 2.dp,
        color = Color.Black,
        shape = RoundedCornerShape(2.dp)
    )
    .background(if (onTurn) Purple80 else White)
    .padding(top = 12.dp, bottom = 24.dp, start = 12.dp, end = 12.dp)
    .clickable { callback?.invoke(score + 1, true) }

@Composable
fun PlayerNameBoard(
    teamPlayer: TeamPlayer?,
    modifier: Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
) {
    if (teamPlayer == null) return
    Column(modifier = modifier, horizontalAlignment = alignment) {
        val mod = Modifier.padding(top = 6.dp)
        PlayerName(
            modifier = mod, alignment,
            name = teamPlayer.player1.name.prettifyName(), teamPlayer.player1.onTurn
        )
        AnimatedVisibility(visible = teamPlayer.player2 != null) {
            PlayerName(
                modifier = mod,
                alignment,
                name = teamPlayer.player2?.name?.prettifyName() ?: "",
                teamPlayer.player2?.onTurn ?: false,
            )
        }
    }

}

@Composable
fun ServeIc() = Icon(painterResource(id = R.drawable.ic_cock), contentDescription = "import_icon")

@Composable
fun PlayerNameWrapper(
    modifier: Modifier = Modifier,
    game: GameScore,
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (left, right) = createRefs()
        PlayerNameBoard(
            modifier = Modifier
                .wrapContentWidth()
                .constrainAs(left) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
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
                    bottom.linkTo(parent.bottom)
                },
            teamPlayer = game.teamB,
            alignment = Alignment.End
        )
    }
}

@Composable
fun PlayerName(
    modifier: Modifier,
    alignment: Alignment.Horizontal,
    name: String,
    onServe: Boolean,
) = Row(
    modifier = modifier.wrapContentSize(),
    horizontalArrangement = Arrangement.Start,
    verticalAlignment = Alignment.CenterVertically,
) {
    if (alignment == Alignment.End && onServe) ServeIc()
    Text(text = name, modifier = Modifier.padding(horizontal = 6.dp))
    if (alignment == Alignment.Start && onServe) ServeIc()
}


@Composable
fun ButtonPointLeft(
    onClickPlus: () -> Unit,
    onClickMin: () -> Unit,
    enabled: Boolean,
) {
    Row {
        Button(
            enabled = enabled,
            onClick = { onClickPlus.invoke() },
            modifier = Modifier.widthIn(min = 60.dp, max = 120.dp),
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
    enabled: Boolean,
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
            enabled = enabled,
            onClick = { onClickPlus.invoke() },
            modifier = Modifier.widthIn(min = 60.dp, max = 120.dp)
        ) {
            Text(text = "+1")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameFinishDialogContent(
    gameIndex: Int,
    winner: String,
    onDone: (Boolean) -> Unit,
) = Card(
    modifier = Modifier.wrapContentSize(),
    shape = RoundedCornerShape(8.dp)
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val compMod = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
        Text(text = "Game $gameIndex Done", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = "$winner Win", modifier = compMod, color = Green)
        Row {
            OutlinedButton(onClick = { onDone.invoke(false) }, modifier = compMod) {
                Text(text = "Cancel")
            }
            OutlinedButton(onClick = { onDone.invoke(true) }, modifier = compMod) {
                Text(text = "Finish")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ScoreBoardView() {
    Column(modifier = Modifier.padding(5.dp)) {
        BaseScoreWrapper(scoreA = 12, scoreB = 13, game = GameScore(), plus = {})
        PlayerNameWrapper(game = GameScore())
    }
}

@Preview
@Composable
private fun PreviewDialog() {
    GameFinishDialogContent(gameIndex = 1, winner = "Player 2") {}
}