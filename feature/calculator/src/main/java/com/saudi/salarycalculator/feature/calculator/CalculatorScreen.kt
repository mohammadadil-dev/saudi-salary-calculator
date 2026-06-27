package com.saudi.salarycalculator.feature.calculator

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.NetSalaryResult
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class AppTab { HOME, SALARY, RESULT, COMPARE, EOS, SAVINGS, REPORT, SETTINGS }

@Composable
fun CalculatorRoute(
  modifier: Modifier = Modifier,
  viewModel: CalculatorViewModel = hiltViewModel(),
  adBanner: @Composable () -> Unit = {}
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  CalculatorScreen(
    state = state,
    onFieldChange = viewModel::onFieldChange,
    onSelectedTab = viewModel::onSelectedTab,
    onEmployeeTypeChange = viewModel::setEmployeeType,
    onCalculateSalary = viewModel::calculateNetSalary,
    onCompareOffers = viewModel::compareOffers,
    onCalculateEos = viewModel::calculateEndOfService,
    onCalculateSavings = viewModel::calculateSavings,
    onToggleYearly = viewModel::toggleYearlyMode,
    onToggleTheme = viewModel::toggleTheme,
    onClearHistory = viewModel::clearHistory,
    adBanner = adBanner,
    modifier = modifier
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
  state: CalculatorUiState,
  onFieldChange: (CalculatorField, String) -> Unit,
  onSelectedTab: (Int) -> Unit,
  onEmployeeTypeChange: (EmployeeType) -> Unit,
  onCalculateSalary: () -> Unit,
  onCompareOffers: () -> Unit,
  onCalculateEos: () -> Unit,
  onCalculateSavings: () -> Unit,
  onToggleYearly: () -> Unit,
  onToggleTheme: () -> Unit,
  onClearHistory: () -> Unit,
  adBanner: @Composable () -> Unit,
  modifier: Modifier = Modifier
) {
  val copy = uiCopy(state.language)
  val isArabic = state.language == "ar"
  val direction = if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr
  var activeInput by remember { mutableStateOf<InputTarget?>(null) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val context = LocalContext.current

  CompositionLocalProvider(LocalLayoutDirection provides direction) {
    Scaffold(
      modifier = modifier,
      bottomBar = { adBanner() },
      containerColor = Color.Transparent
    ) { paddingValues ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(fintechBackground(state.darkMode))
          .padding(paddingValues)
      ) {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          item {
            Header(
              state = state,
              copy = copy,
              onToggleTheme = onToggleTheme,
              onLanguageChange = { onFieldChange(CalculatorField.LANGUAGE, it) }
            )
          }
          item {
            AppTabs(state, copy, onSelectedTab)
          }
          item {
            AnimatedVisibility(visible = true) {
              when (AppTab.entries[state.selectedTab.coerceIn(0, AppTab.entries.lastIndex)]) {
                AppTab.HOME -> HomeScreen(state, copy, onSelectedTab)
                AppTab.SALARY -> SalaryInputScreen(state, copy, onEmployeeTypeChange, onCalculateSalary) { activeInput = it }
                AppTab.RESULT -> ResultDashboard(state, copy, onToggleYearly, onSelectedTab)
                AppTab.COMPARE -> ComparisonScreen(state, copy, onCompareOffers) { activeInput = it }
                AppTab.EOS -> EndOfServiceScreen(state, copy, onFieldChange, onCalculateEos) { activeInput = it }
                AppTab.SAVINGS -> SavingsScreen(state, copy, onCalculateSavings) { activeInput = it }
                AppTab.REPORT -> ReportScreen(state, copy, context, onClearHistory)
                AppTab.SETTINGS -> SettingsScreen(state, copy, onFieldChange, onToggleTheme) { activeInput = it }
              }
            }
          }
          item { Spacer(modifier = Modifier.height(24.dp)) }
        }
      }
    }

    activeInput?.let { target ->
      ModalBottomSheet(
        onDismissRequest = { activeInput = null },
        sheetState = sheetState
      ) {
        KeypadSheet(
          title = target.label,
          value = target.value,
          onValueChange = { onFieldChange(target.field, it) },
          onDone = { activeInput = null }
        )
      }
    }
  }
}

