package com.imams.boardminton.data

@Deprecated("use ViewParam")
fun singlePlayer(name: String, onTurn: Boolean = false) = TeamPlayer(
    Player(name, onTurn), null
)
@Deprecated("use ViewParam")
fun doublePlayer(name1: String, name2: String, onTurn: Boolean = false) = TeamPlayer(
    Player(name1, onTurn), Player(name2)
)

@Deprecated("use ViewParam")
data class TeamPlayer(
    val player1: Player,
    val player2: Player? = null,
) {
    var serve: Serve = Serve.None
    var onTurn: Boolean = if (isSingle()) player1.onTurn else player1.onTurn || player2!!.onTurn

    fun isSingle(): Boolean = player2 == null
}

@Deprecated("use ViewParam")
data class Player(
    var name: String,
    var onTurn: Boolean = false
)

enum class Serve {
    A, B, None
}

enum class ITeam {
    A1, A2, B1, B2
}

enum class ISide {
    A, B
}