package com.imams.boardminton.domain.impl

import com.imams.boardminton.domain.mapper.toModel
import com.imams.boardminton.domain.mapper.toViewParam
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.MatchUIState
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.engine.data.MatchEngine
import com.imams.boardminton.engine.data.model.MatchType
import com.imams.boardminton.engine.data.model.Side
import kotlinx.coroutines.flow.MutableStateFlow


class MatchEngineUseCaseImpl: MatchEngineUseCase {

    lateinit var engine: MatchEngine

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
            else -> {
                // todo add other event here
            }
        }
    }

    override fun get(): MatchViewParam {
        return engine.getMatchScore().toViewParam()
    }

    override fun asState(): MatchUIState {
        return MatchUIState(match = get())
    }

    override fun asStateFlow(): MutableStateFlow<MatchUIState> {
        return MutableStateFlow(asState())
    }

}

interface MatchEngineUseCase {

    fun create(matchType: MatchType, teamA: TeamViewParam, teamB: TeamViewParam, prevGames: MutableList<GameViewParam>)
    fun create(a1: String, a2: String)
    fun create(a1: String, a2: String, b1: String, b2: String)
    fun create(matchViewParam: MatchViewParam)
    fun execute(event: BoardEvent)
    fun get(): MatchViewParam
    fun asState(): MatchUIState
    fun asStateFlow(): MutableStateFlow<MatchUIState>

}

sealed class BoardEvent{
    data class PointTo(val side: Side): BoardEvent()
    data class MinTo(val side: Side): BoardEvent()
    data class ServeTo(val side: Side): BoardEvent()
    object Other: BoardEvent()
}