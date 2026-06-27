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
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.components.ComparisonCard
import com.saudi.salarycalculator.core.designsystem.components.CurrencyInputField
import com.saudi.salarycalculator.core.designsystem.components.GlassCard
import com.saudi.salarycalculator.core.designsystem.components.InteractiveCTA
import com.saudi.salarycalculator.core.designsystem.components.RiyalSymbol
import com.saudi.salarycalculator.core.designsystem.components.SectionHeader
import com.saudi.salarycalculator.core.designsystem.components.SegmentedControl
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.OfferComparisonResult
import com.saudi.salarycalculator.feature.calculator.OfferFormState
import com.saudi.salarycalculator.feature.calculator.R
import java.util.Locale

/** Offer Comparison: two editable [OfferFormState] cards feeding [SalaryViewModel.compareOffers],
 * plus the resulting [OfferComparisonResult] once available. Mirrors the wizard's pattern of
 * passing raw state + an `onUpdate` transform callback rather than owning any state itself. */
@Composable
fun ComparisonScreen(
  darkMode: Boolean,
  currencySymbol: String,
  offerCurrent: OfferFormState,
  offerNew: OfferFormState,
  comparisonResult: OfferComparisonResult?,
  onUpdateCurrent: ((OfferFormState) -> OfferFormState) -> Unit,
  onUpdateNew: ((OfferFormState) -> OfferFormState) -> Unit,
  onCompare: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
  ) {
    item { SectionHeader(title = stringResource(R.string.comparison_title), darkMode = darkMode) }

    item {
      OfferFormCard(
        title = stringResource(R.string.comparison_current_offer),
        offer = offerCurrent,
        currencySymbol = currencySymbol,
        darkMode = darkMode,
        onUpdate = onUpdateCurrent
      )
    }
    item {
      OfferFormCard(
        title = stringResource(R.string.comparison_new_offer),
        offer = offerNew,
        currencySymbol = currencySymbol,
        darkMode = darkMode,
        onUpdate = onUpdateNew
      )
    }

    item {
      InteractiveCTA(
        text = stringResource(R.string.action_compare),
        icon = Icons.Filled.CompareArrows,
        onClick = onCompare,
        modifier = Modifier.fillMaxWidth()
      )
    }

    if (comparisonResult == null) {
      item {
        GlassCard(darkMode = darkMode) {
          Text(
            stringResource(R.string.comparison_empty_title),
            style = MaterialTheme.typography.bodyMedium,
            color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
          )
        }
      }
    } else {
      item {
        GlassCard(darkMode = darkMode) {
          Text(
            stringResource(R.string.comparison_better_offer),
            style = MaterialTheme.typography.labelLarge,
            color = designSystemContentColor(darkMode).copy(alpha = 0.65f),
            fontWeight = FontWeight.Bold
          )
          Text(
            comparisonResult.betterOfferTitle,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = BrandGreen
          )
          Text(
            comparisonResult.scoreLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
          )
        }
      }

      item {
        ComparisonCard(
          label = stringResource(R.string.comparison_net_increase),
          currentValue = String.format(Locale.US, "%,.2f", comparisonResult.offerA.netMonthlySalary),
          newValue = String.format(Locale.US, "%,.2f", comparisonResult.offerB.netMonthlySalary),
          deltaLabel = "${if (comparisonResult.monthlyDifference >= 0) "+" else ""}${String.format(Locale.US, "%,.2f", comparisonResult.monthlyDifference)}",
          isPositive = comparisonResult.monthlyDifference >= 0,
          darkMode = darkMode,
          currencySymbol = currencySymbol
        )
      }

      item {
        GlassCard(darkMode = darkMode) {
          ComparisonDetailRow(
            stringResource(R.string.comparison_percentage_increase),
            "${if (comparisonResult.percentageIncrease >= 0) "+" else ""}${String.format(Locale.US, "%.1f", comparisonResult.percentageIncrease)}%",
            comparisonResult.percentageIncrease >= 0,
            darkMode,
            currencySymbol,
            isCurrency = false
          )
          ComparisonDetailRow(
            stringResource(R.string.comparison_gosi_difference),
            String.format(Locale.US, "%,.2f", comparisonResult.gosiMonthlyDifference),
            comparisonResult.gosiMonthlyDifference <= 0,
            darkMode,
            currencySymbol
          )
          ComparisonDetailRow(
            stringResource(R.string.comparison_eosb_difference),
            "${if (comparisonResult.eosbDifference >= 0) "+" else ""}${String.format(Locale.US, "%,.2f", comparisonResult.eosbDifference)}",
            comparisonResult.eosbDifference >= 0,
            darkMode,
            currencySymbol
          )
          ComparisonDetailRow(
            stringResource(R.string.comparison_monthly_impact),
            "${if (comparisonResult.monthlyDifference >= 0) "+" else ""}${String.format(Locale.US, "%,.2f", comparisonResult.monthlyDifference)}",
            comparisonResult.monthlyDifference >= 0,
            darkMode,
            currencySymbol
          )
          ComparisonDetailRow(
            stringResource(R.string.comparison_yearly_impact),
            "${if (comparisonResult.yearlyDifference >= 0) "+" else ""}${String.format(Locale.US, "%,.2f", comparisonResult.yearlyDifference)}",
            comparisonResult.yearlyDifference >= 0,
            darkMode,
            currencySymbol
          )
        }
      }
    }
  }
}

