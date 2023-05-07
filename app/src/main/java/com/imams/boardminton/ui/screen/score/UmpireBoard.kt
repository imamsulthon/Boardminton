package com.imams.boardminton.ui.screen.score

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.data.GameScore
import com.imams.boardminton.data.ISide
import com.imams.boardminton.data.doublePlayer
import com.imams.boardminton.ui.component.BaseScore
import com.imams.boardminton.ui.component.PlayerNameWrapper
import com.imams.boardminton.ui.theme.Green

@Composable
fun UmpireBoard(
    modifier: Modifier,
    index: Int,
    scoreA: Int,
    scoreB: Int,
    game: GameScore,
    plus: (ISide) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (s1, g, s2, div, name) = createRefs()
        BaseScore(
            modifier = Modifier
                .wrapContentHeight()
                .constrainAs(s1) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(div.top)
                    width = Dimension.wrapContent
                },
            score = scoreA,
            onTurn = game.onTurnA,
            winner = game.gameEnd,
            lastPoint = game.lastPointA,
            callback = { _, _ -> plus.invoke(ISide.A) })

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 10.dp)
                .constrainAs(g) {
                    top.linkTo(s1.top)
                    start.linkTo(s1.end)
                    end.linkTo(s2.start)
                    bottom.linkTo(s1.bottom)
                    width = Dimension.preferredWrapContent
                },
            verticalArrangement = Arrangement.Center,
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
            modifier = Modifier
                .wrapContentHeight()
                .constrainAs(s2) {
                    top.linkTo(s1.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(s1.bottom)
                    width = Dimension.wrapContent
                },
            score = scoreB,
            onTurn = game.onTurnB,
            lastPoint = game.lastPointB,
            winner = game.gameEnd,
            callback = { _, _ -> plus.invoke(ISide.B) })

        Divider(
            modifier = Modifier.padding(top = 6.dp)
                .constrainAs(div) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(name.top)
                    top.linkTo(s1.bottom)
                    height = Dimension.preferredWrapContent
                },
            color = Color.Black,
            thickness = 1.dp
        )

        PlayerNameWrapper(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 6.dp)
                .constrainAs(name) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(div.bottom)
                    height = Dimension.preferredWrapContent
                },
            game = game
        )
    }
}

@Preview(showSystemUi = true)
@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 512, showSystemUi = true)
@Composable
private fun UmpireViewPrev() {
    val tA = doublePlayer(Athlete.Viktor, Athlete.Anthony)
    val tb = doublePlayer(Athlete.Kim_Astrup, Athlete.Anders_Skaarup)
    val game = GameScore().apply {
        teamA = tA
        teamB = tb
    }
    UmpireBoard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        index = 1,
        scoreA = 18, scoreB = 12,
        game = game, plus = {}
    )
}