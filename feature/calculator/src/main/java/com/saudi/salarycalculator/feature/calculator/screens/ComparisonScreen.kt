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
import com.saudi.salarycalculator.core.model.CityCostOfLiving
import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.OfferComparisonResult
import com.saudi.salarycalculator.core.model.SaudiCityTier
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
      // Disabled until both sides have a real basic salary — otherwise tapping it with empty
      // fields produced a confusing 0-vs-0 "result" card (see SalaryViewModel.compareOffers).
      InteractiveCTA(
        text = stringResource(R.string.action_compare),
        icon = Icons.Filled.CompareArrows,
        enabled = (offerCurrent.basicSalary.toDoubleOrNull() ?: 0.0) > 0.0 &&
          (offerNew.basicSalary.toDoubleOrNull() ?: 0.0) > 0.0,
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
      // A real difference gets a green title + green "Clearly better · +3.7%" line so the
      // qualifier always agrees with the number next to it (previously a flat 0-30% raise could
      // land on "Neutral" right beside a green "+3.7%", which read as contradictory).
      val isMeaningfulGap = kotlin.math.abs(comparisonResult.percentageIncrease) >= 1.0
      val isPositive = comparisonResult.monthlyDifference >= 0
      val emphasisColor = when {
        !isMeaningfulGap -> designSystemContentColor(darkMode).copy(alpha = 0.7f)
        isPositive -> BrandGreen
        else -> com.saudi.salarycalculator.core.designsystem.theme.BrandRed
      }
      item {
        GlassCard(darkMode = darkMode) {
          Text(
            stringResource(R.string.comparison_better_offer),
            style = MaterialTheme.typography.labelLarge,
            color = designSystemContentColor(darkMode).copy(alpha = 0.65f),
            fontWeight = FontWeight.Bold
          )
          Text(
            // Below the 1% gap threshold, show a neutral message instead of betterOfferTitle —
            // naming an offer here (even tinted gray) read as a real verdict when the two
            // offers actually net the same take-home pay (e.g. basic/HRA split changes that
            // don't change the basic+HRA total GOSI is computed on).
            if (isMeaningfulGap) comparisonResult.betterOfferTitle else stringResource(R.string.comparison_tie_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = emphasisColor
          )
          Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text(
              comparisonResult.scoreLabel,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold,
              color = emphasisColor
            )
            Text(
              " · ${if (comparisonResult.percentageIncrease >= 0) "+" else ""}${String.format(Locale.US, "%.1f", comparisonResult.percentageIncrease)}% net pay",
              style = MaterialTheme.typography.bodyMedium,
              color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
            )
          }
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
    Text(
      stringResource(R.string.comparison_city_label),
      style = MaterialTheme.typography.labelMedium,
      color = designSystemContentColor(darkMode).copy(alpha = 0.7f)
    )
    val cityOptions = SaudiCityTier.entries
    val cityLabels = listOf(
      stringResource(R.string.comparison_city_riyadh),
      stringResource(R.string.comparison_city_jeddah),
      stringResource(R.string.comparison_city_eastern),
      stringResource(R.string.comparison_city_other)
    )
    SegmentedControl(
      options = cityLabels,
      selectedIndex = offer.city?.let { cityOptions.indexOf(it) } ?: -1,
      darkMode = darkMode,
      onSelect = { index -> onUpdate { it.copy(city = cityOptions[index]) } }
    )
    offer.city?.let { city ->
      val rentRange = CityCostOfLiving.rentRangeSar(city)
      val tierLabel = stringResource(
        when (city) {
          SaudiCityTier.RIYADH, SaudiCityTier.EASTERN_PROVINCE -> R.string.comparison_city_tier_highest
          SaudiCityTier.JEDDAH -> R.string.comparison_city_tier_high
          SaudiCityTier.OTHER -> R.string.comparison_city_tier_lower
        }
      )
      Text(
        stringResource(
          R.string.comparison_city_reference,
          tierLabel,
          rentRange.first,
          rentRange.last,
          CityCostOfLiving.LAST_VERIFIED_LABEL
        ),
        style = MaterialTheme.typography.bodySmall,
        color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
      )
    }
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
