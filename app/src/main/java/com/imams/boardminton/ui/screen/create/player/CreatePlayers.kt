package com.imams.boardminton.ui.screen.create.player

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.R
import com.imams.boardminton.data.asDateTime
import com.imams.boardminton.ui.component.country.CountryPickerBottomSheet
import com.imams.boardminton.ui.component.country.countryCodeToFlag
import com.imams.boardminton.ui.component.country.getCountryName
import com.imams.boardminton.ui.screen.create.TakePhoto
import com.imams.boardminton.ui.screen.create.ipModifierP
import com.imams.boardminton.ui.utils.contactPermissionLauncher
import com.imams.boardminton.ui.utils.hasPermissionContacts
import com.imams.boardminton.ui.utils.keyboardNext
import com.imams.boardminton.ui.utils.launchContactPermission
import com.imams.boardminton.ui.utils.pickContact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlayerScreen(
    viewModel: CreatePlayerVM = hiltViewModel(),
    onSave: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val savedPlayers by viewModel.savePlayers.collectAsState()
    val selfieState by viewModel.tempSelfieUri.collectAsState()

    CreatePlayerContent(
        screenName = stringResource(R.string.label_create_player),
        uiState = uiState,
        event = { viewModel.execute(it) },
        onSave = { viewModel.savePlayer(callback = onSave) },
        onCheckSavePlayers = { openBottomSheet = true },
        onNewSelfie = { viewModel.getNewSelfieUri() },
        selfieState = selfieState
    )

    if (openBottomSheet) {
        PlayerBottomSheet(onDismissRequest = { openBottomSheet = false }, list = savedPlayers)
    }

}

@Composable
fun EditPlayerCreatedScreen(
    id: Int,
    viewModel: CreatePlayerVM = hiltViewModel(),
    onSave: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val selfieState by viewModel.tempSelfieUri.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setupWith(id)
    }
    CreatePlayerContent(
        screenName = stringResource(R.string.label_edit_player) + " (id = ${uiState.id})",
        uiState = uiState,
        event = { viewModel.execute(it) },
        onSave = { viewModel.updatePlayer(callback = onSave) },
        onCheckSavePlayers = { },
        onNewSelfie = { viewModel.getNewSelfieUri() },
        selfieState = selfieState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreatePlayerContent(
    screenName: String,
    uiState: CreatePlayerState,
    event: (CreatePlayerEvent) -> Unit,
    onSave: () -> Unit,
    onCheckSavePlayers: (() -> Unit)? = null,
    onNewSelfie: () -> Uri,
    selfieState: SelfieFieldState?,
) {
    val enableSave by rememberSaveable(uiState) {
        mutableStateOf(uiState.firstName.isNotEmpty() && uiState.gender.isNotEmpty()
                && uiState.handPlay.isNotEmpty())
    }
    val enableClear by rememberSaveable(uiState) {
        mutableStateOf(uiState.firstName.isNotEmpty() || uiState.lastName.isNotEmpty())
    }
    val context = LocalContext.current
    var hasContactPermission by remember { mutableStateOf(context.hasPermissionContacts()) }
    val contactPermissionLauncher = contactPermissionLauncher(isPermitted = { hasContactPermission = it})
    val pickContactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact(),
        onResult = {
            it?.pickContact(context, callback = { _, number ->
                event.invoke(CreatePlayerEvent.ImportContact(number))
            })
        }
    )

    val datePickerDialog = remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()
    var openCountryPicker by rememberSaveable { mutableStateOf(false) }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 840.dp),
        topBar = {
            TopView(title = screenName, onClick = { onCheckSavePlayers?.invoke() })
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
            onDob = { datePickerDialog.value = true },
            onGender = { event.invoke(CreatePlayerEvent.Gender(it)) },
            onPickContact = {
                if (hasContactPermission) pickContactLauncher.launch()
                else contactPermissionLauncher.launchContactPermission()
            },
            onCountry = { openCountryPicker = true },
            takeSelfie = {
                TakePhoto(
                    modifier = Modifier.fillMaxWidth(),
                    imageUri = selfieState?.uri,
                    fileName = selfieState?.fileName,
                    getNewImageUri = { onNewSelfie.invoke() },
                    onPhotoTaken = { event.invoke(CreatePlayerEvent.GenerateSelfie(it)) },
                )
            }
        )
    }
    if (datePickerDialog.value) {
        MyDatePicker(
            datePickerState = datePickerState,
            onDismiss = { datePickerDialog.value = false },
            onConfirm = { it?.let { event.invoke(CreatePlayerEvent.DOB(it)) } },
            onState = { state -> datePickerState = state}
        )
    }
    if (openCountryPicker) {
        CountryPickerBottomSheet(
            onItemSelected = {
                event.invoke(CreatePlayerEvent.Nationality(it.code))
                openCountryPicker = false
            }, onDismissRequest = {
                openCountryPicker = false
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyDatePicker(
    datePickerState: DatePickerState = rememberDatePickerState(),
    confirmEnabled: State<Boolean> = remember { derivedStateOf { datePickerState.selectedDateMillis != null} },
    onDismiss: () -> Unit,
    onConfirm: (Long?) -> Unit,
    onState: ((DatePickerState) -> Unit)? = null,
) {
    DatePickerDialog(
        onDismissRequest = { onDismiss.invoke() },
        confirmButton = {
            TextButton(
                onClick = {
                    val value = datePickerState.selectedDateMillis
                    onState?.invoke(datePickerState)
                    onConfirm.invoke(value)
                    onDismiss.invoke()
                },
                enabled = confirmEnabled.value
            ) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss.invoke() }) { Text(stringResource(R.string.label_cancel)) }
        }
    ) { DatePicker(state = datePickerState) }
}

@Composable
private fun FormContent(
    modifier: Modifier,
    data: CreatePlayerState = CreatePlayerState(),
    onFirstName: (String) -> Unit,
    onLastName: (String) -> Unit,
    onHandPlay: (String) -> Unit,
    onWeight: (Int) -> Unit,
    onHeight: (Int) -> Unit,
    onDob: (Long) -> Unit,
    onPickContact: () -> Unit,
    onCountry: () -> Unit,
    onGender: (String) -> Unit,
    takeSelfie: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = ipModifierP.padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 10.dp),
                value = data.firstName,
                label = stringResource(R.string.first_name),
                onValueChange = { onFirstName.invoke(it) },
            )
            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 10.dp),
                value = data.lastName,
                label = stringResource(R.string.last_name),
                onValueChange = { onLastName.invoke(it) },
            )
        }

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
                onValueChange = {
                    onHeight.invoke(if (it.isEmpty()) 0 else if (it.length <= 3) it.toInt() else 250)
                },
                suffix = { Text(text = "cm") },
                label = stringResource(R.string.height),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 10.dp),
                value = if (data.weight == 0) "" else data.weight.toString(),
                onValueChange = {
                    onWeight.invoke(if (it.isEmpty()) 0 else if (it.length <= 3) it.toInt() else 200)
                },
                suffix = { Text(text = "kg") },
                label = stringResource(R.string.weight),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            value = data.dob.toString().asDateTime("dd MMM yyyy") ?: data.dob.toString(),
            label = { Text(text = stringResource(R.string.label_dob)) },
            onValueChange = {},
            trailingIcon = { IconButton(onClick = { onDob.invoke(data.dob) }) {
                Icon(Icons.Outlined.DateRange, contentDescription = "icon_import_date")
            } },
            enabled = false,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            label = { Text(text = stringResource(R.string.label_phone_number)) },
            value = data.phoneNumber,
            onValueChange = {},
            trailingIcon = { IconButton(onClick = { onPickContact.invoke() }) {
                Icon(Icons.Outlined.Phone, contentDescription = "icon_import_contact")
            } },
            enabled = false,
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth()
                .clickable { onCountry.invoke() },
            label = { Text(text = stringResource(id = R.string.label_nationality)) },
            value = "${countryCodeToFlag(data.nationalityCode)}  " +
                    stringResource(id = getCountryName(data.nationalityCode)),
            onValueChange = {},
            trailingIcon = { IconButton(onClick = { onCountry.invoke() }) {
                Icon(Icons.Outlined.Info, contentDescription = "icon_select_country")
            } },
            enabled = false,
        )
        GenderField(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
            onSelected = onGender::invoke, initialSelection = data.gender
        )
        HandPlays(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
            initialSelection = data.handPlay, onSelected = onHandPlay::invoke
        )
        takeSelfie()
    }
}

