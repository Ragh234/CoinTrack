package com.compose.cryptocurrency.presentation.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = AppPrimaryColor,
    primaryVariant = TextWhite,
    secondary = Color(0xFF9AA3B2),
    secondaryVariant = TextWhite,
    background = Color(0xFF101318),
    surface = Color(0xFF1B202A),
    error = TextRed,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite,
    onError = TextWhite
)

private val LightColorPalette = lightColors(
    primary = AppPrimaryColor,
    primaryVariant = TextBlue,
    secondary = Color(0xFF5D6675),
    secondaryVariant = TextWhite,
    background = CardBgColor,
    surface = Color.White,
    error = TextRed,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onBackground = TextBlue,
    onSurface = TextBlue,
    onError = TextWhite
)

@Composable
fun CryptocurrencyAppYTTheme(darkTheme: Boolean = true, content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
