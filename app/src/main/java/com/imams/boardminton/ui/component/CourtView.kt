package com.imams.boardminton.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.ui.utils.prettifyName
import com.touchlane.gridpad.GridPad
import com.touchlane.gridpad.GridPadCellSize
import com.touchlane.gridpad.GridPadCells

@Composable
fun CourtViewMatchSingle(
    pA1: String,
    pB1: String,
) {
    CourtView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .aspectRatio(ratio = 13.4f / 6.1f),
        pAEven = pA1, pBEven = pB1
    )
}

@Composable
fun CourtViewMatchDouble(
    pA1: String,
    pA2: String,
    pB1: String,
    pB2: String
) {
    CourtView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .aspectRatio(ratio = 13.4f / 6.1f),
        pAEven = pA1, pAOdd = pA2,
        pBEven = pB1, pBOdd = pB2
    )
}

@Composable
fun CourtView(
    modifier: Modifier = Modifier,
    pAEven: String,
    pAOdd: String? = null,
    pBEven: String,
    pBOdd: String? = null,
) {
    GridPad(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
        cells = GridPadCells.Builder(rowCount = 4, columnCount = 6)
            .rowSize(index = 0, size = GridPadCellSize.Weight(0.5f))
            .rowSize(index = 1, size = GridPadCellSize.Weight(2f))
            .rowSize(index = 2, size = GridPadCellSize.Weight(2f))
            .rowSize(index = 3, size = GridPadCellSize.Weight(0.5f))
            .columnSize(index = 0, size = GridPadCellSize.Weight(0.5f))
            .columnSize(index = 1, size = GridPadCellSize.Weight(2f))
            .columnSize(index = 2, size = GridPadCellSize.Weight(1f))
            .columnSize(index = 3, size = GridPadCellSize.Weight(1f))
            .columnSize(index = 4, size = GridPadCellSize.Weight(2f))
            .columnSize(index = 5, size = GridPadCellSize.Weight(0.5f))
            .build()
    ) {
        item(row = 0, column = 0) {
            FieldPlain()
        }
        item(row = 0, column = 1) {
            FieldPlain()
        }
        item(row = 0, column = 2) {
            FieldPlain()
        }
        item(row = 0, column = 3) {
            FieldPlain()
        }
        item(row = 0, column = 4) {
            FieldPlain()
        }
        item(row = 0, column = 5) {
            FieldPlain()
        }
        // row 1
        item(row = 1, column = 0) {
            FieldPlain()
        }
        item(row = 1, column = 1) {
            FieldPlayer(text = pAOdd ?: "")
        }
        item(row = 1, column = 2, rowSpan = 2, columnSpan = 2) {
            FieldPlain()
        }
        item(row = 1, column = 4) {
            FieldPlayer(text = pBEven)
        }
        item(row = 1, column = 5) {
            FieldPlain()
        }
        // row 2
        item(row = 2, column = 0) {
            FieldPlain()
        }
        item(row = 2, column = 1) {
            FieldPlayer(text = pAEven)
        }
        item(row = 2, column = 2, rowSpan = 3, columnSpan = 2) {
            FieldPlain()
        }
        item(row = 2, column = 4) {
            FieldPlayer(text = pBOdd ?: "")
        }
        item(row = 2, column = 5) {
            FieldPlain()
        }
        // row 3
        item(row = 3, column = 0) {
            FieldPlain()
        }
        item(row = 3, column = 1) {
            FieldPlain()
        }
        item(row = 3, column = 2) {
            FieldPlain()
        }
        item(row = 3, column = 3) {
            FieldPlain()
        }
        item(row = 3, column = 4) {
            FieldPlain()
        }
        item(row = 3, column = 5) {
            FieldPlain()
        }
    }
}

@Composable
private fun FieldPlayer(text: String) {
    ItemComponent {
        Text(
            text = text,
            modifier = Modifier.padding(2.dp), fontSize = 11.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
private fun FieldPlain() {
    ItemComponent(
        content = {}
    )
}

@Composable
private fun ItemComponent(
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = 1024,
    heightDp = 512,
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun CourtViewLandscape() {
    CourtViewMatchDouble(
        pA1 = Athlete.Anthony,
        pA2 = Athlete.Imam_Sulthon,
        pB1 = Athlete.Kim_Astrup,
        pB2 = Athlete.Anders_Skaarup,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CourtViewPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(3.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {

        Text(text = "Single match Court View")
        CourtViewMatchSingle(
            pA1 = Athlete.Anthony.prettifyName(),
            pB1 = Athlete.Kim_Astrup,
        )

        Text(text = "Double match Court View")
        CourtViewMatchDouble(
            pA1 = Athlete.Anthony.prettifyName(),
            pA2 = Athlete.Imam_Sulthon,
            pB1 = Athlete.Kim_Astrup,
            pB2 = Athlete.Anders_Skaarup.prettifyName(),
        )
    }
}