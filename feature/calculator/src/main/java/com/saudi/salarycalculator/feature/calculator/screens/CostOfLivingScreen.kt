package com.saudi.salarycalculator.feature.calculator.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
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
import com.saudi.salarycalculator.core.designsystem.components.GlassCard
import com.saudi.salarycalculator.core.designsystem.components.InteractiveCTA
import com.saudi.salarycalculator.core.designsystem.components.NumberStepper
import com.saudi.salarycalculator.core.designsystem.components.SectionHeader
import com.saudi.salarycalculator.core.designsystem.components.SegmentedControl
import com.saudi.salarycalculator.core.designsystem.theme.BrandGold
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreenLight
import com.saudi.salarycalculator.core.designsystem.theme.BrandTeal
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.model.ApartmentSize
import com.saudi.salarycalculator.core.model.CostOfLivingResult
import com.saudi.salarycalculator.core.model.CostOfLivingSchedule
import com.saudi.salarycalculator.core.model.SaudiCityTier
import com.saudi.salarycalculator.core.model.SchoolFeeTier
import com.saudi.salarycalculator.feature.calculator.CostOfLivingFormState
import com.saudi.salarycalculator.feature.calculator.R
import java.util.Locale

/**
 * Household budget check for expatriates weighing "how much salary do I actually need to live
 * here" — stateless one-shot estimator (mirrors [OfferScanScreen]/[ReverseSalaryScreen]'s
 * pattern), built entirely from [CostOfLivingSchedule]'s sourced, deliberately wide reference
 * ranges rather than a single precise figure. The [onUseAsReverseSalaryTarget] action is the
 * intended next step: it seeds the Reverse Salary Calculator with this estimate's suggested
 * target so the user can immediately see what basic salary would cover it.
 */
