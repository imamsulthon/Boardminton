package com.imams.boardminton.ui.screen

import com.imams.boardminton.data.toJson
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.ui.screen.destinations.EditPlayersScreenDestination
import com.imams.boardminton.ui.screen.destinations.ScoreBoardScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

fun DestinationsNavigator.toScoreBoard(list: List<String>, singleMatch: Boolean, resumed: Boolean = false) {
    this.navigate(
        ScoreBoardScreenDestination(
            players = list.toJson(),
            single = singleMatch
        ), onlyIfResumed = resumed
    )
}

fun DestinationsNavigator.toEditPlayers(single: Boolean, team1: TeamViewParam, team2: TeamViewParam) {
    val a1 = team1.player1.name ?: ""
    val a2 = team1.player2.name ?: ""
    val b1 = team2.player1.name ?: ""
    val b2 = team2.player2.name ?: ""
    this.navigate(
        EditPlayersScreenDestination(single = single, a1 = a1, a2 = a2, b1 = b1, b2 = b2)
    )
}