package com.imams.boardminton.domain

import com.imams.boardminton.domain.impl.BoardEvent
import com.imams.boardminton.domain.impl.CombinedMatchBoardUseCaseImpl
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.engine.data.model.Winner
import org.junit.Assert
import org.junit.Test

class MatchEnginUseCaseTest {

    @Test
    fun init_test() {
        val useCase = CombinedMatchBoardUseCaseImpl()
        useCase.create("Imam Sulthon", "Iqbal Kamal")

        useCase.execute(BoardEvent.PointTo(ISide.A))

        val matchUiState = useCase.getMatch()

        Assert.assertEquals(1, matchUiState.currentGame.scoreA.point)
        Assert.assertEquals(true, matchUiState.currentGame.scoreA.onServe)

        useCase.execute(BoardEvent.PointTo(ISide.A))
        Assert.assertEquals(2, useCase.getMatch().currentGame.scoreA.point)
    }

    @Test
    fun check_winner() {
        val useCase = CombinedMatchBoardUseCaseImpl()
        useCase.create("Imam Sulthon", "Iqbal Kamal")
        useCase.execute(BoardEvent.PointTo(ISide.A))

        for (i in 1..20) {
            useCase.execute(BoardEvent.PointTo(ISide.A))
        }
        for (i in 1..5) {
            useCase.execute(BoardEvent.PointTo(ISide.B))
        }
        useCase.execute(BoardEvent.PointTo(ISide.A))

        Assert.assertEquals(Winner.A, useCase.getMatch().currentGame.winner)

    }

}