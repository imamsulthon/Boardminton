package com.imams.boardminton.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppConfigDialog(
    stateData: AppConfig,
    onApply: (AppConfig) -> Unit,
    onDismiss: () -> Unit,
) {
    val state by remember { mutableStateOf(stateData) }
    var selected by remember(state) { mutableIntStateOf(state.theme.selected) }
    var switchDynamicTheme by remember(state) { mutableStateOf(state.theme.dynamicColor) }
    var switchIsVibrate by remember(state) { mutableStateOf(state.matchBoard.isVibrateAddPoint) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .widthIn(max = 200.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {
            Text(
                text = "Select Theme:",
                modifier = Modifier.wrapContentSize().padding(vertical = 10.dp)
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))

            Column(Modifier.selectableGroup()) {
                state.theme.optionals.forEach { text ->
                    InputChip(
                        selected = selected == text.ordinal,
                        onClick = { selected = text.ordinal },
                        label = {
                            Text(
                                text = text.name,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Switch(
                    checked = switchDynamicTheme,
                    onCheckedChange = { switchDynamicTheme = it },
                )
                Text(text = "Dynamic Color", modifier = Modifier.padding(start = 10.dp))
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
            Text(
                text = "Match Board Settings:",
                modifier = Modifier.wrapContentSize().padding(vertical = 15.dp)
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Switch(
                    checked = switchIsVibrate,
                    onCheckedChange = { switchIsVibrate = it },
                )
                Text(text = "Button Vibrate", modifier = Modifier.padding(start = 10.dp))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                OutlinedButton(
                    onClick = { onDismiss.invoke() },
                    modifier = Modifier.padding(end = 10.dp)
                ) { Text(text = "Cancel") }
                OutlinedButton(
                    onClick = {
                        val config = stateData.copy(
                            theme = stateData.theme.copy(
                                selected = selected,
                                dynamicColor = switchDynamicTheme
                            ),
                            matchBoard = stateData.matchBoard.copy(
                                isVibrateAddPoint = switchIsVibrate
                            )
                        )
                        printLog("OnApply $config")
                        onApply.invoke(config)
                        onDismiss.invoke()
                    }
                ) { Text(text = "Apply") }
            }

        }
    }
}

private fun printLog(m: String) {
    println("ChangeTheme $m")
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ChangeThemePreview() {
    AppConfigDialog(
        stateData = AppConfig(),
        onApply = { _ -> },
        onDismiss = {},
    )
}

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