package com.imams.boardminton.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.imams.boardminton.domain.model.Sort

@Composable
fun FancyIndicator(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier
            .padding(12.dp)
            .fillMaxSize()
            .border(BorderStroke(2.dp, color), RoundedCornerShape(10.dp))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortField(
    label: String,
    options: List<Sort>,
    initialSelection: String = "",
    onSelected: (Sort) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            Modifier
                .selectableGroup()
                .padding(vertical = 3.dp)) {
            options.forEach { text ->
                InputChip(
                    modifier = Modifier.padding(end = 5.dp),
                    selected = text.name.equals(initialSelection, true),
                    onClick = { onSelected.invoke(text) },
                    label = {
                        Text(
                            text = text.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(3.dp)
                        )
                    }
                )
            }
        }
        Text(text = "By $label")
    }
}
