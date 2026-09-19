package com.saudi.salarycalculator.feature.calculator.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.components.GlassCard
import com.saudi.salarycalculator.core.designsystem.components.HistoryRecordCard
import com.saudi.salarycalculator.core.designsystem.components.SalaryInputField
import com.saudi.salarycalculator.core.designsystem.components.SecondaryButton
import com.saudi.salarycalculator.core.designsystem.components.SectionHeader
import com.saudi.salarycalculator.core.designsystem.components.SegmentedControl
import com.saudi.salarycalculator.core.designsystem.components.ToggleRow
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.model.CalculationRecord
import com.saudi.salarycalculator.core.model.CalculationType
import com.saudi.salarycalculator.core.model.GosiRateSchedule
import com.saudi.salarycalculator.core.model.GosiRates
import com.saudi.salarycalculator.core.model.GosiSystem
import com.saudi.salarycalculator.core.model.PaydayResult
import com.saudi.salarycalculator.feature.calculator.PayrollSummaryExporter
import com.saudi.salarycalculator.feature.calculator.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Settings: language, dark mode, editable GOSI contribution rates, and calculation history.
 * GOSI rates are edited as plain percentages (e.g. "9.75") and converted to/from the fractional
 * [GosiRates] doubles the calculator services expect (e.g. 0.0975) at the edges of this screen. */
