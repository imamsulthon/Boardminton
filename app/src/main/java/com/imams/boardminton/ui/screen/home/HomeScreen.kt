package com.imams.boardminton.ui.screen.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imams.boardminton.R
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.domain.model.MatchUIState
import com.imams.boardminton.domain.model.PlayerViewParam
import com.imams.boardminton.domain.model.ScoreByCourt
import com.imams.boardminton.domain.model.ScoreViewParam
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.ui.component.MainNameBoardView

@Composable
fun HomeScreen(
    uiState: MatchUIState? = null,
    onCreateMatch: (String) -> Unit,
    onCreatePlayer: (String) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 840.dp),
        topBar = {
            HomeAppBar()
        },
    ) { padding ->
        HomeContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            onCreateMatch = {
                if (it) onCreateMatch.invoke("single")
                else onCreateMatch.invoke("double")
            },
            onCreatePlayer = onCreatePlayer::invoke,
            uiState = uiState
        )
    }
}

@Composable
private fun HomeAppBar() {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val appName = stringResource(id = R.string.app_name)
        Text(
            text = appName,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp
        )
    }
}

@Composable
internal fun HomeContent(
    modifier: Modifier,
    onCreateMatch: (Boolean) -> Unit,
    onCreatePlayer: (String) -> Unit,
    uiState: MatchUIState? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MenuGroup(
            label = "Create New Match"
        ) {
            ItemMenu(label = "Single Match") {
                onCreateMatch.invoke(true)
            }
            ItemMenu(label = "Double Match") {
                onCreateMatch.invoke(false)
            }
        }

        MenuGroup(
            label = "Create New Player"
        ) {
            ItemMenu(label = "New Player") {
                onCreatePlayer.invoke("create")
            }
            ItemMenu(label = "Registered Players", enabled = true) {
                onCreatePlayer.invoke("seeAll")
            }
        }
        AnimatedVisibility(visible = uiState != null) {
            if (uiState != null) {
                Text(
                    text = "On Going Match",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
        }
        AnimatedVisibility(visible = uiState != null) {
            if (uiState != null) {
                MainNameBoardView(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    team1 = uiState.match.teamA,
                    team2 = uiState.match.teamB,
                    scoreA = uiState.match.currentGame.scoreA.point,
                    scoreB =uiState.match.currentGame.scoreB.point,
                    histories = uiState.match.games
                )
            }
        }
    }
}

@Composable
private fun MenuGroup(
    label: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            modifier = Modifier.align(Alignment.Start),
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(max = 400.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemMenu(
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Card(
        onClick = { onClick.invoke() },
        enabled = enabled,
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 5.dp,
            bottomEnd = 20.dp,
            bottomStart = 5.dp
        ),
        modifier = Modifier
            .size(width = 160.dp, height = 80.dp)
            .padding(5.dp),
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(label, Modifier.align(Alignment.Center))
        }
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(onCreateMatch = {}, onCreatePlayer = {})
}

@Preview
@Composable
private fun HomeContentWithCurrentMatch() {
    val latestMatch = ScoreByCourt(
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