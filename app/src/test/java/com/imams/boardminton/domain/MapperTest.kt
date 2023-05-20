package com.imams.boardminton.domain

import com.imams.boardminton.domain.mapper.toVp
import com.imams.boardminton.engine.data.model.Score
import com.imams.boardminton.engine.data.model.Side
import com.imams.boardminton.engine.data.model.Winner
import com.imams.boardminton.engine.implementation.GameEngine
import org.junit.Assert
import org.junit.Test

class MapperTest {

    @Test
    fun game_model_to_param_check_winner() {

        val gameEngine = GameEngine(1, Score(19), Score(16))
        gameEngine.serveTo(Side.A) // init server

        gameEngine.apply {
            pointTo(Side.A)
            pointTo(Side.A)
        }

        val gameViewParam = gameEngine.asGame().toVp()
        Assert.assertEquals(Winner.A, gameViewParam.winner)
    }

    @Test
    fun game_model_to_param_check_almost_win() {

        val gameEngine = GameEngine(1, Score(19), Score(16))
        gameEngine.serveTo(Side.A) // init server

        gameEngine.apply {
            pointTo(Side.A)
            pointTo(Side.B)
        }

        val gameViewParam = gameEngine.asGame().toVp()
        Assert.assertEquals(Winner.None, gameViewParam.winner)
    }

    @Test
    fun game_model_to_param_winn() {

        val gameEngine = GameEngine(1, Score(19), Score(16))
        gameEngine.serveTo(Side.A) // init server

        gameEngine.apply {
            pointTo(Side.A)
            pointTo(Side.A)
            minPoint(Side.A)
        }

        val gameViewParam = gameEngine.asGame().toVp()
        Assert.assertEquals(Winner.None, gameViewParam.winner)
    }


}