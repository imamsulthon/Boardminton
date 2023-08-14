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

    val appThemes = dataStore.data.map { pref ->
        ChangeThemeState(selected = pref[themeMode] ?: 0, dynamicColor = pref[themeDynamicColor] ?: false)
    }

    suspend fun setTheme(state: ChangeThemeState) {
        dataStore.edit {
            setTheme(state.selected)
            setDynamicColor(state.dynamicColor)
        }
    }

}

enum class AppThemes {
    MODE_AUTO,
    MODE_LIGHT,
    MODE_DARK,
}