@Composable
private fun Header(
  state: CalculatorUiState,
  copy: UiCopy,
  onToggleTheme: () -> Unit,
  onLanguageChange: (String) -> Unit
) {
  Column(
    modifier = Modifier.padding(top = 18.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          copy.appName,
          color = contentColor(state.darkMode),
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Black,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          copy.tagline,
          color = contentColor(state.darkMode).copy(alpha = 0.68f),
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 2
        )
      }
      Spacer(Modifier.width(12.dp))
      Switch(checked = state.darkMode, onCheckedChange = { onToggleTheme() })
    }
    LanguageToggle(language = state.language, darkMode = state.darkMode, onLanguageChange = onLanguageChange)
  }
}

@Composable
private fun AppTabs(state: CalculatorUiState, copy: UiCopy, onSelectedTab: (Int) -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    AppTab.entries.forEachIndexed { index, tab ->
      ChoiceChip(
        text = copy.tab(tab),
        selected = state.selectedTab == index,
        darkMode = state.darkMode,
        onClick = { onSelectedTab(index) }
      )
    }
  }
}

@Composable
private fun HomeScreen(state: CalculatorUiState, copy: UiCopy, onSelectedTab: (Int) -> Unit) {
  Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
    HeroCard(state, copy, onSelectedTab)
    SmartSummaryCard(state, copy)
  }
}

@Composable
private fun HeroCard(state: CalculatorUiState, copy: UiCopy, onSelectedTab: (Int) -> Unit) {
  GlassCard(state.darkMode) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(74.dp)
          .clip(CircleShape)
          .background(Brush.linearGradient(listOf(accentColor, secondaryAccent))),
        contentAlignment = Alignment.Center
      ) {
        Text("SAR", color = Color.White, fontWeight = FontWeight.Black)
      }
      Spacer(Modifier.width(14.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(copy.heroTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = contentColor(state.darkMode))
        Text(copy.heroBody, style = MaterialTheme.typography.bodyMedium, color = contentColor(state.darkMode).copy(alpha = 0.7f))
      }
    }
    Button(
      onClick = { onSelectedTab(AppTab.SALARY.ordinal) },
      modifier = Modifier.fillMaxWidth(),
      colors = ButtonDefaults.buttonColors(containerColor = accentColor, contentColor = Color.White)
    ) {
      Text(copy.start)
    }
  }
}

@Composable
private fun SalaryInputScreen(
  state: CalculatorUiState,
  copy: UiCopy,
  onEmployeeTypeChange: (EmployeeType) -> Unit,
  onCalculate: () -> Unit,
  openInput: (InputTarget) -> Unit
) {
  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    GlassCard(state.darkMode) {
      SectionTitle(copy.salaryInput, state.darkMode)
      EmployeeTypeToggle(state.employeeType, copy, onEmployeeTypeChange)
      InputRow(copy.basic, state.basicSalary, CalculatorField.BASIC_SALARY, state.darkMode, openInput)
      InputRow(copy.housing, state.housingAllowance, CalculatorField.HOUSING_ALLOWANCE, state.darkMode, openInput)
      InputRow(copy.transport, state.transportAllowance, CalculatorField.TRANSPORT_ALLOWANCE, state.darkMode, openInput)
      InputRow(copy.food, state.foodAllowance, CalculatorField.FOOD_ALLOWANCE, state.darkMode, openInput)
      InputRow(copy.mobile, state.mobileAllowance, CalculatorField.MOBILE_ALLOWANCE, state.darkMode, openInput)
      InputRow(copy.other, state.otherAllowances, CalculatorField.OTHER_ALLOWANCES, state.darkMode, openInput)
      InputRow(copy.deductions, state.deductions, CalculatorField.DEDUCTIONS, state.darkMode, openInput)
      PrimaryButton(copy.calculateTakeHome, onCalculate)
    }
  }
}

@Composable
private fun ResultDashboard(
  state: CalculatorUiState,
  copy: UiCopy,
  onToggleYearly: () -> Unit,
  onSelectedTab: (Int) -> Unit
) {
  val result = state.netSalaryResult
  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    SmartSummaryCard(state, copy)
    GlassCard(state.darkMode) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        SectionTitle(copy.resultDashboard, state.darkMode)
        FilledTonalButton(onClick = onToggleYearly) {
          Text(if (state.showYearly) copy.yearly else copy.monthly)
        }
      }
      if (result == null) {
        Text(copy.runCalculator, color = contentColor(state.darkMode).copy(alpha = 0.75f))
        PrimaryButton(copy.openSalaryInput) { onSelectedTab(AppTab.SALARY.ordinal) }
      } else {
        state.calculationAck?.let { AckBanner(copy.calculatedAck) }
        ResultHero(result, state.showYearly, copy)
        DashboardMetrics(result, state.showYearly, state.darkMode, copy)
        WpsBreakdown(result, state.showYearly, state.darkMode, copy)
      }
    }
  }
}

