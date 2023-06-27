package com.imams.boardminton.ui.screen.create

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.data.toJson
import com.imams.boardminton.domain.model.ITeam
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.boardminton.ui.screen.create.player.PlayerBottomSheetContent
import com.imams.boardminton.ui.theme.Orientation

@Composable
fun CreateMatchScreen(
    single: Boolean,
    toScoreBoard: (String, String) -> Unit,
    onBackPressed: () -> Unit,
    vm: CreateMatchVM = hiltViewModel(),
) {
    var singleMatch by rememberSaveable { mutableStateOf(single) }
    val playerA1 by rememberSaveable { vm.playerA1 }
    val playerA2 by rememberSaveable { vm.playerA2 }
    val playerB1 by rememberSaveable { vm.playerB1 }
    val playerB2 by rememberSaveable { vm.playerB2 }
    val enableNext by rememberSaveable(singleMatch, playerA1, playerA2, playerB1, playerB2) {
        mutableStateOf(
            if (singleMatch) playerA1.isNotEmpty() && playerB1.isNotEmpty()
            else playerA1.isNotEmpty() && playerA2.isNotEmpty() && playerB1.isNotEmpty() && playerB2.isNotEmpty()
        )
    }
    val enableClear by rememberSaveable(singleMatch, playerA1, playerA2, playerB1, playerB2) {
        mutableStateOf(playerA1.isNotEmpty() || playerA2.isNotEmpty() || playerB1.isNotEmpty() || playerB2.isNotEmpty())
    }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var selectedFieldForImport by remember { mutableStateOf(ITeam.A1) }
    val savedPlayers by vm.savePlayers.collectAsState()

    val config = LocalConfiguration.current

    fun gotoScoreBoard() {
        toScoreBoard.invoke(if (singleMatch) "single" else "double", listOf(playerA1, playerA2, playerB1, playerB2).toJson())
    }

    val bottomListener = object : BottomListener {
        override fun onNext() = gotoScoreBoard()

        override fun onBackPressed() {
            onBackPressed.invoke()
        }

        override fun onClear() = vm.onClearPlayers()

        override fun onRandom() {
            vm.randomPlayers(singleMatch)
        }

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
                swapPlayer = vm::swapSingleMatch,
                importPerson = {
                    selectedFieldForImport = it
                    openBottomSheet = true                }
            )
        } else {
            FieldInputDoubleMatch(
                modifier = modifier,
                pA1 = playerA1, pA2 = playerA2,
                pB1 = playerB1, pB2 = playerB2,
                orientation = orientation,
                onChange = vm::updatePlayerName,
                swapPlayer = vm::swapPlayerByTeam,
                swapTeam = { vm.swapDoubleMatch() },
                importPerson = {
                    selectedFieldForImport = it
                    openBottomSheet = true
                }
            )
        }
    }

    @Composable
    fun bottomView(orientation: Orientation) = BottomButton(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        horizontalArrangement = if (orientation == Orientation.Landscape) Arrangement.End
        else Arrangement.SpaceBetween,
        enableClear = enableClear,
        enableNext = enableNext,
        listener = bottomListener
    )

    when (config.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PortraitContent(
                topView = { topView() },
                formField = { formView(mPortrait, orientation = Orientation.Portrait) },
                bottomView = { bottomView(orientation = Orientation.Portrait) }
            )
        }
        else -> {
            LandscapeContent(
                topView = { topView() },
                formField = { formView(mLandscape, orientation = Orientation.Landscape) },
                bottomView = { bottomView(orientation = Orientation.Landscape) }
            )
        }
    }

    // show Dialog optional players from database to choose
    ImportPlayers(
        openBottomSheet = openBottomSheet,
        onField = selectedFieldForImport,
        dismiss = { openBottomSheet = false },
        optionalPlayers = savedPlayers,
        onChoose = { i, data ->
            vm.updatePlayerName(i, data)
            openBottomSheet = false
        },
    )

    BackHandler(true) {
        println("CreateMatch onBackHandler")
        vm.saveInputPlayer(singleMatch)
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
    topView: @Composable () -> Unit,
    formField: @Composable () -> Unit,
    bottomView: @Composable () -> Unit,
) {
    Surface {
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
            bottomView()
        }
    }

}

@Composable
private fun LandscapeContent(
    topView: @Composable () -> Unit,
    formField: @Composable () -> Unit,
    bottomView: @Composable () -> Unit,
) {
    Surface {
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
            bottomView()
        }
    }

}

@Composable
private fun BottomButton(
    modifier: Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    listener: BottomListener,
    enableClear: Boolean = true,
    enableNext: Boolean = true,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {

        OutlinedButton(
            onClick = { listener.onBackPressed() },
            modifier = Modifier.padding(end = 5.dp)
        ) {
            Text(text = "Back")
        }

        OutlinedButton(onClick = { listener.onClear() }, enabled = enableClear) {
            Icon(Icons.Outlined.Delete, contentDescription = "clear_icon")
        }

        OutlinedButton(onClick = { listener.onRandom() }) {
            Icon(Icons.Outlined.Star, contentDescription = "star_icon")
        }

        Button(
            onClick = { listener.onNext() },
            modifier = Modifier.padding(start = 5.dp),
            enabled = enableNext
        ) {
            Text(text = "Next")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImportPlayers(
    openBottomSheet: Boolean,
    onField: ITeam,
    optionalPlayers: List<CreatePlayerState>,
    onChoose: (ITeam, CreatePlayerState) -> Unit,
    dismiss: () -> Unit,
) {
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { dismiss.invoke() },
            sheetState = bottomSheetState,
        ) {
            PlayerBottomSheetContent(list = optionalPlayers, onSelect = {
                onChoose.invoke(onField, it)
            })
        }
    }
}

private interface BottomListener {
    fun onNext()
    fun onBackPressed()
    fun onClear()
    fun onRandom()
}

@Preview(showBackground = true)
@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 512)
@Composable
fun CreateMatchPreview() {
    CreateMatchScreen(single = false, toScoreBoard = { _, _ -> }, onBackPressed = {})
}

@Preview(showSystemUi = true)
@Preview(device = Devices.PIXEL_4_XL, widthDp = 768, heightDp = 470)
@Composable
fun CreateMatchPreview2() {
    CreateMatchScreen(single = false, toScoreBoard = { _, _ -> }, onBackPressed = {})
}