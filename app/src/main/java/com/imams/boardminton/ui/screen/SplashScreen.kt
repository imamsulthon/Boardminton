package com.imams.boardminton.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.imams.boardminton.ui.screen.destinations.ScoreBoardScreenDestination
import com.imams.boardminton.ui.theme.AppPrimaryColor
import com.imams.boardminton.ui.theme.Purple200
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@Destination(start = true)
@Composable
fun BoardmintonSplashView(
    navigator: DestinationsNavigator?
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(AppPrimaryColor)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(text = "BoardMinton", fontSize = 32.sp, color = Purple200)
        }

        LaunchedEffect(Unit) {
            delay(2000)
            navigator?.popBackStack()
            navigator?.navigate(ScoreBoardScreenDestination())
        }

    }
}