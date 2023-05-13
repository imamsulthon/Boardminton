package com.imams.boardminton.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.data.GameHistory
import com.imams.boardminton.data.TeamPlayer
import com.imams.boardminton.data.doublePlayer
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.ui.getLabel

@Composable
fun MainNameBoardView(
    modifier: Modifier = Modifier,
    team1: TeamViewParam,
    team2: TeamViewParam,
    scoreA: Int,
    scoreB: Int,
    histories: List<GameViewParam>,
    single: Boolean = true,
) {
    val teamLabel1 = team1.getLabel()
    val teamLabel2 = team2.getLabel()

    Column(modifier = modifier.drawBorder()) {
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = "${if (single) "Single" else "Double"} Match",
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

@ExperimentalMaterial3Api
@Composable
fun CourtView(
    team1: TeamPlayer,
    team2: TeamPlayer,
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
    ) {
        item {
            Text("Title")
        }
        item(
            span = { GridItemSpan(2) }
        ) {
            Text("Title 2")
        }
        items(2) {
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

@Preview
@Composable
fun CourtViewP() {
//    MainNameBoardView(
//        team1 = doublePlayer(Athlete.Imam_Sulthon, Athlete.Taufik_Hidayat),
//        team2 = doublePlayer(Athlete.Viktor, Athlete.Anthony),
//        scoreA = 1, scoreB =  14,
//        histories = listOf(
//            GameHistory(1, 23, 21),
//            GameHistory(2, 19, 21),
//        )
//    )

}