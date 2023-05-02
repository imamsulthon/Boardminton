package com.imams.boardminton.ui.screen.create

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imams.boardminton.R
import com.imams.boardminton.data.ITeam
import com.imams.boardminton.data.toJson
import com.imams.boardminton.ui.keyBoardDone
import com.imams.boardminton.ui.keyboardNext
import com.imams.boardminton.ui.screen.destinations.ScoreBoardScreenDestination
import com.imams.boardminton.ui.viewmodel.CreateMatchVM
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
        navigator?.navigate(
            ScoreBoardScreenDestination(
                players = params.toJson(),
                single = singleMatch
            )
        )
    }

    @Composable
    fun topView() = MatchTypeView(single = singleMatch, onChange = { singleMatch = it })

    @Composable
    fun formView(
        modifier: Modifier = Modifier,
        arrangement: Arrangement.Vertical = Arrangement.Top
    ) {
        if (singleMatch) {
            FieldInputSingleMatch(
                modifier = modifier,
                pA1 = playerA1,
                pB1 = playerB1,
                onChangeA1 = { vm.setA1(it) },
                onChangeB1 = { vm.setB1(it) },
                onSwap = { vm.swapSingleMatch() },
                importPerson = {

                }
            )
        } else {
            FieldInputDoubleMatch(
                modifier = modifier,
                vArrangement = arrangement,
                pA1 = playerA1, pA2 = playerA2,
                pB1 = playerB1, pB2 = playerB2,
                onChangeA1 = { vm.setA1(it) }, onChangeA2 = { vm.setA2(it) },
                onChangeB1 = { vm.setB1(it) }, onChangeB2 = { vm.setB2(it) },
                swapA = { vm.swapTeamA() }, swapB = { vm.swapTeamB() },
                swapTeam = { vm.swapDoubleMatch() },
                importPerson = {

                }
            )
        }
    }
    when (config.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            val mPortrait = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
            PortraitContent(
                topView = { topView() },
                onNext = { gotoScoreBoard() },
                onBack = { navigator?.navigateUp() },
                onClear = { vm.onClearPlayers() },
                formField = { formView(mPortrait) }
            )
        }
        else -> {
            val mLandscape = Modifier
                .widthIn(max = 400.dp)
                .fillMaxHeight()
                .padding(10.dp)
            LandscapeContent(
                topView = { topView() },
                onNext = { gotoScoreBoard() },
                onBack = { navigator?.navigateUp() },
                onClear = { vm.onClearPlayers() },
                formField = { formView(mLandscape, Arrangement.SpaceBetween) }
            )
        }
    }

}

@Composable
private fun MatchTypeView(
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
                .padding(10.dp),
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
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        formField()
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            topView()
            BottomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
                onBackPressed = { onBack.invoke() },
                onNext = { onNext.invoke() },
                onClear = { onClear.invoke() }
            )
        }
    }

}

@Composable
private fun FieldInputSingleMatch(
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
private fun FieldInputDoubleMatch(
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

private fun printLog(msg: String) = println("CreateMatch: msg -> $msg")

@Preview("Create Match")
@Composable
fun CreateMatchPreview() {
    CreateMatchScreen(navigator = null)
}

@Preview("Create Match")
@Composable
fun CreateMatchPreview2() {
    CreateMatchScreen(single = false, navigator = null)
}