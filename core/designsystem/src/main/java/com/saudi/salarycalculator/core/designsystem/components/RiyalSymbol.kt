package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.R

/** The official Saudi Riyal currency mark. Source art is a plain black glyph, pre-processed
 * (white background stripped to alpha) so it can be tinted to match whatever text color
 * surrounds it — used everywhere the app would otherwise print the word "SAR"/"ريال". */
@Composable
fun RiyalSymbol(
  modifier: Modifier = Modifier,
  tint: Color = Color.Unspecified,
  size: Dp = 14.dp,
  contentDescription: String? = null
) {
  Image(
    painter = painterResource(R.drawable.ic_riyal),
    contentDescription = contentDescription,
    modifier = modifier.size(size),
    colorFilter = if (tint == Color.Unspecified) null else ColorFilter.tint(tint)
  )
}
