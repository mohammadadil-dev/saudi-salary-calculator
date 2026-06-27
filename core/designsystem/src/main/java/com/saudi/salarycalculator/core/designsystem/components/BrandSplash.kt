package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreenLight
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import kotlinx.coroutines.delay

/** Branded animated launch screen: a pulsing gradient badge holding the app's actual Riyal mark,
 * with the app name/tagline fading in underneath, over the same [AnimatedAppBackground] wash used
 * on every other screen so the launch feels continuous with the rest of the app rather than a
 * separate static placeholder. Shown as the Splash destination at the start of the nav graph
 * (right after the system splash screen dismisses). Tappable to skip. Lives in the design system
 * — rather than a single feature module — because Splash is the app's actual entry point,
 * conceptually shared infrastructure rather than calculator-specific UI. */
@Composable
fun BrandSplash(
  appName: String,
  tagline: String,
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  holdMillis: Long = 1100,
  onFinished: () -> Unit
) {
  var dismissed by remember { mutableStateOf(false) }
  val badgeScale = remember { Animatable(0.4f) }
  val badgeAlpha = remember { Animatable(0f) }
  val textAlpha = remember { Animatable(0f) }

  fun finish() {
    if (!dismissed) {
      dismissed = true
      onFinished()
    }
  }

  LaunchedEffect(Unit) {
    badgeAlpha.animateTo(1f, tween(360, easing = FastOutSlowInEasing))
    badgeScale.animateTo(
      1f,
      animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    )
    textAlpha.animateTo(1f, tween(420))
    delay(holdMillis)
    finish()
  }

  val infinite = rememberInfiniteTransition(label = "splash-pulse")
  val ringScale by infinite.animateFloat(
    initialValue = 1f,
    targetValue = 1.22f,
    animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "ring-scale"
  )
  val ringAlpha by infinite.animateFloat(
    initialValue = 0.32f,
    targetValue = 0f,
    animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "ring-alpha"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .clickable { finish() },
    contentAlignment = Alignment.Center
  ) {
    AnimatedAppBackground(darkMode = darkMode, modifier = Modifier.fillMaxSize())

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
      Box(contentAlignment = Alignment.Center) {
        Box(
          modifier = Modifier
            .size(148.dp)
            .graphicsLayer {
              scaleX = ringScale
              scaleY = ringScale
              alpha = ringAlpha
            }
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(BrandGreen, BrandGreenLight)))
        )
        Box(
          modifier = Modifier
            .size(108.dp)
            .graphicsLayer {
              scaleX = badgeScale.value
              scaleY = badgeScale.value
              alpha = badgeAlpha.value
            }
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(BrandGreen, BrandGreenLight))),
          contentAlignment = Alignment.Center
        ) {
          RiyalSymbol(tint = androidx.compose.ui.graphics.Color.White, size = 48.dp)
        }
      }
      Spacer(Modifier.height(22.dp))
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.graphicsLayer { alpha = textAlpha.value }
      ) {
        Text(appName, color = designSystemContentColor(darkMode), fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(6.dp))
        Text(tagline, color = designSystemContentColor(darkMode).copy(alpha = 0.65f), style = MaterialTheme.typography.bodyMedium)
      }
    }
  }
}
