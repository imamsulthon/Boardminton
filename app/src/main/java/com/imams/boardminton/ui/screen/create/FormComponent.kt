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
import androidx.compose.ui.unit.dp
import com.imams.boardminton.R
import com.imams.boardminton.data.ITeam
import com.imams.boardminton.ui.keyBoardDone
import com.imams.boardminton.ui.keyboardNext


@Composable
fun FieldInputSingleMatch(
    modifier: Modifier,
    vArrangement: Arrangement.Vertical = Arrangement.Top,
    pA1: String,
    pB1: String,
    onChangeA1: (String) -> Unit,
    onChangeB1: (String) -> Unit,
    onSwap: () -> Unit,
    importPerson: (ITeam) -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = vArrangement) {
        InputPlayer(
            modifier = Modifier.fillMaxWidth(),
            value = pA1,
            onValueChange = {
                onChangeA1.invoke(it)
            },
            label = "Player Name 1",
            endIconClick = { importPerson.invoke(ITeam.A1) }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Versus", modifier = Modifier.padding(vertical = 10.dp))
            SwapButton { onSwap.invoke() }
        }

        InputPlayer(
            modifier = Modifier.fillMaxWidth(),
            value = pB1,
            onValueChange = {
                onChangeB1.invoke(it)
            },
            label = "Player Name 2",
            keyboardOptions = keyBoardDone(),
            endIconClick = { importPerson.invoke(ITeam.B1) }
        )
    }

}

@Composable
fun FieldInputDoubleMatch(
    modifier: Modifier,
    vArrangement: Arrangement.Vertical = Arrangement.Top,
    pA1: String,
    pA2: String,
    pB1: String,
    pB2: String,
    onChangeA1: (String) -> Unit,
    onChangeA2: (String) -> Unit,
    onChangeB1: (String) -> Unit,
    onChangeB2: (String) -> Unit,
    swapA: () -> Unit,
    swapB: () -> Unit,
    swapTeam: () -> Unit,
    importPerson: (ITeam) -> Unit,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = vArrangement,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputPlayer(
            modifier = Modifier.fillMaxWidth(),
            value = pA1,
            onValueChange = {
                onChangeA1.invoke(it)
            },
            label = "Player Name 1",
            endIconClick = { importPerson.invoke(ITeam.A1) },
        )
        SwapButton { swapA.invoke() }
        InputPlayer(
            modifier = Modifier.fillMaxWidth(),
            value = pA2,
            onValueChange = {
                onChangeA2.invoke(it)
            },
            label = "Player Name 2",
            endIconClick = { importPerson.invoke(ITeam.A2) },
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Versus", modifier = Modifier.padding(vertical = 10.dp))
            SwapButton { swapTeam.invoke() }
        }

        InputPlayer(
            modifier = Modifier.fillMaxWidth(),
            value = pB1,
            onValueChange = {
                onChangeB1.invoke(it)
            },
            label = "Player Name 1",
            endIconClick = { importPerson.invoke(ITeam.B1) },
        )
        SwapButton { swapB.invoke() }

        InputPlayer(
            modifier = Modifier.fillMaxWidth(),
            value = pB2,
            onValueChange = {
                onChangeB2.invoke(it)
            },
            label = "Player Name 2",
            keyboardOptions = keyBoardDone(),
            endIconClick = { importPerson.invoke(ITeam.B2) },
        )
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
            painter = painterResource(id = R.drawable.ic_swap_1),
            contentDescription = "swap_icon"
        )
    }
}

val mPortrait = Modifier
    .fillMaxWidth()
    .wrapContentHeight()
    .padding(10.dp)

val mLandscape = Modifier
    .widthIn(max = 400.dp)
    .fillMaxHeight()
    .padding(10.dp)