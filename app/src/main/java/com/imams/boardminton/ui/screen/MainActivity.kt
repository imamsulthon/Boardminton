package com.imams.boardminton.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.imams.boardminton.navigation.BoardMintonNavHost
import com.imams.boardminton.ui.screen.home.HomeScreenVM
import com.imams.boardminton.ui.settings.AppThemes
import com.imams.boardminton.ui.settings.ChangeThemeState
import com.imams.boardminton.ui.theme.BoardMintonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: HomeScreenVM by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val theme = viewModel.theme.collectAsState(initial = AppThemes.MODE_AUTO).value
            val themeState = viewModel.dynamic.collectAsState(initial = false).value ?: false
            val useDarkTheme = theme?.let {
                when (it) {
                    AppThemes.MODE_AUTO -> isSystemInDarkTheme()
                    AppThemes.MODE_LIGHT -> false
                    AppThemes.MODE_DARK -> true
                }
            } ?: isSystemInDarkTheme()

            BoardMintonTheme(
                darkTheme = useDarkTheme,
                isDynamicColor = themeState
            ) {
                BoardMintonNavHost(
                    changeThemeState = ChangeThemeState().copy(
                        selected = theme?.ordinal ?: -1,
                        dynamicColor = themeState
                    ),
                    onChangeTheme = { viewModel.saveTheme(it) }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}