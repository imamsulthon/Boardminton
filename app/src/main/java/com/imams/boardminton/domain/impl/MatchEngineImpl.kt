package com.imams.boardminton.domain.impl

import com.imams.boardminton.domain.mapper.toModel
import com.imams.boardminton.domain.mapper.toViewParam
import com.imams.boardminton.domain.mapper.toVp
import com.imams.boardminton.domain.model.CourtSide
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.IMatchType
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.domain.model.MatchUIState
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.boardminton.domain.model.ScoreByCourt
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.engine.implementation.MatchEngine

class CombinedMatchBoardUseCaseImpl: MatchBoardUseCase {

    private lateinit var engine: MatchEngine

    override fun create(
        matchType: IMatchType,
        teamA: TeamViewParam,
        teamB: TeamViewParam,
        prevGames: MutableList<GameViewParam>
    ) {
        engine = MatchEngine(matchType.toModel(), teamA.toVp(), teamB.toVp(),
            prevGames.map { it.toVp() }.toMutableList()
        )
    }

    override fun create(a1: String, a2: String) {
        engine = MatchEngine(a1, a2)
    }

    override fun create(a1: String, a2: String, b1: String, b2: String) {
        engine = MatchEngine(a1, a2, b1, b2)
    }

    override fun create(matchViewParam: MatchViewParam) {
        engine = MatchEngine(matchViewParam.toVp())
    }

    override fun updatePlayers(matchType: IMatchType, a1: String, a2: String, b1: String, b2: String) {
        engine.updatePlayers(matchType.toModel(), a1, a2, b1, b2)
    }

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
            else -> { // todo add other event here
            }
        }
    }

    override fun get(): MatchViewParam {
        return engine.getMatchScore().toViewParam()
    }

    override fun asState(): MatchUIState {
        return MatchUIState(match = get())
    }

    override fun asStateByCourtConfig(courtSide: CourtSide): ScoreByCourt {
        val match = get()
        val left = if (courtSide.left == ISide.A) match.currentGame.scoreA
            else match.currentGame.scoreB
            val right = if (courtSide.right == ISide.A) match.currentGame.scoreA
            else match.currentGame.scoreB
            val teamOnLeft = if (courtSide.left == ISide.A) match.teamA
            else match.teamB
            val teamOnRight = if (courtSide.right == ISide.A) match.teamA
            else match.teamB
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
    fun get(): MatchViewParam
    fun asState(): MatchUIState
    fun asStateByCourtConfig(courtSide: CourtSide): ScoreByCourt

}

sealed class BoardEvent{
    data class PointTo(val side: ISide): BoardEvent()
    data class MinTo(val side: ISide): BoardEvent()
    data class ServeTo(val side: ISide): BoardEvent()
    object SwapServer: BoardEvent()
    object SwapBoardSide: BoardEvent()
    object Other: BoardEvent()
}