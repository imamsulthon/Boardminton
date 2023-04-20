package com.imams.boardminton.data

class GameScore constructor() {

    var teamA: TeamPlayer? = null
    var teamB: TeamPlayer? = null
    
    var pointA = 0
    var pointB = 0
    
    var onTurnA = false
    var onTurnB = false

    var lastPointA = false
    var lastPointB = false
    
    var gameEnd = false

    constructor(team1: TeamPlayer? = null, team2: TeamPlayer? = null): this() {
        teamA = team1
        teamB = team2
    }
    
    fun createSingles(playerA: String = "", playerB: String = ""): GameScore {
        teamA = singlePlayer(playerA, true)
        teamB = singlePlayer(playerB)
        return GameScore(teamA, teamB)
    }
    
    fun createDoubles(
        playerA1: String = "", playerA2: String = "",
        playerB1: String = "", playerB2: String = "",
    ): GameScore {
        teamA = doublePlayer(playerA1, playerA2, true)
        teamB = doublePlayer(playerB1, playerB2)
        return GameScore(teamA, teamB)
    }
    
    fun addA(revertServe: Boolean = true, revertLp: Boolean = true) {
        if (pointA.notDeuce(pointB)) pointA += 1
        if (revertServe) serveA()
        if (revertLp) pointA.revertLastPoint(pointB)
        gameEnd = pointA.setEndGame(pointB)
    }
    
    fun addB(revertServe: Boolean = true, revertLp: Boolean = true) {
        if (pointB.notDeuce(pointB)) pointB += 1
        if (revertServe) serveB()
        if (revertLp) pointB.revertLastPoint(pointA)
        gameEnd = pointB.setEndGame(pointA)
    }
    
    fun minA(revertLp: Boolean = true) {
        if (pointA > 0) pointA -= 1
        if (revertLp) pointA.revertLastPoint(pointA)
    }
    
    fun minB(revertLp: Boolean = true) {
        if (pointB > 0) pointB -= 1
        if (revertLp) pointB.revertLastPoint(pointA)
    }
    
    fun serveA() {
        onTurnA = true
        onTurnB = false
    }
    
    fun serveB() {
        onTurnA = false
        onTurnB = true
    }
    
    fun server(): String = if (onTurnA) "A" else if (onTurnB) "B" else "A"

    private fun Int.revertLastPoint(opposite: Int) {
        lastPointA = this.lastPoint(opposite)
        lastPointB = false
    }
    
    private fun Int.notDeuce(opposite: Int) = !this.setEndGame(opposite)

    private fun Int.setEndGame(opposite: Int) = this in 21..30 && this - opposite > 1

    private fun Int.lastPoint(opposite: Int) = this in 20..29 && this - opposite > 0

}