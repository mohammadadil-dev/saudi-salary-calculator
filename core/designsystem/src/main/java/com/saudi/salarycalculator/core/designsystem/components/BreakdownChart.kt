package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreenLight
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import java.util.Locale
import kotlin.math.abs

/** One slice of a salary breakdown chart. Decoupled from any `core:model` type on purpose so this
 * design-system module has no dependency on the app's domain layer — callers map their own
 * `SalaryBreakdownItem` into this shape at the screen/ViewModel boundary. */
data class BreakdownSlice(
  val label: String,
  val amount: Double,
  val color: Color,
  val isDeduction: Boolean = false
)

/** Animated donut chart + tappable legend summarizing a salary breakdown (basic, housing,
 * transport, bonus, GOSI, deductions, ...). Tapping a legend row highlights it. */
@Composable
fun DonutBreakdownChart(
  slices: List<BreakdownSlice>,
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  centerLabel: String? = null
) {
  val total = slices.sumOf { abs(it.amount) }.coerceAtLeast(1.0)
  var selectedLabel by remember { mutableStateOf<String?>(null) }
  val animationProgress = remember { Animatable(0f) }
  LaunchedEffect(slices) {
    animationProgress.snapTo(0f)
    animationProgress.animateTo(1f, tween(900, easing = FastOutSlowInEasing))
  }

  Column(modifier = modifier.fillMaxWidth()) {
    Box(modifier = Modifier.size(150.dp).align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
      Canvas(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        val strokeWidth = 20.dp.toPx()
        val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
        val topLeft = androidx.compose.ui.geometry.Offset(strokeWidth / 2f, strokeWidth / 2f)
        var startAngle = -90f
        slices.forEach { slice ->
          val sweep = (abs(slice.amount) / total * 360.0 * animationProgress.value).toFloat()
          val isDimmed = selectedLabel != null && selectedLabel != slice.label
          drawArc(
            color = if (isDimmed) slice.color.copy(alpha = 0.25f) else slice.color,
            startAngle = startAngle,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = stroke
          )
          startAngle += sweep
        }
      }
      centerLabel?.let {
        RiyalSymbol(
          tint = designSystemContentColor(darkMode).copy(alpha = 0.6f),
          size = 16.dp,
          contentDescription = it
        )
      }
    }
    Spacer(Modifier.height(14.dp))
    slices.forEach { slice ->
      BreakdownLegendRow(
        slice = slice,
        darkMode = darkMode,
        selected = selectedLabel == slice.label,
        onClick = { selectedLabel = if (selectedLabel == slice.label) null else slice.label }
      )
    }
  }
}

@Composable
private fun BreakdownLegendRow(slice: BreakdownSlice, darkMode: Boolean, selected: Boolean, onClick: () -> Unit) {
  val background by androidx.compose.animation.animateColorAsState(
    targetValue = if (selected) slice.color.copy(alpha = 0.14f) else Color.Transparent,
    animationSpec = tween(200),
    label = "legend-row-bg"
  )
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(background)
      .clickable(onClick = onClick)
      .padding(vertical = 7.dp, horizontal = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
      Box(Modifier.size(10.dp).clip(CircleShape).background(slice.color))
      Spacer(Modifier.width(8.dp))
      Text(
        slice.label,
        color = designSystemContentColor(darkMode).copy(alpha = 0.8f),
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }
    Text(
      "${if (slice.isDeduction) "− " else ""}${String.format(Locale.US, "%,.0f", abs(slice.amount))}",
      color = if (slice.isDeduction) com.saudi.salarycalculator.core.designsystem.theme.BrandRed else designSystemContentColor(darkMode),
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyMedium
    )
  }
}

/** Two animated horizontal bars comparing total earnings against total deductions. */
@Composable
fun EarningsVsDeductionsBarChart(
  earnings: Double,
  deductions: Double,
  earningsLabel: String,
  deductionsLabel: String,
  darkMode: Boolean,
  modifier: Modifier = Modifier
) {
  val max = maxOf(earnings, deductions).coerceAtLeast(1.0)
  val earningsProgress by animateFloatAsState((earnings / max).toFloat(), tween(900, easing = FastOutSlowInEasing), label = "earnings-bar")
  val deductionsProgress by animateFloatAsState((deductions / max).toFloat(), tween(900, easing = FastOutSlowInEasing), label = "deductions-bar")
  Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    BarRow(label = earningsLabel, value = earnings, progress = earningsProgress, color = BrandGreen, darkMode = darkMode)
    BarRow(label = deductionsLabel, value = deductions, progress = deductionsProgress, color = com.saudi.salarycalculator.core.designsystem.theme.BrandRed, darkMode = darkMode)
  }
}

@Composable
private fun BarRow(label: String, value: Double, progress: Float, color: Color, darkMode: Boolean) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Text(label, style = MaterialTheme.typography.labelMedium, color = designSystemContentColor(darkMode).copy(alpha = 0.7f), fontWeight = FontWeight.SemiBold)
      Text(String.format(Locale.US, "%,.0f", value), style = MaterialTheme.typography.labelMedium, color = designSystemContentColor(darkMode), fontWeight = FontWeight.Bold)
    }
    Spacer(Modifier.height(6.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(10.dp)
        .clip(RoundedCornerShape(50))
        .background(if (darkMode) Color.White.copy(alpha = 0.08f) else Color(0xFFE7ECE8))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(progress.coerceIn(0f, 1f))
          .fillMaxHeight()
          .clip(RoundedCornerShape(50))
          .background(color)
      )
    }
  }
}

/** Net-of-gross progress ring/bar shown on the Result screen, with the net% printed in the
 * center label area. */
@Composable
fun NetSalaryProgressIndicator(
  netSalary: Double,
  grossSalary: Double,
  title: String,
  darkMode: Boolean,
  modifier: Modifier = Modifier
) {
  val ratio = if (grossSalary > 0) (netSalary / grossSalary).toFloat().coerceIn(0f, 1f) else 0f
  val progress by animateFloatAsState(ratio, tween(900, easing = FastOutSlowInEasing), label = "net-progress")
  Column(modifier = modifier.fillMaxWidth()) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Text(title, style = MaterialTheme.typography.labelLarge, color = designSystemContentColor(darkMode).copy(alpha = 0.7f), fontWeight = FontWeight.SemiBold)
      Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, color = BrandGreen, fontWeight = FontWeight.Black)
    }
    Spacer(Modifier.height(8.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(14.dp)
        .clip(RoundedCornerShape(50))
        .background(if (darkMode) Color.White.copy(alpha = 0.08f) else Color(0xFFE7ECE8))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(progress)
          .fillMaxHeight()
          .clip(RoundedCornerShape(50))
          .background(Brush.horizontalGradient(listOf(BrandGreen, BrandGreenLight)))
      )
    }
  }
}
