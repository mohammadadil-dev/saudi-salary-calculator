package com.saudi.salarycalculator.feature.calculator.navigation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.saudi.salarycalculator.core.model.CalculationRecord
import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.EmploymentSector
import com.saudi.salarycalculator.core.model.NetSalaryResult
import com.saudi.salarycalculator.feature.calculator.PayrollSummaryExporter
import com.saudi.salarycalculator.feature.calculator.PdfReportExporter
import com.saudi.salarycalculator.feature.calculator.R
import com.saudi.salarycalculator.feature.calculator.SalaryViewModel
import com.saudi.salarycalculator.feature.calculator.WizardFieldsState
import com.saudi.salarycalculator.feature.calculator.screens.CalculatorWizardScreen
import com.saudi.salarycalculator.feature.calculator.screens.ComparisonScreen
import com.saudi.salarycalculator.feature.calculator.screens.EosbTrackerScreen
import com.saudi.salarycalculator.feature.calculator.screens.ExpatCostScreen
import com.saudi.salarycalculator.feature.calculator.screens.HomeScreen
import com.saudi.salarycalculator.feature.calculator.screens.LeaveTrackerScreen
import com.saudi.salarycalculator.feature.calculator.screens.OfferScanScreen
import com.saudi.salarycalculator.feature.calculator.screens.CostOfLivingScreen
import com.saudi.salarycalculator.feature.calculator.screens.PayslipScreen
import com.saudi.salarycalculator.feature.calculator.screens.ResultScreen
import com.saudi.salarycalculator.feature.calculator.screens.ReverseSalaryScreen
import com.saudi.salarycalculator.feature.calculator.screens.SettingsScreen
import com.saudi.salarycalculator.feature.calculator.screens.SplashScreen
import com.saudi.salarycalculator.review.ReviewPrompter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

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
  val coroutineScope = rememberCoroutineScope()

  // Whether the "Enjoying the app?" sentiment-gate dialog is showing right now — see
  // ReviewPrompter for why this sits in front of the actual Play review flow instead of firing
  // it directly. Resolved by the dialog's own Yes/No handlers below.
  var showSentimentPrompt by remember { mutableStateOf(false) }

  // Checked after a real "value delivered" moment (a completed PDF export, offer comparison,
  // etc.), never on a raw tap — ReviewPrompter's own gating (min success count, min days since
  // install, cooldown) decides whether this is actually a good moment to ask at all. If it is,
  // the sentiment dialog decides whether the user actually sees the native Play review flow.
  val maybePromptForReview: () -> Unit = {
    coroutineScope.launch {
      if (ReviewPrompter.onSuccessEvent(context)) {
        showSentimentPrompt = true
      }
    }
  }

  // Loyalty trigger: fires once per app session so users who only ever use the quick calculator
  // — and never hit one of the success-event moments above — still get a fair chance to be
  // asked once they've opened the app enough times to count as a real user.
  LaunchedEffect(Unit) {
    if (ReviewPrompter.onAppOpen(context)) {
      showSentimentPrompt = true
    }
  }

  LocalizedContent(languageCode = state.language) {
  val currencySymbol = stringResource(R.string.common_currency_sar)

  if (showSentimentPrompt) {
    val sentimentThanksMessage = stringResource(R.string.review_sentiment_thanks)
    SentimentReviewDialog(
      onEnjoyingIt = {
        showSentimentPrompt = false
        coroutineScope.launch {
          ReviewPrompter.markPrompted(context)
          (context as? Activity)?.let { activity -> ReviewPrompter.launchReviewFlow(context, activity) }
        }
      },
      onNotReally = {
        showSentimentPrompt = false
        coroutineScope.launch { ReviewPrompter.markPrompted(context) }
        viewModel.showToast(sentimentThanksMessage)
      }
    )
  }

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
      // Every route renders *something* in this slot, even when there's no title bar to show
      // (Home/Calculator/Result/Comparison/Settings/Splash) — with edge-to-edge enforced
      // (targetSdk 35+), an empty topBar slot has zero measured height, so Scaffold's `content`
      // padding ends up not reserving any status-bar space either, and those screens' own
      // headers would render straight under the status bar icons exactly like AppTopBar did
      // before it started applying statusBarsPadding() itself.
      when (currentRoute) {
        Screen.Payslip.route -> AppTopBar(
          title = stringResource(R.string.payslip_title),
          darkMode = state.darkMode,
          onBack = { navController.popBackStack() }
        )
        Screen.ExpatCosts.route -> AppTopBar(
          title = stringResource(R.string.expat_costs_title),
          darkMode = state.darkMode,
          onBack = { navController.popBackStack() }
        )
        Screen.EosbTracker.route -> AppTopBar(
          title = stringResource(R.string.eosb_tracker_title),
          darkMode = state.darkMode,
          onBack = { navController.popBackStack() }
        )
        Screen.LeaveTracker.route -> AppTopBar(
          title = stringResource(R.string.leave_tracker_title),
          darkMode = state.darkMode,
          onBack = { navController.popBackStack() }
        )
        Screen.OfferScan.route -> AppTopBar(
          title = stringResource(R.string.offer_scan_title),
          darkMode = state.darkMode,
          onBack = { navController.popBackStack() }
        )
        Screen.ReverseSalary.route -> AppTopBar(
          title = stringResource(R.string.reverse_salary_title),
          darkMode = state.darkMode,
          onBack = { navController.popBackStack() }
        )
        Screen.CostOfLiving.route -> AppTopBar(
          title = stringResource(R.string.cost_of_living_title),
          darkMode = state.darkMode,
          onBack = { navController.popBackStack() }
        )
        else -> androidx.compose.foundation.layout.Spacer(
          modifier = Modifier.statusBarsPadding()
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
          onDeleteRecord = viewModel::deleteRecord,
          onOpenExpatCosts = { navController.navigate(Screen.ExpatCosts.route) },
          onOpenEosbTracker = { navController.navigate(Screen.EosbTracker.route) },
          onOpenLeaveTracker = { navController.navigate(Screen.LeaveTracker.route) },
          onOpenOfferScan = { navController.navigate(Screen.OfferScan.route) },
          onOpenReverseSalary = { navController.navigate(Screen.ReverseSalary.route) },
          onOpenCostOfLiving = { navController.navigate(Screen.CostOfLiving.route) }
        )
      }

      composable(Screen.ExpatCosts.route) {
        ExpatCostScreen(
          darkMode = state.darkMode,
          currencySymbol = currencySymbol,
          form = state.expatCostForm,
          result = state.expatCostResult,
          lastNetSalary = state.netSalaryResult?.netSalary,
          onUpdateForm = viewModel::updateExpatCostForm,
          onCalculate = viewModel::calculateExpatCosts
        )
      }

      composable(Screen.EosbTracker.route) {
        EosbTrackerScreen(
          darkMode = state.darkMode,
          currencySymbol = currencySymbol,
          form = state.eosbTrackerForm,
          saved = state.eosbTrackerSaved,
          result = state.eosbTrackerResult,
          onUpdateForm = viewModel::updateEosbTrackerForm,
          onSave = viewModel::saveEosbTrackerProfile,
          onClear = viewModel::clearEosbTrackerProfile,
          onRefresh = viewModel::refreshEosbTracker
        )
      }

      composable(Screen.LeaveTracker.route) {
        LeaveTrackerScreen(
          darkMode = state.darkMode,
          form = state.leaveTrackerForm,
          saved = state.leaveTrackerSaved,
          result = state.leaveTrackerResult,
          onUpdateForm = viewModel::updateLeaveTrackerForm,
          onSave = viewModel::saveLeaveTrackerProfile,
          onClear = viewModel::clearLeaveTrackerProfile,
          onRefresh = viewModel::refreshLeaveTracker
        )
      }

      composable(Screen.OfferScan.route) {
        OfferScanScreen(
          darkMode = state.darkMode,
          currencySymbol = currencySymbol,
          form = state.offerScanForm,
          result = state.offerScanResult,
          onUpdateForm = viewModel::updateOfferScanForm,
          onScan = viewModel::scanOffer
        )
      }

      composable(Screen.ReverseSalary.route) {
        ReverseSalaryScreen(
          darkMode = state.darkMode,
          currencySymbol = currencySymbol,
          form = state.reverseSalaryForm,
          result = state.reverseSalaryResult,
          onUpdateForm = viewModel::updateReverseSalaryForm,
          onCalculate = viewModel::calculateReverseSalary
        )
      }

      composable(Screen.CostOfLiving.route) {
        CostOfLivingScreen(
          darkMode = state.darkMode,
          form = state.costOfLivingForm,
          result = state.costOfLivingResult,
          onUpdateForm = viewModel::updateCostOfLivingForm,
          onEstimate = viewModel::estimateCostOfLiving,
          onUseAsReverseSalaryTarget = {
            viewModel.useCostOfLivingResultAsReverseSalaryTarget()
            navController.navigate(Screen.ReverseSalary.route)
          }
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
        val certificateExportedConfirmation = stringResource(R.string.certificate_exported_confirmation)
        val certificateShareTitle = stringResource(R.string.action_export_certificate)
        PayslipScreen(
          darkMode = state.darkMode,
          currencySymbol = currencySymbol,
          wizard = state.wizard,
          result = state.netSalaryResult,
          onExportPdf = {
            state.netSalaryResult?.let { result ->
              writePayslipPdf(context, state.wizard, result, currencySymbol)
              viewModel.showToast(exportedConfirmation)
              maybePromptForReview()
            }
          },
          onSaveCalculation = { viewModel.showToast(savedConfirmation) },
          onShare = {
            state.netSalaryResult?.let { result ->
              val file = writePayslipPdf(context, state.wizard, result, currencySymbol)
              shareFile(context, file, mimeType = "application/pdf", chooserTitle = exportLabel)
            }
          },
          onExportSalaryCertificate = {
            state.netSalaryResult?.let { result ->
              val file = writeSalaryCertificatePdf(context, state.wizard, result, currencySymbol)
              shareFile(context, file, mimeType = "application/pdf", chooserTitle = certificateShareTitle)
              viewModel.showToast(certificateExportedConfirmation)
              maybePromptForReview()
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
          // The Compare CTA is disabled until both sides have a real basic salary (see
          // ComparisonScreen), so by the time this fires the comparison is guaranteed to
          // succeed — another genuine "value delivered" moment worth counting toward the
          // in-app review prompt, same as the payslip/certificate exports below.
          onCompare = {
            viewModel.compareOffers()
            maybePromptForReview()
          }
        )
      }

      composable(Screen.Settings.route) {
        val payrollExportedConfirmation = stringResource(R.string.settings_payroll_export_confirmation)
        val payrollExportShareTitle = stringResource(R.string.settings_export_payroll_summary)
        val shareAppTitle = stringResource(R.string.settings_share_app)
        val shareAppMessage = stringResource(R.string.settings_share_app_message)
        SettingsScreen(
          darkMode = state.darkMode,
          language = state.language,
          gosiRates = state.gosiRates,
          gosiSystem = state.gosiSystem,
          paydayDayOfMonth = state.paydayDayOfMonth,
          paydayResult = state.paydayResult,
          history = state.history,
          onToggleDarkMode = viewModel::toggleDarkMode,
          onSetLanguage = viewModel::setLanguage,
          onUpdateGosiRates = viewModel::updateGosiRates,
          onSetGosiSystem = viewModel::setGosiSystem,
          onSetPaydayDayOfMonth = viewModel::setPaydayDayOfMonth,
          onClearPaydayDayOfMonth = viewModel::clearPaydayDayOfMonth,
          onClearHistory = viewModel::clearHistory,
          onEditRecord = { record ->
            viewModel.editRecord(record)
            navController.navigateToTab(Screen.Calculator.route)
          },
          onDeleteRecord = viewModel::deleteRecord,
          onExportPayrollSummary = {
            val file = writePayrollSummaryCsv(context, state.history)
            shareFile(context, file, mimeType = "text/csv", chooserTitle = payrollExportShareTitle)
            viewModel.showToast(payrollExportedConfirmation)
            maybePromptForReview()
          },
          onRateApp = { openPlayStoreListing(context) },
          onShareApp = { shareAppLink(context, shareAppMessage, shareAppTitle) }
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

/** "Enjoying the app?" sentiment check shown before the native Play in-app review dialog — see
 * [ReviewPrompter]. A "yes" here is what actually triggers the real review flow; a "no" (or
 * dismissing the dialog any other way, e.g. tapping outside it or the system back button) is
 * routed away from it entirely so an unhappy moment never turns into a public low rating. */
@Composable
private fun SentimentReviewDialog(
  onEnjoyingIt: () -> Unit,
  onNotReally: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onNotReally,
    title = { Text(stringResource(R.string.review_sentiment_title)) },
    text = { Text(stringResource(R.string.review_sentiment_body)) },
    confirmButton = {
      TextButton(onClick = onEnjoyingIt) {
        Text(stringResource(R.string.review_sentiment_yes))
      }
    },
    dismissButton = {
      TextButton(onClick = onNotReally) {
        Text(stringResource(R.string.review_sentiment_no))
      }
    }
  )
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

private fun writePayrollSummaryCsv(context: Context, history: List<CalculationRecord>): File {
  val file = File(context.cacheDir, "saudi_salary_payroll_summary.csv")
  PayrollSummaryExporter.write(file, history)
  return file
}

private fun shareFile(context: Context, file: File, mimeType: String, chooserTitle: String) {
  val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
  val intent = Intent(Intent.ACTION_SEND).apply {
    type = mimeType
    putExtra(Intent.EXTRA_STREAM, uri)
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
  }
  context.startActivity(Intent.createChooser(intent, chooserTitle))
}

/** Opens this app's own Play Store listing for "Rate this app". Prefers the Play Store app
 * itself (nicer in-app review UI) and falls back to a browser if Play Store isn't installed —
 * e.g. an emulator without Play services, or a sideloaded install. */
private fun openPlayStoreListing(context: Context) {
  val playStoreIntent = Intent(
    Intent.ACTION_VIEW,
    Uri.parse("market://details?id=${context.packageName}")
  ).apply { setPackage("com.android.vending") }
  try {
    context.startActivity(playStoreIntent)
  } catch (e: ActivityNotFoundException) {
    val webIntent = Intent(
      Intent.ACTION_VIEW,
      Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
    )
    context.startActivity(webIntent)
  }
}

/** Plain-text share sheet for "Share this app" — [message] already has the marketing blurb;
 * this just appends the Play Store link and hands it to whatever app the user picks
 * (WhatsApp, SMS, email, etc.), same chooser pattern as [shareFile]. */
private fun shareAppLink(context: Context, message: String, chooserTitle: String) {
  val playStoreUrl = "https://play.google.com/store/apps/details?id=${context.packageName}"
  val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, "$message\n\n$playStoreUrl")
  }
  context.startActivity(Intent.createChooser(intent, chooserTitle))
}

private fun buildPayslipReportText(
  wizard: WizardFieldsState,
  result: NetSalaryResult,
  currencySymbol: String
): String = buildString {
  fun money(value: Double) = "${String.format(Locale.US, "%,.2f", value)} $currencySymbol"

  appendLine("Saudi Salary Calculator: EOSB - Payslip")
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

/** Watermark tiled diagonally across the certificate PDF, in addition to the plain-text
 * disclaimer baked into the report body itself (see [buildSalaryCertificateReportText]) — see
 * [PdfReportExporter.write] for why both layers exist. */
private const val SALARY_CERTIFICATE_WATERMARK = "UNOFFICIAL - NOT EMPLOYER-CERTIFIED"

private fun writeSalaryCertificatePdf(
  context: Context,
  wizard: WizardFieldsState,
  result: NetSalaryResult,
  currencySymbol: String
): File {
  val file = File(context.cacheDir, "saudi_salary_certificate.pdf")
  PdfReportExporter.write(
    file,
    buildSalaryCertificateReportText(wizard, result, currencySymbol),
    watermarkText = SALARY_CERTIFICATE_WATERMARK
  )
  return file
}

/** Deliberately avoids certifying language like "this is to certify that..." — that phrasing
 * mimics an employer-issued document, which this explicitly is not. It's framed instead as a
 * personal record the user compiled themselves from self-entered figures, with the disclaimer
 * repeated as plain, unmissable text (not just the faint diagonal watermark) since that's the
 * part someone skimming the PDF is most likely to actually read. */
private fun buildSalaryCertificateReportText(
  wizard: WizardFieldsState,
  result: NetSalaryResult,
  currencySymbol: String
): String = buildString {
  fun money(value: Double) = "${String.format(Locale.US, "%,.2f", value)} $currencySymbol"
  val generatedOn = SimpleDateFormat("d MMM yyyy", Locale.US).format(Date())

  appendLine("=== UNOFFICIAL - SELF-GENERATED ESTIMATE ===")
  appendLine("NOT EMPLOYER-CERTIFIED. NOT AN OFFICIAL DOCUMENT.")
  appendLine()
  appendLine("Personal Salary Summary")
  appendLine("Generated on: $generatedOn")
  appendLine()
  appendLine("This summary was self-generated by the individual named below from figures they")
  appendLine("entered into the Saudi Salary Calculator: EOSB app. It has NOT been reviewed, verified,")
  appendLine("or issued by any employer, and carries no employer signature or stamp. Do not")
  appendLine("submit this as if it were an official HR-issued salary certificate.")
  appendLine()
  appendLine("Employee name: ${wizard.employeeName.ifBlank { "-" }}")
  appendLine("Job title: ${wizard.jobTitle.ifBlank { "-" }}")
  appendLine(
    "Nationality: ${if (wizard.employeeType == EmployeeType.SAUDI) "Saudi" else "Non-Saudi"}"
  )
  appendLine(
    "Employment sector: " +
      if (wizard.employmentSector == EmploymentSector.PRIVATE) "Private" else "Government"
  )
  wizard.joiningDateMillis?.let {
    appendLine("Joining date: ${SimpleDateFormat("d MMM yyyy", Locale.US).format(Date(it))}")
  }
  appendLine()
  appendLine("Monthly basic salary: ${money(wizard.basicSalary.toDoubleOrNull() ?: 0.0)}")
  appendLine("Monthly housing allowance: ${money(wizard.housingAllowance.toDoubleOrNull() ?: 0.0)}")
  appendLine("Monthly transport allowance: ${money(wizard.transportAllowance.toDoubleOrNull() ?: 0.0)}")
  appendLine("Monthly gross salary: ${money(result.grossSalary)}")
  appendLine("Monthly GOSI (employee): ${money(result.employeeGosiAmount)}")
  appendLine("Monthly net salary: ${money(result.netSalary)}")
  appendLine("Annual net salary: ${money(result.yearlyNetSalary)}")
  appendLine()
  appendLine("Figures reflect data entered by the user in this app and are not independently")
  appendLine("verified against payroll records.")
}
