package com.imams.boardminton.ui.screen.create

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.ui.screen.toScoreBoard
import com.imams.boardminton.ui.viewmodel.CreateMatchVM
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun EditPlayersScreen(
    single: Boolean,
    a1: String,
    a2: String = "",
    b1: String,
    b2: String = "",
    navigator: DestinationsNavigator?,
    vm: CreateMatchVM = hiltViewModel<CreateMatchVM>().apply {
        setupPlayers(a1, a2, b1, b2)
    },
) {
    var singleMatch by rememberSaveable { mutableStateOf(single) }
    val playerA1 by rememberSaveable { vm.playerA1 }
    val playerA2 by rememberSaveable { vm.playerA2 }
    val playerB1 by rememberSaveable { vm.playerB1 }
    val playerB2 by rememberSaveable { vm.playerB2 }

    val config = LocalConfiguration.current

    @Composable
    fun singleMatchView(modifier: Modifier) = FieldInputSingleMatch(
        modifier = modifier,
        pA1 = playerA1,
        pB1 = playerB1,
        onChangeA1 = { vm.setA1(it) },
        onChangeB1 = { vm.setB1(it) },
        onSwap = { vm.swapSingleMatch() },
        importPerson = {

        }
    )

    @Composable
    fun doubleMatchView(
        modifier: Modifier = Modifier,
    ) = FieldInputDoubleMatch(
        modifier = modifier,
        vArrangement = Arrangement.Top,
        pA1 = playerA1, pA2 = playerA2,
        pB1 = playerB1, pB2 = playerB2,
        onChangeA1 = { vm.setA1(it) }, onChangeA2 = { vm.setA2(it) },
        onChangeB1 = { vm.setB1(it) }, onChangeB2 = { vm.setB2(it) },
        swapA = { vm.swapTeamA() }, swapB = { vm.swapTeamB() },
        swapTeam = { vm.swapDoubleMatch() },
        importPerson = {
            // todo
        }
    )

    @Composable
    fun topView() = TopView(onApply = {
        if (it) {
            val params = if (singleMatch) listOf(playerA1, playerB1)
            else listOf(playerA1, playerA2, playerB1, playerB2)
            navigator?.popBackStack()
            navigator?.toScoreBoard(params, singleMatch, true)
        } else {
            navigator?.navigateUp()
        }
    })

    @Composable
    fun formView(modifier: Modifier = Modifier): Unit =
        if (singleMatch) singleMatchView(modifier = modifier)
        else doubleMatchView(modifier = modifier)

    when (config.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PortraitContent(
                topView = { topView() },
                formField = { formView(mPortrait) }
            )
        }
        else -> {
            LandscapeContent(
                topView = { topView() },
                formField = { formView(mLandscape) }
            )
        }
    }

}


@Composable
private fun TopView(
    single: Boolean = true,
    onApply: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 5.dp),
            text = "Edit ${if (single) "Single" else "Double"} match",
            fontSize = 16.sp
        )
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onApply.invoke(false) },
                modifier = Modifier.padding(horizontal = 5.dp),
            ) {
                Text(text = "Cancel")
            }
            Button(
                onClick = { onApply.invoke(true) },
                modifier = Modifier.padding(horizontal = 5.dp),
            ) {
                Text(text = "Apply")
            }
        }

    }
}

@Composable
private fun PortraitContent(
    topView: @Composable () -> Unit,
    formField: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            topView()
            formField()
        }
    }

}

@Composable
private fun LandscapeContent(
    topView: @Composable () -> Unit,
    formField: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        formField()
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            topView()
        }
    }

}