@Composable
private fun InputField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    suffix: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = keyboardNext(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange.invoke(it) },
        label = { Text(text = label) },
        suffix = suffix,
        isError = isError,
        supportingText = supportingText,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandPlays(
    modifier: Modifier,
    initialSelection: String = "",
    onSelected: (String) -> Unit,
) {
    val radioOptions = listOf(
        Pair("Left", stringResource(R.string.label_left)),
        Pair("Right", stringResource(R.string.label_right)),
        Pair("Both", stringResource(R.string.label_both)),
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringResource(R.string.hand_play) + ": ",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(end = 10.dp)
                .weight(.25f),
        )
        Row(
            Modifier
                .fillMaxWidth()
                .weight(.75f)
                .selectableGroup()
        ) {
            radioOptions.forEach { text ->
                InputChip(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    selected = text.first == initialSelection,
                    onClick = { onSelected.invoke(text.first) },
                    label = {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = text.second,
                            textAlign = TextAlign.Center,
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
fun GenderField(
    modifier: Modifier,
    initialSelection: String = "",
    onSelected: (String) -> Unit,
) {
    val radioOptions = listOf(
        Pair("Man", stringResource(R.string.label_gender_man)),
        Pair("Woman", stringResource(R.string.label_gender_woman))
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringResource(R.string.label_gender) + ": ",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(end = 10.dp)
                .weight(.25f),
        )
        Row(
            Modifier
                .selectableGroup()
                .weight(.75f)) {
            radioOptions.forEach { gender ->
                InputChip(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    selected = gender.first == initialSelection,
                    onClick = { onSelected.invoke(gender.first) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Localized description"
                        )
                    },
                    label = {
                        Text(
                            text = gender.second,
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
fun TopView(
    title: String,
    onClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
fun BottomView(
    enableClear: Boolean = false,
    enableSave: Boolean = false,
    onClear: () -> Unit,
    onSave: () -> Unit,
) {
    Surface(tonalElevation = 1.dp, shadowElevation = 1.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(onClick = { onClear.invoke() }, enabled = enableClear) {
                Text(text = stringResource(R.string.label_clear))
            }
            OutlinedButton(
                enabled = enableSave, onClick = { onSave.invoke() },
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(text = stringResource(R.string.label_save))
            }
        }
    }
}
@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 512)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreatePlayerPreview() {
    CreatePlayerContent("Create Player", CreatePlayerState(),
        event = {}, onSave = {},
        onCheckSavePlayers = {},
        selfieState = null, onNewSelfie = { Uri.parse("") }
    )
}