@Composable
fun SettingsScreen(
  darkMode: Boolean,
  language: String,
  gosiRates: GosiRates,
  gosiSystem: GosiSystem,
  paydayDayOfMonth: Int?,
  paydayResult: PaydayResult?,
  history: List<CalculationRecord>,
  onToggleDarkMode: () -> Unit,
  onSetLanguage: (String) -> Unit,
  onUpdateGosiRates: ((GosiRates) -> GosiRates) -> Unit,
  onSetGosiSystem: (GosiSystem) -> Unit,
  onSetPaydayDayOfMonth: (Int) -> Unit,
  onClearPaydayDayOfMonth: () -> Unit,
  onClearHistory: () -> Unit,
  onEditRecord: (CalculationRecord) -> Unit,
  onDeleteRecord: (String) -> Unit,
  onExportPayrollSummary: () -> Unit,
  onRateApp: () -> Unit,
  onShareApp: () -> Unit,
  modifier: Modifier = Modifier
) {
  val dateFormat = remember { SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault()) }

  LazyColumn(
    modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
  ) {
    item { SectionHeader(title = stringResource(R.string.settings_title), darkMode = darkMode) }

    item {
      GlassCard(darkMode = darkMode) {
        Text(
          stringResource(R.string.settings_language),
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Black,
          color = designSystemContentColor(darkMode)
        )
        SegmentedControl(
          options = listOf("English", "العربية"),
          selectedIndex = if (language == "ar") 1 else 0,
          darkMode = darkMode,
          onSelect = { index -> onSetLanguage(if (index == 1) "ar" else "en") }
        )
        HorizontalDivider(color = if (darkMode) Color.White.copy(alpha = 0.08f) else Color(0xFFE7ECE8))
        ToggleRow(
          label = stringResource(R.string.settings_dark_mode),
          checked = darkMode,
          darkMode = darkMode,
          onCheckedChange = { onToggleDarkMode() }
        )
      }
    }

    item {
      GlassCard(darkMode = darkMode) {
        Text(
          stringResource(R.string.settings_gosi_rates),
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Black,
          color = designSystemContentColor(darkMode)
        )
        Text(
          stringResource(R.string.settings_gosi_system),
          style = MaterialTheme.typography.labelMedium,
          color = designSystemContentColor(darkMode).copy(alpha = 0.7f)
        )
        SegmentedControl(
          options = listOf(
            stringResource(R.string.settings_gosi_system_existing),
            stringResource(R.string.settings_gosi_system_new)
          ),
          selectedIndex = if (gosiSystem == GosiSystem.NEW) 1 else 0,
          darkMode = darkMode,
          onSelect = { index -> onSetGosiSystem(if (index == 1) GosiSystem.NEW else GosiSystem.EXISTING) }
        )
        // The pill labels above are deliberately just "Existing"/"New" — the full cutoff date
        // used to be crammed into the pill itself and would overflow/overlap on narrower screens.
        // Spelling it out here instead, at readable text size, keeps the date fully legible and
        // reacts immediately when the user switches systems.
        Text(
          if (gosiSystem == GosiSystem.NEW) {
            stringResource(R.string.settings_gosi_system_new_caption)
          } else {
            stringResource(R.string.settings_gosi_system_existing_caption)
          },
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
        )
        HorizontalDivider(color = if (darkMode) Color.White.copy(alpha = 0.08f) else Color(0xFFE7ECE8))
        GosiRatePercentField(
          label = stringResource(R.string.settings_saudi_employee_rate),
          rate = gosiRates.saudiEmployeeRate,
          darkMode = darkMode,
          onRateChange = { newRate -> onUpdateGosiRates { it.copy(saudiEmployeeRate = newRate) } }
        )
        GosiRatePercentField(
          label = stringResource(R.string.settings_saudi_employer_rate),
          rate = gosiRates.saudiEmployerRate,
          darkMode = darkMode,
          onRateChange = { newRate -> onUpdateGosiRates { it.copy(saudiEmployerRate = newRate) } }
        )
        GosiRatePercentField(
          label = stringResource(R.string.settings_expat_employee_rate),
          rate = gosiRates.expatEmployeeRate,
          darkMode = darkMode,
          onRateChange = { newRate -> onUpdateGosiRates { it.copy(expatEmployeeRate = newRate) } }
        )
        GosiRatePercentField(
          label = stringResource(R.string.settings_expat_employer_rate),
          rate = gosiRates.expatEmployerHazardRate,
          darkMode = darkMode,
          onRateChange = { newRate -> onUpdateGosiRates { it.copy(expatEmployerHazardRate = newRate) } }
        )
        GosiCapField(
          label = stringResource(R.string.settings_gosi_cap),
          capSar = gosiRates.contributionCapSar,
          darkMode = darkMode,
          onCapChange = { newCap -> onUpdateGosiRates { it.copy(contributionCapSar = newCap) } }
        )
        // Without this, a high earner watching their GOSI deduction stop scaling with salary
        // could assume the app is miscalculating, rather than realizing wages above the cap
        // simply aren't assessed for GOSI at all.
        Text(
          stringResource(R.string.settings_gosi_cap_helper),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
        )
        Text(
          stringResource(R.string.settings_gosi_disclaimer, GosiRateSchedule.LAST_VERIFIED_LABEL),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
        )
      }
    }

    item { SectionHeader(title = stringResource(R.string.settings_payday_title), darkMode = darkMode) }

    item {
      GlassCard(darkMode = darkMode) {
        Text(
          stringResource(R.string.settings_payday_description),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.7f)
        )
        PaydayDaySelector(
          dayOfMonth = paydayDayOfMonth,
          darkMode = darkMode,
          onDayChange = onSetPaydayDayOfMonth,
          onClear = onClearPaydayDayOfMonth
        )
        if (paydayResult != null) {
          HorizontalDivider(color = if (darkMode) Color.White.copy(alpha = 0.08f) else Color(0xFFE7ECE8))
          Text(
            if (paydayResult.daysRemaining <= 0L) {
              stringResource(R.string.settings_payday_countdown_today)
            } else {
              stringResource(R.string.settings_payday_countdown, paydayResult.daysRemaining)
            },
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Black,
            color = designSystemContentColor(darkMode)
          )
        }
        Text(
          stringResource(R.string.settings_payday_widget_hint),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
        )
      }
    }

    item { SectionHeader(title = stringResource(R.string.settings_history), darkMode = darkMode) }

    item {
      val hasExportableRecords = PayrollSummaryExporter.hasExportableRecords(history)
      GlassCard(darkMode = darkMode) {
        SecondaryButton(
          text = stringResource(R.string.settings_export_payroll_summary),
          darkMode = darkMode,
          enabled = hasExportableRecords,
          icon = Icons.Filled.Download,
          onClick = onExportPayrollSummary
        )
        Text(
          stringResource(R.string.settings_export_payroll_summary_helper),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
        )
      }
    }

    if (history.isEmpty()) {
      item {
        GlassCard(darkMode = darkMode) {
          Text(
            stringResource(R.string.home_no_recent_calculations),
            style = MaterialTheme.typography.bodyMedium,
            color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
          )
        }
      }
    } else {
      items(history) { record ->
        HistoryRecordCard(
          title = record.title,
          subtitle = record.resultSummary,
          dateLabel = dateFormat.format(Date(record.createdAtMillis)),
          darkMode = darkMode,
          editContentDescription = stringResource(R.string.common_edit),
          deleteContentDescription = stringResource(R.string.common_delete),
          onEdit = if (record.type == CalculationType.NET_SALARY && record.netSalaryInput != null) {
            { onEditRecord(record) }
          } else null,
          onDelete = { onDeleteRecord(record.id) }
        )
      }
      item {
        SecondaryButton(
          text = stringResource(R.string.settings_clear_history),
          darkMode = darkMode,
          icon = Icons.Filled.DeleteSweep,
          onClick = onClearHistory
        )
      }
    }

    item { SectionHeader(title = stringResource(R.string.settings_support_title), darkMode = darkMode) }

    item {
      GlassCard(darkMode = darkMode) {
        SecondaryButton(
          text = stringResource(R.string.settings_rate_app),
          darkMode = darkMode,
          icon = Icons.Filled.Star,
          onClick = onRateApp
        )
        Text(
          stringResource(R.string.settings_rate_app_helper),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
        )
        HorizontalDivider(color = if (darkMode) Color.White.copy(alpha = 0.08f) else Color(0xFFE7ECE8))
        SecondaryButton(
          text = stringResource(R.string.settings_share_app),
          darkMode = darkMode,
          icon = Icons.Filled.Share,
          onClick = onShareApp
        )
        Text(
          stringResource(R.string.settings_share_app_helper),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
        )
      }
    }
  }
}

