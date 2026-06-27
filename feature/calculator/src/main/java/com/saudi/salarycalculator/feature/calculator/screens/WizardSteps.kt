package com.saudi.salarycalculator.feature.calculator.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.components.CalendarPicker
import com.saudi.salarycalculator.core.designsystem.components.CurrencyInputField
import com.saudi.salarycalculator.core.designsystem.components.GlassCard
import com.saudi.salarycalculator.core.designsystem.components.formatHijriDate
import com.saudi.salarycalculator.core.designsystem.components.formatHijriMonthYear
import com.saudi.salarycalculator.core.designsystem.components.InfoTooltip
import com.saudi.salarycalculator.core.designsystem.components.InteractiveCTA
import com.saudi.salarycalculator.core.designsystem.components.NumberStepper
import com.saudi.salarycalculator.core.designsystem.components.ResultCard
import com.saudi.salarycalculator.core.designsystem.components.RiyalSymbol
import com.saudi.salarycalculator.core.designsystem.components.SalaryInputField
import com.saudi.salarycalculator.core.designsystem.components.SectionHeader
import com.saudi.salarycalculator.core.designsystem.components.SegmentedControl
import com.saudi.salarycalculator.core.designsystem.components.ToggleRow
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.calculator.DefaultEndOfServiceCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultGosiCalculatorService
import com.saudi.salarycalculator.core.model.ContractType
import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.EmploymentSector
import com.saudi.salarycalculator.core.model.EndOfServiceInput
import com.saudi.salarycalculator.core.model.GosiInput
import com.saudi.salarycalculator.core.model.GosiRates
import com.saudi.salarycalculator.feature.calculator.R
import com.saudi.salarycalculator.feature.calculator.WizardFieldsState
import java.text.SimpleDateFormat
import java.util.Locale

/** The six step bodies of the calculator wizard. Each takes the raw [WizardFieldsState] plus an
 * [onUpdate] transform-callback so every field edit flows back through [SalaryViewModel.updateWizard]
 * without these composables needing to know about the ViewModel itself. */