@Composable
private fun OfferFormCard(
  title: String,
  offer: OfferFormState,
  currencySymbol: String,
  darkMode: Boolean,
  onUpdate: ((OfferFormState) -> OfferFormState) -> Unit
) {
  val optional = stringResource(R.string.common_optional)
  GlassCard(darkMode = darkMode) {
    Text(
      title,
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.Black,
      color = designSystemContentColor(darkMode)
    )
    CurrencyInputField(
      label = stringResource(R.string.label_basic_salary),
      value = offer.basicSalary,
      onValueChange = { v -> onUpdate { it.copy(basicSalary = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol
    )
    CurrencyInputField(
      label = stringResource(R.string.label_housing_allowance),
      value = offer.housingAllowance,
      onValueChange = { v -> onUpdate { it.copy(housingAllowance = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      optionalLabel = optional
    )
    CurrencyInputField(
      label = stringResource(R.string.label_transport_allowance),
      value = offer.transportAllowance,
      onValueChange = { v -> onUpdate { it.copy(transportAllowance = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      optionalLabel = optional
    )
    CurrencyInputField(
      label = stringResource(R.string.label_other_allowance),
      value = offer.otherAllowances,
      onValueChange = { v -> onUpdate { it.copy(otherAllowances = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      optionalLabel = optional
    )
    SegmentedControl(
      options = listOf(stringResource(R.string.option_saudi), stringResource(R.string.option_non_saudi)),
      selectedIndex = if (offer.employeeType == EmployeeType.EXPAT) 1 else 0,
      darkMode = darkMode,
      onSelect = { index -> onUpdate { it.copy(employeeType = if (index == 1) EmployeeType.EXPAT else EmployeeType.SAUDI) } }
    )
  }
}

@Composable
private fun ComparisonDetailRow(
  label: String,
  value: String,
  isPositive: Boolean,
  darkMode: Boolean,
  currencySymbol: String,
  isCurrency: Boolean = true
) {
  val color = if (isPositive) BrandGreen else com.saudi.salarycalculator.core.designsystem.theme.BrandRed
  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Text(
      label,
      style = MaterialTheme.typography.bodyMedium,
      color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
    )
    if (isCurrency) {
      Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
        Spacer(Modifier.width(3.dp))
        RiyalSymbol(tint = color, size = 11.dp, contentDescription = currencySymbol)
      }
    } else {
      Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
    }
  }
}
