package com.imams.boardminton.ui.screen.create

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.ui.screen.toScoreBoard
import com.imams.boardminton.ui.theme.Orientation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun CreateMatchScreen(
    single: Boolean = true,
    navigator: DestinationsNavigator?,
    vm: CreateMatchVM = hiltViewModel(),
) {
    var singleMatch by rememberSaveable { mutableStateOf(single) }
    val playerA1 by rememberSaveable { vm.playerA1 }
    val playerA2 by rememberSaveable { vm.playerA2 }
    val playerB1 by rememberSaveable { vm.playerB1 }
    val playerB2 by rememberSaveable { vm.playerB2 }

    val config = LocalConfiguration.current

    fun gotoScoreBoard() {
        val params = if (singleMatch) listOf(playerA1, playerB1)
        else listOf(playerA1, playerA2, playerB1, playerB2)
        navigator?.popBackStack()
        navigator?.toScoreBoard(params, singleMatch)
    }

    @Composable
    fun topView() = TopView(single = singleMatch, onChange = { singleMatch = it })

    @Composable
    fun formView(
        modifier: Modifier = Modifier,
        orientation: Orientation = Orientation.Portrait,
    ) {
        if (singleMatch) {
            FieldInputSingleMatch(
                modifier = modifier,
                pA1 = playerA1,
                pB1 = playerB1,
                orientation = orientation,
                onChange = vm::updatePlayerName,
                onSwap = { vm.swapSingleMatch() },
                importPerson = {

                }
            )
        } else {
            FieldInputDoubleMatch(
                modifier = modifier,
                pA1 = playerA1, pA2 = playerA2,
                pB1 = playerB1, pB2 = playerB2,
                orientation = orientation,
                onChange = vm::updatePlayerName,
                swapA = { vm.swapTeamA() }, swapB = { vm.swapTeamB() },
                swapTeam = { vm.swapDoubleMatch() },
                importPerson = {

                }
            )
        }
    }
    when (config.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PortraitContent(
                topView = { topView() },
                onNext = { gotoScoreBoard() },
                onBack = { navigator?.navigateUp() },
                onClear = { vm.onClearPlayers() },
                formField = { formView(mPortrait, orientation = Orientation.Portrait) }
            )
        }
        else -> {
            LandscapeContent(
                topView = { topView() },
                onNext = { gotoScoreBoard() },
                onBack = { navigator?.navigateUp() },
                onClear = { vm.onClearPlayers() },
                formField = { formView(mLandscape, orientation = Orientation.Landscape) }
            )
        }
    }

}

@Composable
private fun TopView(
    single: Boolean = true,
    onChange: (Boolean) -> Unit,
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
            text = "Create ${if (single) "Single" else "Double"} match",
            fontSize = 16.sp
        )

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onChange.invoke(true) },
                modifier = Modifier.padding(horizontal = 5.dp),
                enabled = !single
            ) {
                Text(text = "Single")
            }
            Button(
                onClick = { onChange.invoke(false) },
                modifier = Modifier.padding(horizontal = 5.dp),
                enabled = single
            ) {
                Text(text = "Double")
            }
        }

    }
}

@Composable
private fun PortraitContent(
    onNext: () -> Unit,
    onBack: () -> Unit,
    onClear: () -> Unit,
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
        BottomButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
                .align(Alignment.End),
            onBackPressed = { onBack.invoke() },
            onNext = { onNext.invoke() },
            onClear = { onClear.invoke() }
        )
    }

}

@Composable
private fun LandscapeContent(
    onNext: () -> Unit,
    onBack: () -> Unit,
    onClear: () -> Unit,
    topView: @Composable () -> Unit,
    formField: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        topView()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            formField()
        }
        BottomButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
                .align(Alignment.End),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End,
            onBackPressed = { onBack.invoke() },
            onNext = { onNext.invoke() },
            onClear = { onClear.invoke() }
        )
    }

}

@Composable
private fun BottomButton(
    modifier: Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    onNext: () -> Unit,
    onBackPressed: () -> Unit,
    onClear: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {

        OutlinedButton(
            onClick = { onBackPressed.invoke() },
            modifier = Modifier.padding(end = 5.dp)
        ) {
            Text(text = "Back")
        }

        OutlinedButton(onClick = { onClear.invoke() }) {
            Icon(Icons.Outlined.Delete, contentDescription = "clear_icon")
        }

        Button(onClick = { onNext.invoke() }, modifier = Modifier.padding(start = 5.dp)) {
            Text(text = "Next")
        }
    }
}

private fun printLog(msg: String) = println("CreateMatch: msg -> $msg")

@Preview("Create Match")
@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 512)
@Composable
fun CreateMatchPreview() {
    CreateMatchScreen(single = false, navigator = null)
}

@Preview
@Preview(device = Devices.PIXEL_4_XL, widthDp = 768, heightDp = 470)
@Composable
fun CreateMatchPreview2() {
    CreateMatchScreen(single = false, navigator = null)
}