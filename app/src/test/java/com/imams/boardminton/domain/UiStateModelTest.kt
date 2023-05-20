package com.imams.boardminton.domain

import com.imams.boardminton.domain.model.ISide
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

        Assert.assertEquals(ISide.A, court.left)
        Assert.assertEquals(ISide.B, court.right)

        court.swap()
        execute(court.left)

        Assert.assertEquals(ISide.B, court.left)
        Assert.assertEquals(Side.A, court.right)
    }

    private fun execute(side: ISide) {
        when (side) {
            ISide.A -> println("is side ${side.name}")
            ISide.B -> println("is side ${side.name}")
        }
    }


}
