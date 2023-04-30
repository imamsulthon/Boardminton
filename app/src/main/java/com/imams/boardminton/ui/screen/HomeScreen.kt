package com.imams.boardminton.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imams.boardminton.ui.screen.destinations.CreateMatchScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator?
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .widthIn(max = 840.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Create Match")
            
            Spacer(modifier = Modifier.padding(10.dp))

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .widthIn(max = 400.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        navigator?.navigate(CreateMatchScreenDestination())
                    }
                ) {
                    Text(text = "Single Match")
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Button(
                    onClick = {
                        navigator?.navigate(CreateMatchScreenDestination(single = false))
                    }
                ) {
                    Text(text = "Double Match")
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(navigator = null)
}