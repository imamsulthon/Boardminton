package com.imams.boardminton.ui.screen.player

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.imams.boardminton.R
import com.imams.boardminton.data.asDateTime
import com.imams.boardminton.data.epochToAge
import com.imams.boardminton.ui.component.RowInfoData
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.boardminton.ui.utils.horizontalGradientBackground
import com.imams.boardminton.ui.utils.sendWhatsappMessage

private const val initialImageFloat = 150f

@Composable
fun PlayerDetailScreen(
    viewModel: PlayerDetailVM = hiltViewModel(),
    playerId: Int,
    onEdit: (Int) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getPlayer(playerId)
    }

    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    Content(
        uiState = uiState,
        onEdit = { onEdit.invoke(uiState.id)},
        onSendMessage = {
        context.sendWhatsappMessage(uiState.phoneNumber,
            message = "Hai ${uiState.fullName}, This message from Boardminton")
        }
    )
}

@Composable
internal fun Content(
    uiState: CreatePlayerState,
    onEdit: () -> Unit,
    onSendMessage: () -> Unit,
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .semantics { testTag = "Profile Screen" }
        ) {
            val scrollState = rememberScrollState(0)
            TopBackground()
            TopAppBarView(uiState, scrollState.value.toFloat(), onEdit = onEdit::invoke )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState)
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                TopScrollingContent(uiState, scrollState, onEdit = onEdit::invoke, onSendMessage = onSendMessage)
                BottomScrollingContent(uiState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarView(
    state: CreatePlayerState,
    scroll: Float,
    onEdit: () -> Unit,
) {
    if (scroll > initialImageFloat + 5) {
        TopAppBar(
            title = {
                Text(text = state.fullName, color = MaterialTheme.colorScheme.surface)
            },
            navigationIcon = {
                val imgRes = if (state.gender.equals("man", true))
                    R.drawable.ic_player_man_color else R.drawable.ic_player_woman_color
                Image(
                    painter = painterResource(imgRes),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            },
            actions = {
                IconButton(onClick = onEdit::invoke ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            },
        )
    }
}

@Composable
private fun TopBackground() {
    val gradient = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    )
    Spacer(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .horizontalGradientBackground(gradient)
    )
}

@Composable
fun TopScrollingContent(
    state: CreatePlayerState,
    scrollState: ScrollState,
    onEdit: () -> Unit,
    onSendMessage: () -> Unit,
) {
    val visibilityChangeFloat = scrollState.value > initialImageFloat - 20

    Row {
        val imgRes = if (state.gender.equals("man", true))
            R.drawable.ic_player_man_color else R.drawable.ic_player_woman_color
        AnimatedImage(
            state.photoProfileUri,
            imgRes,
            scroll = scrollState.value.toFloat()
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp, top = 48.dp)
                .alpha(animateFloatAsState(if (visibilityChangeFloat) 0f else 1f, label = "").value)
        ) {
            Text(
                text = state.fullName,
                style = typography.displayLarge.copy(fontSize = 22.sp),
                modifier = Modifier.padding(bottom = 4.dp), maxLines = 3,
            )
            Text(
                text = "${state.gender} (${state.dob.epochToAge()} years old)",
                style = typography.labelMedium,
                modifier = Modifier.padding(bottom = 8.dp), maxLines = 2
            )
            Row(horizontalArrangement = Arrangement.Start) {
                val iSize = 20.dp
                IconButton(
                    onClick = onEdit::invoke,
                    modifier = Modifier.size(width = iSize, height = iSize),
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = onEdit::invoke,
                    enabled = false,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .size(width = iSize, height = iSize),
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = onSendMessage::invoke,
                    enabled = state.phoneNumber.isNotEmpty(),
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .size(width = iSize, height = iSize),
                ) { Icon(imageVector = Icons.Default.Send, contentDescription = null) }
            }
        }
    }
}

@Composable
fun AnimatedImage(
    imgUriPath: String? = null,
    @DrawableRes imgDefault: Int,
    scroll: Float
) {
    val dynamicAnimationSizeValue = (initialImageFloat - scroll).coerceIn(36f, initialImageFloat)
    val i = ImageRequest.Builder(LocalContext.current)
        .data(if (imgUriPath.isNullOrEmpty())  imgDefault else Uri.parse(imgUriPath))
        .crossfade(true)
        .build()
    AsyncImage(
        model = i,
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .padding(start = 16.dp)
            .size(animateDpAsState(Dp(dynamicAnimationSizeValue), label = "").value)
            .clip(CircleShape)
            .background(Color.White)
    )
}


@Composable
fun BottomScrollingContent(state: CreatePlayerState) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .fillMaxSize()
    ) {
        SectionPlayerData(state = state)
    }
}

@Composable
fun SectionPlayerData(
    modifier: Modifier = Modifier,
    state: CreatePlayerState,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Player Data",
                style = typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(modifier = Modifier
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colorScheme.onSurface))
            Column {
                RowInfoData(label = "Player ID", content = "${state.id}")
                RowInfoData(label = "Full Name", content = state.fullName)
                RowInfoData(label = "DoB", content = "${state.dob.asDateTime("dd MMM yyyy")}")
                RowInfoData(label = "Gender", content = state.gender)
                RowInfoData(label = "Phone Number", content = state.phoneNumber)
                RowInfoData(label = "Hand Play", content = state.handPlay)
                RowInfoData(label = "Height", content = "${state.height} cm")
                RowInfoData(label = "Weight", content = "${state.weight} kg")
            }
        }
    }

}

@Preview
@Composable
private fun PlayerDetailPreview() {
    Content(
        CreatePlayerState(id = 10,
            firstName = "Imam", lastName = "Sulthon",
            weight = 56, height = 168,
            gender = "Man", handPlay = "Left",
        ),
        onEdit = {}, onSendMessage = {},
    )
}


