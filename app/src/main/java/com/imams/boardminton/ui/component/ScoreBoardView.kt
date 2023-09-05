package com.imams.boardminton.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
    var before by remember { mutableIntStateOf(score) }
    val boardColor by animateColorAsState(
        if (onTurn) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        tween(500), label = ""
    )
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
                }, label = ""
            ) { targetCount ->
                Text(
                    text = targetCount.toString(), fontSize = 64.sp,
                    color = if (lastPoint) {
                        if (winner) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    } else if (onTurn) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    },
                    textAlign = TextAlign.Center
                )
                before = targetCount
            }
        }
    }

}

@Composable
private fun Modifier.scoreMod(
    score: Int,
    callback: ((Int, Boolean) -> Unit)?
): Modifier = this
    .heightIn(min = 80.dp, max = 180.dp)
    .widthIn(min = 80.dp, max = 180.dp)
    .border(
        width = 2.dp,
        color = MaterialTheme.colorScheme.onBackground,
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
        PlayerName(
            modifier = mod,
            alignment,
            name = teamPlayer.player2.name.prettifyName(),
            teamPlayer.player2.onServe,
        )
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
    Text(text = name, modifier = Modifier.padding(horizontal = 6.dp),
        color = MaterialTheme.colorScheme.onSurface)
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
        ) { Text(text = "+1") }
        OutlinedButton(
            onClick = { onClickMin.invoke() },
            modifier = Modifier
                .widthIn(min = 40.dp, max = 70.dp)
                .padding(start = 3.dp)
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
                .widthIn(min = 40.dp, max = 70.dp)
                .wrapContentHeight()
                .padding(end = 3.dp)
        ) { Text(text = "-1", modifier = Modifier.padding(2.dp)) }
        Button(
            enabled = enabled,
            onClick = { onClickPlus.invoke() },
            modifier = Modifier.widthIn(min = 60.dp, max = 120.dp)
        ) {
            Text(text = "+1")
        }
    }
}

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
        Text(text = "Winner by: ${state.by}", modifier = compMod, color = MaterialTheme.colorScheme.primary)
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

@Composable
fun MiniIconButton(
    @DrawableRes icon: Int,
    size: Dp = 32.dp,
    padding: Dp = 2.dp,
    iconSize: Dp = 16.dp,
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick.invoke() },
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .8f))
            .padding(padding)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "swap_server",
            modifier = Modifier.size(iconSize),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun PreviewDialog() {
    GameFinishDialogContent(
        WinnerState(type = WinnerState.Type.Game, index = 1, by = "Imams" , show = true, isWin = true),
        onDone = { _, _ -> }
    )
}