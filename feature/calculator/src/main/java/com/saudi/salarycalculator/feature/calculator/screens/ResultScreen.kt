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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.components.AnimatedCounter
import com.saudi.salarycalculator.core.designsystem.components.BreakdownSlice
import com.saudi.salarycalculator.core.designsystem.components.DonutBreakdownChart
import com.saudi.salarycalculator.core.designsystem.components.EarningsVsDeductionsBarChart
import com.saudi.salarycalculator.core.designsystem.components.GlassCard
import com.saudi.salarycalculator.core.designsystem.components.InteractiveCTA
import com.saudi.salarycalculator.core.designsystem.components.NetSalaryProgressIndicator
import com.saudi.salarycalculator.core.designsystem.components.RiyalSymbol
import com.saudi.salarycalculator.core.designsystem.components.ResultCard
import com.saudi.salarycalculator.core.designsystem.components.SectionHeader
import com.saudi.salarycalculator.core.designsystem.components.SegmentedControl
import com.saudi.salarycalculator.core.designsystem.theme.BrandGold
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.designsystem.theme.BrandTeal
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.model.NetSalaryResult
import com.saudi.salarycalculator.feature.calculator.R

/** Result dashboard: shown right after [com.saudi.salarycalculator.feature.calculator.SalaryViewModel.calculateNetSalary]
 * completes, and reachable again any time from Home/history while [result] is non-null. A
 * Monthly/Yearly [SegmentedControl] re-scales every figure on the screen from the same
 * [NetSalaryResult] without re-running the calculation. */
@Composable
fun ResultScreen(
  darkMode: Boolean,
  currencySymbol: String,
  result: NetSalaryResult?,
  onViewPayslip: () -> Unit,
  onCompareOffer: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (result == null) {
    ResultEmptyState(darkMode = darkMode, modifier = modifier)
    return
  }

  var yearly by remember { mutableStateOf(false) }
  val multiplier = if (yearly) 12.0 else 1.0
  val net = if (yearly) result.yearlyNetSalary else result.netSalary
  val gross = if (yearly) result.yearlyGrossSalary else result.grossSalary
  val deductions = result.totalDeductions * multiplier
  val gosiEmployee = result.employeeGosiAmount * multiplier
  val gosiEmployer = result.employerGosiAmount * multiplier
  val overtime = result.overtimePay * multiplier

  LazyColumn(
    modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
  ) {
    item { SectionHeader(title = stringResource(R.string.result_title), darkMode = darkMode) }

    item {
      SegmentedControl(
        options = listOf(stringResource(R.string.common_monthly), stringResource(R.string.common_yearly)),
        selectedIndex = if (yearly) 1 else 0,
        darkMode = darkMode,
        onSelect = { index -> yearly = index == 1 }
      )
    }

    item {
      GlassCard(darkMode = darkMode) {
        Text(
          stringResource(R.string.result_net_salary),
          style = MaterialTheme.typography.labelLarge,
          color = designSystemContentColor(darkMode).copy(alpha = 0.65f),
          fontWeight = FontWeight.Bold
        )
        Row(verticalAlignment = Alignment.Bottom) {
          AnimatedCounter(
            targetValue = net,
            decimals = 2,
            style = MaterialTheme.typography.displaySmall,
            color = designSystemContentColor(darkMode)
          )
          Spacer(Modifier.width(8.dp))
          RiyalSymbol(
            tint = BrandGreen,
            size = 22.dp,
            contentDescription = currencySymbol,
            modifier = Modifier.padding(bottom = 8.dp)
          )
        }
      }
    }

    item {
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ResultCard(
          label = stringResource(R.string.result_gross_salary),
          value = gross,
          currencySymbol = currencySymbol,
          darkMode = darkMode,
          accent = BrandGreen,
          icon = Icons.Filled.AccountBalanceWallet,
          modifier = Modifier.weight(1f)
        )
        ResultCard(
          label = stringResource(R.string.result_total_deductions),
          value = deductions,
          currencySymbol = currencySymbol,
          darkMode = darkMode,
          accent = BrandRed,
          isDeduction = deductions > 0,
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ResultCard(
          label = stringResource(R.string.result_gosi_employee),
          value = gosiEmployee,
          currencySymbol = currencySymbol,
          darkMode = darkMode,
          accent = BrandRed,
          isDeduction = gosiEmployee > 0,
          modifier = Modifier.weight(1f)
        )
        ResultCard(
          label = stringResource(R.string.result_gosi_employer),
          value = gosiEmployer,
          currencySymbol = currencySymbol,
          darkMode = darkMode,
          accent = BrandTeal,
          modifier = Modifier.weight(1f)
        )
      }
    }

    if (overtime > 0.0 || result.estimatedEosb > 0.0) {
      item {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          if (overtime > 0.0) {
            ResultCard(
              label = stringResource(R.string.result_overtime_amount),
              value = overtime,
              currencySymbol = currencySymbol,
              darkMode = darkMode,
              accent = BrandGold,
              modifier = Modifier.weight(1f)
            )
          }
          if (result.estimatedEosb > 0.0) {
            ResultCard(
              label = stringResource(R.string.result_eosb_estimate),
              value = result.estimatedEosb,
              currencySymbol = currencySymbol,
              darkMode = darkMode,
              accent = BrandGreen,
              icon = Icons.Filled.Savings,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }

    item { SectionHeader(title = stringResource(R.string.result_breakdown_title), darkMode = darkMode) }

    item {
      GlassCard(darkMode = darkMode) {
        DonutBreakdownChart(
          slices = result.breakdown.map {
            BreakdownSlice(label = it.label, amount = it.amount, color = Color(it.colorHex), isDeduction = it.isDeduction)
          },
          darkMode = darkMode,
          centerLabel = currencySymbol
        )
      }
    }

    item { SectionHeader(title = stringResource(R.string.result_bar_chart_title), darkMode = darkMode) }

    item {
      GlassCard(darkMode = darkMode) {
        EarningsVsDeductionsBarChart(
          earnings = result.grossSalary,
          deductions = result.totalDeductions,
          earningsLabel = stringResource(R.string.result_bar_earnings),
          deductionsLabel = stringResource(R.string.result_bar_deductions),
          darkMode = darkMode
        )
      }
    }

    item {
      GlassCard(darkMode = darkMode) {
        NetSalaryProgressIndicator(
          netSalary = result.netSalary,
          grossSalary = result.grossSalary,
          title = stringResource(R.string.result_progress_title),
          darkMode = darkMode
        )
      }
    }

    item {
      InteractiveCTA(
        text = stringResource(R.string.action_view_payslip),
        icon = Icons.Filled.Receipt,
        onClick = onViewPayslip,
        modifier = Modifier.fillMaxWidth()
      )
    }
    item {
      InteractiveCTA(
        text = stringResource(R.string.action_compare_offer),
        icon = Icons.Filled.TrendingUp,
        onClick = onCompareOffer,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
private fun ResultEmptyState(darkMode: Boolean, modifier: Modifier = Modifier) {
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
