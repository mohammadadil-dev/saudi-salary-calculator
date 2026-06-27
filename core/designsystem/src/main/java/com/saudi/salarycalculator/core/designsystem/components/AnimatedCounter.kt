package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import java.util.Locale

/** Animates a number counting up from zero to [targetValue] whenever it changes, used for every
 * headline figure in the app (net salary, GOSI, EOSB, comparison deltas). [decimals] controls the
 * formatted precision and [prefix]/[suffix] let call sites attach a currency symbol or "%". */
@Composable
fun AnimatedCounter(
  targetValue: Double,
  modifier: Modifier = Modifier,
  prefix: String = "",
  suffix: String = "",
  decimals: Int = 0,
  style: TextStyle = MaterialTheme.typography.headlineSmall,
  color: Color = LocalContentColor.current,
  fontWeight: FontWeight = FontWeight.Black,
  durationMillis: Int = 900
) {
  val animated = remember { Animatable(0f) }
  LaunchedEffect(targetValue) {
    animated.animateTo(targetValue.toFloat(), animationSpec = tween(durationMillis, easing = FastOutSlowInEasing))
  }
  val formatted = remember(animated.value, decimals) {
    String.format(Locale.US, "%,.${decimals}f", animated.value)
  }
  Text(
    text = "$prefix$formatted$suffix",
    modifier = modifier,
    style = style,
    color = color,
    fontWeight = fontWeight
  )
}
