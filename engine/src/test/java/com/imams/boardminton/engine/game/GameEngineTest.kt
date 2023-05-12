package com.imams.boardminton.engine.game

import com.imams.boardminton.engine.data.model.Game
import com.imams.boardminton.engine.data.GameEngine
import com.imams.boardminton.engine.data.model.Score
import com.imams.boardminton.engine.data.model.Side
import org.junit.Assert
import org.junit.Test

class GameEngineTest {

    @Test
    fun engine_with_init_game() {
        val engine = GameEngine(Game(1, Score(19)))
        engine.apply {
            pointTo(Side.A)
            pointTo(Side.A)
            pointTo(Side.A)
        }

        Assert.assertEquals(21, engine.asGame().scoreA.point)
    }

    @Test
    fun check_init_point_and_server() {
        val gameEngine = GameEngine(
            1,
            Score(0), // score A
            Score(0), // score B
        )
        gameEngine.serveTo(Side.A) // init server

        gameEngine.apply {
            pointTo(Side.A)
            pointTo(Side.A)
            pointTo(Side.A)
            pointTo(Side.B)
            minPoint(Side.A)
        }
        Assert.assertEquals(2, gameEngine.asGame().scoreA.point)
        Assert.assertEquals(false, gameEngine.asGame().scoreA.onServe)
        Assert.assertEquals(true, gameEngine.asGame().scoreB.onServe)
    }

    @Test
    fun last_point_below_20() {
        val gameEngine = GameEngine(1, Score(19), Score(16))
        gameEngine.serveTo(Side.A) // init server

        gameEngine.apply {
            pointTo(Side.A)
            pointTo(Side.B)
        }

        Assert.assertEquals(true, gameEngine.asGame().scoreA.lastPoint)
        Assert.assertEquals(false, gameEngine.asGame().scoreB.lastPoint)

        val game = gameEngine.asGame()

        Assert.assertEquals(true, gameEngine.asGame().scoreA.lastPoint)
        Assert.assertEquals(false, gameEngine.asGame().scoreB.lastPoint)
    }

}