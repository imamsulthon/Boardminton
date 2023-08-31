package com.imams.boardminton.ui.component

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.ui.utils.getLabel

@Composable
fun MainNameBoardView(
    modifier: Modifier = Modifier,
    team1: TeamViewParam,
    team2: TeamViewParam,
    scoreA: Int,
    scoreB: Int,
    histories: List<GameViewParam>,
    single: Boolean = true,
    isExpand: Boolean = true,
) {
    val teamLabel1 = team1.getLabel()
    val teamLabel2 = team2.getLabel()
    var isExpanded by remember { mutableStateOf(isExpand) }

    Column(modifier = modifier.drawBorder()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${if (single) "Single" else "Double"} Match",
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = { isExpanded = !isExpanded}) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        AnimatedVisibility(visible = isExpanded) {
            LazyRow {
                item {
                    Column {
                        Text(
                            text = teamLabel1, modifier = Modifier.padding(top = 5.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = teamLabel2, modifier = Modifier.padding(top = 5.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                }
                items(histories.size) {
                    Column {
                        Text(
                            text = histories[it].scoreA.point.toString(),
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = histories[it].scoreB.point.toString(),
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                if (histories.size >= 3) return@LazyRow
                item {
                    Column {
                        Text(
                            text = scoreA.toString(),
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = scoreB.toString(),
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Modifier.drawBorder() = this
    .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(1.dp)
    )
    .padding(vertical = 10.dp, horizontal = 10.dp)

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imgUriPath: String? = null,
    @DrawableRes imgDefault: Int,
    size: Dp = 48.dp
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(if (imgUriPath.isNullOrEmpty())  imgDefault else Uri.parse(imgUriPath))
            .crossfade(true)
            .error(imgDefault)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier.size(size).clip(CircleShape)
    )
}