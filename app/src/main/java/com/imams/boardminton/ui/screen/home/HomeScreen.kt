package com.imams.boardminton.ui.screen.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.imams.boardminton.R
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.boardminton.ui.component.ChildLayout
import com.imams.boardminton.ui.component.LoadItemAfterSafeCast
import com.imams.boardminton.ui.component.VerticalScroll
import com.imams.boardminton.ui.screen.matches.MatchItem
import com.imams.boardminton.ui.settings.ChangeThemeDialog
import com.imams.boardminton.ui.settings.ChangeThemeState

@Composable
fun HomeScreen(
    changeThemeData: ChangeThemeState,
    onChangeTheme: (ChangeThemeState) -> Unit,
    onGoingMatches: List<MatchViewParam>? = null,
    onCreateMatch: (String) -> Unit,
    onCreatePlayer: (String) -> Unit,
    gotoScoreboard: ((String, Int) -> Unit)? = null,
    seeAllMatch: () -> Unit,
) {
    var dialogStateChangeTheme by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 840.dp),
        topBar = {
            HomeAppBar(onSettings = {
                dialogStateChangeTheme = true
            })
        }
    ) { padding ->
        HomeContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            onCreateMatch = { onCreateMatch.invoke(if (it) "single" else "double") },
            onCreatePlayer = onCreatePlayer::invoke,
            onGoingMatches = onGoingMatches ?: emptyList(),
            gotoMatch = { type, id ->
                gotoScoreboard?.invoke(
                    if (onGoingMatches == null || type.equals("single", true)) "single"
                    else "double", id
                )
            },
            seeAllMatch = { seeAllMatch.invoke() },
        )
        if (dialogStateChangeTheme) {
            Dialog(
                onDismissRequest = { dialogStateChangeTheme = false },
                content = {
                    ChangeThemeDialog(
                        stateData = changeThemeData,
                        onApply = onChangeTheme::invoke,
                        onDismiss = { dialogStateChangeTheme = false }
                    )
                },
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeAppBar(
    onSettings: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val appName = stringResource(id = R.string.app_name)
        Text(
            text = appName,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp
        )
    }
    TopAppBar(
        title = {
            val appName = stringResource(id = R.string.app_name)
            Text(
                text = appName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        },
        actions = {
            IconButton(onClick = onSettings::invoke) {
                Icon(Icons.Filled.Settings, contentDescription = "Localized description")
            }
        },
    )
}

@Composable
internal fun HomeContent(
    modifier: Modifier,
    onCreateMatch: (Boolean) -> Unit,
    onCreatePlayer: (String) -> Unit,
    onGoingMatches: List<MatchViewParam>,
    gotoMatch: ((String, Int) -> Unit)? = null,
    seeAllMatch: () -> Unit,
) {
    VerticalScroll(
        modifier = modifier.fillMaxSize(),
        ChildLayout(contentType = "menu_section",
            content = {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MenuGroup(label = "Create New Match") {
                        ItemMenu(label = "Single Match") {
                            onCreateMatch.invoke(true)
                        }
                        ItemMenu(label = "Double Match") {
                            onCreateMatch.invoke(false)
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.onBackground)
                    )
                    MenuGroup(label = "Create New Player") {
                        ItemMenu(label = "New Player") {
                            onCreatePlayer.invoke("create")
                        }
                        ItemMenu(label = "Registered Players", enabled = true) {
                            onCreatePlayer.invoke("seeAll")
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        ),
        *latestMatchGroup(
            list = onGoingMatches,
            gotoMatch = { type, id -> gotoMatch?.invoke(type, id) },
            seeAllMatch = seeAllMatch::invoke
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun latestMatchGroup(
    list: List<MatchViewParam>,
    gotoMatch: ((String, Int) -> Unit)? = null,
    seeAllMatch: () -> Unit,
) = arrayOf(
    ChildLayout(
        contentType = "latest_match_label",
        content = {
            AnimatedVisibility(visible = list.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "On Going Match",
                        modifier = Modifier.wrapContentWidth()
                    )
                    Text(
                        text = "See All",
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(start = 10.dp)
                            .padding(vertical = 10.dp)
                            .clickable { seeAllMatch.invoke() }
                    )
                }
            }
        }
    ),
    ChildLayout(
        contentType = "latest_match_items",
        items = list,
        content = { item ->
            AnimatedVisibility(visible = list.isNotEmpty()) {
                LoadItemAfterSafeCast<MatchViewParam>(item) { data ->
                    Card(
                        onClick = {
                            gotoMatch?.invoke(data.matchType.name, data.id)
                        },
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 5.dp,
                            bottomEnd = 5.dp,
                            bottomStart = 5.dp
                        ),
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp),
                    ) {
                        MatchItem(
                            item = data,
                            onClick = { gotoMatch?.invoke(data.matchType.name, data.id) }
                        )
                    }
                }
            }
        }
    ),
)

@Composable
private fun MenuGroup(
    label: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            modifier = Modifier.align(Alignment.Start),
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(max = 400.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemMenu(
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Card(
        onClick = { onClick.invoke() },
        enabled = enabled,
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 5.dp,
            bottomEnd = 20.dp,
            bottomStart = 5.dp
        ),
        modifier = Modifier
            .size(width = 160.dp, height = 70.dp)
            .padding(5.dp),
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(label, Modifier.align(Alignment.Center))
        }
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(changeThemeData = ChangeThemeState(), onChangeTheme = {}, onCreateMatch = {}, onCreatePlayer = {}, seeAllMatch = {})
}