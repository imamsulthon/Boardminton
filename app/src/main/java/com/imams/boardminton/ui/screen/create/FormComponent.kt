package com.imams.boardminton.ui.screen.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imams.boardminton.R
import com.imams.boardminton.data.ISide
import com.imams.boardminton.data.ITeam
import com.imams.boardminton.ui.utils.keyBoardDone
import com.imams.boardminton.ui.utils.keyboardNext
import com.imams.boardminton.ui.theme.Orientation

val mPortrait = Modifier
    .fillMaxWidth()
    .wrapContentHeight()
    .padding(10.dp)

val mLandscape = Modifier
    .fillMaxWidth()
    .wrapContentHeight()
    .padding(10.dp)

private val ipModifierP = Modifier.fillMaxWidth()
private val ipModifierL = Modifier.widthIn(min = 250.dp, max = 480.dp)

@Composable
fun FieldInputSingleMatch(
    modifier: Modifier,
    orientation: Orientation = Orientation.Portrait,
    pA1: String,
    pB1: String,
    onChange: (ITeam, String) -> Unit,
    swapPlayer: () -> Unit,
    importPerson: (ITeam) -> Unit,
) {
    // Landscape
    if (orientation == Orientation.Landscape) {
        FieldInputSingleLandscape(
            modifier = modifier,
            pA1 = pA1,
            pB1 = pB1,
            onChange = onChange::invoke,
            onSwap = { swapPlayer.invoke() },
            importPerson = {},
        )
        return
    }
    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {
        InputPlayer(
            modifier = ipModifierP,
            value = pA1,
            onValueChange = {
                onChange.invoke(ITeam.A1, it)
            },
            label = "Player 1 Name",
            endIconClick = { importPerson.invoke(ITeam.A1) }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Versus", modifier = Modifier.padding(vertical = 10.dp))
            SwapButton({ swapPlayer.invoke() })
        }

        InputPlayer(
            modifier = ipModifierP,
            value = pB1,
            onValueChange = {
                onChange.invoke(ITeam.B1, it)
            },
            label = "Player 2 Name",
            keyboardOptions = keyBoardDone(),
            endIconClick = { importPerson.invoke(ITeam.B1) }
        )
    }

}

@Composable
fun FieldInputSingleLandscape(
    modifier: Modifier,
    pA1: String,
    pB1: String,
    onChange: (ITeam, String) -> Unit,
    onSwap: () -> Unit,
    importPerson: (ITeam) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InputPlayer(
            modifier = ipModifierL,
            value = pA1,
            onValueChange = {
                onChange.invoke(ITeam.A1, it)
            },
            label = "Player 1 Name",
            endIconClick = { importPerson.invoke(ITeam.A1) }
        )

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Versus", modifier = Modifier.padding(vertical = 10.dp))
            SwapButton(orientation = Orientation.Landscape, onSwap = { onSwap.invoke() })
        }

        InputPlayer(
            modifier = ipModifierL,
            value = pB1,
            onValueChange = {
                onChange.invoke(ITeam.B1, it)
            },
            label = "Player 2 Name",
            keyboardOptions = keyBoardDone(),
            endIconClick = { importPerson.invoke(ITeam.B1) }
        )
    }
}

@Composable
fun FieldInputDoubleMatch(
    modifier: Modifier,
    orientation: Orientation = Orientation.Portrait,
    pA1: String,
    pA2: String,
    pB1: String,
    pB2: String,
    onChange: (ITeam, String) -> Unit,
    swapPlayer: (ISide) -> Unit,
    swapTeam: () -> Unit,
    importPerson: (ITeam) -> Unit,
) {
    // Landscape
    if (orientation == Orientation.Landscape) {
        FieldInputDoubleLandscape(
            modifier = modifier,
            pA1 = pA1,
            pA2 = pA2,
            pB1 = pB1,
            pB2 = pB2,
            onChange = onChange::invoke,
            swapPlayer = swapPlayer::invoke,
            swapTeam = { swapTeam.invoke() },
            importPerson = { importPerson.invoke(it) }
        )
        return
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputPlayer(
            modifier = ipModifierP,
            value = pA1,
            onValueChange = {
                onChange.invoke(ITeam.A1, it)
            },
            label = "Player 1 Name",
            endIconClick = { importPerson.invoke(ITeam.A1) },
        )
        SwapButton({ swapPlayer.invoke(ISide.A) })
        InputPlayer(
            modifier = ipModifierP,
            value = pA2,
            onValueChange = {
                onChange.invoke(ITeam.A2, it)
            },
            label = "Player 2 Name",
            endIconClick = { importPerson.invoke(ITeam.A2) },
        )

        Row(
            modifier = ipModifierP,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Versus", modifier = Modifier.padding(vertical = 10.dp))
            SwapButton({ swapTeam.invoke() })
        }

        InputPlayer(
            modifier = ipModifierP,
            value = pB1,
            onValueChange = { onChange.invoke(ITeam.B1, it) },
            label = "Player 1 Name",
            endIconClick = { importPerson.invoke(ITeam.B1) },
        )
        SwapButton({ swapPlayer.invoke(ISide.B) })
        InputPlayer(
            modifier = ipModifierP,
            value = pB2,
            onValueChange = { onChange.invoke(ITeam.B2, it) },
            label = "Player 2 Name",
            keyboardOptions = keyBoardDone(),
            endIconClick = { importPerson.invoke(ITeam.B2) },
        )
    }

}

