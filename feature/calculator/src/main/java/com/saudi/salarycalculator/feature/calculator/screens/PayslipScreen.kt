package com.saudi.salarycalculator.feature.calculator.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.components.AnimatedCounter
import com.saudi.salarycalculator.core.designsystem.components.GlassCard
import com.saudi.salarycalculator.core.designsystem.components.InteractiveCTA
import com.saudi.salarycalculator.core.designsystem.components.PayslipLine
import com.saudi.salarycalculator.core.designsystem.components.PayslipPreviewCard
import com.saudi.salarycalculator.core.designsystem.components.RiyalSymbol
import com.saudi.salarycalculator.core.designsystem.components.SecondaryButton
import com.saudi.salarycalculator.core.designsystem.components.SectionHeader
import com.saudi.salarycalculator.core.designsystem.theme.BrandGold
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.model.ContractType
import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.EmploymentSector
import com.saudi.salarycalculator.core.model.NetSalaryResult
import com.saudi.salarycalculator.feature.calculator.R
import com.saudi.salarycalculator.feature.calculator.WizardFieldsState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Read-only payslip rendering of the last [NetSalaryResult], grouped the same way a printed
 * Saudi payslip would be (employee details, earnings, allowances, deductions, GOSI, net). The
 * actual PDF/share/save side effects are the caller's responsibility (see [onExportPdf],
 * [onSaveCalculation], [onShare]) so this screen stays a pure function of [wizard] + [result],
 * consistent with every other screen in the feature. */
