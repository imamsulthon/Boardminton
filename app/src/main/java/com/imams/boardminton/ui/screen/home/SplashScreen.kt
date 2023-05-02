package com.imams.boardminton.ui.screen.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.imams.boardminton.R
import com.imams.boardminton.ui.screen.destinations.HomeScreenDestination
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
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)
        navigator?.popBackStack()
        navigator?.navigate(HomeScreenDestination())
    }
    Splash(alpha = alphaAnim.value)
}

@Composable
private fun Splash(alpha: Float) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(AppPrimaryColor)
    ) {
        Icon(
            modifier = Modifier.alpha(alpha),
            painter = painterResource(id = R.drawable.ic_cock),
            tint = Color.White,
            contentDescription = "swap_server"
        )
        Box(contentAlignment = Alignment.Center) {
            Text(text = "BoardMinton", fontSize = 32.sp, color = Purple200, modifier = Modifier.alpha(alpha))
        }
    }
}

@Preview(uiMode = UI_MODE_TYPE_NORMAL)
@Composable
fun SplashScreenPreview() {
    BoardmintonSplashView(navigator = null)
}


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SplashScreenPreviewDark() {
    BoardmintonSplashView(navigator = null)
}