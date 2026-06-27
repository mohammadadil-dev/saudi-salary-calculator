package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import java.util.Locale

/** One key/value line item inside a payslip section (e.g. "Housing allowance" / "1,500"). */
data class PayslipLine(val label: String, val amount: Double, val isDeduction: Boolean = false)

/** A titled, rule-bound section of a payslip (Earnings / Allowances / Deductions / GOSI /
 * Employer contribution), rendered inside a [GlassCard] to match the rest of the app. */
@Composable
fun PayslipPreviewCard(
  sectionTitle: String,
  lines: List<PayslipLine>,
  currencySymbol: String,
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  totalLabel: String? = null,
  total: Double? = null
) {
  GlassCard(darkMode = darkMode, modifier = modifier) {
    Text(
      sectionTitle,
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.Black,
      color = designSystemContentColor(darkMode)
    )
    lines.forEach { line ->
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
          line.label,
          style = MaterialTheme.typography.bodyMedium,
          color = designSystemContentColor(darkMode).copy(alpha = 0.75f)
        )
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
          Text(
            "${if (line.isDeduction) "− " else ""}${String.format(Locale.US, "%,.2f", line.amount)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (line.isDeduction) BrandRed else designSystemContentColor(darkMode)
          )
          androidx.compose.foundation.layout.Spacer(androidx.compose.ui.Modifier.width(4.dp))
          RiyalSymbol(
            tint = if (line.isDeduction) BrandRed else designSystemContentColor(darkMode),
            size = 11.dp,
            contentDescription = currencySymbol
          )
        }
      }
    }
    if (totalLabel != null && total != null) {
      HorizontalDivider(color = if (darkMode) Color.White.copy(alpha = 0.08f) else Color(0xFFE7ECE8))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(totalLabel, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = designSystemContentColor(darkMode))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
          Text(
            String.format(Locale.US, "%,.2f", total),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Black,
            color = BrandGreen
          )
          androidx.compose.foundation.layout.Spacer(androidx.compose.ui.Modifier.width(4.dp))
          RiyalSymbol(tint = BrandGreen, size = 13.dp, contentDescription = currencySymbol)
        }
      }
    }
  }
}
