package com.imams.boardminton.engine.domain

import com.imams.boardminton.engine.domain.model.Score
import com.imams.boardminton.engine.domain.model.Side

class GameEngine {

    val scoreA = Score()
    val scoreB = Score()

    fun pointTo(side: Side) {
        when (side) {
            Side.A -> scoreA.add()
            Side.B -> scoreB.add()
        }
    }

}

data class GameConfig(
    val maxPoint: Int,
    val advPoint: Int,
)