package com.imams.boardminton.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.imams.boardminton.navigation.BoardMintonNavHost
import com.imams.boardminton.ui.theme.BoardMintonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardMintonTheme {
                BoardMintonNavHost()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}