@Composable
private fun ComparisonScreen(
  state: CalculatorUiState,
  copy: UiCopy,
  onCompare: () -> Unit,
  openInput: (InputTarget) -> Unit
) {
  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    OfferForm(copy.currentOffer, listOf(
      InputSpec(copy.basic, state.offerABasic, CalculatorField.OFFER_A_BASIC),
      InputSpec(copy.housing, state.offerAHousing, CalculatorField.OFFER_A_HOUSING),
      InputSpec(copy.transport, state.offerATransport, CalculatorField.OFFER_A_TRANSPORT),
      InputSpec(copy.food, state.offerAFood, CalculatorField.OFFER_A_FOOD),
      InputSpec(copy.mobile, state.offerAMobile, CalculatorField.OFFER_A_MOBILE),
      InputSpec(copy.other, state.offerAOther, CalculatorField.OFFER_A_OTHER),
      InputSpec(copy.deductions, state.offerADeduction, CalculatorField.OFFER_A_DEDUCTION)
    ), state.darkMode, openInput)
    OfferForm(copy.newOffer, listOf(
      InputSpec(copy.basic, state.offerBBasic, CalculatorField.OFFER_B_BASIC),
      InputSpec(copy.housing, state.offerBHousing, CalculatorField.OFFER_B_HOUSING),
      InputSpec(copy.transport, state.offerBTransport, CalculatorField.OFFER_B_TRANSPORT),
      InputSpec(copy.food, state.offerBFood, CalculatorField.OFFER_B_FOOD),
      InputSpec(copy.mobile, state.offerBMobile, CalculatorField.OFFER_B_MOBILE),
      InputSpec(copy.other, state.offerBOther, CalculatorField.OFFER_B_OTHER),
      InputSpec(copy.deductions, state.offerBDeduction, CalculatorField.OFFER_B_DEDUCTION)
    ), state.darkMode, openInput)
    PrimaryButton(copy.compareOffers, onCompare)
    state.offerComparisonResult?.let {
      GlassCard(state.darkMode) {
        SectionTitle(copy.offerScore, state.darkMode)
        Text("${it.acceptanceScore}/100", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = accentColor)
        Text(it.scoreLabel, color = contentColor(state.darkMode))
        MetricRow(copy.betterOffer, it.betterOfferTitle, state.darkMode)
        MetricRow(copy.monthlyDifference, it.monthlyDifference.sar(), state.darkMode)
        MetricRow(copy.yearlyDifference, it.yearlyDifference.sar(), state.darkMode)
      }
    }
  }
}

@Composable
private fun EndOfServiceScreen(
  state: CalculatorUiState,
  copy: UiCopy,
  onFieldChange: (CalculatorField, String) -> Unit,
  onCalculate: () -> Unit,
  openInput: (InputTarget) -> Unit
) {
  GlassCard(state.darkMode) {
    SectionTitle(copy.eosTitle, state.darkMode)
    InputRow(copy.lastBasic, state.eosLastSalary, CalculatorField.EOS_LAST_SALARY, state.darkMode, openInput)
    InputRow(copy.years, state.eosYears, CalculatorField.EOS_YEARS, state.darkMode, openInput)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
      Text(copy.resignation, color = contentColor(state.darkMode))
      Switch(checked = state.eosResigned, onCheckedChange = { onFieldChange(CalculatorField.EOS_RESIGNED, it.toString()) })
    }
    PrimaryButton(copy.calculateEos, onCalculate)
    state.endOfServiceResult?.let {
      MetricRow(copy.eligibleYears, it.eligibleYears.toString(), state.darkMode)
      MetricRow(copy.estimatedBenefit, it.rewardAmount.sar(), state.darkMode)
    }
  }
}

@Composable
private fun SavingsScreen(
  state: CalculatorUiState,
  copy: UiCopy,
  onCalculate: () -> Unit,
  openInput: (InputTarget) -> Unit
) {
  GlassCard(state.darkMode) {
    SectionTitle(copy.savingsPlanner, state.darkMode)
    InputRow(copy.netOverride, state.savingsNetSalary, CalculatorField.SAVINGS_NET_SALARY, state.darkMode, openInput)
    InputRow(copy.rent, state.rentExpense, CalculatorField.RENT_EXPENSE, state.darkMode, openInput)
    InputRow(copy.food, state.foodExpense, CalculatorField.FOOD_EXPENSE, state.darkMode, openInput)
    InputRow(copy.transport, state.transportExpense, CalculatorField.TRANSPORT_EXPENSE, state.darkMode, openInput)
    InputRow(copy.family, state.familyExpense, CalculatorField.FAMILY_EXPENSE, state.darkMode, openInput)
    InputRow(copy.other, state.otherExpense, CalculatorField.OTHER_EXPENSE, state.darkMode, openInput)
    PrimaryButton(copy.estimateSavings, onCalculate)
    state.savingsResult?.let {
      MetricRow(copy.monthlySavings, it.monthlySavings.sar(), state.darkMode)
      MetricRow(copy.yearlySavings, it.yearlySavings.sar(), state.darkMode)
      MetricRow(copy.savingsRate, "${it.savingsRate}%", state.darkMode)
    }
  }
}

