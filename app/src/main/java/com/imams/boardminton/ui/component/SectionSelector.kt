package com.imams.boardminton.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imams.boardminton.ui.theme.BoardMintonTheme

data class Section(val id: String, val title: String)

@Composable
fun SectionSelector(
    modifier: Modifier = Modifier,
    sections: Collection<Section>,
    selection: Section,
    onClick: (Section) -> Unit = {},
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Row {
            sections.forEach { item ->
                val isSelected = item == selection
                SectionItem(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onClick(item) },
                    title = item.title,
                    isSelected = isSelected,
                )
            }
        }
    }
}

@Composable
fun SectionItem(modifier: Modifier, title: String, isSelected: Boolean) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        animationSpec = tween(200), label = ""
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary,
        animationSpec = tween(200), label = ""
    )
    Box(
        modifier = modifier.background(bgColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = title,
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Composable
@Preview
private fun PreviewSectionSelector() {
    BoardMintonTheme(darkTheme = false) {
        val sectionTitles = remember {
            listOf(
                Section("c", "Comments"),
                Section("p", "Popular"),
                Section("o", "Others")
            )
        }
        var currentSelection by remember { mutableStateOf(sectionTitles.first()) }
        SectionSelector(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            sections = sectionTitles,
            selection = currentSelection,
            onClick = { selection ->
                if (selection != currentSelection) {
                    currentSelection = selection
                }
            })
    }
}