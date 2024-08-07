package com.davirdgs.tunes.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.davirdgs.tunes.ui.theme.AppTheme

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    maxLines: Int = 1,
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    var hasFocus by remember { mutableStateOf(false) }
    BasicTextField(
        modifier = modifier.onFocusChanged { hasFocus = it.hasFocus },
        value = value,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        onValueChange = onValueChange,
        maxLines = maxLines,
        textStyle = textStyle,
        decorationBox = { inputField ->
            Row(
                modifier = Modifier
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Sharp.Search,
                    contentDescription = null
                )
                if (value.isBlank() && !hasFocus) {
                    Text(
                        text = "Search for songs",
                        style = textStyle.copy(
                            color = textStyle.color.copy(alpha = 0.5f)
                        )
                    )
                } else {
                    inputField()
                }
            }
        }
    )
}

@Preview
@Composable
private fun SearchInputPreview() {
    AppTheme {
        SearchField(
            value = "",
            onValueChange = {},
            onSearch = {}
        )
    }
}
