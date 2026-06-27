package com.saudi.salarycalculator.feature.calculator.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.saudi.salarycalculator.core.designsystem.components.AppTopBar
import com.saudi.salarycalculator.core.designsystem.components.BottomNavItem
import com.saudi.salarycalculator.core.designsystem.components.BottomNavigationBar
import com.saudi.salarycalculator.core.designsystem.components.AnimatedAppBackground
import com.saudi.salarycalculator.core.designsystem.util.LocalizedContent
import com.saudi.salarycalculator.core.model.NetSalaryResult
import com.saudi.salarycalculator.feature.calculator.PdfReportExporter
import com.saudi.salarycalculator.feature.calculator.R
import com.saudi.salarycalculator.feature.calculator.SalaryViewModel
import com.saudi.salarycalculator.feature.calculator.WizardFieldsState
import com.saudi.salarycalculator.feature.calculator.screens.CalculatorWizardScreen
import com.saudi.salarycalculator.feature.calculator.screens.ComparisonScreen
import com.saudi.salarycalculator.feature.calculator.screens.HomeScreen
import com.saudi.salarycalculator.feature.calculator.screens.PayslipScreen
import com.saudi.salarycalculator.feature.calculator.screens.ResultScreen
import com.saudi.salarycalculator.feature.calculator.screens.SettingsScreen
import com.saudi.salarycalculator.feature.calculator.screens.SplashScreen
import java.io.File
import java.util.Locale

/** Single shared-ViewModel NavHost for the whole app. [SalaryViewModel] is obtained once here
 * (scoped to whichever ViewModelStoreOwner hosts this composable, normally the Activity) and
 * handed to every destination, so wizard/result/comparison state survives tab switches. Hosts
 * the bottom navigation bar for [Screen.bottomNavScreens] and a slim back-only [AppTopBar] for
 * [Screen.Payslip], which is pushed from Result rather than being a tab itself. [adBanner] is an
 * injection point for the existing AdMob banner so this graph stays free of Activity-level setup. */
@Composable
fun SalaryNavGraph(
  modifier: Modifier = Modifier,
  adBanner: @Composable () -> Unit = {}
) {
  val viewModel: SalaryViewModel = hiltViewModel()
  val state by viewModel.state.collectAsStateWithLifecycle()
  val navController = rememberNavController()
  val context = LocalContext.current

  LocalizedContent(languageCode = state.language) {
  val currencySymbol = stringResource(R.string.common_currency_sar)

  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = backStackEntry?.destination?.route
  val bottomNavRoutes = remember { Screen.bottomNavScreens.map { it.route } }

  val snackbarHostState = remember { SnackbarHostState() }
  LaunchedEffect(state.toastMessage) {
    val message = state.toastMessage
    if (message != null) {
      snackbarHostState.showSnackbar(message)
      viewModel.dismissToast()
    }
  }

  val bottomNavItems = listOf(
    BottomNavItem(Screen.Home.route, stringResource(R.string.nav_home), Icons.Filled.Home),
    BottomNavItem(Screen.Calculator.route, stringResource(R.string.nav_calculator), Icons.Filled.Calculate),
    BottomNavItem(Screen.Result.route, stringResource(R.string.nav_result), Icons.Filled.Assessment),
    BottomNavItem(Screen.Comparison.route, stringResource(R.string.nav_compare), Icons.Filled.CompareArrows),
    BottomNavItem(Screen.Settings.route, stringResource(R.string.nav_settings), Icons.Filled.Settings)
  )

  Scaffold(
    modifier = modifier,
    containerColor = androidx.compose.ui.graphics.Color.Transparent,
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
      if (currentRoute == Screen.Payslip.route) {
        AppTopBar(
          title = stringResource(R.string.payslip_title),
          darkMode = state.darkMode,
          onBack = { navController.popBackStack() }
        )
      }
    },
    bottomBar = {
      if (currentRoute in bottomNavRoutes) {
        Column {
          adBanner()
          BottomNavigationBar(
            items = bottomNavItems,
            selectedKey = currentRoute ?: Screen.Home.route,
            darkMode = state.darkMode,
            onSelect = { route -> navController.navigateToTab(route) }
          )
        }
      }
    }
  ) { innerPadding ->
    AnimatedAppBackground(darkMode = state.darkMode)
    NavHost(
      navController = navController,
      startDestination = Screen.Splash.route,
      modifier = Modifier.padding(innerPadding)
    ) {
      composable(Screen.Splash.route) {
        SplashScreen(
          darkMode = state.darkMode,
          onFinished = {
            navController.navigate(Screen.Home.route) {
              popUpTo(Screen.Splash.route) { inclusive = true }
            }
          }
        )
      }

      composable(Screen.Home.route) {
        HomeScreen(
          darkMode = state.darkMode,
          lastResultNetSalary = state.netSalaryResult?.netSalary,
          history = state.history,
          currencySymbol = currencySymbol,
          onStartCalculation = {
            viewModel.resetWizard()
            navController.navigateToTab(Screen.Calculator.route)
          },
          onViewAllHistory = { navController.navigateToTab(Screen.Settings.route) },
          onEditRecord = { record ->
            viewModel.editRecord(record)
            navController.navigateToTab(Screen.Calculator.route)
          },
          onDeleteRecord = viewModel::deleteRecord
        )
      }

      composable(Screen.Calculator.route) {
        CalculatorWizardScreen(
          wizard = state.wizard,
          wizardStep = state.wizardStep,
          gosiRates = state.gosiRates,
          isCalculating = state.isCalculating,
          darkMode = state.darkMode,
          currencySymbol = currencySymbol,
          onUpdateWizard = viewModel::updateWizard,
          onGoToStep = viewModel::goToWizardStep,
          onCalculate = {
            viewModel.calculateNetSalary(onComplete = { navController.navigateToTab(Screen.Result.route) })
          },
          onExitWizard = { navController.navigateToTab(Screen.Home.route) }
        )
      }

      composable(Screen.Result.route) {
        ResultScreen(
          darkMode = state.darkMode,
          currencySymbol = currencySymbol,
          result = state.netSalaryResult,
          onViewPayslip = { navController.navigate(Screen.Payslip.route) },
          onCompareOffer = { navController.navigateToTab(Screen.Comparison.route) }
        )
      }

      composable(Screen.Payslip.route) {
        val savedConfirmation = stringResource(R.string.payslip_saved_confirmation)
        val exportedConfirmation = stringResource(R.string.payslip_exported_confirmation)
        val exportLabel = stringResource(R.string.action_export_pdf)
        PayslipScreen(
          darkMode = state.darkMode,
          currencySymbol = currencySymbol,
          wizard = state.wizard,
          result = state.netSalaryResult,
          onExportPdf = {
            state.netSalaryResult?.let { result ->
              writePayslipPdf(context, state.wizard, result, currencySymbol)
              viewModel.showToast(exportedConfirmation)
            }
          },
          onSaveCalculation = { viewModel.showToast(savedConfirmation) },
          onShare = {
            state.netSalaryResult?.let { result ->
              val file = writePayslipPdf(context, state.wizard, result, currencySymbol)
              sharePdfFile(context, file, exportLabel)
            }
          }
        )
      }

      composable(Screen.Comparison.route) {
        ComparisonScreen(
          darkMode = state.darkMode,
          currencySymbol = currencySymbol,
          offerCurrent = state.offerCurrent,
          offerNew = state.offerNew,
          comparisonResult = state.offerComparisonResult,
          onUpdateCurrent = viewModel::updateOfferCurrent,
          onUpdateNew = viewModel::updateOfferNew,
          onCompare = viewModel::compareOffers
        )
      }

      composable(Screen.Settings.route) {
        SettingsScreen(
          darkMode = state.darkMode,
          language = state.language,
          gosiRates = state.gosiRates,
          history = state.history,
          onToggleDarkMode = viewModel::toggleDarkMode,
          onSetLanguage = viewModel::setLanguage,
          onUpdateGosiRates = viewModel::updateGosiRates,
          onClearHistory = viewModel::clearHistory,
          onEditRecord = { record ->
            viewModel.editRecord(record)
            navController.navigateToTab(Screen.Calculator.route)
          },
          onDeleteRecord = viewModel::deleteRecord
        )
      }
    }
  }
  } // end LocalizedContent
}

