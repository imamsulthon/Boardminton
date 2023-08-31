package com.imams.boardminton.ui.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DesignPreferenceStore(context: Context) {

    private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val dataStore = context.createDataStore

    private val themeMode = intPreferencesKey("theme")
    private val themeDynamicColor = booleanPreferencesKey("theme_dynamic_colors")
    private val isVibratePoint = booleanPreferencesKey("is_vibrate_point")

    // region App Themes
    suspend fun setTheme(id: Int) {
        dataStore.edit { settings ->
            settings[themeMode] = id
        }
    }

    val currentTheme = dataStore.data.map { preferences ->
        preferences[themeMode]
    }

    suspend fun setDynamicColor(isDynamic: Boolean) {
        dataStore.edit { settings ->
            settings[themeDynamicColor] = isDynamic
        }
    }

    val dynamicColorTheme = dataStore.data.map { pref -> pref[themeDynamicColor] }
    // endregion

    // region Match Board Config
    suspend fun setVibrate(isVibrate: Boolean) {
        dataStore.edit {
            it[isVibratePoint] = isVibrate
        }
    }

    val isPointButtonVibrate = dataStore.data.map { pref -> pref[isVibratePoint] }
    // endregion

    val appConfig = dataStore.data.map { pref ->
        AppConfig(
            theme = ChangeThemeState(
                selected = pref[themeMode] ?: 0,
                dynamicColor = pref[themeDynamicColor] ?: false
            ),
            matchBoard = MatchBoardSetting(
                isVibrateAddPoint = pref[isVibratePoint] ?: false
            )
        )
    }

    suspend fun setAppConfig(appConfig: AppConfig) {
        dataStore.edit { settings ->
            settings[themeMode] = appConfig.theme.selected
            settings[themeDynamicColor] = appConfig.theme.dynamicColor
            settings[isVibratePoint] = appConfig.matchBoard.isVibrateAddPoint
        }
    }

}

data class AppConfig(
    val theme: ChangeThemeState = ChangeThemeState(),
    var matchBoard: MatchBoardSetting = MatchBoardSetting(),
)