@Composable
fun StepBasicSalaryContent(
  wizard: WizardFieldsState,
  darkMode: Boolean,
  currencySymbol: String,
  onUpdate: ((WizardFieldsState) -> WizardFieldsState) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
    SalaryInputField(
      label = stringResource(R.string.label_employee_name),
      value = wizard.employeeName,
      onValueChange = { v -> onUpdate { it.copy(employeeName = v) } },
      darkMode = darkMode,
      placeholder = stringResource(R.string.hint_employee_name)
    )
    SalaryInputField(
      label = stringResource(R.string.label_job_title),
      value = wizard.jobTitle,
      onValueChange = { v -> onUpdate { it.copy(jobTitle = v) } },
      darkMode = darkMode,
      placeholder = stringResource(R.string.hint_job_title)
    )
    CurrencyInputField(
      label = stringResource(R.string.label_basic_salary),
      value = wizard.basicSalary,
      onValueChange = { v -> onUpdate { it.copy(basicSalary = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      helperText = stringResource(R.string.helper_basic_salary),
      invalidNumberText = stringResource(R.string.common_error_invalid_number),
      showSlider = true,
      sliderRange = 0f..30000f
    )
  }
}

@Composable
fun StepAllowancesContent(
  wizard: WizardFieldsState,
  darkMode: Boolean,
  currencySymbol: String,
  onUpdate: ((WizardFieldsState) -> WizardFieldsState) -> Unit,
  modifier: Modifier = Modifier
) {
  val optional = stringResource(R.string.common_optional)
  Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
    CurrencyInputField(
      label = stringResource(R.string.label_housing_allowance),
      value = wizard.housingAllowance,
      onValueChange = { v -> onUpdate { it.copy(housingAllowance = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      optionalLabel = optional
    )
    CurrencyInputField(
      label = stringResource(R.string.label_transport_allowance),
      value = wizard.transportAllowance,
      onValueChange = { v -> onUpdate { it.copy(transportAllowance = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      optionalLabel = optional
    )
    CurrencyInputField(
      label = stringResource(R.string.label_other_allowance),
      value = wizard.otherAllowances,
      onValueChange = { v -> onUpdate { it.copy(otherAllowances = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      optionalLabel = optional
    )
    CurrencyInputField(
      label = stringResource(R.string.label_bonus),
      value = wizard.bonus,
      onValueChange = { v -> onUpdate { it.copy(bonus = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      optionalLabel = optional
    )
    CurrencyInputField(
      label = stringResource(R.string.label_commission),
      value = wizard.commission,
      onValueChange = { v -> onUpdate { it.copy(commission = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      optionalLabel = optional
    )
    HorizontalDivider(color = designSystemContentColor(darkMode).copy(alpha = 0.08f))
    SectionHeader(title = stringResource(R.string.section_overtime), darkMode = darkMode)
    SalaryInputField(
      label = stringResource(R.string.label_overtime_hours),
      value = wizard.overtimeHours,
      onValueChange = { v -> onUpdate { it.copy(overtimeHours = v.filter { ch -> ch.isDigit() || ch == '.' }) } },
      darkMode = darkMode,
      keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal,
      placeholder = "0"
    )
    val rateOverrideHelp = stringResource(R.string.helper_overtime_rate_override)
    SalaryInputField(
      label = stringResource(R.string.label_overtime_rate_override),
      value = wizard.overtimeHourlyRateOverride,
      onValueChange = { v -> onUpdate { it.copy(overtimeHourlyRateOverride = v.filter { ch -> ch.isDigit() || ch == '.' }) } },
      darkMode = darkMode,
      keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal,
      placeholder = optional,
      labelTrailing = { InfoTooltip(text = rateOverrideHelp, darkMode = darkMode) }
    )
  }
}

@Composable
fun StepDeductionsContent(
  wizard: WizardFieldsState,
  darkMode: Boolean,
  currencySymbol: String,
  onUpdate: ((WizardFieldsState) -> WizardFieldsState) -> Unit,
  modifier: Modifier = Modifier
) {
  val optional = stringResource(R.string.common_optional)
  Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
    CurrencyInputField(
      label = stringResource(R.string.label_loan_deduction),
      value = wizard.loanDeduction,
      onValueChange = { v -> onUpdate { it.copy(loanDeduction = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      optionalLabel = optional
    )
    CurrencyInputField(
      label = stringResource(R.string.label_absence_deduction),
      value = wizard.absenceDeduction,
      onValueChange = { v -> onUpdate { it.copy(absenceDeduction = v) } },
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      optionalLabel = optional
    )
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Text(
        stringResource(R.string.label_unpaid_leave_days),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = designSystemContentColor(darkMode).copy(alpha = 0.85f)
      )
      NumberStepper(
        value = wizard.unpaidLeaveDays.toIntOrNull() ?: 0,
        darkMode = darkMode,
        max = 30,
        onValueChange = { v -> onUpdate { it.copy(unpaidLeaveDays = v.toString()) } }
      )
      Text(
        stringResource(R.string.helper_unpaid_leave_days),
        style = MaterialTheme.typography.labelSmall,
        color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepEmploymentDetailsContent(
  wizard: WizardFieldsState,
  darkMode: Boolean,
  onUpdate: ((WizardFieldsState) -> WizardFieldsState) -> Unit,
  modifier: Modifier = Modifier
) {
  var datePickerTarget by remember { mutableStateOf<DateTarget?>(null) }

  Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Text(
        stringResource(R.string.label_nationality),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = designSystemContentColor(darkMode).copy(alpha = 0.85f)
      )
      SegmentedControl(
        options = listOf(stringResource(R.string.option_saudi), stringResource(R.string.option_non_saudi)),
        selectedIndex = if (wizard.employeeType == EmployeeType.SAUDI) 0 else 1,
        darkMode = darkMode,
        onSelect = { i -> onUpdate { it.copy(employeeType = if (i == 0) EmployeeType.SAUDI else EmployeeType.EXPAT) } }
      )
    }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Text(
        stringResource(R.string.label_employment_sector),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = designSystemContentColor(darkMode).copy(alpha = 0.85f)
      )
      SegmentedControl(
        options = listOf(stringResource(R.string.option_private), stringResource(R.string.option_government)),
        selectedIndex = if (wizard.employmentSector == EmploymentSector.PRIVATE) 0 else 1,
        darkMode = darkMode,
        onSelect = { i -> onUpdate { it.copy(employmentSector = if (i == 0) EmploymentSector.PRIVATE else EmploymentSector.GOVERNMENT) } }
      )
    }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Text(
        stringResource(R.string.label_contract_type),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = designSystemContentColor(darkMode).copy(alpha = 0.85f)
      )
      SegmentedControl(
        options = listOf(stringResource(R.string.option_limited), stringResource(R.string.option_unlimited)),
        selectedIndex = if (wizard.contractType == ContractType.LIMITED) 0 else 1,
        darkMode = darkMode,
        onSelect = { i -> onUpdate { it.copy(contractType = if (i == 0) ContractType.LIMITED else ContractType.UNLIMITED) } }
      )
    }
    DateField(
      label = stringResource(R.string.label_joining_date),
      actionLabel = stringResource(R.string.action_pick_date),
      millis = wizard.joiningDateMillis,
      pattern = "d MMM yyyy",
      monthOnly = false,
      darkMode = darkMode,
      onClick = { datePickerTarget = DateTarget.JOINING }
    )
    DateField(
      label = stringResource(R.string.label_calculation_month),
      actionLabel = stringResource(R.string.action_pick_month),
      millis = wizard.calculationMonthMillis,
      pattern = "MMM yyyy",
      monthOnly = true,
      darkMode = darkMode,
      onClick = { datePickerTarget = DateTarget.CALCULATION_MONTH }
    )
  }

  val target = datePickerTarget
  if (target != null) {
    val initialMillis = when (target) {
      DateTarget.JOINING -> wizard.joiningDateMillis
      DateTarget.CALCULATION_MONTH -> wizard.calculationMonthMillis
    }
    DatePickerSheet(
      initialMillis = initialMillis,
      darkMode = darkMode,
      onDismiss = { datePickerTarget = null },
      onConfirm = { selectedMillis ->
        onUpdate {
          when (target) {
            DateTarget.JOINING -> it.copy(joiningDateMillis = selectedMillis)
            DateTarget.CALCULATION_MONTH -> it.copy(calculationMonthMillis = selectedMillis)
          }
        }
        datePickerTarget = null
      }
    )
  }
}

private enum class DateTarget { JOINING, CALCULATION_MONTH }

@Composable
private fun DateField(
  label: String,
  actionLabel: String,
  millis: Long?,
  pattern: String,
  monthOnly: Boolean,
  darkMode: Boolean,
  onClick: () -> Unit
) {
  val isArabic = LocalLayoutDirection.current == LayoutDirection.Rtl
  val display = millis?.let {
    if (isArabic) {
      if (monthOnly) formatHijriMonthYear(it) else formatHijriDate(it)
    } else {
      SimpleDateFormat(pattern, Locale.getDefault()).format(java.util.Date(it))
    }
  }
  Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
    Text(
      label,
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = designSystemContentColor(darkMode).copy(alpha = 0.85f)
    )
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(if (darkMode) Color.White.copy(alpha = 0.05f) else Color.White)
        .clickable(onClick = onClick)
        .padding(horizontal = 14.dp, vertical = 14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
      Text(
        display ?: actionLabel,
        color = if (display != null) designSystemContentColor(darkMode) else designSystemContentColor(darkMode).copy(alpha = 0.45f),
        fontWeight = FontWeight.SemiBold
      )
      Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = BrandGreen)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerSheet(
  initialMillis: Long?,
  darkMode: Boolean,
  onDismiss: () -> Unit,
  onConfirm: (Long) -> Unit
) {
  val sheetState: SheetState = rememberModalBottomSheetState()
  val isArabic = LocalLayoutDirection.current == LayoutDirection.Rtl
  var selectedMillis by remember { mutableStateOf(initialMillis) }

  ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
      CalendarPicker(
        initialMillis = initialMillis,
        darkMode = darkMode,
        useHijri = isArabic,
        todayLabel = stringResource(R.string.common_today),
        modifier = Modifier.fillMaxWidth(),
        onDateSelected = { selectedMillis = it }
      )
      Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.End
      ) {
        Text(
          stringResource(R.string.common_cancel),
          color = designSystemContentColor(darkMode).copy(alpha = 0.6f),
          fontWeight = FontWeight.Bold,
          modifier = Modifier.clickable(onClick = onDismiss).padding(12.dp)
        )
        Text(
          stringResource(R.string.common_confirm),
          color = BrandGreen,
          fontWeight = FontWeight.Black,
          modifier = Modifier
            .clickable {
              selectedMillis?.let(onConfirm)
            }
            .padding(12.dp)
        )
      }
    }
  }
}

@Composable
fun StepGosiEosbContent(
  wizard: WizardFieldsState,
  darkMode: Boolean,
  currencySymbol: String,
  gosiRates: GosiRates,
  onUpdate: ((WizardFieldsState) -> WizardFieldsState) -> Unit,
  modifier: Modifier = Modifier
) {
  val basic = wizard.basicSalary.toDoubleOrNull() ?: 0.0
  val housing = wizard.housingAllowance.toDoubleOrNull() ?: 0.0
  val yearsOfService = remember(wizard.joiningDateMillis, wizard.calculationMonthMillis) {
    val start = wizard.joiningDateMillis
    if (start == null) {
      0.0
    } else {
      val end = wizard.calculationMonthMillis ?: System.currentTimeMillis()
      (end - start).coerceAtLeast(0L) / (1000.0 * 60.0 * 60.0 * 24.0 * 365.25)
    }
  }
  val gosiPreview = remember(basic, housing, wizard.employeeType, gosiRates) {
    DefaultGosiCalculatorService().calculate(
      GosiInput(baseAmount = basic + housing, employeeType = wizard.employeeType, rates = gosiRates)
    )
  }
  val eosbPreview = remember(yearsOfService, basic, wizard.resigned) {
    DefaultEndOfServiceCalculatorService().calculate(
      EndOfServiceInput(lastBasicSalary = basic, yearsOfService = yearsOfService, resigned = wizard.resigned)
    )
  }

  Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
    ToggleRow(
      label = stringResource(R.string.label_gosi_included),
      checked = wizard.gosiIncluded,
      darkMode = darkMode,
      description = stringResource(R.string.helper_gosi_included),
      onCheckedChange = { checked -> onUpdate { it.copy(gosiIncluded = checked) } }
    )
    ToggleRow(
      label = stringResource(R.string.label_resigned),
      checked = wizard.resigned,
      darkMode = darkMode,
      description = stringResource(R.string.helper_resigned),
      onCheckedChange = { checked -> onUpdate { it.copy(resigned = checked) } }
    )
    GlassCard(darkMode = darkMode) {
      Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text(
          stringResource(R.string.label_years_of_service),
          style = MaterialTheme.typography.bodyMedium,
          color = designSystemContentColor(darkMode).copy(alpha = 0.7f)
        )
        Text(
          "%.1f".format(yearsOfService),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Black,
          color = designSystemContentColor(darkMode)
        )
      }
    }
    ResultCard(
      label = stringResource(R.string.preview_gosi_employee),
      value = if (wizard.gosiIncluded) gosiPreview.employeeContribution else 0.0,
      currencySymbol = currencySymbol,
      darkMode = darkMode,
      accent = BrandRed,
      isDeduction = true
    )
    ResultCard(
      label = stringResource(R.string.preview_gosi_employer),
      value = if (wizard.gosiIncluded) gosiPreview.employerContribution else 0.0,
      currencySymbol = currencySymbol,
      darkMode = darkMode,
      accent = BrandGreen
    )
    ResultCard(
      label = stringResource(R.string.preview_eosb_estimate),
      value = eosbPreview.rewardAmount,
      currencySymbol = currencySymbol,
      darkMode = darkMode,
      accent = BrandGreen
    )
  }
}

@Composable
fun StepReviewContent(
  wizard: WizardFieldsState,
  darkMode: Boolean,
  currencySymbol: String,
  isCalculating: Boolean,
  onCalculate: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
    Column {
      Text(
        stringResource(R.string.review_title),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Black,
        color = designSystemContentColor(darkMode)
      )
      Text(
        stringResource(R.string.review_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
      )
    }

    ReviewSection(
      title = stringResource(R.string.review_section_basic),
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      rows = listOfNotNull(
        wizard.employeeName.takeIf { it.isNotBlank() }?.let { ReviewRow(stringResource(R.string.label_employee_name), it) },
        wizard.jobTitle.takeIf { it.isNotBlank() }?.let { ReviewRow(stringResource(R.string.label_job_title), it) },
        ReviewRow(stringResource(R.string.label_basic_salary), wizard.basicSalary.ifBlank { "0" }, isCurrency = true)
      )
    )
    ReviewSection(
      title = stringResource(R.string.review_section_allowances),
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      rows = listOf(
        ReviewRow(stringResource(R.string.label_housing_allowance), wizard.housingAllowance.ifBlank { "0" }, isCurrency = true),
        ReviewRow(stringResource(R.string.label_transport_allowance), wizard.transportAllowance.ifBlank { "0" }, isCurrency = true),
        ReviewRow(stringResource(R.string.label_other_allowance), wizard.otherAllowances.ifBlank { "0" }, isCurrency = true),
        ReviewRow(stringResource(R.string.label_bonus), wizard.bonus.ifBlank { "0" }, isCurrency = true),
        ReviewRow(stringResource(R.string.label_commission), wizard.commission.ifBlank { "0" }, isCurrency = true),
        ReviewRow(stringResource(R.string.label_overtime_hours), wizard.overtimeHours.ifBlank { "0" })
      )
    )
    ReviewSection(
      title = stringResource(R.string.review_section_deductions),
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      rows = listOf(
        ReviewRow(stringResource(R.string.label_loan_deduction), wizard.loanDeduction.ifBlank { "0" }, isCurrency = true),
        ReviewRow(stringResource(R.string.label_absence_deduction), wizard.absenceDeduction.ifBlank { "0" }, isCurrency = true),
        ReviewRow(stringResource(R.string.label_unpaid_leave_days), wizard.unpaidLeaveDays.ifBlank { "0" })
      )
    )
    ReviewSection(
      title = stringResource(R.string.review_section_employment),
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      rows = listOf(
        ReviewRow(stringResource(R.string.label_nationality), stringResource(if (wizard.employeeType == EmployeeType.SAUDI) R.string.option_saudi else R.string.option_non_saudi)),
        ReviewRow(stringResource(R.string.label_employment_sector), stringResource(if (wizard.employmentSector == EmploymentSector.PRIVATE) R.string.option_private else R.string.option_government)),
        ReviewRow(stringResource(R.string.label_contract_type), stringResource(if (wizard.contractType == ContractType.LIMITED) R.string.option_limited else R.string.option_unlimited))
      )
    )
    ReviewSection(
      title = stringResource(R.string.review_section_gosi_eosb),
      darkMode = darkMode,
      currencySymbol = currencySymbol,
      rows = listOf(
        ReviewRow(stringResource(R.string.label_gosi_included), stringResource(if (wizard.gosiIncluded) R.string.common_yes else R.string.common_no)),
        ReviewRow(stringResource(R.string.label_resigned), stringResource(if (wizard.resigned) R.string.common_yes else R.string.common_no))
      )
    )

    InteractiveCTA(
      text = stringResource(R.string.action_calculate_net_salary),
      icon = Icons.Filled.Calculate,
      enabled = (wizard.basicSalary.toDoubleOrNull() ?: 0.0) > 0.0,
      loading = isCalculating,
      onClick = onCalculate,
      modifier = Modifier.fillMaxWidth()
    )
  }
}

private data class ReviewRow(val label: String, val value: String, val isCurrency: Boolean = false)

@Composable
private fun ReviewSection(title: String, darkMode: Boolean, currencySymbol: String, rows: List<ReviewRow>) {
  GlassCard(darkMode = darkMode) {
    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = designSystemContentColor(darkMode))
    rows.forEach { row ->
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(row.label, style = MaterialTheme.typography.bodyMedium, color = designSystemContentColor(darkMode).copy(alpha = 0.6f))
        if (row.isCurrency) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(row.value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = designSystemContentColor(darkMode))
            Spacer(Modifier.width(4.dp))
            RiyalSymbol(tint = designSystemContentColor(darkMode), size = 12.dp, contentDescription = currencySymbol)
          }
        } else {
          Text(row.value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = designSystemContentColor(darkMode))
        }
      }
    }
  }
}