@Composable
private fun GosiRatePercentField(
  label: String,
  rate: Double,
  darkMode: Boolean,
  onRateChange: (Double) -> Unit
) {
  var text by remember(rate) { mutableStateOf(formatPercent(rate)) }
  SalaryInputField(
    label = label,
    value = text,
    onValueChange = { input ->
      val filtered = input.filter { ch -> ch.isDigit() || ch == '.' }
      text = filtered
      filtered.toDoubleOrNull()?.let { percent -> onRateChange(percent / 100.0) }
    },
    darkMode = darkMode,
    keyboardType = KeyboardType.Decimal,
    trailing = { Text("%", color = designSystemContentColor(darkMode).copy(alpha = 0.5f)) }
  )
}

@Composable
private fun GosiCapField(
  label: String,
  capSar: Double,
  darkMode: Boolean,
  onCapChange: (Double) -> Unit
) {
  var text by remember(capSar) { mutableStateOf(formatCap(capSar)) }
  SalaryInputField(
    label = label,
    value = text,
    onValueChange = { input ->
      val filtered = input.filter { ch -> ch.isDigit() || ch == '.' }
      text = filtered
      filtered.toDoubleOrNull()?.let { cap -> onCapChange(cap) }
    },
    darkMode = darkMode,
    keyboardType = KeyboardType.Decimal,
    trailing = { Text("SAR", color = designSystemContentColor(darkMode).copy(alpha = 0.5f)) }
  )
}

/** Day-of-month picker for the payday countdown (Settings preview + home-screen widget). Starts
 * at day 25 on first use (a common mid/end-of-month payday default) rather than an arbitrary 1,
 * so a first-time user's initial +/- taps land near a plausible value; every tap saves
 * immediately via [onDayChange], matching this screen's other settings (no separate Save button).
 * [onClear] only shows once a day is set, to unset the countdown entirely. */
@Composable
private fun PaydayDaySelector(
  dayOfMonth: Int?,
  darkMode: Boolean,
  onDayChange: (Int) -> Unit,
  onClear: () -> Unit
) {
  val displayedDay = dayOfMonth ?: 25
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      stringResource(R.string.settings_payday_day_label, displayedDay),
      style = MaterialTheme.typography.bodyMedium,
      color = designSystemContentColor(darkMode),
      modifier = Modifier.weight(1f)
    )
    IconButton(onClick = { onDayChange((displayedDay - 1).coerceIn(1, 31)) }) {
      Icon(Icons.Filled.Remove, contentDescription = stringResource(R.string.common_decrease), tint = designSystemContentColor(darkMode))
    }
    IconButton(onClick = { onDayChange((displayedDay + 1).coerceIn(1, 31)) }) {
      Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.common_increase), tint = designSystemContentColor(darkMode))
    }
  }
  if (dayOfMonth != null) {
    SecondaryButton(
      text = stringResource(R.string.settings_payday_clear),
      darkMode = darkMode,
      onClick = onClear
    )
  }
}

private fun formatCap(cap: Double): String =
  if (cap == cap.toLong().toDouble()) cap.toLong().toString() else String.format(Locale.US, "%.2f", cap)

private fun formatPercent(rate: Double): String {
  val percent = rate * 100.0
  return if (percent == percent.toLong().toDouble()) {
    percent.toLong().toString()
  } else {
    String.format(Locale.US, "%.2f", percent)
  }
}
