package com.saudi.salarycalculator.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Brand palette: premium Saudi-fintech identity built around the national green, with a warm
// gold secondary for highlights/positive numbers and a cool teal tertiary for charts. Lives in
// :core:designsystem so both :app and :feature:calculator (and any future feature module) read
// from the same single source of truth instead of hand-picked one-off colors.
val BrandGreen = Color(0xFF0F7A4D)
val BrandGreenDark = Color(0xFF0B5C3A)
val BrandGreenLight = Color(0xFF34C28E)
val BrandGold = Color(0xFFC8932B)
val BrandTeal = Color(0xFF2DA8A8)
val BrandRed = Color(0xFFE5484D)

val LightColors = lightColorScheme(
  primary = BrandGreen,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFD6F0E2),
  onPrimaryContainer = Color(0xFF06301E),
  secondary = BrandGold,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFF6E6C8),
  onSecondaryContainer = Color(0xFF4A3408),
  tertiary = BrandTeal,
  onTertiary = Color.White,
  background = Color(0xFFF7F9F6),
  onBackground = Color(0xFF10201B),
  surface = Color.White,
  onSurface = Color(0xFF10201B),
  surfaceVariant = Color(0xFFEFF4F0),
  onSurfaceVariant = Color(0xFF445048),
  outline = Color(0xFFD6DDD8),
  error = BrandRed,
  onError = Color.White
)

val DarkColors = darkColorScheme(
  primary = BrandGreenLight,
  onPrimary = Color(0xFF04261A),
  primaryContainer = Color(0xFF0B5C3A),
  onPrimaryContainer = Color(0xFFD6F0E2),
  secondary = BrandGold,
  onSecondary = Color(0xFF332305),
  secondaryContainer = Color(0xFF5A4310),
  onSecondaryContainer = Color(0xFFF6E6C8),
  tertiary = BrandTeal,
  onTertiary = Color.White,
  background = Color(0xFF0A1612),
  onBackground = Color.White,
  surface = Color(0xFF11201B),
  onSurface = Color.White,
  surfaceVariant = Color(0xFF1C2E27),
  onSurfaceVariant = Color(0xFFC4D2CB),
  outline = Color(0xFF3C5249),
  error = Color(0xFFFF6B6B),
  onError = Color.White
)

val AppShapes = Shapes(
  extraSmall = RoundedCornerShape(8.dp),
  small = RoundedCornerShape(12.dp),
  medium = RoundedCornerShape(16.dp),
  large = RoundedCornerShape(20.dp),
  extraLarge = RoundedCornerShape(28.dp)
)

private val baseTypography = Typography()
val AppTypography = baseTypography.copy(
  headlineSmall = baseTypography.headlineSmall.copy(fontWeight = FontWeight.Black),
  headlineLarge = baseTypography.headlineLarge.copy(fontWeight = FontWeight.Black),
  titleLarge = baseTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
  titleMedium = baseTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
)

/** Resolves the correct on-surface text color for the current dark/light mode. Every component
 * in this package takes an explicit [darkMode] flag rather than reading MaterialTheme directly,
 * so screens can preview/force a mode and so the design system stays decoupled from any single
 * app-level ViewModel. */
fun designSystemContentColor(darkMode: Boolean): Color = if (darkMode) Color.White else Color(0xFF10201B)

@Composable
fun SalaryCalculatorTheme(
  darkTheme: Boolean = false,
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = if (darkTheme) DarkColors else LightColors,
    typography = AppTypography,
    shapes = AppShapes,
    content = content
  )
}