@Composable
private fun SettingsScreen(
  state: CalculatorUiState,
  copy: UiCopy,
  onFieldChange: (CalculatorField, String) -> Unit,
  onToggleTheme: () -> Unit,
  openInput: (InputTarget) -> Unit
) {
  GlassCard(state.darkMode) {
    SectionTitle(copy.settings, state.darkMode)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
      Text(copy.darkMode, color = contentColor(state.darkMode))
      Switch(checked = state.darkMode, onCheckedChange = { onToggleTheme() })
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      LanguageToggle(language = state.language, darkMode = state.darkMode) {
        onFieldChange(CalculatorField.LANGUAGE, it)
      }
    }
    InputRow(copy.saudiEmployeeRate, state.saudiEmployeeGosiRate, CalculatorField.SAUDI_EMPLOYEE_GOSI_RATE, state.darkMode, openInput)
    InputRow(copy.saudiEmployerRate, state.saudiEmployerGosiRate, CalculatorField.SAUDI_EMPLOYER_GOSI_RATE, state.darkMode, openInput)
    InputRow(copy.expatEmployeeRate, state.expatEmployeeGosiRate, CalculatorField.EXPAT_EMPLOYEE_GOSI_RATE, state.darkMode, openInput)
    InputRow(copy.expatHazardRate, state.expatEmployerHazardRate, CalculatorField.EXPAT_EMPLOYER_HAZARD_RATE, state.darkMode, openInput)
  }
}

@Composable
private fun ReportScreen(
  state: CalculatorUiState,
  copy: UiCopy,
  context: Context,
  onClearHistory: () -> Unit
) {
  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    GlassCard(state.darkMode) {
      SectionTitle(copy.reportPreview, state.darkMode)
      ReportPreviewCard(state, copy)
      PrimaryButton(copy.exportPdf) { sharePdf(context, state.reportPreview) }
    }
    GlassCard(state.darkMode) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        SectionTitle(copy.saved, state.darkMode)
        TextButton(onClick = onClearHistory) { Text(copy.clear) }
      }
      if (state.history.isEmpty()) {
        Text(copy.emptyHistory, color = contentColor(state.darkMode).copy(alpha = 0.7f))
      } else {
        state.history.take(8).forEach {
          MetricRow(it.title, "${it.resultSummary} - ${formatDate(it.createdAtMillis)}", state.darkMode)
        }
      }
    }
  }
}

