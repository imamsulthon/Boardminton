package com.imams.boardminton

import com.imams.boardminton.data.GameScore
import com.imams.boardminton.data.domain.GameUseCase
import com.imams.boardminton.data.domain.MatchType
import com.imams.boardminton.data.singlePlayer
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun add_one_point() {
        val gameScore = GameScore(singlePlayer("player 1"), singlePlayer("player 2"))

        // add player 1 2 point
        gameScore.addA()
        gameScore.addA()

        assertEquals(gameScore.pointA, 2)
        assertEquals(gameScore.onTurnA, true)

        // add player B 1 point
        gameScore.addB()

        assertEquals(gameScore.pointB, 1)
        assertEquals(gameScore.onTurnB, true)

        assertEquals(gameScore.server(), "B")
    }


    @Test
    fun add_point_asState_doubleMatch() {
        val useCaseDoubleMatch = GameUseCase(MatchType.DOUBLE)

        // add player 1 2 point
        useCaseDoubleMatch.addA()
        useCaseDoubleMatch.addA()

        assertEquals(useCaseDoubleMatch.gameScore.value.pointA, 2)
        assertEquals(useCaseDoubleMatch.gameScore.value.onTurnA, true)

        // add player B 1 point
        useCaseDoubleMatch.addB()

        assertEquals(useCaseDoubleMatch.gameScore.value.pointB, 1)
        assertEquals(useCaseDoubleMatch.gameScore.value.onTurnB, true)
        assertEquals(useCaseDoubleMatch.gameScore.value.onTurnA, false)

    }

}