/** Single-top, non-stacking navigation between bottom-nav tabs (mirrors the standard
 * bottom-navigation pattern) so repeatedly tapping tabs doesn't grow the back stack. */
private fun NavController.navigateToTab(route: String) {
  navigate(route) { launchSingleTop = true }
}

private fun writePayslipPdf(
  context: Context,
  wizard: WizardFieldsState,
  result: NetSalaryResult,
  currencySymbol: String
): File {
  val file = File(context.cacheDir, "saudi_salary_payslip.pdf")
  PdfReportExporter.write(file, buildPayslipReportText(wizard, result, currencySymbol))
  return file
}

private fun sharePdfFile(context: Context, file: File, chooserTitle: String) {
  val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
  val intent = Intent(Intent.ACTION_SEND).apply {
    type = "application/pdf"
    putExtra(Intent.EXTRA_STREAM, uri)
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
  }
  context.startActivity(Intent.createChooser(intent, chooserTitle))
}

private fun buildPayslipReportText(
  wizard: WizardFieldsState,
  result: NetSalaryResult,
  currencySymbol: String
): String = buildString {
  fun money(value: Double) = "${String.format(Locale.US, "%,.2f", value)} $currencySymbol"

  appendLine("Saudi Salary Calculator - Payslip")
  appendLine()
  appendLine("Employee: ${wizard.employeeName.ifBlank { "-" }}")
  appendLine("Job title: ${wizard.jobTitle.ifBlank { "-" }}")
  appendLine()
  appendLine("Gross salary: ${money(result.grossSalary)}")
  appendLine("Total allowances: ${money(result.totalAllowances)}")
  appendLine("Overtime pay: ${money(result.overtimePay)}")
  appendLine("Total deductions: ${money(result.totalDeductions)}")
  appendLine("Employee GOSI: ${money(result.employeeGosiAmount)}")
  appendLine("Employer GOSI: ${money(result.employerGosiAmount)}")
  appendLine("Net salary: ${money(result.netSalary)}")
  appendLine()
  appendLine("Yearly net salary: ${money(result.yearlyNetSalary)}")
  appendLine("Estimated end-of-service benefit: ${money(result.estimatedEosb)}")
}
