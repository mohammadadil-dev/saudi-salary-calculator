package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreenLight
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** Full-width filled gradient CTA with a press-scale bounce and haptic tick. Used for the primary
 * action on every screen (Start Calculation, Calculate Net Salary, Export PDF, Compare Offers). */
@Composable
fun PrimaryButton(
  text: String,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  loading: Boolean = false,
  icon: ImageVector? = null,
  onClick: () -> Unit
) {
  val haptics = LocalHapticFeedback.current
  val interactionSource = remember { MutableInteractionSource() }
  val pressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (pressed) 0.97f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "primary-button-scale"
  )
  Row(
    modifier = modifier
      .fillMaxWidth()
      .graphicsLayer { scaleX = scale; scaleY = scale }
      .clip(RoundedCornerShape(16.dp))
      .background(
        if (enabled) Brush.linearGradient(listOf(BrandGreen, BrandGreenLight))
        else Brush.linearGradient(listOf(Color.Gray, Color.Gray))
      )
      .clickable(
        interactionSource = interactionSource,
        indication = rememberRipple(color = Color.White),
        enabled = enabled && !loading
      ) {
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        onClick()
      }
      .padding(vertical = 16.dp),
    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (loading) {
      CircularProgressIndicator(
        modifier = Modifier
          .size(18.dp)
          .padding(end = 10.dp),
        color = Color.White,
        strokeWidth = 2.dp
      )
    } else {
      icon?.let {
        Icon(it, contentDescription = null, tint = Color.White, modifier = Modifier.padding(end = 8.dp))
      }
    }
    Text(text, color = Color.White, fontWeight = FontWeight.Black)
  }
}

/** Compact pill CTA with a soft breathing glow ring and a trailing arrow that nudges on a loop, on
 * top of the same press-scale bounce + haptic tick as [PrimaryButton], to read as more inviting to
 * tap than a flat bar. Content-sized by default (e.g. Home's "Start Calculation"), but accepts
 * [Modifier.fillMaxWidth] like any other CTA — [CircleShape] degrades gracefully to a full
 * "stadium" pill shape at any width. [enabled]/[loading] mirror [PrimaryButton] so screens that
 * gate their main action (e.g. the wizard's Review step) can reuse this in place of [PrimaryButton]
 * without losing that behavior. */
@Composable
fun InteractiveCTA(
  text: String,
  modifier: Modifier = Modifier,
  icon: ImageVector = Icons.Filled.Calculate,
  enabled: Boolean = true,
  loading: Boolean = false,
  onClick: () -> Unit
) {
  val haptics = LocalHapticFeedback.current
  val interactionSource = remember { MutableInteractionSource() }
  val pressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (pressed) 0.95f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "interactive-cta-scale"
  )

  val transition = rememberInfiniteTransition(label = "interactive-cta-pulse")
  val glowAlpha by transition.animateFloat(
    initialValue = 0.20f,
    targetValue = 0.55f,
    animationSpec = infiniteRepeatable(
      animation = tween(1100, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "interactive-cta-glow"
  )
  val arrowOffset by transition.animateFloat(
    initialValue = 0f,
    targetValue = 5f,
    animationSpec = infiniteRepeatable(
      animation = tween(700, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "interactive-cta-arrow"
  )

  Row(
    modifier = modifier
      .graphicsLayer { scaleX = scale; scaleY = scale }
      .clip(CircleShape)
      .background(
        if (enabled) Brush.linearGradient(listOf(BrandGreen, BrandGreenLight))
        else Brush.linearGradient(listOf(Color.Gray, Color.Gray))
      )
      .border(1.5.dp, Color.White.copy(alpha = if (enabled) glowAlpha else 0f), CircleShape)
      .clickable(
        interactionSource = interactionSource,
        indication = rememberRipple(color = Color.White),
        enabled = enabled && !loading,
        onClick = {
          haptics.performHapticFeedback(HapticFeedbackType.LongPress)
          onClick()
        }
      )
      .padding(horizontal = 26.dp, vertical = 14.dp),
    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (loading) {
      CircularProgressIndicator(
        modifier = Modifier.size(18.dp),
        color = Color.White,
        strokeWidth = 2.dp
      )
    } else {
      Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
    }
    androidx.compose.foundation.layout.Spacer(Modifier.size(10.dp))
    Text(text, color = Color.White, fontWeight = FontWeight.Black)
    if (!loading) {
      androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
      Icon(
        Icons.Filled.ArrowForward,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier.size(16.dp).graphicsLayer { translationX = arrowOffset }
      )
    }
  }
}

/** Outlined, lower-emphasis sibling to [PrimaryButton] for secondary actions (Share, Save,
 * Edit/Back) that should not visually compete with the screen's main CTA. */
@Composable
fun SecondaryButton(
  text: String,
  modifier: Modifier = Modifier,
  darkMode: Boolean = false,
  enabled: Boolean = true,
  icon: ImageVector? = null,
  onClick: () -> Unit
) {
  val haptics = LocalHapticFeedback.current
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .border(1.5.dp, BrandGreen.copy(alpha = if (enabled) 0.7f else 0.3f), RoundedCornerShape(16.dp))
      .clickable(enabled = enabled) {
        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        onClick()
      }
      .padding(vertical = 14.dp),
    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    icon?.let {
      Icon(it, contentDescription = null, tint = BrandGreen, modifier = Modifier.padding(end = 8.dp))
    }
    Text(text, color = if (enabled) BrandGreen else designSystemContentColor(darkMode).copy(alpha = 0.4f), fontWeight = FontWeight.Bold)
  }
}
