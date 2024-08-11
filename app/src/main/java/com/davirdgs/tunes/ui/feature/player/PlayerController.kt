package com.davirdgs.tunes.ui.feature.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.davirdgs.tunes.R
import com.davirdgs.tunes.ui.component.PlayerSeekProgress
import com.davirdgs.tunes.ui.theme.AppTheme

@Composable
internal fun PlayerController(
    modifier: Modifier = Modifier,
    songName: String,
    artistName: String,
    position: Long,
    duration: Long,
    progress: Float,
    isPlaying: Boolean,
    onSeekChange: (Float) -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onForward: () -> Unit,
    onBackward: () -> Unit
) {
    val playerAction = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
    val playerActionClick = if (isPlaying) onPause else onPlay
    val playerActionDescription = if (isPlaying) {
        stringResource(id = R.string.content_description_pause)
    } else {
        stringResource(id = R.string.content_description_play)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                ),
            text = songName,
            style = MaterialTheme.typography.labelLarge.copy(
                textAlign = TextAlign.Center
            )
        )
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = artistName,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        )
        PlayerSeekProgress(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            backgroundColor = MaterialTheme.colorScheme.surface,
            progressColor = MaterialTheme.colorScheme.onSurface,
            thumbColor = MaterialTheme.colorScheme.onBackground,
            position = position,
            duration = duration,
            progress = progress,
            onSeekChange = onSeekChange
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { onBackward() }
                    .padding(8.dp)
                    .size(32.dp),
                painter = painterResource(id = R.drawable.ic_backward),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                contentDescription = stringResource(id = R.string.content_description_backward)
            )
            Image(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(50))
                    .clickable { playerActionClick() },
                painter = painterResource(id = playerAction),
                contentDescription = playerActionDescription
            )
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { onForward() }
                    .padding(8.dp)
                    .size(32.dp),
                painter = painterResource(id = R.drawable.ic_forward),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                contentDescription = stringResource(id = R.string.content_description_forward)
            )
        }
    }
}

@Preview
@Composable
private fun PlayerControllerPreview() {
    AppTheme {
        PlayerController(
            songName = "Something",
            artistName = "Someone",
            position = 1000L,
            duration = 10000L,
            progress = 0.1f,
            isPlaying = false,
            onSeekChange = { },
            onPlay = { },
            onPause = { },
            onForward = { },
            onBackward = { }
        )
    }
}
