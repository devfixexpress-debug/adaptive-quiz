package com.veltia.adaptivequiz.mobile.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF3157D5),
    secondary = Color(0xFF58627B),
    tertiary = Color(0xFF006C65),
    surface = Color(0xFFFAF9FF),
    surfaceVariant = Color(0xFFE8E8F1)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB8C3FF),
    secondary = Color(0xFFC0C5DC),
    tertiary = Color(0xFF7BDBD1)
)

@Composable
fun AdaptiveQuizTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content
    )
}
