package com.saudi.salarycalculator.feature.calculator.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.components.CurrencyInputField
import com.saudi.salarycalculator.core.designsystem.components.GlassCard
import com.saudi.salarycalculator.core.designsystem.components.InteractiveCTA
import com.saudi.salarycalculator.core.designsystem.components.NumberStepper
import com.saudi.salarycalculator.core.designsystem.components.ResultCard
import com.saudi.salarycalculator.core.designsystem.components.SectionHeader
import com.saudi.salarycalculator.core.designsystem.components.SegmentedControl
import com.saudi.salarycalculator.core.designsystem.components.ToggleRow
import com.saudi.salarycalculator.core.designsystem.theme.BrandGold
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.designsystem.theme.BrandTeal
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.model.ExpatCostResult
import com.saudi.salarycalculator.core.model.ExpatCostSchedule
import com.saudi.salarycalculator.feature.calculator.ExpatCostFormState
import com.saudi.salarycalculator.feature.calculator.R
import kotlin.math.roundToInt

/** Standalone tool (pushed from Home, not a bottom-nav tab) that estimates the recurring
 * residency costs a non-Saudi employee personally carries — dependent levy, exit/re-entry
 * travel, optional own-iqama renewal, and health insurance — against their net salary. See
 * [com.saudi.salarycalculator.core.model.ExpatCostInput] for why the employer-side work-permit
 * levy is deliberately excluded from this total. */
