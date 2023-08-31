package com.imams.boardminton.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimeCounterView(
    modifier: Modifier,
    timeString: String,
    play: () -> Unit?,
    pause: () -> Unit?,
    stop: () -> Unit?,
) {
    Box(
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onBackground,
            shape = RoundedCornerShape(12.dp))
            .clickable {
                play.invoke()
            },
    ) {
        Text(
            modifier = Modifier.wrapContentSize().padding(vertical = 8.dp, horizontal = 12.dp),
            text = timeString, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TimeCounterPreview() {
    TimeCounterView(
        modifier = Modifier.wrapContentSize(),
        timeString = "00:00:00",
        play = { /*TODO*/ },
        pause = { /*TODO*/ }) {
    }
}