package com.saudi.salarycalculator.feature.calculator.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.components.CurrencyInputField
import com.saudi.salarycalculator.core.designsystem.components.GlassCard
import com.saudi.salarycalculator.core.designsystem.components.InteractiveCTA
import com.saudi.salarycalculator.core.designsystem.components.ResultCard
import com.saudi.salarycalculator.core.designsystem.components.SectionHeader
import com.saudi.salarycalculator.core.designsystem.components.formatHijriDate
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.designsystem.theme.BrandGold
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.model.EosbResignationEligibility
import com.saudi.salarycalculator.core.model.EosbTrackerResult
import com.saudi.salarycalculator.feature.calculator.EosbTrackerFormState
import com.saudi.salarycalculator.feature.calculator.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/** Standalone tool (pushed from Home, not a bottom-nav tab): a persisted "my employment" profile
 * (joining date + last basic salary) whose EOSB estimate is re-derived against *today's* date
 * every time this screen opens (see [onRefresh]/SalaryViewModel.refreshEosbTracker), rather than
 * a one-off snapshot from the calculator wizard. Two modes in one screen: setup/edit (no profile
 * yet, or the user tapped "update") and the live tracker (profile saved, [result] present). */
@Composable
fun EosbTrackerScreen(
  darkMode: Boolean,
  currencySymbol: String,
  form: EosbTrackerFormState,
  saved: Boolean,
  result: EosbTrackerResult?,
  onUpdateForm: ((EosbTrackerFormState) -> EosbTrackerFormState) -> Unit,
  onSave: () -> Unit,
  onClear: () -> Unit,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier
) {
  LaunchedEffect(Unit) { onRefresh() }

  // Starts in edit mode when there's no saved profile yet; once a profile exists, editing is
  // only entered by explicitly tapping "Update joining date or salary".
  var editing by remember(saved) { mutableStateOf(!saved) }
  var showDatePicker by remember { mutableStateOf(false) }

  val canSave = form.joiningDateMillis != null && (form.lastBasicSalary.toDoubleOrNull() ?: 0.0) > 0.0

  LazyColumn(
    modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
    contentPadding = PaddingValues(top = 4.dp, bottom = 32.dp)
  ) {
    if (editing) {
      item {
        Text(
          stringResource(R.string.eosb_tracker_setup_intro),
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
          CurrencyInputField(
            label = stringResource(R.string.eosb_tracker_label_basic_salary),
            value = form.lastBasicSalary,
            onValueChange = { v -> onUpdateForm { it.copy(lastBasicSalary = v) } },
            darkMode = darkMode,
            currencySymbol = currencySymbol
          )
        }
      }
      item {
        InteractiveCTA(
          text = stringResource(R.string.eosb_tracker_action_start),
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
            stringResource(R.string.eosb_tracker_years_months_format, years, months)
          } else {
            stringResource(R.string.eosb_tracker_months_only_format, months)
          },
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = designSystemContentColor(darkMode)
        )
      }

      item { SectionHeader(title = stringResource(R.string.eosb_tracker_section_estimate), darkMode = darkMode) }
      item {
        ResultCard(
          label = stringResource(R.string.eosb_tracker_result_resigned_today),
          value = result.accruedIfResignedToday,
          currencySymbol = currencySymbol,
          darkMode = darkMode,
          accent = BrandGold
        )
      }
      item {
        Text(
          stringResource(eligibilityStringRes(result.resignationEligibility)),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
        )
      }
      item {
        ResultCard(
          label = stringResource(R.string.eosb_tracker_result_terminated_today),
          value = result.accruedIfTerminated,
          currencySymbol = currencySymbol,
          darkMode = darkMode,
          accent = BrandGreen
        )
      }

      result.nextMilestone?.let { milestone ->
        item {
          val isArabic = LocalLayoutDirection.current == LayoutDirection.Rtl
          val dateLabel = if (isArabic) {
            formatHijriDate(milestone.dateMillis)
          } else {
            SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(Date(milestone.dateMillis))
          }
          GlassCard(darkMode = darkMode) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Filled.Flag, contentDescription = null, tint = BrandRed)
              Spacer(Modifier.width(6.dp))
              Text(
                stringResource(R.string.eosb_tracker_milestone_title),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = designSystemContentColor(darkMode).copy(alpha = 0.7f)
              )
            }
            Text(
              stringResource(milestoneLabelRes(milestone.milestoneKey)),
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = designSystemContentColor(darkMode)
            )
            Text(
              stringResource(R.string.eosb_tracker_milestone_days_remaining, milestone.daysRemaining.toInt(), dateLabel),
              style = MaterialTheme.typography.bodyMedium,
              color = BrandGreen,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              stringResource(milestoneDescriptionRes(milestone.milestoneKey)),
              style = MaterialTheme.typography.bodySmall,
              color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
            )
          }
        }
      }

      item {
        Text(
          stringResource(R.string.eosb_tracker_edit_details),
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = BrandGreen,
          modifier = Modifier.clickable { editing = true }
        )
      }
      item {
        Text(
          stringResource(R.string.eosb_tracker_clear_action),
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
          stringResource(R.string.eosb_tracker_disclaimer),
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

private fun eligibilityStringRes(eligibility: EosbResignationEligibility): Int = when (eligibility) {
  EosbResignationEligibility.NONE -> R.string.eosb_tracker_eligibility_none
  EosbResignationEligibility.ONE_THIRD -> R.string.eosb_tracker_eligibility_one_third
  EosbResignationEligibility.TWO_THIRDS -> R.string.eosb_tracker_eligibility_two_thirds
  EosbResignationEligibility.FULL -> R.string.eosb_tracker_eligibility_full
}

private fun milestoneLabelRes(key: String): Int = when (key) {
  "2_YEAR" -> R.string.eosb_tracker_milestone_2_year_label
  "5_YEAR" -> R.string.eosb_tracker_milestone_5_year_label
  else -> R.string.eosb_tracker_milestone_10_year_label
}

private fun milestoneDescriptionRes(key: String): Int = when (key) {
  "2_YEAR" -> R.string.eosb_tracker_milestone_2_year_description
  "5_YEAR" -> R.string.eosb_tracker_milestone_5_year_description
  else -> R.string.eosb_tracker_milestone_10_year_description
}
