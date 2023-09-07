package com.imams.boardminton.ui.screen.create.player

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imams.boardminton.R
import com.imams.boardminton.data.Athlete
import com.imams.boardminton.ui.component.country.countryCodeToFlag
import com.imams.boardminton.ui.utils.bottomDialogPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
    onDismissRequest: () -> Unit,
    list: List<CreatePlayerState>,
    onSelect: ((CreatePlayerState) -> Unit)? = null,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest::invoke,
        sheetState = sheetState,
    ) {
        PlayerBottomSheetContent(
            list = list,
            onSelect = { onSelect?.invoke(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlayerBottomSheetContent(
    list: List<CreatePlayerState>,
    onSelect: ((CreatePlayerState) -> Unit)? = null,
) {
    LazyColumn(modifier = Modifier.bottomDialogPadding()) {
        item {
            Text(
                text = stringResource(R.string.label_registered_player) +
                        if (list.isNotEmpty()) " (${list.size})" else "",
                fontSize = 16.sp,
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
                        var label = "${it.id}. ${it.firstName} ${it.lastName}"
                        if (it.gender.isNotEmpty() || it.handPlay.isNotEmpty()) {
                            label = label + " " + "(${it.gender}/${it.handPlay})"
                        }
                        Text(label)
                    },
                    trailingContent = {
                        Text(text = countryCodeToFlag(it.nationalityCode))
                    }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PreviewSheet() {
    val list = mutableListOf<CreatePlayerState>()
    list.add(CreatePlayerState(1, Athlete.Viktor, "", "Left", "Man"))
    list.add(CreatePlayerState(2, Athlete.Imam_Sulthon, "", "Right", "Man"))
    list.add(CreatePlayerState(3, Athlete.Taufik_Hidayat, "", "", ""))
    list.add(CreatePlayerState(4, "Anthony", "Ginting", "", ""))
    PlayerBottomSheetContent(list = list)
}