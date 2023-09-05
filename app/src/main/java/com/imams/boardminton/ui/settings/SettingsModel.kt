package com.imams.boardminton.ui.settings

data class MatchBoardSetting(
    var isVibrateAddPoint: Boolean = false,
)

data class ChangeThemeState(
    var selected: Int = -1,
    val optionals: List<AppThemes> = listOf(
        AppThemes.MODE_AUTO,
        AppThemes.MODE_LIGHT,
        AppThemes.MODE_DARK,
    ),
    val dynamicColor: Boolean = false,
)

fun ChangeThemeState.toAppThemes(): AppThemes = when (this.selected) {
    0 -> AppThemes.MODE_AUTO
    1 -> AppThemes.MODE_LIGHT
    2 -> AppThemes.MODE_DARK
    else -> AppThemes.MODE_AUTO
}

enum class AppThemes {
    MODE_AUTO,
    MODE_LIGHT,
    MODE_DARK,
}