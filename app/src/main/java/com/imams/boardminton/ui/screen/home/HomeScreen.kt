package com.imams.boardminton.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imams.boardminton.R
import com.imams.boardminton.ui.screen.destinations.CreateMatchScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(
    navigator: DestinationsNavigator?
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 840.dp)
            .padding(10.dp),
        topBar = {
            HomeAppBar()
        },
    ) { padding ->
        HomeContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            navigator = navigator
        )
    }
}

@Composable
private fun HomeAppBar() {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val appName = stringResource(id = R.string.app_name)
        Text(
            text = appName,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp
        )
    }
}

@Composable
internal fun HomeContent(
    modifier: Modifier,
    navigator: DestinationsNavigator?
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MenuGroup(
            label = "Create New Match"
        ) {
            ItemMenu(label = "Single Match") {
                navigator?.navigate(CreateMatchScreenDestination())
            }
            ItemMenu(label = "Double Match") {
                navigator?.navigate(CreateMatchScreenDestination(single = false))
            }
        }

        MenuGroup(
            label = "Create New Player"
        ) {
            ItemMenu(label = "Single Player") {
                // todo
            }
            ItemMenu(label = "Double Player", enabled = false) {
                // todo
            }
        }
    }
}

@Composable
private fun MenuGroup(
    label: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label)
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 5.dp)
                .widthIn(max = 400.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            content()
        }
    }
}

@Composable
private fun ItemMenu(
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        enabled = enabled,
        onClick = { onClick.invoke() },
        modifier = Modifier.padding(horizontal = 10.dp),
    ) {
        Text(text = label)
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navigator = null)
}