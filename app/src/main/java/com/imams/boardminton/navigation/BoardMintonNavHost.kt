package com.imams.boardminton.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.imams.boardminton.navigation.Destination.CreateMatch
import com.imams.boardminton.navigation.Destination.EditPlayers
import com.imams.boardminton.navigation.Destination.Home
import com.imams.boardminton.navigation.Destination.ScoreBoard
import com.imams.boardminton.ui.screen.create.CreateMatchScreen
import com.imams.boardminton.ui.screen.create.EditPlayersScreen
import com.imams.boardminton.ui.screen.home.HomeScreen
import com.imams.boardminton.ui.screen.score.ScoreBoardScreen

sealed class Destination(protected val route: String, vararg params: String) {

    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class DestinationNoArgs(route: String): Destination(route) {
        operator fun invoke(): String = route
    }

    object Home: DestinationNoArgs("home")

    object CreateMatch: Destination("create-match", "type") {
        const val TYPE = "type"
        operator fun invoke(type: String): String = route.appendParams(
            TYPE to type
        )
    }

    object ScoreBoard: Destination("scoreboard", "type") {
        const val TYPE = "type"
        const val PLAYERS = "players"
        operator fun invoke(type: String, players: String): String = route.appendParams(
            TYPE to type
        ).plus("?$PLAYERS=$players")

    }

    object EditPlayers: Destination("edit-players", "type") {
        const val TYPE = "type"
        const val PLAYERS = "players"
        operator fun invoke(type: String, players: String): String = route.appendParams(TYPE to type)
            .plus("?$PLAYERS=$players")

    }

}

@Composable
fun BoardMintonNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = Home.fullRoute) {
        composable(Home.fullRoute) {
            HomeScreen {
                navController.navigate(CreateMatch.invoke(it))
            }
        }

        composable(
            route = CreateMatch.fullRoute,
            arguments = listOf(navArgument(CreateMatch.TYPE) { defaultValue = "single" })
        ) {
            val isSingle = it.arguments?.getString(CreateMatch.TYPE) ?: "single"
            CreateMatchScreen(
                single = isSingle == "single",
                toScoreBoard = { matchType, players ->
                    printLog("${CreateMatch.fullRoute} type $matchType pl $players")
                    navController.popBackStack()
                    navController.navigate(ScoreBoard.invoke(matchType, players))
                },
                onBackPressed = navController::navigateUp
            )
        }

        composable(
            route = ScoreBoard.fullRoute.plus("?players={players}"),
            arguments = listOf(
                navArgument(ScoreBoard.TYPE) { defaultValue = "single" },
                navArgument(ScoreBoard.PLAYERS) { defaultValue = "[]" },
            ),
        ) {
            val type = it.arguments?.getString(ScoreBoard.TYPE)
            val players = it.arguments?.getString(ScoreBoard.PLAYERS).orEmpty()
            printLog("${ScoreBoard.fullRoute} type $type pl $players")
            ScoreBoardScreen(
                single = type == "single", players = players,
                onEdit = { _, json ->
                    navController.navigate(EditPlayers.invoke(type ?: "single", json))
                },
                savedStateHandle = it.savedStateHandle
            )
        }

        composable(
            route = EditPlayers.fullRoute.plus("?players={players}"),
            arguments = listOf(
                navArgument(EditPlayers.TYPE) { defaultValue = "single" },
                navArgument(EditPlayers.PLAYERS) { defaultValue = "[]" },
            )
        ) {
            val type = it.arguments?.getString(EditPlayers.TYPE)
            val players = it.arguments?.getString(EditPlayers.PLAYERS).orEmpty()
            val isSingle = type == "single"
            printLog("${EditPlayers.fullRoute} type $type players $players")
            EditPlayersScreen(
                single = isSingle,
                players = players,
                onApply = { result ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(EditPlayers.PLAYERS, result)
                    navController.popBackStack()
                },
                onCancel = navController::navigateUp
            )
        }
    }
}

private fun printLog(msg: String) = println("MyNavigation $msg")

// todo example of nested navigation graphs
fun NavGraphBuilder.scoreBoardGraph(navController: NavController) {
    navigation(startDestination = "username", route = "login") {
        composable("username") {

        }
        composable("password") {

        }
        composable("registration") {

        }
    }
}
