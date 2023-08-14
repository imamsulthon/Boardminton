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
fun ChangeThemeDialog(
    stateData: ChangeThemeState,
    onApply: (ChangeThemeState) -> Unit,
    onDismiss: () -> Unit,
) {
    val state by remember { mutableStateOf(stateData) }
    var selected by remember(state) { mutableIntStateOf(state.selected) }
    var switchState by remember(state) { mutableStateOf(state.dynamicColor) }

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
                Modifier
                    .wrapContentSize()
                    .padding(vertical = 10.dp)
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))

            Column(Modifier.selectableGroup()) {
                state.optionals.forEach { text ->
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
                    checked = switchState,
                    onCheckedChange = { switchState = it },
                )
                Text(text = "Dynamic Color", modifier = Modifier.padding(start = 10.dp))
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
                            selected = selected,
                            dynamicColor = switchState
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
    ChangeThemeDialog(
        stateData = ChangeThemeState(),
        onApply = {},
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