@Composable
private fun SmartSummaryCard(state: CalculatorUiState, copy: UiCopy) {
  GlassCard(state.darkMode) {
    Text(
      state.netSalaryResult?.smartSummary ?: copy.emptySummary,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Black,
      color = contentColor(state.darkMode)
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmployeeTypeToggle(type: EmployeeType, copy: UiCopy, onChange: (EmployeeType) -> Unit) {
  SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
    EmployeeType.entries.forEachIndexed { index, option ->
      SegmentedButton(
        selected = type == option,
        onClick = { onChange(option) },
        shape = SegmentedButtonDefaults.itemShape(index, EmployeeType.entries.size),
        label = { Text(if (option == EmployeeType.SAUDI) copy.saudi else copy.expat) }
      )
    }
  }
}

@Composable
private fun OfferForm(
  heading: String,
  inputs: List<InputSpec>,
  darkMode: Boolean,
  openInput: (InputTarget) -> Unit
) {
  GlassCard(darkMode) {
    SectionTitle(heading, darkMode)
    inputs.forEach { InputRow(it.label, it.value, it.field, darkMode, openInput) }
  }
}

@Composable
private fun InputRow(
  label: String,
  value: String,
  field: CalculatorField,
  darkMode: Boolean,
  openInput: (InputTarget) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(if (darkMode) Color.White.copy(alpha = 0.08f) else Color(0xFFF4F7F5))
      .clickable { openInput(InputTarget(label, value, field)) }
      .padding(horizontal = 14.dp, vertical = 13.dp)
      .animateContentSize(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, color = contentColor(darkMode), style = MaterialTheme.typography.bodyMedium)
    Text(value.ifBlank { "0" }, color = contentColor(darkMode), fontWeight = FontWeight.Black, textAlign = TextAlign.End)
  }
}

@Composable
private fun KeypadSheet(title: String, value: String, onValueChange: (String) -> Unit, onDone: () -> Unit) {
  var current by remember(value) { mutableStateOf(value) }
  Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    Text(current.ifBlank { "0" }, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
    listOf(
      listOf("1", "2", "3"),
      listOf("4", "5", "6"),
      listOf("7", "8", "9"),
      listOf(".", "0", "Del")
    ).forEach { row ->
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        row.forEach { key ->
          FilledTonalButton(
            onClick = {
              current = when (key) {
                "Del" -> current.dropLast(1)
                "." -> if (current.contains(".")) current else current + key
                else -> current + key
              }
              onValueChange(current)
            },
            modifier = Modifier
              .weight(1f)
              .height(56.dp),
            shape = RoundedCornerShape(8.dp)
          ) { Text(key, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        }
      }
    }
    PrimaryButton("Done", onDone)
  }
}

@Composable
private fun LanguageToggle(language: String, darkMode: Boolean, onLanguageChange: (String) -> Unit) {
  val isArabic = language == "ar"
  val knobOffset by animateDpAsState(
    targetValue = if (isArabic) 72.dp else 0.dp,
    animationSpec = tween(durationMillis = 260),
    label = "language-toggle"
  )
  Box(
    modifier = Modifier
      .width(148.dp)
      .height(42.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(if (darkMode) Color.White.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.72f))
      .clickable { onLanguageChange(if (isArabic) "en" else "ar") }
      .padding(4.dp)
  ) {
    Box(
      modifier = Modifier
        .width(68.dp)
        .height(34.dp)
        .offset(x = knobOffset)
        .clip(RoundedCornerShape(8.dp))
        .background(Brush.linearGradient(listOf(accentColor, secondaryAccent)))
    )
    Row(
      modifier = Modifier.fillMaxSize(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text("EN", modifier = Modifier.width(68.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Black, color = if (!isArabic) Color.White else contentColor(darkMode))
      Text("AR", modifier = Modifier.width(68.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Black, color = if (isArabic) Color.White else contentColor(darkMode))
    }
  }
}

@Composable
private fun AckBanner(message: String) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(Brush.linearGradient(listOf(accentColor.copy(alpha = 0.95f), secondaryAccent.copy(alpha = 0.95f))))
      .padding(12.dp)
  ) {
    Text(message, color = Color.White, fontWeight = FontWeight.Black)
  }
}

@Composable
private fun ResultHero(result: NetSalaryResult, yearly: Boolean, copy: UiCopy) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(Brush.linearGradient(listOf(Color(0xFFFF8A3D), Color(0xFF2563EB))))
      .padding(16.dp)
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Text(if (yearly) copy.yearly else copy.monthly, color = Color.White.copy(alpha = 0.82f), fontWeight = FontWeight.Bold)
      AnimatedSarCounter(if (yearly) result.yearlyNetSalary else result.netSalary, darkMode = true)
      Text(copy.netWage, color = Color.White.copy(alpha = 0.84f), style = MaterialTheme.typography.bodyMedium)
    }
  }
}

@Composable
private fun DashboardMetrics(result: NetSalaryResult, yearly: Boolean, darkMode: Boolean, copy: UiCopy) {
  val multiplier = if (yearly) 12.0 else 1.0
  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
      CompactMetricCard(copy.employerCost, (result.employerMonthlyCost * multiplier).sar(), darkMode, Modifier.weight(1f))
      CompactMetricCard("GOSI", (result.employeeGosiAmount * multiplier).sar(), darkMode, Modifier.weight(1f))
    }
    CircularNetSalaryChart(result, darkMode)
  }
}

@Composable
private fun CompactMetricCard(label: String, value: String, darkMode: Boolean, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (darkMode) Color.White.copy(alpha = 0.08f) else Color(0xFFF7F2EA))
      .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Text(label, color = contentColor(darkMode).copy(alpha = 0.68f), style = MaterialTheme.typography.bodySmall, maxLines = 2)
    Text(value, color = contentColor(darkMode), fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleSmall, maxLines = 2)
  }
}

