package com.imams.boardminton.ui.screen.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imams.boardminton.R
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.domain.model.ITeam
import com.imams.boardminton.ui.component.MiniIconButton
import com.imams.boardminton.ui.theme.Orientation
import com.imams.boardminton.ui.utils.keyBoardDone
import com.imams.boardminton.ui.utils.keyboardNext

val mPortrait = Modifier
    .fillMaxWidth()
    .wrapContentHeight()
    .padding(10.dp)

val mLandscape = Modifier
    .fillMaxWidth()
    .wrapContentHeight()
    .padding(10.dp)

val ipModifierP = Modifier.fillMaxWidth()
val ipModifierL = Modifier.widthIn(min = 250.dp, max = 480.dp)

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
            label = stringResource(R.string.label_field_player, "1"),
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
            label = stringResource(R.string.label_field_player, "2"),
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
            label = stringResource(R.string.label_field_player, "1"),
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
            label = stringResource(R.string.label_field_player, "2"),
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
    importTeam: ((ISide) -> Unit)? = null,
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
            importPerson = { importPerson.invoke(it) },
            importTeam = { importTeam?.invoke(it) },
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
            label = stringResource(R.string.label_field_player, "1"),
            endIconClick = { importPerson.invoke(ITeam.A1) },
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        SwapButton({ swapPlayer.invoke(ISide.A) })
        InputPlayer(
            modifier = ipModifierP,
            value = pA2,
            onValueChange = {
                onChange.invoke(ITeam.A2, it)
            },
            label = stringResource(R.string.label_field_player, "2"),
            endIconClick = { importPerson.invoke(ITeam.A2) },
        )
        AnimatedVisibility(visible = importTeam != null) {
            OutlinedButton(
                modifier = Modifier.padding(top = 6.dp),
                onClick = { importTeam?.invoke(ISide.A) }
            ) { Text(text = stringResource(R.string.import_team)) }
        }
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
            label = stringResource(R.string.label_field_player, "1"),
            endIconClick = { importPerson.invoke(ITeam.B1) },
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        SwapButton({ swapPlayer.invoke(ISide.B) })
        InputPlayer(
            modifier = ipModifierP,
            value = pB2,
            onValueChange = { onChange.invoke(ITeam.B2, it) },
            label = stringResource(R.string.label_field_player, "2"),
            keyboardOptions = keyBoardDone(),
            endIconClick = { importPerson.invoke(ITeam.B2) },
        )
        AnimatedVisibility(visible = importTeam != null) {
            OutlinedButton(
                modifier = Modifier.padding(top = 6.dp),
                onClick = { importTeam?.invoke(ISide.B) }
            ) { Text(text = stringResource(R.string.import_team)) }
        }
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
    importTeam: ((ISide) -> Unit)? = null,
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
                label = stringResource(R.string.label_field_player, "1"),
                endIconClick = { importPerson.invoke(ITeam.A1) },
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            SwapButton({ swapPlayer.invoke(ISide.A) })
            InputPlayer(
                modifier = ipModifierL,
                value = pA2,
                onValueChange = {
                    onChange.invoke(ITeam.A2, it)
                },
                label = stringResource(R.string.label_field_player, "2"),
                endIconClick = { importPerson.invoke(ITeam.A2) },
            )
            AnimatedVisibility(visible = importTeam != null) {
                OutlinedButton(
                    modifier = Modifier.padding(top = 6.dp),
                    onClick = { importTeam?.invoke(ISide.A) }
                ) { Text(text = stringResource(R.string.import_team)) }
            }
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
                label = stringResource(R.string.label_field_player, "1"),
                endIconClick = { importPerson.invoke(ITeam.B1) },
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            SwapButton({ swapPlayer.invoke(ISide.B) })
            InputPlayer(
                modifier = ipModifierL,
                value = pB2,
                onValueChange = { onChange.invoke(ITeam.B2, it) },
                label = stringResource(R.string.label_field_player, "2"),
                keyboardOptions = keyBoardDone(),
                endIconClick = { importPerson.invoke(ITeam.B2) },
            )
            AnimatedVisibility(visible = importTeam != null) {
                OutlinedButton(
                    modifier = Modifier.padding(top = 6.dp),
                    onClick = { importTeam?.invoke(ISide.B) }
                ) { Text(text = stringResource(R.string.import_team)) }
            }
        }
    }

}

@Composable
fun InputPlayer(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Player",
    enabled: Boolean = true,
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
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )
}

@Composable
fun SwapButton(
    onSwap: () -> Unit,
    orientation: Orientation = Orientation.Portrait,
) {
    MiniIconButton(
        padding = 4.dp,
        icon = if (orientation == Orientation.Portrait) R.drawable.ic_swap_1 else R.drawable.ic_swap_3,
        onClick = onSwap::invoke
    )
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
        importPerson = {},
        importTeam = {},
    )
}