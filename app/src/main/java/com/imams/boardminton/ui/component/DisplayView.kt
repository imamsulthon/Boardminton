package com.imams.boardminton.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imams.boardminton.data.TeamPlayer
import com.imams.boardminton.data.doublePlayer
import com.imams.boardminton.ui.concatLastName

@Composable
fun MainNameBoardView(
    modifier: Modifier = Modifier,
    team1: TeamPlayer? = doublePlayer("Test A1 NameA1", "Test A2 NameA2"),
    team2: TeamPlayer? = doublePlayer("Player B1", "Player B2"),
    scoreA: Int,
    scoreB: Int,
) {
    val teamLabel1 =
        if (team1?.isSingle() == true) team1.player1.name ?: ""
        else team1?.concatLastName() ?: ""
    val teamLabel2 =
        if (team2?.isSingle() == true) team2.player1.name ?: ""
        else team2?.concatLastName() ?: ""
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(1.dp)
            )
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        Text(text = "Players", modifier = Modifier.padding(top = 5.dp))
        Row {
            Column {
                Text(text = teamLabel1, modifier = Modifier.padding(top = 5.dp))
                Text(text = teamLabel2, modifier = Modifier.padding(top = 5.dp))
            }

            Spacer(modifier = Modifier.padding(horizontal = 10.dp))

            // Game 1
            Column {
                Text(
                    text = "21",
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                )
                Text(
                    text = "11",
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                )
            }
            // Game 2
            AnimatedVisibility(visible = true) {
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
            // Game 3
            AnimatedVisibility(visible = false) {
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
            MainNameBoardView(team1 = team1, team2 = team2, scoreA = 1, scoreB = 1)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CourtViewP() {
    MainNameBoardView(
        team1 = doublePlayer("Muhammad Asrozi", "Imam Sulthon"),
        team2 = doublePlayer("Kim Jong Un", "Michael Blur"),
        scoreA = 1, scoreB =  14
    )

}