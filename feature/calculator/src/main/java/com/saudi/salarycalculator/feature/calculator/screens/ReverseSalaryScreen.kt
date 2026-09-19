package com.saudi.salarycalculator.feature.calculator.screens

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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.saudi.salarycalculator.core.designsystem.theme.BrandGold
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandTeal
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.ReverseSalaryResult
import com.saudi.salarycalculator.feature.calculator.R
import com.saudi.salarycalculator.feature.calculator.ReverseSalaryFormState
import java.util.Locale

/** Reverse "what salary do I need" calculator: stateless one-shot check (mirrors
 * [OfferScanScreen]'s pattern — form always visible, a CTA, result appears below). Solves for
 * the basic salary/package that would produce a target net take-home by binary-searching the
 * real net-salary formula (see [com.saudi.salarycalculator.core.calculator.DefaultReverseSalaryCalculatorService]),
 * so the answer can never drift out of sync with how net salary is actually calculated
 * elsewhere in the app. */
@Composable
fun ReverseSalaryScreen(
  darkMode: Boolean,
  currencySymbol: String,
  form: ReverseSalaryFormState,
  result: ReverseSalaryResult?,
  onUpdateForm: ((ReverseSalaryFormState) -> ReverseSalaryFormState) -> Unit,
  onCalculate: () -> Unit,
  modifier: Modifier = Modifier
) {
  val canCalculate = (form.targetNetMonthlySalary.toDoubleOrNull() ?: 0.0) > 0.0

  LazyColumn(
    modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
    contentPadding = PaddingValues(top = 4.dp, bottom = 32.dp)
  ) {
    item {
      Text(
        stringResource(R.string.reverse_salary_intro),
        style = MaterialTheme.typography.bodyMedium,
        color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
      )
    }

    item {
      GlassCard(darkMode = darkMode) {
        CurrencyInputField(
          label = stringResource(R.string.reverse_salary_label_target),
          value = form.targetNetMonthlySalary,
          onValueChange = { v -> onUpdateForm { it.copy(targetNetMonthlySalary = v) } },
          darkMode = darkMode,
          currencySymbol = currencySymbol
        )
        SegmentedControl(
          options = listOf(stringResource(R.string.option_saudi), stringResource(R.string.option_non_saudi)),
          selectedIndex = if (form.employeeType == EmployeeType.EXPAT) 1 else 0,
          darkMode = darkMode,
          onSelect = { index ->
            onUpdateForm { it.copy(employeeType = if (index == 1) EmployeeType.EXPAT else EmployeeType.SAUDI) }
          }
        )
      }
    }

    item {
      GlassCard(darkMode = darkMode) {
        Text(
          stringResource(R.string.reverse_salary_label_housing_percent),
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.SemiBold,
          color = designSystemContentColor(darkMode)
        )
        NumberStepper(
          value = form.housingAllowancePercent,
          darkMode = darkMode,
          increaseContentDescription = stringResource(R.string.common_increase),
          decreaseContentDescription = stringResource(R.string.common_decrease),
          min = 0,
          max = 60,
          onValueChange = { v -> onUpdateForm { it.copy(housingAllowancePercent = v) } }
        )
        Text(
          stringResource(R.string.reverse_salary_label_transport_percent),
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.SemiBold,
          color = designSystemContentColor(darkMode)
        )
        NumberStepper(
          value = form.transportAllowancePercent,
          darkMode = darkMode,
          increaseContentDescription = stringResource(R.string.common_increase),
          decreaseContentDescription = stringResource(R.string.common_decrease),
          min = 0,
          max = 60,
          onValueChange = { v -> onUpdateForm { it.copy(transportAllowancePercent = v) } }
        )
        Text(
          stringResource(R.string.reverse_salary_allowance_percent_helper),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
        )
      }
    }

    item {
      InteractiveCTA(
        text = stringResource(R.string.action_calculate_salary_needed),
        icon = Icons.Filled.Calculate,
        enabled = canCalculate,
        onClick = onCalculate,
        modifier = Modifier.fillMaxWidth()
      )
    }

    if (result == null) {
      item {
        GlassCard(darkMode = darkMode) {
          Text(
            stringResource(R.string.reverse_salary_empty_title),
            style = MaterialTheme.typography.bodyMedium,
            color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
          )
        }
      }
    } else {
      item { SectionHeader(title = stringResource(R.string.reverse_salary_section_result), darkMode = darkMode) }

      if (!result.isAchievable) {
        item {
          GlassCard(darkMode = darkMode) {
            Row(verticalAlignment = Alignment.Top) {
              Icon(
                Icons.Filled.WarningAmber,
                contentDescription = null,
                tint = BrandGold,
                modifier = Modifier.width(20.dp).padding(top = 2.dp)
              )
              Spacer(Modifier.width(10.dp))
              androidx.compose.foundation.layout.Column {
                Text(
                  stringResource(R.string.reverse_salary_not_achievable_title),
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = designSystemContentColor(darkMode)
                )
                Text(
                  stringResource(R.string.reverse_salary_not_achievable_body),
                  style = MaterialTheme.typography.bodySmall,
                  color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
                )
              }
            }
          }
        }
      }

      // Only rendered when the target is actually achievable: result.requiredBasicSalary is the
      // search ceiling (SEARCH_CEILING_SAR, see DefaultReverseSalaryCalculatorService) when it
      // isn't, and showing that under labels like "Required basic salary" made an arbitrary
      // upper bound read as a real recommendation right below the "not realistic" warning above.
      if (result.isAchievable) {
        item {
          ResultCard(
            label = stringResource(R.string.reverse_salary_basic_label),
            value = result.requiredBasicSalary,
            currencySymbol = currencySymbol,
            darkMode = darkMode,
            accent = BrandGreen
          )
        }
        item {
          ResultCard(
            label = stringResource(R.string.reverse_salary_package_label),
            value = result.requiredTotalMonthlyPackage,
            currencySymbol = currencySymbol,
            darkMode = darkMode,
            accent = BrandTeal
          )
        }
        item {
          Text(
            stringResource(
              R.string.reverse_salary_package_breakdown,
              String.format(Locale.US, "%,.0f", result.requiredBasicSalary),
              String.format(Locale.US, "%,.0f", result.requiredHousingAllowance),
              String.format(Locale.US, "%,.0f", result.requiredTransportAllowance)
            ),
            style = MaterialTheme.typography.bodySmall,
            color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
          )
        }
        item {
          ResultCard(
            label = stringResource(R.string.reverse_salary_achieved_label),
            value = result.achievedNetSalary,
            currencySymbol = currencySymbol,
            darkMode = darkMode,
            accent = BrandGold
          )
        }
        // The gap between the package and the achieved net is exactly the GOSI employee
        // contribution — this screen's NetSalaryInput sets no other deduction (no bonus, loan,
        // absence, or unpaid leave), so package minus net can only be GOSI. Computed here rather
        // than re-deriving a rate/base locally, so it can never drift out of sync with whatever
        // GOSI system/rate is actually configured in Settings. Hidden below a few cents (rather
        // than an exact > 0.0) so floating-point noise doesn't show a stray "SAR 0" line for a
        // non-Saudi employee, who has 0% employee GOSI in this app's model.
        val gosiGap = result.requiredTotalMonthlyPackage - result.achievedNetSalary
        if (gosiGap > 1.0) {
          item {
            Text(
              stringResource(
                R.string.reverse_salary_gosi_gap_note,
                String.format(Locale.US, "%,.0f", gosiGap)
              ),
              style = MaterialTheme.typography.bodySmall,
              color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
            )
          }
        }
      }
    }

    item { HorizontalDivider(color = designSystemContentColor(darkMode).copy(alpha = 0.08f)) }
    item {
      Text(
        stringResource(R.string.reverse_salary_disclaimer),
        style = MaterialTheme.typography.bodySmall,
        color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
      )
    }
  }
}
