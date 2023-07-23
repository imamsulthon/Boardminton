package com.imams.boardminton.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.imams.boardminton.R

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    message: String,
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_cock), contentDescription = "ic_cock")
            Text(text = message)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun EmptyScreenPreview() {

    EmptyContent(message = "No data Found")

}