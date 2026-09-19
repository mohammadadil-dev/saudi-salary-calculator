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
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
  // 4+ options (e.g. a 4-city picker) leave each pill only a quarter of the row's width — the
  // labelLarge/6.dp combination used for the common 2-3 option case (Saudi/Non-Saudi, GOSI
  // system) is too wide for that and wraps mid-word ("Jeddah" -> "Jedda"/"h"). Stepping down to
  // a smaller label style and tighter padding once there are more options keeps every option on
  // one line without shrinking the common cases at all.
  val manyOptions = options.size >= 4
  val labelStyle = if (manyOptions) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelLarge
  val horizontalPadding = if (manyOptions) 2.dp else 6.dp
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
          .padding(horizontal = horizontalPadding, vertical = 10.dp),
        contentAlignment = Alignment.Center
      ) {
        // maxLines = 2 (rather than forcing a single line) lets longer options — GOSI system
        // labels, some Arabic translations that run longer than their English source — wrap
        // within their own pill instead of visually overflowing into the neighboring option.
        // TextOverflow.Ellipsis is a last-resort safety net for anything that still doesn't fit.
        Text(
          option,
          style = labelStyle,
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
 * typing on a numeric keyboard is more friction than tapping. [increaseContentDescription] and
 * [decreaseContentDescription] are required (no default) rather than left to fall back to
 * something generic, so every call site is forced to supply a real, localized label for what
 * would otherwise be two unlabeled icon-only buttons for screen-reader users. */
@Composable
fun NumberStepper(
  value: Int,
  darkMode: Boolean,
  increaseContentDescription: String,
  decreaseContentDescription: String,
  modifier: Modifier = Modifier,
  min: Int = 0,
  max: Int = 365,
  onValueChange: (Int) -> Unit
) {
  val haptics = LocalHapticFeedback.current
  // spacedBy(6.dp): without it the two circular buttons and the number between them sit flush
  // against each other with zero gap, which reads as cramped/"squished together" — especially
  // now that both buttons are solid filled circles rather than a faint tint.
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    StepperButton(
      icon = Icons.Filled.Remove,
      contentDescription = decreaseContentDescription,
      enabled = value > min
    ) {
      haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
      onValueChange((value - 1).coerceAtLeast(min))
    }
    Text(
      value.toString(),
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Black,
      color = designSystemContentColor(darkMode),
      // 32dp, matching the (now smaller) stepper circles rather than the old 48dp — a wide
      // number column next to compact 32dp buttons was throwing off the proportions of the
      // control as a whole, not just the buttons themselves.
      modifier = Modifier.width(32.dp),
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
    StepperButton(
      icon = Icons.Filled.Add,
      contentDescription = increaseContentDescription,
      enabled = value < max
    ) {
      haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
      onValueChange((value + 1).coerceAtMost(max))
    }
  }
}

@Composable
private fun StepperButton(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  contentDescription: String,
  enabled: Boolean,
  onClick: () -> Unit
) {
  Box(
    // The visible circle is a compact 32dp — minimumInteractiveComponentSize() pads the actual
    // tap target out to Material's 48dp accessible minimum around it invisibly, so this stays
    // just as easy to tap as the old 48dp circle without looking that big. (Before that fix,
    // this was a 36dp circle with only a 36dp tap target — the small-and-inaccessible version;
    // this is small-and-accessible instead, which is the combination that was missing.)
    //
    // The background/icon colors below are NOT conditional on [enabled] — both the +/- buttons
    // always render as the exact same solid BrandGreen circle with a white glyph, full stop.
    // Disabled state is expressed as a single uniform alpha dim over that identical button
    // rather than switching to a different color family (a previous version swapped the
    // disabled background to a near-invisible gray tint, which made the +/- pair look like two
    // different controls whenever one end of the range was hit, e.g. decrementing to 0).
    modifier = Modifier
      .minimumInteractiveComponentSize()
      .size(32.dp)
      .clip(CircleShape)
      .background(BrandGreen)
      .alpha(if (enabled) 1f else 0.35f)
      .clickable(enabled = enabled, onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      icon,
      contentDescription = contentDescription,
      tint = Color.White,
      modifier = Modifier.size(15.dp)
    )
  }
}
