package com.imams.boardminton.ui.screen.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.imams.boardminton.R
import kotlinx.coroutines.delay

@Composable
fun BoardmintonSplashView() {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)
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
    ) {
        Icon(
            modifier = Modifier.alpha(alpha),
            painter = painterResource(id = R.drawable.ic_cock),
            tint = Color.White,
            contentDescription = "swap_server"
        )
        Box(contentAlignment = Alignment.Center) {
            Text(text = "BoardMinton", fontSize = 32.sp, modifier = Modifier.alpha(alpha))
        }
    }
}

@Preview(uiMode = UI_MODE_TYPE_NORMAL)
@Composable
fun SplashScreenPreview() {
    BoardmintonSplashView()
}


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SplashScreenPreviewDark() {
    BoardmintonSplashView()
}