@Composable
fun PayslipScreen(
  darkMode: Boolean,
  currencySymbol: String,
  wizard: WizardFieldsState,
  result: NetSalaryResult?,
  onExportPdf: () -> Unit,
  onSaveCalculation: () -> Unit,
  onShare: () -> Unit,
  onExportSalaryCertificate: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (result == null) {
    PayslipEmptyState(darkMode = darkMode, modifier = modifier)
    return
  }

  val dateFormat = remember(wizard.joiningDateMillis) { SimpleDateFormat("d MMM yyyy", Locale.getDefault()) }
  val monthFormat = remember(wizard.calculationMonthMillis) { SimpleDateFormat("MMM yyyy", Locale.getDefault()) }
  val basic = wizard.basicSalary.toDoubleOrNull() ?: 0.0
  val housing = wizard.housingAllowance.toDoubleOrNull() ?: 0.0
  val transport = wizard.transportAllowance.toDoubleOrNull() ?: 0.0
  val otherAllowance = wizard.otherAllowances.toDoubleOrNull() ?: 0.0
  val bonus = wizard.bonus.toDoubleOrNull() ?: 0.0
  val commission = wizard.commission.toDoubleOrNull() ?: 0.0
  val loan = wizard.loanDeduction.toDoubleOrNull() ?: 0.0
  val absence = wizard.absenceDeduction.toDoubleOrNull() ?: 0.0
  val unpaidLeaveDays = wizard.unpaidLeaveDays.toDoubleOrNull() ?: 0.0
  val unpaidLeaveDeduction = (basic / 30.0) * unpaidLeaveDays
  val otherDeduction = wizard.otherDeductions.toDoubleOrNull() ?: 0.0

  LazyColumn(
    modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
  ) {
    item { SectionHeader(title = stringResource(R.string.payslip_title), darkMode = darkMode) }

    item {
      GlassCard(darkMode = darkMode) {
        Text(
          stringResource(R.string.payslip_employee_details),
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Black,
          color = designSystemContentColor(darkMode)
        )
        PayslipDetailRow(stringResource(R.string.payslip_employee_name), wizard.employeeName.ifBlank { "—" }, darkMode)
        PayslipDetailRow(stringResource(R.string.payslip_job_title), wizard.jobTitle.ifBlank { "—" }, darkMode)
        PayslipDetailRow(
          stringResource(R.string.payslip_nationality),
          stringResource(if (wizard.employeeType == EmployeeType.SAUDI) R.string.option_saudi else R.string.option_non_saudi),
          darkMode
        )
        PayslipDetailRow(
          stringResource(R.string.payslip_sector),
          stringResource(if (wizard.employmentSector == EmploymentSector.PRIVATE) R.string.option_private else R.string.option_government),
          darkMode
        )
        PayslipDetailRow(
          stringResource(R.string.payslip_contract_type),
          stringResource(if (wizard.contractType == ContractType.LIMITED) R.string.option_limited else R.string.option_unlimited),
          darkMode
        )
        PayslipDetailRow(
          stringResource(R.string.payslip_joining_date),
          wizard.joiningDateMillis?.let { dateFormat.format(Date(it)) } ?: "—",
          darkMode
        )
        PayslipDetailRow(
          stringResource(R.string.payslip_calculation_month),
          wizard.calculationMonthMillis?.let { monthFormat.format(Date(it)) } ?: "—",
          darkMode
        )
      }
    }

    item {
      PayslipPreviewCard(
        sectionTitle = stringResource(R.string.payslip_earnings),
        lines = listOf(
          PayslipLine(stringResource(R.string.label_basic_salary), basic),
          PayslipLine(stringResource(R.string.label_bonus), bonus),
          PayslipLine(stringResource(R.string.label_commission), commission),
          PayslipLine(stringResource(R.string.result_overtime_amount), result.overtimePay)
        ),
        currencySymbol = currencySymbol,
        darkMode = darkMode
      )
    }

    item {
      PayslipPreviewCard(
        sectionTitle = stringResource(R.string.payslip_allowances),
        lines = listOf(
          PayslipLine(stringResource(R.string.label_housing_allowance), housing),
          PayslipLine(stringResource(R.string.label_transport_allowance), transport),
          PayslipLine(stringResource(R.string.label_other_allowance), otherAllowance)
        ),
        currencySymbol = currencySymbol,
        darkMode = darkMode,
        totalLabel = stringResource(R.string.result_total_allowances),
        total = result.totalAllowances
      )
    }

    item {
      PayslipPreviewCard(
        sectionTitle = stringResource(R.string.payslip_deductions),
        lines = listOf(
          PayslipLine(stringResource(R.string.label_loan_deduction), loan, isDeduction = true),
          PayslipLine(stringResource(R.string.label_absence_deduction), absence, isDeduction = true),
          PayslipLine(stringResource(R.string.label_unpaid_leave_days), unpaidLeaveDeduction, isDeduction = true),
          PayslipLine(stringResource(R.string.label_other_deduction), otherDeduction, isDeduction = true)
        ),
        currencySymbol = currencySymbol,
        darkMode = darkMode
      )
    }

    item {
      PayslipPreviewCard(
        sectionTitle = stringResource(R.string.payslip_gosi),
        lines = listOf(
          PayslipLine(stringResource(R.string.result_gosi_employee), result.employeeGosiAmount, isDeduction = true),
          PayslipLine(stringResource(R.string.payslip_employer_contribution), result.employerGosiAmount)
        ),
        currencySymbol = currencySymbol,
        darkMode = darkMode
      )
    }
    item {
      // Same reasoning as ResultScreen: the employer contribution line sits right under the
      // employee's GOSI deduction with no minus sign, so without this a reader could think it's
      // also coming out of their pay (it isn't — it never touches net salary).
      Text(
        stringResource(R.string.result_gosi_employer_helper),
        style = MaterialTheme.typography.bodySmall,
        color = designSystemContentColor(darkMode).copy(alpha = 0.55f)
      )
    }

    item {
      GlassCard(darkMode = darkMode) {
        Text(
          stringResource(R.string.payslip_net_salary),
          style = MaterialTheme.typography.labelLarge,
          color = designSystemContentColor(darkMode).copy(alpha = 0.65f),
          fontWeight = FontWeight.Bold
        )
        Row(verticalAlignment = Alignment.Bottom) {
          AnimatedCounter(
            targetValue = result.netSalary,
            decimals = 2,
            style = MaterialTheme.typography.headlineMedium,
            color = designSystemContentColor(darkMode)
          )
          Spacer(Modifier.width(6.dp))
          RiyalSymbol(
            tint = BrandGreen,
            size = 18.dp,
            contentDescription = currencySymbol,
            modifier = Modifier.padding(bottom = 6.dp)
          )
        }
      }
    }

    item {
      InteractiveCTA(
        text = stringResource(R.string.action_export_pdf),
        icon = Icons.Filled.Download,
        onClick = onExportPdf,
        modifier = Modifier.fillMaxWidth()
      )
    }
    item {
      // Wrapped in the same GlassCard surface as every other section on this screen — previously
      // these sat directly on the animated background with nothing but a thin outline, which
      // picked up the background's color noise and looked smudgy next to the crisp cards above.
      GlassCard(darkMode = darkMode, contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          SecondaryButton(
            text = stringResource(R.string.action_save_calculation),
            darkMode = darkMode,
            icon = Icons.Filled.Save,
            modifier = Modifier.weight(1f),
            onClick = onSaveCalculation
          )
          SecondaryButton(
            text = stringResource(R.string.common_share),
            darkMode = darkMode,
            icon = Icons.Filled.Share,
            modifier = Modifier.weight(1f),
            onClick = onShare
          )
        }
      }
    }

    item {
      GlassCard(darkMode = darkMode) {
        Text(
          stringResource(R.string.certificate_section_title),
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Black,
          color = designSystemContentColor(darkMode)
        )
        Row(verticalAlignment = Alignment.Top) {
          Icon(
            Icons.Filled.WarningAmber,
            contentDescription = null,
            tint = BrandGold,
            modifier = Modifier.width(18.dp).padding(top = 2.dp)
          )
          Spacer(Modifier.width(8.dp))
          Text(
            stringResource(R.string.certificate_disclaimer),
            style = MaterialTheme.typography.bodySmall,
            color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
          )
        }
        SecondaryButton(
          text = stringResource(R.string.action_export_certificate),
          darkMode = darkMode,
          icon = Icons.Filled.Download,
          onClick = onExportSalaryCertificate
        )
      }
    }
  }
}

@Composable
private fun PayslipDetailRow(label: String, value: String, darkMode: Boolean) {
  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Text(
      label,
      style = MaterialTheme.typography.bodyMedium,
      color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
    )
    Text(
      value,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.SemiBold,
      color = designSystemContentColor(darkMode)
    )
  }
}

@Composable
private fun PayslipEmptyState(darkMode: Boolean, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    GlassCard(darkMode = darkMode) {
      Text(
        stringResource(R.string.result_empty_title),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Black,
        color = designSystemContentColor(darkMode)
      )
      Text(
        stringResource(R.string.result_empty_body),
        style = MaterialTheme.typography.bodyMedium,
        color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
      )
    }
  }
}
