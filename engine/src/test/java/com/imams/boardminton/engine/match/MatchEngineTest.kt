package com.imams.boardminton.engine.match

import com.imams.boardminton.engine.implementation.MatchEngine
import com.imams.boardminton.engine.data.model.MatchType
import com.imams.boardminton.engine.data.model.Player
import com.imams.boardminton.engine.data.model.Side
import com.imams.boardminton.engine.data.model.Team
import org.junit.Assert
import org.junit.Test

class MatchEngineTest {

    @Test
    fun init_test() {
        val matchEngine = MatchEngine(null,
            MatchType.Single,
            Team(Player("Imam Sulthon"), Player("Iqbal Kamal")),
            Team(Player("Achif Kaizan"), Player("Al Ghazi")),
        )
        Assert.assertEquals(1, matchEngine.getMatchScore().currentGame.index)

        // setup server
        matchEngine.run {
            pointTo(Side.A) // +1
            pointTo(Side.A) // +1
            pointTo(Side.B) // +1
            pointTo(Side.A) // +1
        }

        Assert.assertEquals(3, matchEngine.getMatchScore().currentGame.scoreA.point)
        Assert.assertEquals(1, matchEngine.getMatchScore().currentGame.scoreB.point)
    }

    @Test
    fun init_match_with_player_name() {

        // test single match
        val engine = MatchEngine("Imam Sulthon", "Achif Kaizan")
        engine.run {
            pointTo(Side.A)
            pointTo(Side.A)
            pointTo(Side.B)
            pointTo(Side.A)
            pointTo(Side.A)
        }

        Assert.assertEquals(MatchType.Single, engine.getMatchScore().type)
        Assert.assertEquals(4, engine.getMatchScore().currentGame.scoreA.point)
        Assert.assertEquals(1, engine.getMatchScore().currentGame.scoreB.point)

        // test double match
        val engine2 = MatchEngine("Imam Sulthon", "Achif Kaizan", "Iqbal Kamal", "Al Ghazi")
        engine2.run {
            pointTo(Side.A)
            pointTo(Side.A)
            minTo(Side.A)
        }

        Assert.assertEquals(MatchType.Double, engine2.getMatchScore().type)
        Assert.assertEquals(1, engine2.getMatchScore().currentGame.scoreA.point)
        Assert.assertEquals(0, engine2.getMatchScore().currentGame.scoreB.point)
    }

}