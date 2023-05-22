package com.imams.boardminton.ui.screen.create.player

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imams.boardminton.data.Athlete


@Composable
fun PlayerBottomSheetContent(list: List<CreatePlayerState>) {
    LazyColumn(modifier = Modifier.padding(vertical = 10.dp)) {
        item {
            Text(
                text = "Registered Players (${list.size})",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        items(items = list) {
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
                    Text("${it.id}. ${it.firstName} ${it.lastName} (${it.gender}/${it.handPlay})")
                },
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PreviewSheet() {
    val list = mutableListOf<CreatePlayerState>()
    list.add(CreatePlayerState(1, Athlete.Viktor, "", "", ""))
    list.add(CreatePlayerState(2, Athlete.Imam_Sulthon, "", "", ""))
    list.add(CreatePlayerState(3, Athlete.Taufik_Hidayat, "", "", ""))
    list.add(CreatePlayerState(4, "Anthony", "Ginting", "", ""))
    PlayerBottomSheetContent(list = list)
}