@Composable
private fun ReportPreviewCard(state: CalculatorUiState, copy: UiCopy) {
  val salary = state.netSalaryResult
  val offer = state.offerComparisonResult
  val savings = state.savingsResult
  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    if (salary == null && offer == null && savings == null) {
      Text(state.reportPreview, color = contentColor(state.darkMode).copy(alpha = 0.72f))
    } else {
      salary?.let {
        CompactMetricCard(copy.netWage, it.netSalary.sar(), state.darkMode, Modifier.fillMaxWidth())
        MetricRow(copy.employerCost, it.employerMonthlyCost.sar(), state.darkMode, accentColor)
      }
      offer?.let {
        MetricRow(copy.offerScore, "${it.acceptanceScore}/100", state.darkMode, secondaryAccent)
        MetricRow(copy.monthlyDifference, it.monthlyDifference.sar(), state.darkMode, accentColor)
      }
      savings?.let {
        MetricRow(copy.monthlySavings, it.monthlySavings.sar(), state.darkMode, Color(0xFF7C3AED))
      }
    }
  }
}

@Composable
private fun AnimatedSarCounter(amount: Double, darkMode: Boolean) {
  val animated by animateFloatAsState(
    targetValue = amount.toFloat(),
    animationSpec = tween(durationMillis = 900),
    label = "salary-counter"
  )
  Text(animated.toDouble().sar(), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black, color = contentColor(darkMode))
}

@Composable
private fun CircularNetSalaryChart(result: NetSalaryResult, darkMode: Boolean) {
  val netRatio = (result.netSalary / result.grossSalary.coerceAtLeast(1.0)).toFloat().coerceIn(0f, 1f)
  Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    Canvas(modifier = Modifier.size(112.dp)) {
      drawArc(Color.White.copy(alpha = 0.18f), -90f, 360f, false, style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round))
      drawArc(accentColor, -90f, 360f * netRatio, false, style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round))
    }
    Spacer(Modifier.width(16.dp))
    Column {
      Text("Net ratio", color = contentColor(darkMode).copy(alpha = 0.72f))
      Text("${(netRatio * 100).toInt()}%", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = contentColor(darkMode))
    }
  }
}

@Composable
private fun WpsBreakdown(result: NetSalaryResult, yearly: Boolean, darkMode: Boolean, copy: UiCopy) {
  val multiplier = if (yearly) 12.0 else 1.0
  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    result.breakdown.forEach { item ->
      val signed = if (item.isDeduction) -item.amount else item.amount
      MetricRow(item.label, (signed * multiplier).sar(), darkMode, Color(item.colorHex))
    }
    MetricRow(copy.netWage, (result.netSalary * multiplier).sar(), darkMode, accentColor)
    MetricRow(copy.employerCost, (result.employerMonthlyCost * multiplier).sar(), darkMode, secondaryAccent)
  }
}

@Composable
private fun SectionTitle(text: String, darkMode: Boolean) {
  Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = contentColor(darkMode))
}

@Composable
private fun MetricRow(label: String, value: String, darkMode: Boolean, marker: Color = accentColor) {
  Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
      Box(Modifier.size(9.dp).clip(CircleShape).background(marker))
      Spacer(Modifier.width(8.dp))
      Text(label, color = contentColor(darkMode).copy(alpha = 0.78f), maxLines = 2)
    }
    Text(value, color = contentColor(darkMode), fontWeight = FontWeight.Bold, textAlign = TextAlign.End, modifier = Modifier.padding(start = 10.dp))
  }
}

@Composable
private fun ChoiceChip(text: String, selected: Boolean, darkMode: Boolean, onClick: () -> Unit) {
  val bg = when {
    selected -> accentColor
    darkMode -> Color.White.copy(alpha = 0.10f)
    else -> Color.White.copy(alpha = 0.82f)
  }
  val fg = if (selected) Color.White else contentColor(darkMode)
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(bg)
      .clickable(onClick = onClick)
      .padding(horizontal = 14.dp, vertical = 10.dp)
  ) {
    Text(text, color = fg, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
  }
}

@Composable
private fun PrimaryButton(text: String, onClick: () -> Unit) {
  Button(
    onClick = onClick,
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(8.dp),
    colors = ButtonDefaults.buttonColors(containerColor = accentColor, contentColor = Color.White)
  ) {
    Text(text, fontWeight = FontWeight.Bold)
  }
}

@Composable
private fun GlassCard(darkMode: Boolean, content: @Composable () -> Unit) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = glassColor(darkMode)),
    shape = RoundedCornerShape(8.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      content()
    }
  }
}

