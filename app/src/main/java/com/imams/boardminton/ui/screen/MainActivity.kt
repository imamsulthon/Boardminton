package com.imams.boardminton.ui.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.imams.boardminton.navigation.BoardMintonNavHost
import com.imams.boardminton.ui.screen.home.HomeScreenVM
import com.imams.boardminton.ui.settings.AppConfig
import com.imams.boardminton.ui.settings.AppThemes
import com.imams.boardminton.ui.settings.toAppThemes
import com.imams.boardminton.ui.theme.BoardMintonTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: HomeScreenVM by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appConfig by viewModel.appConfig.collectAsState(initial = AppConfig())
            val useDarkTheme = appConfig.theme.toAppThemes().let {
                when (it) {
                    AppThemes.MODE_AUTO -> isSystemInDarkTheme()
                    AppThemes.MODE_LIGHT -> false
                    AppThemes.MODE_DARK -> true
                }
            }
            val isEnglish by remember(appConfig.isEnglish) { derivedStateOf { appConfig.isEnglish } }

            BoardMintonTheme(
                darkTheme = useDarkTheme,
                isDynamicColor = appConfig.theme.dynamicColor
            ) {
                setLocale(isEnglish)
                BoardMintonNavHost(
                    appConfig = appConfig,
                    onAppConfig = { viewModel.saveConfig(it) },
                )
            }
        }
    }

    private fun setLocale(isEnglish: Boolean) {
        if (isEnglish) setLocale("en") else setLocale("id")
    }

    private fun setLocale(code: String) {
        val config = resources.configuration
        val locale = Locale(code)
        Locale.setDefault(locale)
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}