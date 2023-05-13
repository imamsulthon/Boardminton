package com.imams.boardminton.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.imams.boardminton.domain.model.TeamViewParam

fun Int.even(): Boolean {
    return this % 2 == 0
}
fun TeamViewParam.getLabel() = if (this.isSingle) this.player1.name.prettifyName() else this.concatLastName() ?: ""

fun TeamViewParam.concatLastName(): String {
    return this.player1.name.lastNameWith(this.player2.name ?: "")
}

fun String.lastNameWith(friend: String): String {
    return this.lastName() + "/" + friend.lastName()
}

fun String.lastName(): String {
    return this.substringAfterLast(" ")
}

fun String.prettifyName(): String {
    val words = this.split(" ".toRegex()).toTypedArray().toMutableList()
    val tLength = this.trim().length
    return if (words.size == 1) this
    else if (words.size >= 3 && tLength > 20) this.justLastName()
    else if (words.size >= 3) this.shortMiddle()
    else if (tLength > 20) this.shortFirst()
    else this

}

/**
 * save type only consisted two array string
 */
private fun String.shortFirst(): String {
    val words = this.split(" ".toRegex()).toTypedArray().toMutableList()
    return "${words.firstOrNull()?.get(0)}." + " " + words.lastOrNull()
}

private fun String.shortMiddle(): String {
    val words = this.split(" ".toRegex()).toTypedArray().toMutableList()
    if (words.size < 3) return this
    val new = words.mapIndexed { index, s ->
        if (index == 0 || index == words.size - 1) s
        else s[0] + "."
    }
    var middle = " "
    new.subList(1, new.size - 1).forEach {
        middle = "$middle$it "
    }
    return new.firstOrNull() + middle + new.lastOrNull()
}

private fun String.justLastName(): String {
    val words = this.split(" ".toRegex()).toTypedArray().toMutableList()
    if (words.size < 3) return this
    val new = words.mapIndexed { index, s ->
        if (index == words.size - 1) s
        else s[0] + "."
    }
    var first = " "
    new.subList(0, new.size - 1).forEach {
        first = "$first$it "
    }
    return first.trim() + " " + new.lastOrNull()
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