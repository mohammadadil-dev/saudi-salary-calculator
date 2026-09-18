package com.saudi.salarycalculator.feature.calculator.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.components.AnimatedCounter
import com.saudi.salarycalculator.core.designsystem.components.GlassCard
import com.saudi.salarycalculator.core.designsystem.components.InteractiveCTA
import com.saudi.salarycalculator.core.designsystem.components.SalaryInputField
import com.saudi.salarycalculator.core.designsystem.components.SectionHeader
import com.saudi.salarycalculator.core.designsystem.components.formatHijriDate
import com.saudi.salarycalculator.core.designsystem.theme.BrandGold
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.model.LeaveTrackerResult
import com.saudi.salarycalculator.feature.calculator.LeaveTrackerFormState
import com.saudi.salarycalculator.feature.calculator.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/** Standalone tool (pushed from Home, not a bottom-nav tab): a persisted "my leave" profile
 * (joining date + leave days already taken this leave year) whose balance is re-derived against
 * *today's* date every time this screen opens (see [onRefresh]/SalaryViewModel.refreshLeaveTracker),
 * mirroring [EosbTrackerScreen]'s setup-vs-live-tracker pattern. */
@Composable
fun LeaveTrackerScreen(
  darkMode: Boolean,
  form: LeaveTrackerFormState,
  saved: Boolean,
  result: LeaveTrackerResult?,
  onUpdateForm: ((LeaveTrackerFormState) -> LeaveTrackerFormState) -> Unit,
  onSave: () -> Unit,
  onClear: () -> Unit,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier
) {
  LaunchedEffect(Unit) { onRefresh() }

  // Starts in edit mode when there's no saved profile yet; once a profile exists, editing is
  // only entered by explicitly tapping "Update joining date or days taken".
  var editing by remember(saved) { mutableStateOf(!saved) }
  var showDatePicker by remember { mutableStateOf(false) }

  val canSave = form.joiningDateMillis != null && (form.daysTakenThisYear.toIntOrNull() ?: -1) >= 0

  LazyColumn(
    modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
    contentPadding = PaddingValues(top = 4.dp, bottom = 32.dp)
  ) {
    if (editing) {
      item {
        Text(
          stringResource(R.string.leave_tracker_setup_intro),
          style = MaterialTheme.typography.bodyMedium,
          color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
        )
      }
      item {
        GlassCard(darkMode = darkMode) {
          DateField(
            label = stringResource(R.string.label_joining_date),
            actionLabel = stringResource(R.string.action_pick_date),
            millis = form.joiningDateMillis,
            pattern = "d MMM yyyy",
            monthOnly = false,
            darkMode = darkMode,
            onClick = { showDatePicker = true }
          )
          SalaryInputField(
            label = stringResource(R.string.leave_tracker_label_days_taken),
            value = form.daysTakenThisYear,
            onValueChange = { v -> onUpdateForm { it.copy(daysTakenThisYear = v.filter { ch -> ch.isDigit() }) } },
            darkMode = darkMode,
            keyboardType = KeyboardType.Number
          )
        }
      }
      item {
        InteractiveCTA(
          text = stringResource(R.string.leave_tracker_action_start),
          enabled = canSave,
          onClick = {
            onSave()
            editing = false
          },
          modifier = Modifier.fillMaxWidth()
        )
      }
      if (saved) {
        item {
          Text(
            stringResource(R.string.common_cancel),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = designSystemContentColor(darkMode).copy(alpha = 0.6f),
            modifier = Modifier.clickable { editing = false }
          )
        }
      }
    } else if (result != null) {
      item {
        val totalMonths = (result.yearsOfService * 12.0).roundToInt().coerceAtLeast(0)
        val years = totalMonths / 12
        val months = totalMonths % 12
        Text(
          if (years > 0) {
            stringResource(R.string.leave_tracker_years_months_format, years, months)
          } else {
            stringResource(R.string.leave_tracker_months_only_format, months)
          },
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = designSystemContentColor(darkMode)
        )
      }

      item { SectionHeader(title = stringResource(R.string.leave_tracker_section_balance), darkMode = darkMode) }

      item {
        val daysSuffix = stringResource(R.string.leave_tracker_days_suffix)
        val isNegative = result.daysRemainingBalance < 0.0
        GlassCard(darkMode = darkMode) {
          Text(
            stringResource(R.string.leave_tracker_balance_label),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = designSystemContentColor(darkMode).copy(alpha = 0.7f)
          )
          AnimatedCounter(
            targetValue = result.daysRemainingBalance,
            decimals = 1,
            suffix = daysSuffix,
            style = MaterialTheme.typography.titleLarge,
            color = if (isNegative) BrandRed else designSystemContentColor(darkMode)
          )
          if (isNegative) {
            Text(
              stringResource(R.string.leave_tracker_balance_negative_note),
              style = MaterialTheme.typography.bodySmall,
              color = BrandRed.copy(alpha = 0.85f)
            )
          }
          // Without this, the three numbers below (entitlement/accrued/taken) read as an
          // unexplained list — nothing states that Balance is derived from the other two, or
          // why "accrued so far" is normally well under the full annual entitlement (leave
          // builds up gradually through the year rather than being granted all at once).
          Text(
            stringResource(R.string.leave_tracker_balance_formula_helper),
            style = MaterialTheme.typography.bodySmall,
            color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
          )
          HorizontalDivider(color = if (darkMode) androidx.compose.ui.graphics.Color.White.copy(alpha = 0.08f) else androidx.compose.ui.graphics.Color(0xFFE7ECE8))
          Text(
            stringResource(R.string.leave_tracker_entitlement_label, result.annualEntitlementDays),
            style = MaterialTheme.typography.bodySmall,
            color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
          )
          Text(
            stringResource(R.string.leave_tracker_accrued_label) + ": " +
              String.format(Locale.US, "%.1f", result.daysAccruedSoFarThisYear) + daysSuffix,
            style = MaterialTheme.typography.bodySmall,
            color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
          )
          Text(
            stringResource(R.string.leave_tracker_taken_label) + ": " +
              result.daysTakenThisYear.toString() + daysSuffix,
            style = MaterialTheme.typography.bodySmall,
            color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
          )
        }
      }

      item {
        val isArabic = LocalLayoutDirection.current == LayoutDirection.Rtl
        val dateLabel = if (isArabic) {
          formatHijriDate(result.nextAnniversaryMillis)
        } else {
          SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(Date(result.nextAnniversaryMillis))
        }
        GlassCard(darkMode = darkMode) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.DateRange, contentDescription = null, tint = BrandGold)
            Spacer(Modifier.width(6.dp))
            Text(
              stringResource(R.string.leave_tracker_next_reset_title),
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = designSystemContentColor(darkMode).copy(alpha = 0.7f)
            )
          }
          Text(
            stringResource(R.string.leave_tracker_next_reset_days, result.daysUntilNextAnniversary.toInt(), dateLabel),
            style = MaterialTheme.typography.bodyMedium,
            color = BrandGreen,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      item {
        Text(
          stringResource(R.string.leave_tracker_edit_details),
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = BrandGreen,
          modifier = Modifier.clickable { editing = true }
        )
      }
      item {
        Text(
          stringResource(R.string.leave_tracker_clear_action),
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = BrandRed,
          modifier = Modifier.clickable {
            onClear()
            editing = true
          }
        )
      }

      item { HorizontalDivider(color = designSystemContentColor(darkMode).copy(alpha = 0.08f)) }
      item {
        Text(
          stringResource(R.string.leave_tracker_disclaimer),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
        )
      }
    }
  }

  if (showDatePicker) {
    DatePickerSheet(
      initialMillis = form.joiningDateMillis,
      darkMode = darkMode,
      onDismiss = { showDatePicker = false },
      onConfirm = { millis ->
        onUpdateForm { it.copy(joiningDateMillis = millis) }
        showDatePicker = false
      }
    )
  }
}
