package com.imams.boardminton.ui.screen.create

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.R
import com.imams.boardminton.data.toJson
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.domain.model.ITeam
import com.imams.boardminton.ui.component.Section
import com.imams.boardminton.ui.component.SectionSelector
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.boardminton.ui.screen.create.player.PlayerBottomSheet
import com.imams.boardminton.ui.screen.create.team.CreateTeamState
import com.imams.boardminton.ui.screen.create.team.TeamBottomSheet
import com.imams.boardminton.ui.theme.Orientation
import com.imams.boardminton.ui.utils.isNotEmptyAndSameName

@Composable
fun CreateMatchScreen(
    single: Boolean,
    toScoreBoard: (String, String) -> Unit,
    toScoreBoardWithId: ((String, Int) -> Unit) ? = null,
    onBackPressed: () -> Unit,
    vm: CreateMatchVM = hiltViewModel(),
) {
    var singleMatch by rememberSaveable { mutableStateOf(single) }
    val playerA1 by rememberSaveable { vm.playerA1 }
    val playerA2 by rememberSaveable { vm.playerA2 }
    val playerB1 by rememberSaveable { vm.playerB1 }
    val playerB2 by rememberSaveable { vm.playerB2 }
    val umpire by rememberSaveable { vm.umpire }
    val enableNext by rememberSaveable(singleMatch, playerA1, playerA2, playerB1, playerB2) {
        mutableStateOf(
            if (singleMatch) isNotEmptyAndSameName(playerA1, playerB1)
            else isNotEmptyAndSameName(playerA1, playerA2, playerB1, playerB2)
        )
    }
    val enableClear by rememberSaveable(singleMatch, playerA1, playerA2, playerB1, playerB2) {
        mutableStateOf(
            if (singleMatch) playerA1.isNotEmpty() || playerB1.isNotEmpty()
            else playerA1.isNotEmpty() || playerA2.isNotEmpty() || playerB1.isNotEmpty() || playerB2.isNotEmpty())
    }

    var openOptionalPlayer by rememberSaveable { mutableStateOf(false) }
    var selectedPlayerForImport by remember { mutableStateOf(ITeam.A1) }
    val savedPlayers by vm.savePlayers.collectAsState()

    var openOptionalTeam by rememberSaveable { mutableStateOf(false) }
    var selectedFieldForTeam by remember { mutableStateOf(ISide.A) }
    val savedTeams by vm.saveTeams.collectAsState()

    val config = LocalConfiguration.current

    fun gotoScoreBoard() {
        vm.saveInputPlayer(
            singleMatch,
            callback = {t, id ->
                if (id > 0) toScoreBoardWithId?.invoke(t, id)
                else toScoreBoard.invoke(
                    if (singleMatch) "single" else "double",
                    listOf(playerA1, playerA2, playerB1, playerB2).toJson()
                )
            }
        )
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
    fun topView(orientation: Orientation) {
        TopView(single = singleMatch, orientation = orientation, onChange = { singleMatch = it })
    }

    @Composable
    fun umpireField(orientation: Orientation) {
        FieldInputUmpire(
            modifier = if (orientation == Orientation.Portrait) ipModifierP.padding(top = 4.dp)
            else ipModifierL.padding(top = 4.dp),
            umpireName = umpire, onUmpire = vm::umpireName
        )
    }

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
                    selectedPlayerForImport = it
                    openOptionalPlayer = true
                },
                umpireField = { umpireField(orientation) }
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
                    selectedPlayerForImport = it
                    openOptionalPlayer = true
                },
                importTeam = {
                    selectedFieldForTeam = it
                    openOptionalTeam = true
                },
                umpireView = {umpireField(orientation)}
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
                topView = { topView(orientation = Orientation.Portrait) },
                formField = { formView(mPortrait, orientation = Orientation.Portrait) },
                bottomView = { bottomView(orientation = Orientation.Portrait) }
            )
        }
        else -> {
            LandscapeContent(
                topView = { topView(orientation = Orientation.Landscape) },
                formField = { formView(mLandscape, orientation = Orientation.Landscape) },
                bottomView = { bottomView(orientation = Orientation.Landscape) }
            )
        }
    }

    // show Dialog optional players from database to choose
    ImportPlayers(
        openBottomSheet = openOptionalPlayer,
        onField = selectedPlayerForImport,
        dismiss = { openOptionalPlayer = false },
        optionalPlayers = savedPlayers,
        onChoose = { i, data ->
            vm.updatePlayerName(i, data)
            openOptionalPlayer = false
        },
    )
    ImportTeams(
        openBottomSheet = openOptionalTeam,
        onField = selectedFieldForTeam,
        dismiss = { openOptionalTeam = false },
        optionalTeams = savedTeams, onChoose = { i, data ->
            vm.updatePlayerName(i, data)
            openOptionalTeam = false
        }
    )

}

