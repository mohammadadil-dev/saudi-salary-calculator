package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** Small "i" affordance that reveals a one-line explanation on tap, used next to wizard fields
 * whose meaning isn't obvious at a glance (e.g. overtime rate override, GOSI inclusion). */
@Composable
fun InfoTooltip(text: String, darkMode: Boolean, modifier: Modifier = Modifier) {
  var expanded by remember { mutableStateOf(false) }
  Box(modifier = modifier) {
    Icon(
      Icons.Filled.Info,
      contentDescription = text,
      tint = BrandGreen.copy(alpha = 0.8f),
      modifier = Modifier
        .clip(RoundedCornerShape(50))
        .clickable { expanded = !expanded }
        .padding(4.dp)
    )
  }
  AnimatedVisibility(visible = expanded, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .background(if (darkMode) Color.White.copy(alpha = 0.06f) else Color(0xFFEFF4F0))
        .padding(10.dp)
    ) {
      Text(text, style = MaterialTheme.typography.labelSmall, color = designSystemContentColor(darkMode).copy(alpha = 0.75f))
    }
  }
}
