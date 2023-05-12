package com.imams.boardminton.engine.data.model


data class MatchRule(
    val maxGame: Int = 3,
)

data class GameRule(
    val maxPoint: Int = 21,
    val intervalPoint: Int = 11,
    val advPoint: Int = 30,
)