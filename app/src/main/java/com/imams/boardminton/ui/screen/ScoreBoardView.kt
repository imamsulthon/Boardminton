package com.imams.boardminton.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import com.imams.boardminton.R
import com.imams.boardminton.data.TeamPlayer
import com.imams.boardminton.ui.theme.*

@Composable
fun BaseScore(
    score: Int,
    onTurn: Boolean,
    lastPoint: Boolean = false,
    winner: Boolean = false,
    callback: ((Int, Boolean) -> Unit)? = null,
    modifier: Modifier = boardStyle(onTurn, score, callback),
) {
    ConstraintLayout(
        modifier = modifier
    ) {
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

private fun printLog(msg: String) {
    println("ScoreBoardView $msg")
}


fun boardStyle(onTurn: Boolean, score: Int, callback: ((Int, Boolean) -> Unit)?): Modifier {
    val bgColor = if (onTurn) Purple80 else White
    return Modifier
        .widthIn(min = 128.dp, max = 160.dp)
        .heightIn(min = 128.dp, max = 160.dp)
        .border(
            width = 4.dp,
            color = Color.Black,
            shape = RoundedCornerShape(4.dp)
        )
        .background(bgColor)
        .padding(top = 12.dp, bottom = 24.dp, start = 12.dp, end = 12.dp)
        .clickable {
            callback?.invoke(score + 1, true)
        }
}

@Composable
fun TimeCounterView(
    modifier: Modifier,
    timeString: String,
    play: () -> Unit?,
    pause: () -> Unit?,
    stop: () -> Unit?,
) {
    Text(text = timeString, fontSize = 24.sp, modifier = modifier)
}

@Composable
fun PlayerNameBoard(
    teamPlayer: TeamPlayer?,
    modifier: Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
) {
    if (teamPlayer == null) return
    Column(modifier = modifier, horizontalAlignment = alignment) {

        Text(text = teamPlayer.player1.name, modifier = Modifier.padding(top = 10.dp))

        AnimatedVisibility(visible = teamPlayer.player2 != null) {
            Text(text = teamPlayer.player2?.name ?: "", modifier = Modifier.padding(top = 10.dp))
        }
    }

}

@Preview
@Composable
fun ScoreBoardView() {
    Row {
        BaseScore(12, onTurn = false)
        Spacer(modifier = Modifier.size(2.dp))
        BaseScore(score = 13, onTurn = true)
    }
}