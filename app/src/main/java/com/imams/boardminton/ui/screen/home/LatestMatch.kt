package com.imams.boardminton.ui.screen.home

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.domain.model.PlayerViewParam
import com.imams.boardminton.domain.model.ScoreByCourt
import com.imams.boardminton.domain.model.ScoreViewParam
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.ui.screen.score.UmpireBoard

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

) {

}

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