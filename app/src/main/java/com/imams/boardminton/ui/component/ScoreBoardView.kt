package com.imams.boardminton.ui.component

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import com.imams.boardminton.R
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.domain.model.WinnerState
import com.imams.boardminton.ui.theme.*
import com.imams.boardminton.ui.utils.prettifyName

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BaseScore(
    modifier: Modifier = Modifier,
    score: Int,
    onTurn: Boolean,
    lastPoint: Boolean = false,
    winner: Boolean = false,
    callback: ((Int, Boolean) -> Unit)? = null,
) {
    var before by remember { mutableStateOf(score) }
    val boardColor by animateColorAsState(if (onTurn) Purple80 else White, tween(500))
    Box(
        modifier = modifier
            .scoreMod(score, callback)
            .background(boardColor)
            .padding(vertical = 24.dp, horizontal = 12.dp)
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

            AnimatedContent(
                modifier = Modifier
                    .constrainAs(tvScore) {
                        top.linkTo(ivTurn.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(6.dp),
                targetState = score,
                transitionSpec = {
                    scoreUpDownAnimation(increase = score >= before)
                        .using(SizeTransform(clip = false))
                }
            ) { targetCount ->
                Text(
                    text = targetCount.toString(), fontSize = 64.sp,
                    color = if (lastPoint) {
                        if (winner) Green else Yellow
                    } else if (onTurn) AppPrimaryColor else AppPrimaryColor,
                    textAlign = TextAlign.Center
                )
                before = targetCount
            }
        }
    }

}

private fun Modifier.scoreMod(
    score: Int,
    callback: ((Int, Boolean) -> Unit)?
): Modifier = this
    .heightIn(min = 80.dp, max = 180.dp)
    .widthIn(min = 80.dp, max = 180.dp)
    .border(
        width = 2.dp,
        color = Color.Black,
        shape = RoundedCornerShape(2.dp)
    )
    .clickable { callback?.invoke(score + 1, true) }

@Composable
fun PlayerNameBoard(
    teamPlayer: TeamViewParam,
    modifier: Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(modifier = modifier, horizontalAlignment = alignment) {
        val mod = Modifier.padding(top = 6.dp)
        PlayerName(
            modifier = mod, alignment,
            name = teamPlayer.player1.name.prettifyName(), teamPlayer.player1.onServe
        )
        AnimatedVisibility(visible = !teamPlayer.isSingle) {
            PlayerName(
                modifier = mod,
                alignment,
                name = teamPlayer.player2.name.prettifyName(),
                teamPlayer.player2.onServe,
            )
        }
    }

}

@Composable
fun ServeIc() = Icon(painterResource(id = R.drawable.ic_cock), contentDescription = "import_icon")

@Composable
fun PlayerNameWrapper(
    modifier: Modifier = Modifier,
    teamA: TeamViewParam,
    teamB: TeamViewParam,
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
                }
                .fillMaxWidth(.5f),
            teamPlayer = teamA,
            alignment = Alignment.Start
        )
        PlayerNameBoard(
            modifier = Modifier
                .wrapContentWidth()
                .constrainAs(right) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth(.5f),
            teamPlayer = teamB,
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
    state: WinnerState,
    onDone: (Boolean, WinnerState.Type) -> Unit,
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

        val title = if (state.type == WinnerState.Type.Game) "Game ${state.index} Done"
        else "Match Done"
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = "Winner by: ${state.by}", modifier = compMod, color = Green)
        Row {
            OutlinedButton(onClick = { onDone.invoke(false, state.type) }, modifier = compMod) {
                Text(text = "Cancel")
            }
            OutlinedButton(onClick = { onDone.invoke(true, state.type) }, modifier = compMod) {
                Text(text = "Finish")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDialog() {
    GameFinishDialogContent(
        WinnerState(type = WinnerState.Type.Game, index = 1, by = "Imams" , show = true, isWin = true),
        onDone = { _, _ -> {

        }}
    )
}