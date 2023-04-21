package com.imams.boardminton.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp


@Composable
fun TimeCounterView(
    modifier: Modifier,
    timeString: String,
    play: () -> Unit?,
    pause: () -> Unit?,
    stop: () -> Unit?,
) {
    Text(text = timeString, fontSize = 24.sp, modifier = modifier)
}

fun printLog(any: Any? = null, msg: String = "") {
    if (any == null) println("AppLog: msg -> $msg")
    else println("${any.javaClass.simpleName}: msg -> $msg")
}