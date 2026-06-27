package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** Title (+ optional subtitle / trailing slot, e.g. a "View all" link) used to introduce every
 * section of every screen: Home's "Recent calculations", Result's "Salary breakdown", etc. */
@Composable
fun SectionHeader(
  title: String,
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  subtitle: String? = null,
  trailing: @Composable (() -> Unit)? = null
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Black,
        color = designSystemContentColor(darkMode),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      subtitle?.let {
        Text(
          it,
          style = MaterialTheme.typography.bodyMedium,
          color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
        )
      }
    }
    trailing?.let {
      Spacer(Modifier.width(10.dp))
      it()
    }
  }
}
