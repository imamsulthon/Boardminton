package com.imams.boardminton.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            color = Color.Black,
            shape = RoundedCornerShape(12.dp))
            .clickable {
                play.invoke()
            },
    ) {
        Text(
            modifier = Modifier.wrapContentSize().padding(vertical = 8.dp, horizontal = 12.dp),
            text = timeString, fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimeCounterPreview() {
    TimeCounterView(
        modifier = Modifier.wrapContentSize(),
        timeString = "00:00:00",
        play = { /*TODO*/ },
        pause = { /*TODO*/ }) {
    }
}

fun printLog(any: Any? = null, msg: String = "") {
    if (any == null) println("AppLog: msg -> $msg")
    else println("${any.javaClass.simpleName}: msg -> $msg")
}