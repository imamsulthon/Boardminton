package com.imams.boardminton.ui.screen.create

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.data.toJson
import com.imams.boardminton.ui.theme.Orientation

@Composable
fun EditPlayersScreen(
    single: Boolean,
    players: String,
    onApply: (String) -> Unit,
    onCancel: () -> Unit,
    vm: CreateMatchVM = hiltViewModel<CreateMatchVM>().apply {
        setupPlayers(single, players)
    },
) {
    val playerA1 by rememberSaveable { vm.playerA1 }
    val playerA2 by rememberSaveable { vm.playerA2 }
    val playerB1 by rememberSaveable { vm.playerB1 }
    val playerB2 by rememberSaveable { vm.playerB2 }

    val config = LocalConfiguration.current

    @Composable
    fun singleMatchView(modifier: Modifier, orientation: Orientation) = FieldInputSingleMatch(
        modifier = modifier,
        pA1 = playerA1,
        pB1 = playerB1,
        orientation = orientation,
        onChange = vm::updatePlayerName,
        swapPlayer = vm::swapSingleMatch,
        importPerson = {}
    )

    @Composable
    fun doubleMatchView(modifier: Modifier = Modifier, orientation: Orientation) =
        FieldInputDoubleMatch(
            modifier = modifier,
            pA1 = playerA1, pA2 = playerA2,
            pB1 = playerB1, pB2 = playerB2,
            orientation = orientation,
            onChange = vm::updatePlayerName,
            swapPlayer = vm::swapPlayerByTeam,
            swapTeam = vm::swapDoubleMatch,
            importPerson = {}
        )

    @Composable
    fun topView() = TopView(onApply = {
        if (it) {
            val params = if (single) listOf(playerA1, playerB1)
            else listOf(playerA1, playerA2, playerB1, playerB2)
            onApply.invoke(params.toJson())
        } else {
            onCancel.invoke()
        }
    })

    @Composable
    fun formView(
        modifier: Modifier = Modifier,
        orientation: Orientation = Orientation.Portrait
    ): Unit =
        if (single) singleMatchView(modifier = modifier, orientation)
        else doubleMatchView(modifier = modifier, orientation)

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
                formField = { formView(mLandscape, Orientation.Landscape) }
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        topView()
        formField()
    }

}

@Preview(showSystemUi = true)
@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 512, showSystemUi = true)
@Composable
private fun LandscapePrev() {

}