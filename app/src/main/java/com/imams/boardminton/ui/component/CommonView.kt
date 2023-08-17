package com.imams.boardminton.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FancyIndicator(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier
            .padding(12.dp)
            .fillMaxSize()
            .border(BorderStroke(2.dp, color), RoundedCornerShape(10.dp))
    )
}
