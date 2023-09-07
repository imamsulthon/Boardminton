package com.imams.boardminton.ui.screen.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.imams.boardminton.R
import com.imams.boardminton.ui.settings.MatchBoardSetting

@Composable
fun MatchSettingDialog(
    stateData: MatchBoardSetting,
    onApply: (MatchBoardSetting) -> Unit,
    onDismiss: () -> Unit,
) {
    val state by remember { mutableStateOf(stateData) }
    var switchIsVibrate by remember(state) { mutableStateOf(state.isVibrateAddPoint) }

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Switch(
                    enabled = false,
                    checked = false,
                    onCheckedChange = {  },
                )
                Text(text = stringResource(R.string.button_sound), modifier = Modifier.padding(start = 10.dp))
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
                ) { Text(text = stringResource(R.string.label_cancel)) }
                OutlinedButton(
                    onClick = {
                        val config = stateData.copy(isVibrateAddPoint = switchIsVibrate)
                        onApply.invoke(config)
                        onDismiss.invoke()
                    }
                ) { Text(text = stringResource(R.string.label_apply)) }
            }

        }
    }
}