package com.imams.boardminton.ui.screen.create.team

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imams.boardminton.ui.utils.bottomDialogPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
    onDismissRequest: () -> Unit,
    list: List<CreateTeamState>,
    onSelect: ((CreateTeamState) -> Unit)? = null,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest::invoke, sheetState = sheetState,
    ) {
        TeamBottomSheetContent(
            list = list, onSelect = { onSelect?.invoke(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamBottomSheetContent(
    list: List<CreateTeamState>,
    onSelect: ((CreateTeamState) -> Unit)? = null,
) {
    LazyColumn(modifier = Modifier.bottomDialogPadding()) {
        item {
            Text(
                text = "Registered Teams (${list.size})", fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 6.dp, bottom = 12.dp)
            )
        }
        items(items = list) {
            Card(modifier = Modifier.padding(bottom = 5.dp),
                shape = RoundedCornerShape(
                    topStart = 15.dp, topEnd = 5.dp, bottomEnd = 5.dp, bottomStart = 5.dp
                ),
                onClick = { onSelect?.invoke(it) }) {
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Localized description"
                        )
                    },
                    tonalElevation = 2.dp,
                    shadowElevation = 2.dp,
                    headlineContent = {
                        val label = "${it.id}. ${it.playerName1}/${it.playerName2}"
                        Text(label)
                    },
                )
            }
        }
    }
}