package com.imams.boardminton.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.imams.boardminton.ui.prettifyName
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
        .widthIn(min = 100.dp, max = 160.dp)
        .heightIn(min = 100.dp, max = 160.dp)
        .border(
            width = 2.dp,
            color = Color.Black,
            shape = RoundedCornerShape(2.dp)
        )
        .background(bgColor)
        .padding(top = 12.dp, bottom = 24.dp, start = 12.dp, end = 12.dp)
        .clickable {
            callback?.invoke(score + 1, true)
        }
}

@Composable
fun PlayerNameBoard(
    teamPlayer: TeamPlayer?,
    modifier: Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
) {
    if (teamPlayer == null) return
    Column(modifier = modifier, horizontalAlignment = alignment) {
        val mod = Modifier.padding(top = 6.dp)
        PlayerName(modifier = mod, alignment,
            name = teamPlayer.player1.name.prettifyName(), teamPlayer.player1.onTurn)
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

@Preview
@Composable
fun ScoreBoardView() {
    Row {
        BaseScore(12, onTurn = false)
        Spacer(modifier = Modifier.size(2.dp))
        BaseScore(score = 13, onTurn = true)
    }
}