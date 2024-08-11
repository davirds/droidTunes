package com.davirdgs.tunes.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.davirdgs.tunes.ui.theme.AppTheme

@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    message: String = "No songs found yet"
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .imePadding()
                .fillMaxWidth(0.6f)
                .padding(bottom = 120.dp),
            text = message,
            style = MaterialTheme.typography.labelMedium.copy(
                lineBreak = LineBreak.Heading,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview
@Composable
private fun EmptyStatePreview() {
    AppTheme {
        EmptyState()
    }
}
