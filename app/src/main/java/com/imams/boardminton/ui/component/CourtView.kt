package com.imams.boardminton.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imams.boardminton.R
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.domain.mapper.isSingle
import com.imams.boardminton.domain.model.IMatchType
import com.imams.boardminton.domain.model.ScoreByCourt
import com.imams.boardminton.ui.utils.prettifyName
import com.touchlane.gridpad.GridPad
import com.touchlane.gridpad.GridPadCellSize
import com.touchlane.gridpad.GridPadCells

@Composable
fun MyCourtMatch(
    modifier: Modifier,
    type: IMatchType,
    court: ScoreByCourt,
) {
    if (type.isSingle()) {
        CourtViewMatchSingle(
            modifier = modifier,
            court = court,
            pA1 = court.teamLeft.player1.name.prettifyName(), pB1 = court.teamRight.player1.name.prettifyName()
        )
    } else {
        CourtViewMatchDouble(
            modifier = modifier,
            court = court,
            pA1 = court.teamLeft.player1.name.prettifyName(), pA2 = court.teamLeft.player2.name.prettifyName(),
            pB1 = court.teamRight.player1.name.prettifyName(), pB2 = court.teamRight.player2.name.prettifyName(),
        )
    }
}

@Composable
fun CourtViewMatchSingle(
    modifier: Modifier,
    court: ScoreByCourt,
    pA1: String,
    pB1: String,
) {
    val paEven by remember(court.left.onServe, court.left.point, court.right.point) {
        mutableStateOf(
            if (court.left.onServe) {
                if (court.left.point.odd()) null else pA1
            } else {
                if (court.right.point.odd()) null else pA1
            }
        )
    }
    val paOdd by remember(court.left.onServe, court.left.point, court.right.point) {
        mutableStateOf(
            if (court.left.onServe) {
                if (court.left.point.odd()) pA1 else null
            } else {
                if (court.right.point.odd()) pA1 else null
            }
        )
    }
    val pBEven by remember(court.right.onServe, court.left.point, court.right.point) {
        mutableStateOf(
            if (court.right.onServe) {
                if (court.right.point.odd()) null else pB1
            } else {
                if (court.left.point.odd()) null else pB1
            }
        )
    }
    val pBOdd by remember(court.right.onServe, court.left.point, court.right.point) {
        mutableStateOf(
            if (court.right.onServe) {
                if (court.right.point.odd()) pB1 else null
            } else {
                if (court.left.point.odd()) pB1 else null
            }
        )
    }
    val onIndex: Int by remember(court.right, court.left,) {
        getIndexServer(court)
    }
    CourtView(
        modifier = modifier,
        onIndex = onIndex,
        pAEven = paEven, pAOdd = paOdd, pBEven = pBEven, pBOdd = pBOdd
    )
}

@Composable
fun CourtViewMatchDouble(
    modifier: Modifier = Modifier,
    court: ScoreByCourt,
    pA1: String,
    pA2: String,
    pB1: String,
    pB2: String
) {
    val onIndex: Int by remember(court.left, court.right) { getIndexServer(court) }
    val paEven by remember(court.left.onServe, court.left.point, court.right.point) {
        mutableStateOf(
            if (court.left.onServe) {
                if (court.left.point.odd()) pA1 else pA2
            } else {
                if (court.right.point.odd()) pA2 else pA1
            }
        )
    }
    val paOdd by remember(court.left.onServe, court.left.point, court.right.point) {
        mutableStateOf(
            if (court.left.onServe) {
                if (court.left.point.odd()) pA2 else pA1
            } else {
                if (court.right.point.odd()) pA1 else pA2
            }
        )
    }
    val pBEven by remember(court.right.onServe, court.left.point, court.right.point) {
        mutableStateOf(
            if (court.right.onServe) {
                if (court.right.point.odd()) pB1 else pB2
            } else {
                if (court.left.point.odd()) pB2 else pB1
            }
        )
    }
    val pBOdd by remember(court.right.onServe, court.left.point, court.right.point) {
        mutableStateOf(
            if (court.right.onServe) {
                if (court.right.point.odd()) pB2 else pB1
            } else {
                if (court.left.point.odd()) pB1 else pB2
            }
        )
    }

    CourtView(
        modifier = modifier,
        onIndex = onIndex,
        pAEven = paEven, pAOdd = paOdd,
        pBEven = pBEven, pBOdd = pBOdd
    )
}

private fun Int.odd(): Boolean = this % 2 > 0
private fun getIndexServer(court: ScoreByCourt): MutableIntState {
    return mutableStateOf(
        when {
            court.left.onServe -> if (court.left.point.odd()) 1 else 2
            court.right.onServe -> if (court.right.point.odd()) 3 else 4
            else -> 0
        }
    )
}

@Composable
fun CourtView(
    modifier: Modifier = Modifier,
    onIndex: Int = 0,
    pAEven: String? = null,
    pAOdd: String? = null,
    pBEven: String? = null,
    pBOdd: String? = null,
) {
    GridPad(
        modifier = modifier
            .aspectRatio(ratio = 13.4f / 6.1f)
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
            FieldPlayer(text = pAOdd.orEmpty(), onIndex == 1)
        }
        item(row = 1, column = 2, rowSpan = 2, columnSpan = 2) {
            FieldPlain()
        }
        item(row = 1, column = 4) {
            FieldPlayer(text = pBEven.orEmpty(), onIndex == 4)
        }
        item(row = 1, column = 5) {
            FieldPlain()
        }
        // row 2
        item(row = 2, column = 0) {
            FieldPlain()
        }
        item(row = 2, column = 1) {
            FieldPlayer(text = pAEven.orEmpty(), onIndex == 2)
        }
        item(row = 2, column = 2, rowSpan = 3, columnSpan = 2) {
            FieldPlain()
        }
        item(row = 2, column = 4) {
            FieldPlayer(text = pBOdd.orEmpty(), onIndex == 3)
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
private fun FieldPlayer(text: String, onServe: Boolean = false) {
    ItemComponent {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(2.dp), fontSize = 11.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            AnimatedVisibility(visible = onServe) {
                Icon(
                    modifier = Modifier.size(height = 24.dp, width = 24.dp).padding(vertical = 2.dp),
                    painter = painterResource(id = R.drawable.ic_cock),
                    contentDescription = "cock",
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
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
            .background(MaterialTheme.colorScheme.surfaceTint),
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
    CourtView(
        onIndex = 1,
        pAEven = Athlete.Imam_Sulthon,
        pAOdd = Athlete.Anthony.prettifyName(),
        pBEven = Athlete.Kim_Astrup,
        pBOdd = Athlete.Anders_Skaarup,
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
        CourtView(
            modifier = Modifier.fillMaxWidth(),
            onIndex = 1,
            pAEven = Athlete.Imam_Sulthon.prettifyName(),
            pBEven = Athlete.Kim_Astrup.prettifyName(),
        )
        Text(text = "Double match Court View")
        CourtView(
            modifier = Modifier.fillMaxWidth(),
            onIndex = 1,
            pAEven = Athlete.Imam_Sulthon,
            pAOdd = Athlete.Anthony.prettifyName(),
            pBEven = Athlete.Kim_Astrup,
            pBOdd = Athlete.Anders_Skaarup.prettifyName(),
        )
    }
}