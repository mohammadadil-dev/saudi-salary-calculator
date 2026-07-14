package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** Pill-shaped multi-option control used for Nationality / Sector / Contract type style fields,
 * where a dropdown would be overkill for 2-3 mutually exclusive choices. */
@Composable
fun SegmentedControl(
  options: List<String>,
  selectedIndex: Int,
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  onSelect: (Int) -> Unit
) {
  val haptics = LocalHapticFeedback.current
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(if (darkMode) Color.White.copy(alpha = 0.06f) else Color(0xFFEFF4F0))
      .padding(4.dp)
  ) {
    options.forEachIndexed { index, option ->
      val selected = index == selectedIndex
      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(11.dp))
          .background(if (selected) BrandGreen else Color.Transparent)
          .clickable {
            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onSelect(index)
          }
          .padding(horizontal = 6.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
      ) {
        // maxLines = 2 (rather than forcing a single line) lets longer options — GOSI system
        // labels, some Arabic translations that run longer than their English source — wrap
        // within their own pill instead of visually overflowing into the neighboring option.
        // TextOverflow.Ellipsis is a last-resort safety net for anything that still doesn't fit.
        Text(
          option,
          style = MaterialTheme.typography.labelLarge,
          fontWeight = if (selected) FontWeight.Black else FontWeight.Medium,
          color = if (selected) Color.White else designSystemContentColor(darkMode).copy(alpha = 0.7f),
          textAlign = TextAlign.Center,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

/** Label + description on the left, a brand-colored [Switch] on the right — the standard layout
 * for every boolean toggle in the app (GOSI included, resigned, dark mode). */
@Composable
fun ToggleRow(
  label: String,
  checked: Boolean,
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  description: String? = null,
  onCheckedChange: (Boolean) -> Unit
) {
  val haptics = LocalHapticFeedback.current
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
      Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = designSystemContentColor(darkMode))
      description?.let {
        Text(it, style = MaterialTheme.typography.bodySmall, color = designSystemContentColor(darkMode).copy(alpha = 0.55f))
      }
    }
    Switch(
      checked = checked,
      onCheckedChange = {
        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        onCheckedChange(it)
      },
      colors = SwitchDefaults.colors(checkedTrackColor = BrandGreen, checkedThumbColor = Color.White)
    )
  }
}

/** Round +/- stepper for small integer quantities (unpaid leave days, overtime hours) where
 * typing on a numeric keyboard is more friction than tapping. */
@Composable
fun NumberStepper(
  value: Int,
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  min: Int = 0,
  max: Int = 365,
  onValueChange: (Int) -> Unit
) {
  val haptics = LocalHapticFeedback.current
  Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    StepperButton(icon = Icons.Filled.Remove, enabled = value > min, darkMode = darkMode) {
      haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
      onValueChange((value - 1).coerceAtLeast(min))
    }
    Text(
      value.toString(),
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Black,
      color = designSystemContentColor(darkMode),
      modifier = Modifier.width(48.dp),
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
    StepperButton(icon = Icons.Filled.Add, enabled = value < max, darkMode = darkMode) {
      haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
      onValueChange((value + 1).coerceAtMost(max))
    }
  }
}

@Composable
private fun StepperButton(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  enabled: Boolean,
  darkMode: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .size(36.dp)
      .clip(CircleShape)
      .background(if (enabled) BrandGreen.copy(alpha = 0.14f) else Color.Transparent)
      .clickable(enabled = enabled, onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      icon,
      contentDescription = null,
      tint = if (enabled) BrandGreen else designSystemContentColor(darkMode).copy(alpha = 0.25f),
      modifier = Modifier.size(18.dp)
    )
  }
}
