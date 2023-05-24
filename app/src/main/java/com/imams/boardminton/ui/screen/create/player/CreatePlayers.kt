package com.imams.boardminton.ui.screen.create.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.ui.screen.create.ipModifierP
import com.imams.boardminton.ui.utils.keyboardNext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlayerScreen(
    viewModel: CreatePlayerVM = hiltViewModel(),
    onSave: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val savedPlayers by viewModel.savePlayers.collectAsState()

    CreatePlayerContent(
        uiState = uiState,
        event = { viewModel.execute(it) },
        onSave = { viewModel.savePlayer(callback = onSave) },
        onCheckSavePlayers = { openBottomSheet = true }
    )

    // region check dialog
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    // Sheet content
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
        ) {
            PlayerBottomSheetContent(list = savedPlayers)
        }
    }

}

@Composable
internal fun CreatePlayerContent(
    uiState: CreatePlayerState,
    event: (CreatePlayerEvent) -> Unit,
    onSave: () -> Unit,
    onCheckSavePlayers: () -> Unit,
) {
    val enableSave by rememberSaveable(uiState) { mutableStateOf(uiState.firstName.isNotEmpty()) }
    val enableClear by rememberSaveable(uiState) {
        mutableStateOf(uiState.firstName.isNotEmpty() || uiState.lastName.isNotEmpty())
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 840.dp),
        topBar = {
            TopView(onClick = { onCheckSavePlayers.invoke() })
        },
        bottomBar = {
            BottomView(
                enableClear = enableClear, enableSave = enableSave,
                onClear = { event.invoke(CreatePlayerEvent.Clear) },
                onSave = { onSave.invoke() }
            )
        }
    ) { p ->
        FormContent(
            Modifier
                .fillMaxWidth()
                .padding(p),
            data = uiState,
            onFirstName = { event.invoke(CreatePlayerEvent.FirstName(it)) },
            onLastName = { event.invoke(CreatePlayerEvent.LastName(it)) },
            onHeight = { event.invoke(CreatePlayerEvent.Height(it)) },
            onWeight = { event.invoke(CreatePlayerEvent.Weight(it)) },
            onHandPlay = { event.invoke(CreatePlayerEvent.HandPlay(it)) },
            onGender = { event.invoke(CreatePlayerEvent.Gender(it)) }
        )
    }
}

@Composable
internal fun FormContent(
    modifier: Modifier,
    data: CreatePlayerState = CreatePlayerState(),
    onFirstName: (String) -> Unit,
    onLastName: (String) -> Unit,
    onHandPlay: (String) -> Unit,
    onWeight: (Int) -> Unit,
    onHeight: (Int) -> Unit,
    onGender: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // first name
        InputField(
            modifier = ipModifierP.padding(vertical = 5.dp),
            value = data.firstName,
            label = "First Name",
            onValueChange = { onFirstName.invoke(it) },
        )
        // last name
        InputField(
            modifier = ipModifierP.padding(vertical = 5.dp),
            value = data.lastName,
            label = "Last Name",
            onValueChange = { onLastName.invoke(it) },
        )

        Row(
            modifier = ipModifierP.padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 10.dp),
                value = if (data.height == 0) "" else data.height.toString(),
                onValueChange = { onHeight.invoke(it.toInt()) },
                label = "Height",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 10.dp),
                value = if (data.weight == 0) "" else data.weight.toString(),
                onValueChange = { onWeight.invoke(it.toInt()) },
                label = "Weight",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        HandPlays(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)) { onHandPlay.invoke(it) }
        GenderField(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp), onSelected = { onGender.invoke(it) })
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun InputField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions = keyboardNext(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange.invoke(it) },
        label = { Text(text = label) },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HandPlays(
    modifier: Modifier,
    onSelected: (String) -> Unit,
) {
    val radioOptions = listOf("Left", "Right", "Both")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Hand Play: ",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 10.dp),
        )
        Row(
            Modifier
                .fillMaxWidth()
                .selectableGroup()
        ) {
            radioOptions.forEach { text ->
                InputChip(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    selected = text == selectedOption,
                    onClick = {
                        onOptionSelected(text)
                            .also { onSelected.invoke(selectedOption) }
                    },
                    label = {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = text, textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderField(
    modifier: Modifier,
    onSelected: (String) -> Unit,
) {
    val radioOptions = listOf("Man", "Woman")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Gender: ",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 10.dp),
        )
        Row(Modifier.selectableGroup()) {
            radioOptions.forEach { text ->
                InputChip(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    selected = text == selectedOption,
                    onClick = {
                        onOptionSelected(text)
                            .also { onSelected.invoke(selectedOption) }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Localized description"
                        )
                    },
                    label = {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopView(
    onClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Create Players",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            IconButton(onClick = { onClick.invoke() }, enabled = true) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
private fun BottomView(
    enableClear: Boolean = false,
    enableSave: Boolean = false,
    onClear: () -> Unit,
    onSave: () -> Unit,
) {
    Surface(tonalElevation = 2.dp, shadowElevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(onClick = { onClear.invoke() }, enabled = enableClear) {
                Text(text = "Clear")
            }
            OutlinedButton(
                enabled = enableSave, onClick = { onSave.invoke() },
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreatePlayerPreview() {
    CreatePlayerContent(CreatePlayerState(), event = {}, onSave = {}, onCheckSavePlayers = {})
}