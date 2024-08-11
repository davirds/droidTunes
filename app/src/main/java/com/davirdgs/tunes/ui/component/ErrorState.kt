package com.davirdgs.tunes.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.davirdgs.tunes.R
import com.davirdgs.tunes.ui.theme.AppTheme

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    message: String = stringResource(id = R.string.home_error_title),
    onRetry: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .imePadding()
                .padding(bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .imePadding()
                    .fillMaxWidth(0.6f),
                text = message,
                style = MaterialTheme.typography.labelMedium.copy(
                    lineBreak = LineBreak.Heading,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.size(8.dp))
            Button(
                modifier = Modifier
                    .imePadding(),
                onClick = onRetry,
                content = {
                    Text(
                        text = stringResource(id = R.string.try_again),
                        style = MaterialTheme.typography.labelMedium.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun ErrorStatePreview() {
    AppTheme {
        ErrorState(
            onRetry = {}
        )
    }
}