@Composable
fun CostOfLivingScreen(
  darkMode: Boolean,
  form: CostOfLivingFormState,
  result: CostOfLivingResult?,
  onUpdateForm: ((CostOfLivingFormState) -> CostOfLivingFormState) -> Unit,
  onEstimate: () -> Unit,
  onUseAsReverseSalaryTarget: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
    contentPadding = PaddingValues(top = 4.dp, bottom = 32.dp)
  ) {
    item {
      Text(
        stringResource(R.string.cost_of_living_intro),
        style = MaterialTheme.typography.bodyMedium,
        color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
      )
    }

    item {
      GlassCard(darkMode = darkMode) {
        SectionHeader(title = stringResource(R.string.cost_of_living_section_home), darkMode = darkMode)
        Text(
          stringResource(R.string.cost_of_living_city_label),
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
          selectedIndex = cityOptions.indexOf(form.city),
          darkMode = darkMode,
          onSelect = { index -> onUpdateForm { it.copy(city = cityOptions[index]) } }
        )
        Text(
          stringResource(R.string.cost_of_living_apartment_label),
          style = MaterialTheme.typography.labelMedium,
          color = designSystemContentColor(darkMode).copy(alpha = 0.7f)
        )
        val sizeOptions = ApartmentSize.entries
        val sizeLabels = listOf(
          stringResource(R.string.cost_of_living_apartment_studio),
          stringResource(R.string.cost_of_living_apartment_two_br),
          stringResource(R.string.cost_of_living_apartment_three_br)
        )
        SegmentedControl(
          options = sizeLabels,
          selectedIndex = sizeOptions.indexOf(form.apartmentSize),
          darkMode = darkMode,
          onSelect = { index -> onUpdateForm { it.copy(apartmentSize = sizeOptions[index]) } }
        )
      }
    }

    item {
      GlassCard(darkMode = darkMode) {
        SectionHeader(title = stringResource(R.string.cost_of_living_section_household), darkMode = darkMode)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            stringResource(R.string.cost_of_living_additional_adults),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = designSystemContentColor(darkMode)
          )
          NumberStepper(
            value = form.additionalAdults,
            darkMode = darkMode,
            increaseContentDescription = stringResource(R.string.common_increase),
            decreaseContentDescription = stringResource(R.string.common_decrease),
            max = 5,
            onValueChange = { count -> onUpdateForm { it.copy(additionalAdults = count) } }
          )
        }
        // "Adult family members" sits directly above "Children" — without this, it's easy to
        // read the two rows as overlapping (e.g. wonder whether a teenager counts as an
        // "adult") or to assume it includes the person filling out the form themselves.
        Text(
          stringResource(R.string.cost_of_living_additional_adults_helper),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            stringResource(R.string.cost_of_living_children_count),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = designSystemContentColor(darkMode)
          )
          NumberStepper(
            value = form.childrenCount,
            darkMode = darkMode,
            increaseContentDescription = stringResource(R.string.common_increase),
            decreaseContentDescription = stringResource(R.string.common_decrease),
            max = 10,
            onValueChange = { count ->
              onUpdateForm {
                it.copy(childrenCount = count, childrenInSchool = it.childrenInSchool.coerceAtMost(count))
              }
            }
          )
        }
        if (form.childrenCount > 0) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              stringResource(R.string.cost_of_living_children_in_school),
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.SemiBold,
              color = designSystemContentColor(darkMode)
            )
            NumberStepper(
              value = form.childrenInSchool,
              darkMode = darkMode,
              increaseContentDescription = stringResource(R.string.common_increase),
              decreaseContentDescription = stringResource(R.string.common_decrease),
              max = form.childrenCount,
              onValueChange = { count -> onUpdateForm { it.copy(childrenInSchool = count) } }
            )
          }
          if (form.childrenInSchool > 0) {
            Text(
              stringResource(R.string.cost_of_living_school_fee_tier_label),
              style = MaterialTheme.typography.labelMedium,
              color = designSystemContentColor(darkMode).copy(alpha = 0.7f)
            )
            val tierOptions = SchoolFeeTier.entries
            val tierLabels = listOf(
              stringResource(R.string.cost_of_living_school_fee_budget),
              stringResource(R.string.cost_of_living_school_fee_mid),
              stringResource(R.string.cost_of_living_school_fee_premium)
            )
            SegmentedControl(
              options = tierLabels,
              selectedIndex = tierOptions.indexOf(form.schoolFeeTier),
              darkMode = darkMode,
              onSelect = { index -> onUpdateForm { it.copy(schoolFeeTier = tierOptions[index]) } }
            )
          }
        }
      }
    }

    item {
      InteractiveCTA(
        text = stringResource(R.string.cost_of_living_action_estimate),
        onClick = onEstimate,
        modifier = Modifier.fillMaxWidth()
      )
    }

    if (result == null) {
      item {
        GlassCard(darkMode = darkMode) {
          Text(
            stringResource(R.string.cost_of_living_empty_title),
            style = MaterialTheme.typography.bodyMedium,
            color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
          )
        }
      }
    } else {
      item { SectionHeader(title = stringResource(R.string.cost_of_living_section_results), darkMode = darkMode) }
      item {
        RangeResultCard(
          label = stringResource(R.string.cost_of_living_result_rent),
          range = result.rentRangeSar,
          darkMode = darkMode,
          accent = BrandTeal,
          icon = Icons.Filled.Apartment
        )
      }
      item {
        RangeResultCard(
          label = stringResource(R.string.cost_of_living_result_non_rent),
          range = result.nonRentRangeSar,
          darkMode = darkMode,
          accent = BrandGreenLight,
          icon = Icons.Filled.Home
        )
      }
      if (result.monthlySchoolFeeRangeSar.last > 0) {
        item {
          RangeResultCard(
            label = stringResource(R.string.cost_of_living_result_school_fees),
            range = result.monthlySchoolFeeRangeSar,
            darkMode = darkMode,
            accent = BrandGold,
            icon = Icons.Filled.School
          )
        }
      }
      item {
        RangeResultCard(
          label = stringResource(R.string.cost_of_living_result_total),
          range = result.totalMonthlyRangeSar,
          darkMode = darkMode,
          accent = BrandGreen
        )
      }
      item {
        Text(
          stringResource(R.string.cost_of_living_target_hint),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
        )
      }
      item {
        InteractiveCTA(
          text = stringResource(R.string.cost_of_living_action_use_as_target),
          onClick = onUseAsReverseSalaryTarget,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    item { HorizontalDivider(color = designSystemContentColor(darkMode).copy(alpha = 0.08f)) }
    item {
      Text(
        stringResource(R.string.cost_of_living_disclaimer, CostOfLivingSchedule.LAST_VERIFIED_LABEL),
        style = MaterialTheme.typography.bodySmall,
        color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
      )
    }
  }
}

/** Range variant of [com.saudi.salarycalculator.core.designsystem.components.ResultCard] — this
 * screen's figures are deliberately wide ranges (see [CostOfLivingSchedule]'s class doc), so
 * showing a single animated number the way [ResultCard] does elsewhere would overstate how
 * precise this estimate is. */
@Composable
private fun RangeResultCard(
  label: String,
  range: IntRange,
  darkMode: Boolean,
  accent: androidx.compose.ui.graphics.Color,
  modifier: Modifier = Modifier,
  icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
  GlassCard(darkMode = darkMode, modifier = modifier) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      icon?.let {
        Icon(it, contentDescription = null, tint = accent, modifier = Modifier.padding(end = 8.dp))
      }
      Text(
        label,
        style = MaterialTheme.typography.labelLarge,
        color = designSystemContentColor(darkMode).copy(alpha = 0.7f),
        fontWeight = FontWeight.SemiBold
      )
    }
    Text(
      stringResource(
        R.string.cost_of_living_range_format,
        String.format(Locale.US, "%,d", range.first),
        String.format(Locale.US, "%,d", range.last)
      ),
      style = MaterialTheme.typography.headlineSmall,
      fontWeight = FontWeight.Black,
      color = accent
    )
  }
}
