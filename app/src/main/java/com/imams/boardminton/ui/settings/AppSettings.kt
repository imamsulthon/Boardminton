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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.imams.boardminton.R

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
    var switchLocale by remember { mutableStateOf(state.isEnglish) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .widthIn(max = 240.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            val (contents, buttons) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(contents) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(buttons.top)
                        height = Dimension.preferredWrapContent
                    }
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.select_theme),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 10.dp)
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
                    Text(text = stringResource(R.string.dynamic_colors), modifier = Modifier.padding(start = 10.dp))
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Switch(
                        checked = switchLocale,
                        onCheckedChange = { switchLocale = it },
                        thumbContent = { Text(text = if (switchLocale) "EN" else "ID") }
                    )
                    Text(text = stringResource(R.string.label_app_language), modifier = Modifier.padding(start = 10.dp))
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
                Text(
                    text = stringResource(R.string.match_board_settings),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 15.dp)
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
                    Text(text = stringResource(R.string.button_vibrate), modifier = Modifier.padding(start = 10.dp))
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 12.dp)
                    .constrainAs(buttons) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
                horizontalArrangement = Arrangement.End,
            ) {
                OutlinedButton(
                    onClick = { onDismiss.invoke() },
                    modifier = Modifier.padding(end = 10.dp)
                ) { Text(text = stringResource(R.string.label_cancel)) }
                OutlinedButton(
                    onClick = {
                        val config = stateData.copy(
                            theme = stateData.theme.copy(
                                selected = selected,
                                dynamicColor = switchDynamicTheme
                            ),
                            matchBoard = stateData.matchBoard.copy(
                                isVibrateAddPoint = switchIsVibrate
                            ),
                            isEnglish = switchLocale
                        )
                        onApply.invoke(config)
                        onDismiss.invoke()
                    }
                ) { Text(text = stringResource(R.string.label_apply)) }
            }
        }
    }
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