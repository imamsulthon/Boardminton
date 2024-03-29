package com.imams.boardminton.domain.impl

import com.imams.boardminton.domain.mapper.toModel
import com.imams.boardminton.domain.mapper.toVp
import com.imams.boardminton.domain.model.CourtSide
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.IMatchType
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.boardminton.domain.model.ScoreByCourt
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.engine.implementation.MatchEngine

class CombinedMatchBoardUseCaseImpl : MatchBoardUseCase {

    private lateinit var engine: MatchEngine

    /**
     * initialize [MatchEngine] with Team.
     * @param matchType
     */
    override fun create(
        matchType: IMatchType,
        teamA: TeamViewParam,
        teamB: TeamViewParam,
        prevGames: MutableList<GameViewParam>
    ) {
        engine = MatchEngine(0,
            matchType.toModel(), teamA.toModel(), teamB.toModel(),
            prevGames.map { it.toModel() }.toMutableList()
        )
    }

    /**
     * initialize [MatchEngine] with players name for Single Match.
     */
    override fun create(a1: String, a2: String) {
        engine = MatchEngine(a1, a2)
    }

    /**
     * initialize [MatchEngine] with players name for Double Match.
     */
    override fun create(a1: String, a2: String, b1: String, b2: String) {
        engine = MatchEngine(a1, a2, b1, b2)
    }

    /**
     * initialize [engine] by [matchViewParam].
     *
     * Use this method when resume match with existing data from saved repository
     */
    override fun create(matchViewParam: MatchViewParam) {
        engine = MatchEngine(matchViewParam.toModel())
    }

    /**
     * updating players name on [MatchEngine]
     */
    override fun updatePlayers(matchType: IMatchType, a1: String, a2: String, b1: String, b2: String) {
        engine.updatePlayers(matchType.toModel(), a1, a2, b1, b2)
    }

    /**
     * Generic function to execute an event on [MatchEngine] with [BoardEvent]
     * @param event
     */
    override fun execute(event: BoardEvent) {
        when (event) {
            is BoardEvent.PointTo -> {
                engine.pointTo(event.side.toVp())
            }
            is BoardEvent.MinTo -> {
                engine.minTo(event.side.toVp())
            }
            is BoardEvent.ServeTo -> {
                engine.serveTo(event.side.toVp())
            }
            is BoardEvent.SwapServer -> {
                engine.swapServer()
            }
            is BoardEvent.AddShuttleCock -> {
                engine.addShuttleCock()
            }
            is BoardEvent.OnNewGame -> {
                engine.createNewGame(event.index)
            }
            is BoardEvent.ResetGame -> {
                engine.resetGame()
            }
        }
    }

    /**
     * Return updated values of the match after executing certain event on [MatchEngine] as [MatchViewParam]
     */
    override fun getMatch(): MatchViewParam {
        return engine.getMatchScore().toVp()
    }

    override fun getScore(courtSide: CourtSide): ScoreByCourt {
        val match = getMatch()
        val left = if (courtSide.left == ISide.A) match.currentGame.scoreA else match.currentGame.scoreB
        val right = if (courtSide.right == ISide.A) match.currentGame.scoreA else match.currentGame.scoreB
        val teamOnLeft = if (courtSide.left == ISide.A) match.teamA else match.teamB
        val teamOnRight = if (courtSide.right == ISide.A) match.teamA else match.teamB
        return ScoreByCourt(
            index = match.currentGame.index,
            left = left,
            right = right,
            teamLeft = teamOnLeft,
            teamRight = teamOnRight,
        )
    }

}

interface MatchBoardUseCase {

    fun create(matchType: IMatchType, teamA: TeamViewParam, teamB: TeamViewParam, prevGames: MutableList<GameViewParam>)
    fun create(a1: String, a2: String)
    fun create(a1: String, a2: String, b1: String, b2: String)
    fun create(matchViewParam: MatchViewParam)
    fun updatePlayers(matchType: IMatchType, a1: String, a2: String, b1: String, b2: String)
    fun execute(event: BoardEvent)
    fun getMatch(): MatchViewParam
    fun getScore(courtSide: CourtSide): ScoreByCourt

}

sealed class BoardEvent {
    data class PointTo(val side: ISide) : BoardEvent()
    data class MinTo(val side: ISide) : BoardEvent()
    data class ServeTo(val side: ISide) : BoardEvent()
    object AddShuttleCock: BoardEvent()
    object SwapServer : BoardEvent()
    data class OnNewGame(val index: Int): BoardEvent()
    object ResetGame: BoardEvent()
}