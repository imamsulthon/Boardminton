package com.imams.boardminton.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun RowInfoData(
    label: String,
    content: String,
    weightLabel: Float = .25f,
    weightContent: Float = .7f
) {
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(text = label, weight = weightLabel)
        TableCell(text = ": ", weight = .05f)
        TableCell(text = content, weight = weightContent, maxLines = 2)
    }
}

@Composable
private fun RowScope.TableCell(
    text: String,
    weight: Float,
    maxLines : Int = 1,
) {
    Text(modifier = Modifier
        .weight(weight)
        .padding(vertical = 4.dp, horizontal = 4.dp), text = text, maxLines = maxLines,
    )
}