package com.imams.boardminton.domain.impl

import com.imams.boardminton.data.ISide
import com.imams.boardminton.domain.mapper.toModel
import com.imams.boardminton.domain.mapper.toViewParam
import com.imams.boardminton.domain.model.CourtSide
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.MatchUIState
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.boardminton.domain.model.ScoreByCourt
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.engine.data.model.MatchType
import com.imams.boardminton.engine.data.model.Side
import com.imams.boardminton.engine.implementation.MatchEngine

class CombinedMatchBoardUseCaseImpl: MatchBoardUseCase {

    private lateinit var engine: MatchEngine

    override fun create(
        matchType: MatchType,
        teamA: TeamViewParam,
        teamB: TeamViewParam,
        prevGames: MutableList<GameViewParam>
    ) {
        engine = MatchEngine(matchType, teamA.toModel(), teamB.toModel(),
            prevGames.map { it.toModel() }.toMutableList()
        )
    }

    override fun create(a1: String, a2: String) {
        engine = MatchEngine(a1, a2)
    }

    override fun create(a1: String, a2: String, b1: String, b2: String) {
        engine = MatchEngine(a1, a2, b1, b2)
    }

    override fun create(matchViewParam: MatchViewParam) {
        engine = MatchEngine(matchViewParam.toModel())
    }

    override fun updatePlayers(matchType: MatchType, a1: String, a2: String, b1: String, b2: String) {
        engine.updatePlayers(matchType, a1, a2, b1, b2)
    }

    override fun execute(event: BoardEvent) {
        when (event) {
            is BoardEvent.PointTo -> {
                engine.pointTo(event.side)
            }
            is BoardEvent.MinTo -> {
                engine.minTo(event.side)
            }
            is BoardEvent.ServeTo -> {
                engine.serveTo(event.side)
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
        return MatchUIState(match = get()).apply {
        }
    }

    override fun asStateByCourtConfig(courtSide: CourtSide): MatchUIState {
        return MatchUIState(match = get()).apply {
            val left = if (courtSide.left == ISide.A) this.match.currentGame.scoreA
            else this.match.currentGame.scoreB
            val right = if (courtSide.right == ISide.A) this.match.currentGame.scoreA
            else this.match.currentGame.scoreB
            val tLeft = if (courtSide.left == ISide.A) this.match.teamA
            else this.match.teamB
            val tRight = if (courtSide.right == ISide.A) this.match.teamA
            else this.match.teamB
            scoreByCourt = ScoreByCourt(
                index = this.match.currentGame.index,
                left = left,
                right = right,
                teamLeft = tLeft,
                teamRight = tRight,
            )
        }
    }

}

interface MatchBoardUseCase {

    fun create(matchType: MatchType, teamA: TeamViewParam, teamB: TeamViewParam, prevGames: MutableList<GameViewParam>)
    fun create(a1: String, a2: String)
    fun create(a1: String, a2: String, b1: String, b2: String)
    fun create(matchViewParam: MatchViewParam)
    fun updatePlayers(matchType: MatchType, a1: String, a2: String, b1: String, b2: String)
    fun execute(event: BoardEvent)
    fun get(): MatchViewParam
    fun asState(): MatchUIState
    fun asStateByCourtConfig(courtSide: CourtSide): MatchUIState

}

sealed class BoardEvent{
    data class PointTo(val side: Side): BoardEvent()
    data class MinTo(val side: Side): BoardEvent()
    data class ServeTo(val side: Side): BoardEvent()
    object SwapServer: BoardEvent()
    object SwapBoardSide: BoardEvent()
    object Other: BoardEvent()
}