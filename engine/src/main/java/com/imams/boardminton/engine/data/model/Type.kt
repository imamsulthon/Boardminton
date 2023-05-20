package com.imams.boardminton.engine.data.model

enum class MatchType {
    Single, Double
}

enum class GameIndex(val Winner: Winner) {
    One(Winner.None), Two(Winner.None), Rubber(Winner.None)
}

enum class Side {
    A, B
}

enum class Winner {
    A, B, None
}

enum class OnServe {
    A, B, NONE
}

enum class TeamServer {
    P1, P2, NONE
}

enum class PlayerType {
    MS, WS, MD, WD, XD
}