package com.imams.boardminton.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.imams.boardminton.data.TeamPlayer

fun Int.even(): Boolean {
    return this % 2 == 0
}

fun TeamPlayer.concatLastName(): String {
    return this.player1.name.lastNameWith(this.player2?.name ?: "")
}

fun String.lastNameWith(friend: String): String {
    return this.lastName() + "/" + friend.lastName()
}

fun String.lastName(): String {
    return this.substringAfterLast(" ")
}

fun keyboardNext() = KeyboardOptions(
    capitalization = KeyboardCapitalization.Words,
    keyboardType = KeyboardType.Text,
    imeAction = ImeAction.Next
)

fun keyBoardDone() = KeyboardOptions(
    capitalization = KeyboardCapitalization.Words,
    keyboardType = KeyboardType.Text,
    imeAction = ImeAction.Done,
)