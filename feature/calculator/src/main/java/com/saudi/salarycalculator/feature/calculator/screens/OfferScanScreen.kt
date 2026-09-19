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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.components.CurrencyInputField
import com.saudi.salarycalculator.core.designsystem.components.GlassCard
import com.saudi.salarycalculator.core.designsystem.components.InteractiveCTA
import com.saudi.salarycalculator.core.designsystem.components.NumberStepper
import com.saudi.salarycalculator.core.designsystem.components.SalaryInputField
import com.saudi.salarycalculator.core.designsystem.components.SectionHeader
import com.saudi.salarycalculator.core.designsystem.components.ToggleRow
import com.saudi.salarycalculator.core.designsystem.theme.BrandGold
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.model.OfferFlagSeverity
import com.saudi.salarycalculator.core.model.OfferFlagType
import com.saudi.salarycalculator.core.model.OfferRedFlagFinding
import com.saudi.salarycalculator.core.model.OfferRedFlagResult
import com.saudi.salarycalculator.feature.calculator.OfferScanFormState
import com.saudi.salarycalculator.feature.calculator.R

/** Offer red-flag scanner: a stateless one-shot check (mirrors [ComparisonScreen]'s pattern —
 * form always visible, a "Scan" CTA, result appears below once available; no persisted profile,
 * since a scan is a one-off check rather than a standing fact about the user's job). Every rule
 * lives in [com.saudi.salarycalculator.core.calculator.DefaultOfferRedFlagCalculatorService]; this
 * screen only renders whatever [OfferFlagType]s come back. Deliberately keeps a disclaimer
 * visible at all times (not just after scanning) — this is a screening heuristic, not legal
 * advice, and should never read as a verdict. */
@Composable
fun OfferScanScreen(
  darkMode: Boolean,
  currencySymbol: String,
  form: OfferScanFormState,
  result: OfferRedFlagResult?,
  onUpdateForm: ((OfferScanFormState) -> OfferScanFormState) -> Unit,
  onScan: () -> Unit,
  modifier: Modifier = Modifier
) {
  val canScan = (form.basicSalary.toDoubleOrNull() ?: 0.0) > 0.0

  LazyColumn(
    modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
    contentPadding = PaddingValues(top = 4.dp, bottom = 32.dp)
  ) {
    item {
      Text(
        stringResource(R.string.offer_scan_intro),
        style = MaterialTheme.typography.bodyMedium,
        color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
      )
    }

    item {
      val optional = stringResource(R.string.common_optional)
      GlassCard(darkMode = darkMode) {
        CurrencyInputField(
          label = stringResource(R.string.label_basic_salary),
          value = form.basicSalary,
          onValueChange = { v -> onUpdateForm { it.copy(basicSalary = v) } },
          darkMode = darkMode,
          currencySymbol = currencySymbol
        )
        CurrencyInputField(
          label = stringResource(R.string.offer_scan_label_total_salary),
          value = form.totalMonthlySalary,
          onValueChange = { v -> onUpdateForm { it.copy(totalMonthlySalary = v) } },
          darkMode = darkMode,
          currencySymbol = currencySymbol,
          optionalLabel = optional
        )
      }
    }

    item {
      GlassCard(darkMode = darkMode) {
        // Stacked (label above, stepper below) rather than side-by-side: "Probation period
        // (months)" is long enough that sharing a row with the 152dp-wide stepper pushed the
        // stepper off the edge of the card. Same reason Reverse Salary's housing/transport %
        // fields (equally long labels) use this stacked layout instead of the same-row layout
        // used for short labels like "Additional adults" or "Children" on Cost of Living.
        Text(
          stringResource(R.string.offer_scan_label_probation),
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.SemiBold,
          color = designSystemContentColor(darkMode)
        )
        NumberStepper(
          value = form.probationMonths,
          darkMode = darkMode,
          increaseContentDescription = stringResource(R.string.common_increase),
          decreaseContentDescription = stringResource(R.string.common_decrease),
          min = 0,
          max = 24,
          onValueChange = { v -> onUpdateForm { it.copy(probationMonths = v) } }
        )
        // Only meaningful once probation runs past the 90-day statutory default — hidden
        // otherwise so the form doesn't ask about something that doesn't yet apply.
        if (form.probationMonths > 3) {
          ToggleRow(
            label = stringResource(R.string.offer_scan_label_probation_extension),
            description = stringResource(R.string.offer_scan_label_probation_extension_desc),
            checked = form.probationExtendedInWriting,
            darkMode = darkMode,
            onCheckedChange = { v -> onUpdateForm { it.copy(probationExtendedInWriting = v) } }
          )
        }
      }
    }

    item {
      GlassCard(darkMode = darkMode) {
        SalaryInputField(
          label = stringResource(R.string.offer_scan_label_employee_notice),
          // Raw text, not re-derived from a parsed Int on every keystroke — see
          // OfferScanFormState's KDoc for why that was breaking typed input. Non-digit
          // characters are stripped so the field still only ever holds a plain number, but
          // the field is never forced back to "0" while the user is mid-edit.
          value = form.employeeNoticeDays,
          onValueChange = { v ->
            onUpdateForm { it.copy(employeeNoticeDays = v.filter { ch -> ch.isDigit() }.take(3)) }
          },
          darkMode = darkMode,
          keyboardType = KeyboardType.Number
        )
        SalaryInputField(
          label = stringResource(R.string.offer_scan_label_employer_notice),
          value = form.employerNoticeDays,
          onValueChange = { v ->
            onUpdateForm { it.copy(employerNoticeDays = v.filter { ch -> ch.isDigit() }.take(3)) }
          },
          darkMode = darkMode,
          keyboardType = KeyboardType.Number
        )
      }
    }

    item {
      GlassCard(darkMode = darkMode) {
        ToggleRow(
          label = stringResource(R.string.offer_scan_label_gosi),
          description = stringResource(R.string.offer_scan_label_gosi_desc),
          checked = form.isGosiRegistered,
          darkMode = darkMode,
          onCheckedChange = { v -> onUpdateForm { it.copy(isGosiRegistered = v) } }
        )
        ToggleRow(
          label = stringResource(R.string.offer_scan_label_contract),
          description = stringResource(R.string.offer_scan_label_contract_desc),
          checked = form.hasWrittenContract,
          darkMode = darkMode,
          onCheckedChange = { v -> onUpdateForm { it.copy(hasWrittenContract = v) } }
        )
        ToggleRow(
          label = stringResource(R.string.offer_scan_label_fees),
          description = stringResource(R.string.offer_scan_label_fees_desc),
          checked = form.recruitmentFeesCharged,
          darkMode = darkMode,
          onCheckedChange = { v -> onUpdateForm { it.copy(recruitmentFeesCharged = v) } }
        )
      }
    }

    item {
      InteractiveCTA(
        text = stringResource(R.string.action_scan_offer),
        icon = Icons.Filled.Flag,
        enabled = canScan,
        onClick = onScan,
        modifier = Modifier.fillMaxWidth()
      )
    }

    if (result == null) {
      item {
        GlassCard(darkMode = darkMode) {
          Text(
            stringResource(R.string.offer_scan_empty_title),
            style = MaterialTheme.typography.bodyMedium,
            color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
          )
        }
      }
    } else {
      item { SectionHeader(title = summaryTitle(result), darkMode = darkMode) }
      items(result.findings) { finding ->
        FindingCard(finding = finding, darkMode = darkMode)
      }
    }

    item { HorizontalDivider(color = designSystemContentColor(darkMode).copy(alpha = 0.08f)) }
    item {
      Text(
        stringResource(R.string.offer_scan_disclaimer),
        style = MaterialTheme.typography.bodySmall,
        color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
      )
    }
  }
}

