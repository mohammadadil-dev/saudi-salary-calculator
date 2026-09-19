package com.saudi.salarycalculator.feature.calculator.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.components.GlassCard
import com.saudi.salarycalculator.core.designsystem.components.HistoryRecordCard
import com.saudi.salarycalculator.core.designsystem.components.InsightCard
import com.saudi.salarycalculator.core.designsystem.components.InteractiveCTA
import com.saudi.salarycalculator.core.designsystem.components.SalarySummaryCard
import com.saudi.salarycalculator.core.designsystem.components.SectionHeader
import com.saudi.salarycalculator.core.designsystem.theme.BrandGold
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreenLight
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.designsystem.theme.BrandTeal
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import com.saudi.salarycalculator.core.model.CalculationRecord
import com.saudi.salarycalculator.core.model.CalculationType
import com.saudi.salarycalculator.feature.calculator.R
import java.util.Calendar

/** Root "Home" tab: greeting, last-result summary, recent calculation history (capped to 3,
 * "View all" hands off to Settings where the full history lives), and three educational insight
 * cards. This is the screen users land on after Splash and whenever they tap the Home nav item. */
@Composable
fun HomeScreen(
  darkMode: Boolean,
  lastResultNetSalary: Double?,
  history: List<CalculationRecord>,
  currencySymbol: String,
  onStartCalculation: () -> Unit,
  onViewAllHistory: () -> Unit,
  onEditRecord: (CalculationRecord) -> Unit,
  onDeleteRecord: (String) -> Unit,
  onOpenExpatCosts: () -> Unit,
  onOpenEosbTracker: () -> Unit,
  onOpenLeaveTracker: () -> Unit,
  onOpenOfferScan: () -> Unit,
  onOpenReverseSalary: () -> Unit,
  onOpenCostOfLiving: () -> Unit,
  modifier: Modifier = Modifier
) {
  val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
    in 0..11 -> stringResource(R.string.home_greeting_morning)
    in 12..16 -> stringResource(R.string.home_greeting_afternoon)
    else -> stringResource(R.string.home_greeting_evening)
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(20.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    item {
      Column {
        Text(
          greeting,
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Black,
          color = designSystemContentColor(darkMode)
        )
        Text(
          stringResource(R.string.home_subtitle),
          style = MaterialTheme.typography.bodyMedium,
          color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
        )
      }
    }

    item {
      SalarySummaryCard(
        title = stringResource(R.string.home_quick_summary_title),
        netSalary = lastResultNetSalary,
        emptyText = stringResource(R.string.home_quick_summary_empty),
        currencySymbol = currencySymbol,
        darkMode = darkMode,
        modifier = Modifier.fillMaxWidth()
      )
    }

    item {
      Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        InteractiveCTA(
          text = stringResource(R.string.home_start_calculation),
          onClick = onStartCalculation
        )
      }
    }

    item {
      SectionHeader(
        title = stringResource(R.string.home_recent_calculations),
        darkMode = darkMode,
        trailing = if (history.isNotEmpty()) {
          {
            Text(
              stringResource(R.string.home_view_all),
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = BrandGreen,
              modifier = Modifier.clickable(onClick = onViewAllHistory)
            )
          }
        } else null
      )
    }

    if (history.isEmpty()) {
      item {
        GlassCard(darkMode = darkMode, modifier = Modifier.fillMaxWidth()) {
          Text(
            stringResource(R.string.home_no_recent_calculations),
            style = MaterialTheme.typography.bodyMedium,
            color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
          )
        }
      }
    } else {
      items(history.take(3)) { record ->
        HistoryRecordCard(
          title = record.title,
          subtitle = record.resultSummary,
          darkMode = darkMode,
          editContentDescription = stringResource(R.string.common_edit),
          deleteContentDescription = stringResource(R.string.common_delete),
          onEdit = if (record.type == CalculationType.NET_SALARY && record.netSalaryInput != null) {
            { onEditRecord(record) }
          } else null,
          onDelete = { onDeleteRecord(record.id) },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    item { SectionHeader(title = stringResource(R.string.home_tools_title), darkMode = darkMode) }
    item {
      ToolLinkCard(
        title = stringResource(R.string.home_tool_expat_costs_title),
        subtitle = stringResource(R.string.home_tool_expat_costs_subtitle),
        icon = Icons.Filled.FlightTakeoff,
        accent = BrandTeal,
        darkMode = darkMode,
        onClick = onOpenExpatCosts,
        modifier = Modifier.fillMaxWidth()
      )
    }
    item {
      ToolLinkCard(
        title = stringResource(R.string.home_tool_eosb_tracker_title),
        subtitle = stringResource(R.string.home_tool_eosb_tracker_subtitle),
        icon = Icons.Filled.HourglassBottom,
        accent = BrandGold,
        darkMode = darkMode,
        onClick = onOpenEosbTracker,
        modifier = Modifier.fillMaxWidth()
      )
    }
    item {
      ToolLinkCard(
        title = stringResource(R.string.home_tool_leave_tracker_title),
        subtitle = stringResource(R.string.home_tool_leave_tracker_subtitle),
        icon = Icons.Filled.BeachAccess,
        accent = BrandGreen,
        darkMode = darkMode,
        onClick = onOpenLeaveTracker,
        modifier = Modifier.fillMaxWidth()
      )
    }
    item {
      ToolLinkCard(
        title = stringResource(R.string.home_tool_offer_scan_title),
        subtitle = stringResource(R.string.home_tool_offer_scan_subtitle),
        icon = Icons.Filled.Flag,
        accent = BrandRed,
        darkMode = darkMode,
        onClick = onOpenOfferScan,
        modifier = Modifier.fillMaxWidth()
      )
    }
    item {
      ToolLinkCard(
        title = stringResource(R.string.home_tool_reverse_salary_title),
        subtitle = stringResource(R.string.home_tool_reverse_salary_subtitle),
        icon = Icons.Filled.Calculate,
        accent = BrandGold,
        darkMode = darkMode,
        onClick = onOpenReverseSalary,
        modifier = Modifier.fillMaxWidth()
      )
    }
    item {
      ToolLinkCard(
        title = stringResource(R.string.home_tool_cost_of_living_title),
        subtitle = stringResource(R.string.home_tool_cost_of_living_subtitle),
        icon = Icons.Filled.House,
        accent = BrandGreenLight,
        darkMode = darkMode,
        onClick = onOpenCostOfLiving,
        modifier = Modifier.fillMaxWidth()
      )
    }

    item { SectionHeader(title = stringResource(R.string.home_insights_title), darkMode = darkMode) }

    item {
      InsightCard(
        title = stringResource(R.string.home_insight_gosi_title),
        body = stringResource(R.string.home_insight_gosi_body),
        icon = Icons.Filled.AccountBalance,
        darkMode = darkMode,
        accent = BrandGreen,
        modifier = Modifier.fillMaxWidth()
      )
    }
    item {
      InsightCard(
        title = stringResource(R.string.home_insight_eosb_title),
        body = stringResource(R.string.home_insight_eosb_body),
        icon = Icons.Filled.TrendingUp,
        darkMode = darkMode,
        accent = BrandTeal,
        modifier = Modifier.fillMaxWidth()
      )
    }
    item {
      InsightCard(
        title = stringResource(R.string.home_insight_tax_title),
        body = stringResource(R.string.home_insight_tax_body),
        icon = Icons.Filled.CheckCircle,
        darkMode = darkMode,
        accent = BrandGold,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

/** Clickable entry point into a standalone tool screen pushed from Home (the expat
 * residency-cost estimator and the EOSB accrual tracker). Visually similar to [InsightCard] but
 * adds a trailing chevron to signal it's navigable rather than purely informational. */
@Composable
private fun ToolLinkCard(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  accent: androidx.compose.ui.graphics.Color,
  darkMode: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  GlassCard(darkMode = darkMode, modifier = modifier.clickable(onClick = onClick)) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(accent.copy(alpha = 0.16f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
      }
      Spacer(Modifier.width(10.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          title,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = designSystemContentColor(darkMode)
        )
        Text(
          subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = designSystemContentColor(darkMode).copy(alpha = 0.6f)
        )
      }
      Icon(
        Icons.Filled.ArrowForwardIos,
        contentDescription = null,
        tint = designSystemContentColor(darkMode).copy(alpha = 0.35f),
        modifier = Modifier.size(14.dp)
      )
    }
  }
}
