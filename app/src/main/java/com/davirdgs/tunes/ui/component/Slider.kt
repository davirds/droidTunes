package com.davirdgs.tunes.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.davirdgs.tunes.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    height: Dp = 12.dp,
    backgroundColor: Color,
    progressColor: Color,
    thumbColor: Color,
    enabled: Boolean = true,
    value: Float,
    onValueChange: (Float) -> Unit,
) {
    val cap = StrokeCap.Square
    Slider(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = { onValueChange(it) },
        enabled = enabled,
        track = { sliderState ->
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height),
                onDraw = {
                    drawLine(
                        color = backgroundColor,
                        cap = cap,
                        strokeWidth = size.height,
                        start = Offset(x = 0f, y = size.height / 2),
                        end = Offset(x = size.width, y = size.height / 2)
                    )

                    drawLine(
                        color = progressColor,
                        cap = cap,
                        strokeWidth = size.height,
                        start = Offset(x = 0f, y = size.height / 2),
                        end = Offset(x = size.width * sliderState.value, y = size.height / 2)
                    )
                    val radius = size.height / 2
                    val circlePositionX = sliderState.value * size.width + radius
                    drawCircle(
                        color = thumbColor,
                        radius = radius,
                        center = Offset(x = circlePositionX, y = radius)
                    )
                }
            )
        },
        thumb = { },
    )
}

@Preview(
    showBackground = true,
)
@Composable
private fun CustomSliderPreview() {
    AppTheme {
        CustomSlider(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.LightGray,
            progressColor = Color.Gray,
            thumbColor = Color.DarkGray,
            value = 0.5f,
            onValueChange = { }
        )
    }
}
