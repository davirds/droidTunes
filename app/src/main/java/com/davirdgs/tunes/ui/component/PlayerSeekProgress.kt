package com.davirdgs.tunes.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.davirdgs.tunes.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PlayerSeekProgress(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    progressColor: Color,
    thumbColor: Color,
    position: Long,
    duration: Long,
    progress: Float,
    onSeekChange: (Float) -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        CustomSlider(
            modifier = Modifier.fillMaxWidth(),
            value = progress,
            backgroundColor = backgroundColor,
            progressColor = progressColor,
            thumbColor = thumbColor,
            onValueChange = onSeekChange,
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = position.toMinutes(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = duration.toMinutes(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

private fun Long.toMinutes(): String {
    val time = if (this > 0) this else 0L
    return SimpleDateFormat("m:ss", Locale.US).format(time)
}

@Preview
@Composable
private fun PlayerSeekProgressPreview() {
    AppTheme {
        PlayerSeekProgress(
            modifier = Modifier,
            backgroundColor = Color.LightGray,
            progressColor = Color.Gray,
            thumbColor = Color.DarkGray,
            position = 1000L,
            duration = 100000L,
            progress = 0.01f,
            onSeekChange = {}
        )
    }
}
