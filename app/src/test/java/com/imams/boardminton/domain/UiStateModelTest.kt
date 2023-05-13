package com.imams.boardminton.domain

import com.imams.boardminton.domain.model.CourtSide
import com.imams.boardminton.engine.data.model.Side
import org.junit.Assert
import org.junit.Test

class UiStateModelTest {

    @Test
    fun court_to_side_test() {
        val court = CourtSide()
        execute(court.left)
        execute(court.right)

        Assert.assertEquals(Side.A, court.left)
        Assert.assertEquals(Side.B, court.right)

        court.swap()
        execute(court.left)

        Assert.assertEquals(Side.B, court.left)
        Assert.assertEquals(Side.A, court.right)
    }

    private fun execute(side: Side) {
        when (side) {
            Side.A -> println("is side ${side.name}")
            Side.B -> println("is side ${side.name}")
        }
    }


}