@Composable
private fun TopView(
    single: Boolean = true,
    orientation: Orientation,
    onChange: (Boolean) -> Unit,
) {
    @Composable
    fun component() {
        val typeLbl = if (single) stringResource(R.string.label_single)
        else stringResource(R.string.label_double)
        Text(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 12.dp),
            text = stringResource(R.string.label_create_format_match, typeLbl),
            fontSize = 20.sp
        )
        val selectionTitles = listOf(
            Section("Single", stringResource(R.string.label_single)),
            Section("Double", stringResource(R.string.label_double))
        )
        var currentSelection by remember(single) {
            mutableStateOf(if (single) selectionTitles[0] else selectionTitles[1])
        }
        SectionSelector(
            modifier = Modifier
                .wrapContentWidth()
                .height(50.dp)
                .widthIn(max = 250.dp)
                .padding(5.dp),
            sections = selectionTitles,
            selection = currentSelection,
            onClick = { selection ->
                if (selection != currentSelection) {
                    currentSelection = selection
                    if (selection.id.equals("Single", true)) onChange.invoke(true)
                    else onChange.invoke(false)
                }
            }
        )
    }
    if (orientation == Orientation.Landscape) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) { component() }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 5.dp, horizontal = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) { component() }
    }
}

@Composable
private fun PortraitContent(
    topView: @Composable () -> Unit,
    formField: @Composable () -> Unit,
    bottomView: @Composable () -> Unit,
) {
    Scaffold(
        topBar = { topView() },
        bottomBar = { bottomView() }
    ) { p -> Surface(modifier = Modifier.padding(p)) { formField() } }
}

@Composable
private fun LandscapeContent(
    topView: @Composable () -> Unit,
    formField: @Composable () -> Unit,
    bottomView: @Composable () -> Unit,
) {
    Scaffold(
        topBar = { topView() },
        bottomBar = { bottomView() }
    ) { p -> Surface(modifier = Modifier.padding(p)) { formField() } }
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
            Text(text = stringResource(R.string.label_back))
        }

        OutlinedButton(
            modifier = Modifier.padding(end = 5.dp),
            onClick = { listener.onClear() }, enabled = enableClear
        ) { Icon(Icons.Outlined.Delete, contentDescription = "clear_icon") }

        OutlinedButton(
            onClick = { listener.onRandom() },
        ) { Icon(Icons.Outlined.Star, contentDescription = "star_icon") }

        Button(
            onClick = { listener.onNext() },
            modifier = Modifier.padding(start = 5.dp),
            enabled = enableNext
        ) {
            Text(text = stringResource(R.string.label_next))
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
    if (openBottomSheet) {
        PlayerBottomSheet(
            onDismissRequest = { dismiss.invoke() },
            list = optionalPlayers,
            onSelect = { onChoose.invoke(onField, it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImportTeams(
    openBottomSheet: Boolean,
    onField: ISide,
    optionalTeams: List<CreateTeamState>,
    onChoose: (ISide, CreateTeamState) -> Unit,
    dismiss: () -> Unit,
) {
    if (openBottomSheet) {
        TeamBottomSheet(
            onDismissRequest = { dismiss.invoke() },
            list = optionalTeams,
            onSelect = { onChoose.invoke(onField, it) },
        )
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