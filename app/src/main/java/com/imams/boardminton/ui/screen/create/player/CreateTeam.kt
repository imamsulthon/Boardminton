package com.imams.boardminton.ui.screen.create.player

import com.imams.boardminton.ui.utils.lastNameWith


data class CreateTeamState(
    var id: Int = 0,
    val playerId1: Int = 0,
    val playerId2: Int = 0,
    val playerName1: String = "",
    val playerName2: String = "",
    val type: String = "double",
    val rank: Int = 0,
    val play: Int = 0,
    val win: Int = 0,
    val lose: Int = 0,
) {
    val alias = playerName1.lastNameWith(playerName2)
}

sealed class CreateTeamEvent {
    data class Player1(val data: CreatePlayerState): CreateTeamEvent()
    data class Player2(val data: CreatePlayerState): CreateTeamEvent()
    object Swap: CreateTeamEvent()
    object Clear: CreateTeamEvent()
}