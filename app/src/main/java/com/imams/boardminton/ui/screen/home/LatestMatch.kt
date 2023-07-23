package com.imams.boardminton.ui.screen.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.domain.mapper.isSingle
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.boardminton.domain.model.PlayerViewParam
import com.imams.boardminton.domain.model.ScoreByCourt
import com.imams.boardminton.domain.model.ScoreViewParam
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.ui.utils.getLabel

@Composable
fun LatestMatchItem(
    modifier: Modifier = Modifier,
    data: MatchViewParam,
    boardClick: ((Int) -> Unit)? = null
) {
    val teamLabel1 = data.teamA.getLabel()
    val teamLabel2 = data.teamB.getLabel()
    val histories = data.games

    Column(modifier = modifier.drawBorder()
        .clickable { boardClick?.invoke(data.id) }
    ) {
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = "${if (data.matchType.isSingle()) "Single" else "Double"} Match (id: ${data.id})",
            fontWeight = FontWeight.ExtraBold
        )
        LazyRow {
            item {
                Column {
                    Text(text = teamLabel1, modifier = Modifier.padding(top = 5.dp))
                    Text(text = teamLabel2, modifier = Modifier.padding(top = 5.dp))
                }
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            }
            items(data.games.size) {
                Column {
                    Text(
                        text = histories[it].scoreA.point.toString(),
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                    )
                    Text(
                        text = histories[it].scoreB.point.toString(),
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                    )
                }
            }
            if (histories.size >= 3) return@LazyRow
            item {
                Column {
                    Text(
                        text = data.currentGame.scoreA.point.toString(),
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                    )
                    Text(
                        text = data.currentGame.scoreB.point.toString(),
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                    )
                }
            }
        }
    }
}

private fun Modifier.drawBorder() = this
    .border(
        width = 1.dp,
        color = Color.Black,
        shape = RoundedCornerShape(1.dp)
    )
    .padding(vertical = 10.dp, horizontal = 10.dp)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LatestMatchPrev() {
    val score = ScoreByCourt(
        index = 1,
        left = ScoreViewParam(15, false, false, false),
        right = ScoreViewParam(15, false, false, false),
        teamLeft = TeamViewParam(
            player1 = PlayerViewParam(Athlete.Imam_Sulthon, false), 
            PlayerViewParam(Athlete.Taufik_Hidayat), false),
        teamRight = TeamViewParam(
            player1 = PlayerViewParam(Athlete.Kim_Astrup, false), 
            PlayerViewParam(Athlete.Viktor), true),
    )
}