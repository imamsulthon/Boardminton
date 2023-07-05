package com.imams.boardminton.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.IMatchType
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.engine.data.model.OnServe
import com.imams.boardminton.ui.utils.prettifyName
import com.touchlane.gridpad.GridPad
import com.touchlane.gridpad.GridPadCellSize
import com.touchlane.gridpad.GridPadCells

@Composable
fun MyCourtMatch(
    modifier: Modifier,
    type: IMatchType,
    game: GameViewParam,
    teamA: TeamViewParam,
    teamB: TeamViewParam,
) {
    if (type.isSingle()) {
        CourtViewMatchSingle(
            modifier = modifier,
            game = game,
            pA1 = teamA.player1.name.prettifyName(), pB1 = teamB.player1.name.prettifyName()
        )
    } else {
        CourtViewMatchDouble(
            modifier = modifier,
            game = game,
            pA1 = teamA.player1.name.prettifyName(), pA2 = teamA.player2.name.prettifyName(),
            pB1 = teamB.player1.name.prettifyName(), pB2 = teamB.player2.name.prettifyName(),
        )
    }
}

@Composable
fun CourtViewMatchSingle(
    modifier: Modifier,
    game: GameViewParam,
    pA1: String,
    pB1: String,
) {
    log("onServe ${game.onServe.name}")
    val paEven by remember(game.onServe, game.scoreA.point, game.scoreB.point) {
        mutableStateOf(
            when (game.onServe) {
                OnServe.A -> {
                    log("a 1a")
                    if (game.scoreA.point.odd()) null else pA1
                }
                OnServe.B -> {
                    log("a 1b")
                    if (game.scoreB.point.odd()) null else pA1
                }
                else -> pA1
            }
        )
    }
    val paOdd by remember(game.onServe, game.scoreA.point, game.scoreB.point) {
        mutableStateOf(
            when (game.onServe) {
                OnServe.A -> {
                    log("a 2a")
                    if (game.scoreA.point.odd()) null else pA1
                }
                OnServe.B -> {
                    log("a 2b")
                    if (game.scoreB.point.odd()) null else pA1
                }
                else -> null
            }
        )
    }
    val pBEven by remember(game.onServe, game.scoreA.point, game.scoreB.point) {
        mutableStateOf(
            when (game.onServe) {
                OnServe.B -> {
                    log("b 1b")
                    if (game.scoreB.point.odd()) null else pB1
                }
                OnServe.A -> {
                    log("b 1a")
                    if (game.scoreA.point.odd()) null else pB1
                }
                else -> pB1
            }
        )
    }
    val pBOdd by remember(game.onServe, game.scoreA.point, game.scoreB.point) {
        mutableStateOf(
            when (game.onServe) {
                OnServe.B -> {
                    log("b 2b")
                    if (game.scoreB.point.odd()) pB1 else null
                }
                OnServe.A -> {
                    log("b 2a")
                    if (game.scoreA.point.odd()) pB1 else null
                }
                else -> null
            }
        )
    }
    val onIndex: Int by remember(game.onServe, game.scoreA.point, game.scoreB.point) {
        mutableStateOf(
            when (game.onServe) {
                OnServe.A -> {
                    if (game.scoreA.point.odd()) 1 else 2
                }
                OnServe.B -> {
                    if (game.scoreB.point.odd()) 3 else 4
                }
                else -> 0
            }
        )
    }
    log("result ${game.onServe.name} $paEven $paOdd $pBEven $pBOdd")

    CourtView(
        modifier = modifier,
        onIndex = onIndex,
        pAEven = paEven, pAOdd = paOdd, pBEven = pBEven, pBOdd = pBOdd
    )
}

private fun log(m: String) = println("CourtView: $m")

private fun Int.odd(): Boolean = this % 2 > 0

@Composable
fun CourtViewMatchDouble(
    modifier: Modifier = Modifier,
    game: GameViewParam,
    pA1: String,
    pA2: String,
    pB1: String,
    pB2: String
) {
    val onIndex by remember(game.onServe, game.scoreA, game.scoreB) {
        mutableStateOf(
            when (game.onServe) {
                OnServe.A -> {
                    if (game.scoreA.point.odd()) 1 else 2
                }
                OnServe.B -> {
                    if (game.scoreB.point.odd()) 3 else 4
                }
                else -> 0
            }
        )
    }
    CourtView(
        modifier = modifier,
        onIndex = onIndex,
        pAEven = pA1, pAOdd = pA2,
        pBEven = pB1, pBOdd = pB2
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
                modifier = Modifier.padding(2.dp), fontSize = 11.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            AnimatedVisibility(visible = onServe) {
                Icon(
                    modifier = Modifier.size(height = 24.dp, width = 24.dp).padding(vertical = 2.dp),
                    painter = painterResource(id = R.drawable.ic_cock),
                    contentDescription = "cock"
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
//        CourtViewMatchSingle(
//            pA1 = Athlete.Anthony.prettifyName(),
//            pB1 = Athlete.Kim_Astrup,
//        )
        CourtView(
            onIndex = 1,
            pAEven = Athlete.Imam_Sulthon,
            pAOdd = Athlete.Anthony.prettifyName(),
            pBEven = Athlete.Kim_Astrup,
            pBOdd = Athlete.Anders_Skaarup,
        )

        Text(text = "Double match Court View")
//        CourtViewMatchDouble(
//            pA1 = Athlete.Anthony.prettifyName(),
//            pA2 = Athlete.Imam_Sulthon,
//            pB1 = Athlete.Kim_Astrup,
//            pB2 = Athlete.Anders_Skaarup.prettifyName(),
//        )
    }
}