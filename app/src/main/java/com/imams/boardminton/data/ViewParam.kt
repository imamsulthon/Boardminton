package com.imams.boardminton.data

fun singlePlayer(name: String, onTurn: Boolean = false) = TeamPlayer(
    Player(name, onTurn), null
)

fun doublePlayer(name1: String, name2: String, onTurn: Boolean = false) = TeamPlayer(
    Player(name1, onTurn), Player(name2)
)

data class TeamPlayer(
    val player1: Player,
    val player2: Player? = null,
) {
    var serve: Serve = Serve.None

    var onTurn: Boolean = if (isSingle()) player1.onTurn else player1.onTurn || player2!!.onTurn

    fun isSingle(): Boolean = player2 == null
}

data class Player(
    var name: String,
    var onTurn: Boolean = false
)

enum class Serve {
    A, B, None
}