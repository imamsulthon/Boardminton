package com.imams.boardminton.data

class GameScore constructor() {

    private val gamePoint = 21
    private val maxPoint = 30

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
        if (pointA == 30) return
        if (pointA.notDeuce(pointB)) pointA += 1
        if (revertServe) serveA()
        if (revertLp) pointA.lastPointA(pointB)
        gameEnd = pointA.isEndGame(pointB)
    }
    
    fun addB(revertServe: Boolean = true, revertLp: Boolean = true) {
        if (pointB == 30) return
        if (pointB.notDeuce(pointA)) pointB += 1
        if (revertServe) serveB()
        if (revertLp) pointB.lastPointB(pointA)
        gameEnd = pointB.isEndGame(pointA)
    }
    
    fun minA(revertLp: Boolean = true) {
        if (pointA > 0) pointA -= 1
        if (revertLp) pointA.lastPointA(pointA)
    }
    
    fun minB(revertLp: Boolean = true) {
        if (pointB > 0) pointB -= 1
        if (revertLp) pointB.lastPointB(pointA)
    }
    
    fun serveA() {
        onTurnA = true
        onTurnB = false
    }
    
    fun serveB() {
        onTurnA = false
        onTurnB = true
    }

    fun swapServer() {
        if (onTurnA) serveB() else serveA()
    }
    
    fun server(): String = if (onTurnA) "A" else if (onTurnB) "B" else "A"

    fun reset() {
        pointA = 0
        pointB = 0
        onTurnA = false
        onTurnB = false
        lastPointA = false
        lastPointB = false
        gameEnd = false
    }

    private fun Int.lastPointA(opposite: Int) {
        lastPointA = this.lastPoint(opposite)
        lastPointB = false
    }
    private fun Int.lastPointB(opposite: Int) {
        lastPointB = this.lastPoint(opposite)
        lastPointA = false
    }
    
    private fun Int.notDeuce(opposite: Int) = !this.isEndGame(opposite)

    private fun Int.isEndGame(opposite: Int) = this in gamePoint..maxPoint && this - opposite > 1

    private fun Int.lastPoint(opposite: Int) = this in gamePoint - 1 until maxPoint - 1 && this - opposite > 0

}