private fun fintechBackground(darkMode: Boolean): Brush =
  if (darkMode) {
    Brush.linearGradient(listOf(Color(0xFF101827), Color(0xFF1E2A44), Color(0xFF3A2A21)))
  } else {
    Brush.linearGradient(listOf(Color(0xFFFFF4EA), Color(0xFFEAF1FF), Color(0xFFFFFBF0)))
  }

private fun glassColor(darkMode: Boolean): Color =
  if (darkMode) Color.White.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.78f)

private fun contentColor(darkMode: Boolean): Color =
  if (darkMode) Color.White else Color(0xFF10201B)

private val accentColor = Color(0xFFFF7A2F)
private val secondaryAccent = Color(0xFF2563EB)

private fun Double.sar(): String =
  "SAR ${NumberFormat.getNumberInstance(Locale.US).format(this)}"

private fun formatDate(millis: Long): String {
  val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
  return formatter.format(Date(millis))
}

private fun sharePdf(context: Context, report: String) {
  val file = File(context.cacheDir, "saudi_salary_report.pdf")
  PdfReportExporter.write(file, report)
  val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
  val intent = Intent(Intent.ACTION_SEND).apply {
    type = "application/pdf"
    putExtra(Intent.EXTRA_STREAM, uri)
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
  }
  context.startActivity(Intent.createChooser(intent, "Export salary report"))
}

private data class InputSpec(
  val label: String,
  val value: String,
  val field: CalculatorField
)

private data class InputTarget(
  val label: String,
  val value: String,
  val field: CalculatorField
)

private data class UiCopy(
  val appName: String,
  val tagline: String,
  val heroTitle: String,
  val heroBody: String,
  val start: String,
  val salaryInput: String,
  val resultDashboard: String,
  val runCalculator: String,
  val openSalaryInput: String,
  val currentOffer: String,
  val newOffer: String,
  val compareOffers: String,
  val offerScore: String,
  val betterOffer: String,
  val monthlyDifference: String,
  val yearlyDifference: String,
  val eosTitle: String,
  val savingsPlanner: String,
  val settings: String,
  val reportPreview: String,
  val exportPdf: String,
  val saved: String,
  val clear: String,
  val emptyHistory: String,
  val emptySummary: String,
  val calculatedAck: String,
  val basic: String,
  val housing: String,
  val transport: String,
  val food: String,
  val mobile: String,
  val other: String,
  val deductions: String,
  val calculateTakeHome: String,
  val saudi: String,
  val expat: String,
  val monthly: String,
  val yearly: String,
  val netWage: String,
  val employerCost: String,
  val lastBasic: String,
  val years: String,
  val resignation: String,
  val calculateEos: String,
  val eligibleYears: String,
  val estimatedBenefit: String,
  val netOverride: String,
  val rent: String,
  val family: String,
  val estimateSavings: String,
  val monthlySavings: String,
  val yearlySavings: String,
  val savingsRate: String,
  val darkMode: String,
  val saudiEmployeeRate: String,
  val saudiEmployerRate: String,
  val expatEmployeeRate: String,
  val expatHazardRate: String
) {
  fun tab(tab: AppTab): String = when (tab) {
    AppTab.HOME -> if (appName.startsWith("حاسبة")) "الرئيسية" else "Home"
    AppTab.SALARY -> salaryInput
    AppTab.RESULT -> if (appName.startsWith("حاسبة")) "النتيجة" else "Result"
    AppTab.COMPARE -> if (appName.startsWith("حاسبة")) "العروض" else "Compare"
    AppTab.EOS -> if (appName.startsWith("حاسبة")) "نهاية الخدمة" else "EOS"
    AppTab.SAVINGS -> if (appName.startsWith("حاسبة")) "الادخار" else "Savings"
    AppTab.REPORT -> if (appName.startsWith("حاسبة")) "التقرير" else "Report"
    AppTab.SETTINGS -> settings
  }
}

