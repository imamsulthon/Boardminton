package com.imams.boardminton.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.imams.boardminton.navigation.Destination.AllMatches
import com.imams.boardminton.navigation.Destination.AllPlayers
import com.imams.boardminton.navigation.Destination.CreateMatch
import com.imams.boardminton.navigation.Destination.CreatePlayer
import com.imams.boardminton.navigation.Destination.CreateTeam
import com.imams.boardminton.navigation.Destination.EditCreatedPlayer
import com.imams.boardminton.navigation.Destination.EditPlayers
import com.imams.boardminton.navigation.Destination.Home
import com.imams.boardminton.navigation.Destination.ScoreBoard
import com.imams.boardminton.ui.screen.create.CreateMatchScreen
import com.imams.boardminton.ui.screen.create.EditPlayersScreen
import com.imams.boardminton.ui.screen.create.player.CreatePlayerScreen
import com.imams.boardminton.ui.screen.create.player.EditPlayerCreatedScreen
import com.imams.boardminton.ui.screen.create.team.CreateTeamScreen
import com.imams.boardminton.ui.screen.home.HomeScreen
import com.imams.boardminton.ui.screen.home.HomeScreenVM
import com.imams.boardminton.ui.screen.matches.AllMatchesScreen
import com.imams.boardminton.ui.screen.player.PlayerAndTeamsList
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
        const val ID = "id"
        operator fun invoke(type: String, players: String): String = route.appendParams(
            TYPE to type
        ).plus("?$PLAYERS=$players")

        operator fun invoke(type: String, id: Int): String = route.appendParams(
            TYPE to type
        ).plus("?$ID=$id")

        val routeInitMatch = fullRoute.plus("?players={players}")
        val routeMatch = fullRoute.plus("?id={id}")
    }

    object EditPlayers: Destination("edit-players", "type") {
        const val TYPE = "type"
        const val PLAYERS = "players"
        operator fun invoke(type: String, players: String): String = route.appendParams(TYPE to type)
            .plus("?$PLAYERS=$players")

    }

    object CreatePlayer: DestinationNoArgs("create-player")
    object CreateTeam: Destination("create-team")
    object EditCreatedPlayer: Destination("edit-create-player", "id") {
        operator fun invoke(id: Int): String = route.appendParams("id" to id)
    }

    object AllPlayers: DestinationNoArgs("registered-players")

    object AllMatches: DestinationNoArgs("all-matches")

}

@Composable
fun BoardMintonNavHost(
    viewModel: HomeScreenVM = hiltViewModel<HomeScreenVM>().apply { getLatestMatch() },
    navController: NavHostController = rememberNavController(),
) {

    val latestMatch by viewModel.onGoingMatches.collectAsState()

    NavHost(navController = navController, startDestination = Home.fullRoute) {
        composable(Home.fullRoute) {
            HomeScreen(
                onGoingMatches = latestMatch,
                onCreateMatch = { navController.navigate(CreateMatch.invoke(it)) },
                onCreatePlayer = {
                    when (it) {
                        "create" -> { navController.navigate(CreatePlayer.fullRoute) }
                        "seeAll" -> { navController.navigate(AllPlayers.fullRoute) }
                    }
                },
                gotoScoreboard = { matchType, id ->
                    navController.navigate(ScoreBoard.invoke(matchType, id))
                },
                seeAllMatch = {
                    navController.navigate(AllMatches.fullRoute)
                }
            )
        }

        composable(
            route = CreateMatch.fullRoute,
            arguments = listOf(navArgument(CreateMatch.TYPE) { defaultValue = "single" })
        ) {
            val isSingle = it.arguments?.getString(CreateMatch.TYPE) ?: "single"
            CreateMatchScreen(
                single = isSingle == "single",
                toScoreBoard = { matchType, players ->
                    navController.popBackStack()
                    navController.navigate(ScoreBoard.invoke(matchType, players))
                },
                toScoreBoardWithId = { matchType, id ->
                    navController.popBackStack()
                    navController.navigate(ScoreBoard.invoke(matchType, id))
                },
                onBackPressed = navController::navigateUp
            )
        }

        composable(
            route = ScoreBoard.routeInitMatch,
            arguments = listOf(
                navArgument(ScoreBoard.TYPE) { defaultValue = "single" },
                navArgument(ScoreBoard.PLAYERS) { defaultValue = "[]" },
            ),
        ) {
            val type = it.arguments?.getString(ScoreBoard.TYPE)
            val players = it.arguments?.getString(ScoreBoard.PLAYERS).orEmpty()
            ScoreBoardScreen(
                single = type.equals("single", true), players = players,
                onEdit = { _, json ->
                    navController.navigate(EditPlayers.invoke(type ?: "single", json))
                },
                savedStateHandle = it.savedStateHandle,
                onBackPressed = navController::popBackStack
            )
        }

        composable(
            route = ScoreBoard.routeMatch,
            arguments = listOf(
                navArgument(ScoreBoard.TYPE) { defaultValue = "single" },
                navArgument(ScoreBoard.ID) { type = NavType.IntType },
            ),
        ) {
            val type = it.arguments?.getString(ScoreBoard.TYPE)
            val id = it.arguments?.getInt(ScoreBoard.ID) ?: 0
            ScoreBoardScreen(
                id = id, single = type.equals("single", true), players = "",
                onEdit = { _, json ->
                    navController.navigate(EditPlayers.invoke(type ?: "single", json))
                },
                savedStateHandle = it.savedStateHandle,
                onBackPressed = navController::popBackStack
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

        composable(CreatePlayer.fullRoute) {
            val context = LocalContext.current
            CreatePlayerScreen(
                onSave = {
                    Toast.makeText(context, "Success save", Toast.LENGTH_LONG).show()
                }
            )
        }

        composable(CreateTeam.fullRoute) {
            val context = LocalContext.current
            CreateTeamScreen (
                onSave = {
                    Toast.makeText(context, "Success save", Toast.LENGTH_LONG).show()
                }
            )
        }

        composable(
            EditCreatedPlayer.fullRoute,
            arguments = listOf(navArgument("id") {type = NavType.IntType})
        ) {
            val context = LocalContext.current
            val id = it.arguments?.getInt("id") ?: 0
            EditPlayerCreatedScreen(
                id = id,
                onSave = {
                    Toast.makeText(context, "Success edit", Toast.LENGTH_LONG).show()
                }
            )
        }

        composable(AllPlayers.fullRoute) {
            PlayerAndTeamsList(
                addNewPlayer = { navController.navigate(CreatePlayer.fullRoute) },
                onEditPlayer = { navController.navigate(EditCreatedPlayer.invoke(it)) },
                onEditTeam = { },
                addNewTeam = { navController.navigate(CreateTeam.fullRoute) }
            )
        }

        composable(AllMatches.fullRoute) {
            AllMatchesScreen(
                onSelect = { matchType, id ->
                    navController.navigate(ScoreBoard.invoke(matchType, id))
                }
            )
        }
    }
}

private fun printLog(msg: String) {
    println("MyNavigation $msg")
}

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