@Composable
private fun summaryTitle(result: OfferRedFlagResult): String {
  return if (result.violationCount == 0 && result.cautionCount == 0) {
    stringResource(R.string.offer_scan_summary_clear)
  } else {
    stringResource(R.string.offer_scan_summary_issues, result.violationCount, result.cautionCount)
  }
}

@Composable
private fun FindingCard(finding: OfferRedFlagFinding, darkMode: Boolean) {
  val (titleRes, descRes) = when (finding.type) {
    OfferFlagType.PROBATION_EXCEEDS_MAX ->
      R.string.offer_scan_flag_probation_exceeds_title to R.string.offer_scan_flag_probation_exceeds_desc
    OfferFlagType.PROBATION_EXTENSION_NOT_WRITTEN ->
      R.string.offer_scan_flag_probation_extension_title to R.string.offer_scan_flag_probation_extension_desc
    OfferFlagType.GOSI_NOT_REGISTERED ->
      R.string.offer_scan_flag_gosi_title to R.string.offer_scan_flag_gosi_desc
    OfferFlagType.NO_WRITTEN_CONTRACT ->
      R.string.offer_scan_flag_contract_title to R.string.offer_scan_flag_contract_desc
    OfferFlagType.RECRUITMENT_FEES_CHARGED ->
      R.string.offer_scan_flag_fees_title to R.string.offer_scan_flag_fees_desc
    OfferFlagType.ASYMMETRIC_NOTICE_PERIOD ->
      R.string.offer_scan_flag_notice_title to R.string.offer_scan_flag_notice_desc
    OfferFlagType.LOW_BASIC_RATIO ->
      R.string.offer_scan_flag_basic_ratio_title to R.string.offer_scan_flag_basic_ratio_desc
    OfferFlagType.ALL_CLEAR ->
      R.string.offer_scan_flag_all_clear_title to R.string.offer_scan_flag_all_clear_desc
  }
  val (icon, tint, severityLabelRes) = when (finding.severity) {
    OfferFlagSeverity.VIOLATION -> Triple(Icons.Filled.WarningAmber, BrandRed, R.string.offer_scan_severity_violation)
    OfferFlagSeverity.CAUTION -> Triple(Icons.Filled.Info, BrandGold, R.string.offer_scan_severity_caution)
    OfferFlagSeverity.INFO -> Triple(
      if (finding.type == OfferFlagType.ALL_CLEAR) Icons.Filled.CheckCircle else Icons.Filled.Info,
      if (finding.type == OfferFlagType.ALL_CLEAR) BrandGreen else designSystemContentColor(darkMode).copy(alpha = 0.7f),
      R.string.offer_scan_severity_info
    )
  }

  GlassCard(darkMode = darkMode) {
    Row(verticalAlignment = Alignment.Top) {
      Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.width(20.dp).padding(top = 2.dp))
      Spacer(Modifier.width(10.dp))
      androidx.compose.foundation.layout.Column {
        Text(
          stringResource(severityLabelRes),
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = tint.copy(alpha = 0.85f)
        )
        Text(
          stringResource(titleRes),
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = designSystemContentColor(darkMode)
        )
        Text(
          stringResource(descRes),
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
        )
      }
    }
  }
}
