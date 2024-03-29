package com.imams.boardminton.ui.screen.create.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.ui.screen.create.InputPlayer
import com.imams.boardminton.ui.screen.create.SwapButton
import com.imams.boardminton.ui.screen.create.ipModifierP
import com.imams.boardminton.ui.screen.create.player.BottomView
import com.imams.boardminton.ui.screen.create.player.PlayerBottomSheet
import com.imams.boardminton.ui.screen.create.player.TopView
import com.imams.boardminton.ui.utils.isNotEmptyAndSameName
import com.imams.boardminton.ui.utils.keyBoardDone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamScreen(
    viewModel: CreateTeamVM = hiltViewModel(),
    onSave: () -> Unit,
    onCreatePlayer: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var openSavedTeams by rememberSaveable { mutableStateOf(false) }
    val savedTeams by viewModel.saveTeams.collectAsState()
    var openPlayerOptional by rememberSaveable { mutableStateOf(false) }
    val savedPlayers by viewModel.savePlayers.collectAsState()
    var selectedField by remember { mutableIntStateOf(1) }

    CreateTeamContent(
        uiState = uiState,
        event = viewModel::execute,
        onSave = { viewModel.saveTeams(onSave) },
        import1 = {
            openPlayerOptional = true
            selectedField = 1
        },
        import2 = {
            openPlayerOptional = true
            selectedField = 2
        },
        onCheckSaveTeams = { openSavedTeams = true },
        onCreatePlayer = onCreatePlayer::invoke
    )

    // Sheet content
    if (openSavedTeams) {
        TeamBottomSheet(onDismissRequest = { openSavedTeams = false }, list = savedTeams)
    }
    // sheet saved teams
    if (openPlayerOptional) {
        PlayerBottomSheet(onDismissRequest = { openPlayerOptional = false }, list = savedPlayers,
            onSelect = {
                when (selectedField) {
                    1 -> viewModel.execute(CreateTeamEvent.Player1(it))
                    2 -> viewModel.execute(CreateTeamEvent.Player2(it))
                }
                openPlayerOptional = false
            }
        )
    }
}

@Composable
internal fun CreateTeamContent(
    uiState: CreateTeamState,
    event: (CreateTeamEvent) -> Unit,
    onSave: () -> Unit,
    import1: () -> Unit,
    import2: () -> Unit,
    onCreatePlayer: () -> Unit,
    onCheckSaveTeams: (() -> Unit)? = null,
) {
    val enableSave by rememberSaveable(uiState) {
        mutableStateOf(isNotEmptyAndSameName(uiState.playerName1, uiState.playerName2))
    }
    val enableClear by rememberSaveable(uiState) {
        mutableStateOf(uiState.playerName1.isNotEmpty() || uiState.playerName2.isNotEmpty())
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 840.dp),
        topBar = {
            TopView(title = "Create Team", onClick = { onCheckSaveTeams?.invoke() })
        },
        bottomBar = {
            BottomView(
                enableClear = enableClear, enableSave = enableSave,
                onClear = { event.invoke(CreateTeamEvent.Clear) },
                onSave = { onSave.invoke() }
            )
        }
    ) { p ->
        FormContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(p),
            player1 = uiState.playerName1,
            player2 = uiState.playerName2,
            swap = { event.invoke(CreateTeamEvent.Swap) },
            import1 = { import1.invoke() },
            import2 = { import2.invoke() },
            onCreatePlayer = onCreatePlayer::invoke
        )
    }
}

@Composable
internal fun FormContent(
    modifier: Modifier,
    player1: String,
    player2: String,
    swap: () -> Unit,
    import1: () -> Unit,
    import2: () -> Unit,
    onCreatePlayer: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        InputPlayer(
            modifier = ipModifierP,
            value = player1,
            onValueChange = {},
            enabled = false,
            label = "Player 1 Name",
            endIconClick = import1::invoke
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "And", modifier = Modifier.padding(vertical = 10.dp))
            SwapButton({ swap.invoke() })
        }
        InputPlayer(
            modifier = ipModifierP,
            value = player2,
            onValueChange = {},
            enabled = false,
            label = "Player 2 Name",
            keyboardOptions = keyBoardDone(),
            endIconClick = import2::invoke
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Text(text = "No Player Found?")
        Spacer(modifier = Modifier.padding(vertical = 5.dp))
        OutlinedButton(onClick = onCreatePlayer::invoke) {
            Text(text = "Create Player")
        }

    }
}