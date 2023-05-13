package com.imams.boardminton.domain

import com.imams.boardminton.domain.impl.BoardEvent
import com.imams.boardminton.domain.impl.CombinedMatchBoardUseCaseImpl
import com.imams.boardminton.engine.data.model.Side
import org.junit.Assert
import org.junit.Test

class MatchEnginUseCaseTest {

    @Test
    fun init_test() {
        val useCase = CombinedMatchBoardUseCaseImpl()
        useCase.create("Imam Sulthon", "Iqbal Kamal")

        useCase.execute(BoardEvent.PointTo(Side.A))

        val uiState = useCase.asState()

        Assert.assertEquals(1, uiState.match.currentGame.scoreA.point)
        Assert.assertEquals(true, uiState.match.currentGame.scoreA.onServe)

        useCase.execute(BoardEvent.PointTo(Side.A))
        Assert.assertEquals(2, useCase.asState().match.currentGame.scoreA.point)
    }

    @Test
    fun init_test_as_state_flow_result() {
        val useCase = CombinedMatchBoardUseCaseImpl()
        useCase.create("Imam Sulthon", "Iqbal Kamal")
        useCase.execute(BoardEvent.PointTo(Side.A))

    }
}