@Composable
private fun FieldInputDoubleLandscape(
    modifier: Modifier,
    vArrangement: Arrangement.Vertical = Arrangement.Top,
    pA1: String,
    pA2: String,
    pB1: String,
    pB2: String,
    onChange: (ITeam, String) -> Unit,
    swapPlayer: (ISide) -> Unit,
    swapTeam: () -> Unit,
    importPerson: (ITeam) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = vArrangement,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputPlayer(
                modifier = ipModifierL,
                value = pA1,
                onValueChange = {
                    onChange.invoke(ITeam.A1, it)
                },
                label = "Player 1 Name",
                endIconClick = { importPerson.invoke(ITeam.A1) },
            )
            SwapButton({ swapPlayer.invoke(ISide.A) })
            InputPlayer(
                modifier = ipModifierL,
                value = pA2,
                onValueChange = {
                    onChange.invoke(ITeam.A2, it)
                },
                label = "Player 2 Name",
                endIconClick = { importPerson.invoke(ITeam.A2) },
            )
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Versus", modifier = Modifier.padding(vertical = 10.dp))
            SwapButton(orientation = Orientation.Landscape, onSwap = { swapTeam.invoke() })
        }

        Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = vArrangement,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputPlayer(
                modifier = ipModifierL,
                value = pB1,
                onValueChange = { onChange.invoke(ITeam.B1, it) },
                label = "Player 1 Name",
                endIconClick = { importPerson.invoke(ITeam.B1) },
            )
            SwapButton({ swapPlayer.invoke(ISide.B) })
            InputPlayer(
                modifier = ipModifierL,
                value = pB2,
                onValueChange = { onChange.invoke(ITeam.B2, it) },
                label = "Player 2 Name",
                keyboardOptions = keyBoardDone(),
                endIconClick = { importPerson.invoke(ITeam.B2) },
            )
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputPlayer(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Player",
    keyboardOptions: KeyboardOptions = keyboardNext(),
    endIconClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    @Composable
    fun endIcon() = IconButton(onClick = { endIconClick.invoke() }) {
        Icon(Icons.Outlined.Person, contentDescription = "import_icon")
    }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange.invoke(it) },
        label = { Text(text = label) },
        trailingIcon = { endIcon() },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )
}

@Composable
private fun SwapButton(
    onSwap: () -> Unit,
    orientation: Orientation = Orientation.Portrait,
) {
    IconButton(
        onClick = { onSwap.invoke() },
        modifier = Modifier
            .padding(vertical = 2.dp)
            .width(14.dp)
            .height(14.dp)
    ) {
        Icon(
            modifier = Modifier.wrapContentSize(),
            painter = if (orientation == Orientation.Portrait) painterResource(id = R.drawable.ic_swap_1)
            else painterResource(id = R.drawable.ic_swap_3),
            contentDescription = "swap_icon"
        )
    }
}

@Preview
@Composable
fun SingleLandscapeP() {
    FieldInputSingleLandscape(
        modifier = Modifier.fillMaxWidth(),
        pA1 = "player",
        pB1 = "player2",
        onChange = { x, y -> },
        onSwap = { },
        importPerson = {}
    )
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun DoubleLandscapeP() {
    FieldInputDoubleLandscape(
        modifier = Modifier.fillMaxWidth(),
        pA1 = "playerA1", pA2 = "playerA2",
        pB1 = "playerB1", pB2 = "playerB2",
        onChange = { v, i -> },
        swapPlayer = {},
        swapTeam = { },
        importPerson = {}
    )
}