@Composable
fun ExpatCostScreen(
  darkMode: Boolean,
  currencySymbol: String,
  form: ExpatCostFormState,
  result: ExpatCostResult?,
  lastNetSalary: Double?,
  onUpdateForm: ((ExpatCostFormState) -> ExpatCostFormState) -> Unit,
  onCalculate: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
    contentPadding = PaddingValues(top = 4.dp, bottom = 32.dp)
  ) {
    item {
      Text(
        stringResource(R.string.expat_costs_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
      )
    }

    item {
      GlassCard(darkMode = darkMode) {
        SectionHeader(title = stringResource(R.string.expat_costs_section_dependents), darkMode = darkMode)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            stringResource(R.string.expat_costs_dependent_count),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = designSystemContentColor(darkMode)
          )
          NumberStepper(
            value = form.dependentCount,
            darkMode = darkMode,
            max = 20,
            onValueChange = { count -> onUpdateForm { it.copy(dependentCount = count) } }
          )
        }
        CurrencyInputField(
          label = stringResource(R.string.expat_costs_dependent_levy),
          value = form.dependentMonthlyLevy,
          onValueChange = { v -> onUpdateForm { it.copy(dependentMonthlyLevy = v) } },
          darkMode = darkMode,
          currencySymbol = currencySymbol,
          helperText = stringResource(R.string.expat_costs_dependent_levy_helper)
        )
      }
    }

    item {
      GlassCard(darkMode = darkMode) {
        SectionHeader(title = stringResource(R.string.expat_costs_section_exit_reentry), darkMode = darkMode)
        SegmentedControl(
          options = listOf(
            stringResource(R.string.expat_costs_exit_reentry_single),
            stringResource(R.string.expat_costs_exit_reentry_multiple)
          ),
          selectedIndex = if (form.exitReentryMultipleTrip) 1 else 0,
          darkMode = darkMode,
          onSelect = { index -> onUpdateForm { it.copy(exitReentryMultipleTrip = index == 1) } }
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            stringResource(R.string.expat_costs_trips_per_year),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = designSystemContentColor(darkMode)
          )
          NumberStepper(
            value = form.exitReentryTripsPerYear,
            darkMode = darkMode,
            max = 12,
            onValueChange = { count -> onUpdateForm { it.copy(exitReentryTripsPerYear = count) } }
          )
        }
        Text(
          stringResource(R.string.expat_costs_exit_reentry_dependents_note),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
        )
      }
    }

    item {
      GlassCard(darkMode = darkMode) {
        SectionHeader(title = stringResource(R.string.expat_costs_section_iqama), darkMode = darkMode)
        ToggleRow(
          label = stringResource(R.string.expat_costs_include_own_iqama),
          checked = form.includeOwnIqamaRenewal,
          darkMode = darkMode,
          description = stringResource(R.string.expat_costs_include_own_iqama_helper),
          onCheckedChange = { checked -> onUpdateForm { it.copy(includeOwnIqamaRenewal = checked) } }
        )
        if (form.includeOwnIqamaRenewal) {
          CurrencyInputField(
            label = stringResource(R.string.expat_costs_own_iqama_fee),
            value = form.ownIqamaRenewalFee,
            onValueChange = { v -> onUpdateForm { it.copy(ownIqamaRenewalFee = v) } },
            darkMode = darkMode,
            currencySymbol = currencySymbol
          )
        }
      }
    }

    item {
      GlassCard(darkMode = darkMode) {
        SectionHeader(title = stringResource(R.string.expat_costs_section_health), darkMode = darkMode)
        CurrencyInputField(
          label = stringResource(R.string.expat_costs_health_insurance),
          value = form.monthlyHealthInsurancePremium,
          onValueChange = { v -> onUpdateForm { it.copy(monthlyHealthInsurancePremium = v) } },
          darkMode = darkMode,
          currencySymbol = currencySymbol,
          optionalLabel = stringResource(R.string.common_optional),
          helperText = stringResource(R.string.expat_costs_health_insurance_helper)
        )
      }
    }

    item {
      InteractiveCTA(
        text = stringResource(R.string.expat_costs_action_estimate),
        onClick = onCalculate,
        modifier = Modifier.fillMaxWidth()
      )
    }

    if (result != null) {
      item { SectionHeader(title = stringResource(R.string.expat_costs_section_results), darkMode = darkMode) }
      item {
        ResultCard(
          label = stringResource(R.string.expat_costs_result_dependent_levy),
          value = result.monthlyDependentLevy,
          currencySymbol = currencySymbol,
          darkMode = darkMode,
          accent = BrandRed,
          isDeduction = true
        )
      }
      if (result.monthlyOwnIqamaRenewal > 0.0) {
        item {
          ResultCard(
            label = stringResource(R.string.expat_costs_result_iqama),
            value = result.monthlyOwnIqamaRenewal,
            currencySymbol = currencySymbol,
            darkMode = darkMode,
            accent = BrandGold,
            isDeduction = true
          )
        }
      }
      item {
        ResultCard(
          label = stringResource(R.string.expat_costs_result_exit_reentry),
          value = result.monthlyExitReentry,
          currencySymbol = currencySymbol,
          darkMode = darkMode,
          accent = BrandTeal,
          isDeduction = true
        )
      }
      if (result.monthlyHealthInsurance > 0.0) {
        item {
          ResultCard(
            label = stringResource(R.string.expat_costs_result_health),
            value = result.monthlyHealthInsurance,
            currencySymbol = currencySymbol,
            darkMode = darkMode,
            accent = BrandTeal,
            isDeduction = true
          )
        }
      }
      item {
        ResultCard(
          label = stringResource(R.string.expat_costs_result_total_monthly),
          value = result.totalMonthlyCost,
          currencySymbol = currencySymbol,
          darkMode = darkMode,
          accent = BrandGreen
        )
      }
      item {
        ResultCard(
          label = stringResource(R.string.expat_costs_result_total_yearly),
          value = result.totalYearlyCost,
          currencySymbol = currencySymbol,
          darkMode = darkMode,
          accent = BrandGreen
        )
      }
      if (lastNetSalary != null && lastNetSalary > 0.0) {
        val percentOfIncome = ((result.totalMonthlyCost / lastNetSalary) * 100.0).roundToInt()
        item {
          GlassCard(darkMode = darkMode) {
            Text(
              stringResource(R.string.expat_costs_percent_of_income, percentOfIncome),
              style = MaterialTheme.typography.bodyMedium,
              color = designSystemContentColor(darkMode).copy(alpha = 0.75f)
            )
          }
        }
      }
    }

    item {
      HorizontalDivider(color = designSystemContentColor(darkMode).copy(alpha = 0.08f))
    }
    item {
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
          stringResource(R.string.expat_costs_disclaimer, ExpatCostSchedule.LAST_VERIFIED_LABEL),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
        )
        Text(
          stringResource(R.string.expat_costs_article_40_note),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
        )
      }
    }
  }
}
