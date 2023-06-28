package com.imams.boardminton.ui.screen.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.PlayerViewParam
import com.imams.boardminton.domain.model.ScoreByCourt
import com.imams.boardminton.domain.model.ScoreViewParam
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.ui.screen.score.UmpireBoard
import com.imams.boardminton.ui.utils.getLabel

@Composable
fun LatestMatch(
    uiState: ScoreByCourt,
) {
    UmpireBoard(
        modifier = Modifier
            .widthIn(max = 350.dp)
            .heightIn(max = 250.dp),
        board = uiState,
        plus = {
            
        }
    )
}

@Composable
fun LatestMatchItem(
    modifier: Modifier = Modifier,
    matchId: Int,
    team1: TeamViewParam,
    team2: TeamViewParam,
    scoreA: Int,
    scoreB: Int,
    histories: List<GameViewParam>,
    single: Boolean = true,
    boardClick: ((Int) -> Unit)? = null
) {
    val teamLabel1 = team1.getLabel()
    val teamLabel2 = team2.getLabel()

    Column(modifier = modifier.drawBorder()
        .clickable { boardClick?.invoke(matchId) }
    ) {
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = "${if (single) "Single" else "Double"} Match (id: $matchId)",
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
            items(histories.size) {
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
                        text = scoreA.toString(),
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                    )
                    Text(
                        text = scoreB.toString(),
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
    LatestMatch(uiState = score)
}