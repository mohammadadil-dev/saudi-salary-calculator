package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** One row in a calculation history list — used by both Home's recent-3 preview and Settings'
 * full history so per-record edit/delete actions look and behave identically everywhere history
 * shows up. [onEdit] is null for record types that don't carry a structured input snapshot (e.g.
 * offer comparisons), in which case only the delete action is shown. */
@Composable
fun HistoryRecordCard(
  title: String,
  subtitle: String,
  darkMode: Boolean,
  editContentDescription: String,
  deleteContentDescription: String,
  dateLabel: String? = null,
  onEdit: (() -> Unit)? = null,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  GlassCard(darkMode = darkMode, modifier = modifier) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Top
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          title,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.SemiBold,
          color = designSystemContentColor(darkMode)
        )
        Text(
          subtitle,
          style = MaterialTheme.typography.bodyMedium,
          color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
        )
        dateLabel?.let {
          Text(
            it,
            style = MaterialTheme.typography.labelSmall,
            color = designSystemContentColor(darkMode).copy(alpha = 0.45f)
          )
        }
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        if (onEdit != null) {
          IconButton(onClick = onEdit) {
            Icon(Icons.Filled.Edit, contentDescription = editContentDescription, tint = BrandGreen)
          }
        }
        IconButton(onClick = onDelete) {
          Icon(Icons.Filled.Delete, contentDescription = deleteContentDescription, tint = BrandRed)
        }
      }
    }
  }
}
