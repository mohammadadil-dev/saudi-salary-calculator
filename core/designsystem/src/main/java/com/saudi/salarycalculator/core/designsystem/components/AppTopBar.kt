package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** Shared top bar for every non-Home screen: optional back action (auto-mirrored for RTL),
 * a title, and a trailing actions slot (e.g. a settings shortcut or a share icon).
 *
 * Applies its own [Modifier.statusBarsPadding] rather than relying on [androidx.compose.material3.Scaffold]
 * to reserve that space, since with edge-to-edge enforced (targetSdk 35+), Scaffold's `topBar`
 * slot is drawn from the very top of the screen and a plain [Row] with no inset handling of its
 * own renders straight under the status bar icons. */
@Composable
fun AppTopBar(
  title: String,
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  onBack: (() -> Unit)? = null,
  actions: @Composable RowScope.() -> Unit = {}
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .padding(vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (onBack != null) {
      IconButton(onClick = onBack) {
        Icon(
          Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = null,
          tint = designSystemContentColor(darkMode)
        )
      }
      Spacer(Modifier.width(2.dp))
    }
    Text(
      title,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Black,
      color = designSystemContentColor(darkMode),
      modifier = Modifier.weight(1f),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
    Row(verticalAlignment = Alignment.CenterVertically, content = actions)
  }
}