private fun uiCopy(language: String): UiCopy =
  if (language == "ar") {
    UiCopy(
      appName = "حاسبة الراتب السعودية",
      tagline = "بدون ضريبة دخل. نسب تأمينات قابلة للتعديل. تقارير بالريال.",
      heroTitle = "لوحة راتب ذكية",
      heroBody = "احسب الصافي، تكلفة صاحب العمل، جودة العرض، نهاية الخدمة، والادخار من مكان واحد.",
      start = "ابدأ حساب الراتب",
      salaryInput = "الراتب",
      resultDashboard = "لوحة النتائج",
      runCalculator = "احسب الراتب أولاً لعرض النتائج.",
      openSalaryInput = "فتح إدخال الراتب",
      currentOffer = "العرض الحالي",
      newOffer = "العرض الجديد",
      compareOffers = "قارن العروض",
      offerScore = "درجة قبول العرض",
      betterOffer = "أفضل عرض",
      monthlyDifference = "الفرق الشهري",
      yearlyDifference = "الفرق السنوي",
      eosTitle = "مكافأة نهاية الخدمة",
      savingsPlanner = "مخطط المصاريف والادخار",
      settings = "الإعدادات",
      reportPreview = "معاينة تقرير PDF",
      exportPdf = "تصدير PDF",
      saved = "الحسابات المحفوظة",
      clear = "مسح",
      emptyHistory = "لا توجد حسابات محفوظة",
      emptySummary = "سيظهر صافي الراتب الشهري هنا.",
      calculatedAck = "تم حساب صافي الراتب ونقلك إلى النتائج",
      basic = "الأساسي",
      housing = "السكن",
      transport = "النقل",
      food = "الطعام",
      mobile = "الجوال",
      other = "أخرى",
      deductions = "الاستقطاعات",
      calculateTakeHome = "احسب الصافي",
      saudi = "سعودي",
      expat = "غير سعودي",
      monthly = "شهري",
      yearly = "سنوي",
      netWage = "صافي الأجر",
      employerCost = "تكلفة صاحب العمل",
      lastBasic = "آخر راتب أساسي",
      years = "سنوات الخدمة",
      resignation = "حالة استقالة",
      calculateEos = "احسب نهاية الخدمة",
      eligibleYears = "السنوات المحتسبة",
      estimatedBenefit = "المكافأة التقديرية",
      netOverride = "صافي الراتب",
      rent = "الإيجار",
      family = "مصروف العائلة",
      estimateSavings = "احسب الادخار",
      monthlySavings = "الادخار الشهري",
      yearlySavings = "الادخار السنوي",
      savingsRate = "نسبة الادخار",
      darkMode = "الوضع الداكن",
      saudiEmployeeRate = "نسبة الموظف السعودي",
      saudiEmployerRate = "نسبة صاحب العمل السعودي",
      expatEmployeeRate = "نسبة غير السعودي",
      expatHazardRate = "نسبة الأخطار المهنية"
    )
  } else {
    UiCopy(
      appName = "Saudi Salary Calculator",
      tagline = "No income tax. Configurable GOSI. SAR-ready reports.",
      heroTitle = "Smart salary dashboard",
      heroBody = "Calculate take-home pay, employer cost, offer quality, end-of-service benefit, and savings from one place.",
      start = "Start salary calculation",
      salaryInput = "Salary",
      resultDashboard = "Result dashboard",
      runCalculator = "Run the salary calculator to unlock the dashboard.",
      openSalaryInput = "Open salary input",
      currentOffer = "Current offer",
      newOffer = "New offer",
      compareOffers = "Compare offers",
      offerScore = "Offer acceptance score",
      betterOffer = "Better offer",
      monthlyDifference = "Monthly difference",
      yearlyDifference = "Yearly difference",
      eosTitle = "End-of-service benefit",
      savingsPlanner = "Expense and savings planner",
      settings = "Settings",
      reportPreview = "PDF report preview",
      exportPdf = "Export PDF",
      saved = "Saved calculations",
      clear = "Clear",
      emptyHistory = "No saved records yet",
      emptySummary = "Your monthly take-home salary will appear here.",
      calculatedAck = "Take-home salary calculated. Showing your result.",
      basic = "Basic",
      housing = "Housing",
      transport = "Transport",
      food = "Food",
      mobile = "Mobile",
      other = "Other",
      deductions = "Deductions",
      calculateTakeHome = "Calculate take-home",
      saudi = "Saudi",
      expat = "Expat",
      monthly = "Monthly",
      yearly = "Yearly",
      netWage = "Net wage",
      employerCost = "Employer cost",
      lastBasic = "Last basic salary",
      years = "Years of service",
      resignation = "Resignation case",
      calculateEos = "Calculate EOS",
      eligibleYears = "Eligible years",
      estimatedBenefit = "Estimated benefit",
      netOverride = "Net salary override",
      rent = "Rent",
      family = "Family expense",
      estimateSavings = "Estimate savings",
      monthlySavings = "Monthly savings",
      yearlySavings = "Yearly savings",
      savingsRate = "Savings rate",
      darkMode = "Dark mode",
      saudiEmployeeRate = "Saudi employee rate",
      saudiEmployerRate = "Saudi employer rate",
      expatEmployeeRate = "Expat employee rate",
      expatHazardRate = "Expat hazard rate"
    )
  }
