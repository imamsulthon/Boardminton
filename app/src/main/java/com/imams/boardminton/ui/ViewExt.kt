package com.imams.boardminton.ui

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