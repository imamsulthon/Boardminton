package com.imams.boardminton.data.domain

interface UseCase {

    fun addA()

    fun addB()

    fun minA()

    fun minB()

    fun createSingleMatch(playerA: String, playerB: String)

    fun createDoubleMatch(playerA1: String, playerA2: String, playerB1: String, playerB2: String)

}