package com.saudi.salarycalculator.feature.calculator.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.components.AppTopBar
import com.saudi.salarycalculator.core.designsystem.components.PrimaryButton
import com.saudi.salarycalculator.core.designsystem.components.SecondaryButton
import com.saudi.salarycalculator.core.designsystem.components.StepProgressIndicator
import com.saudi.salarycalculator.core.model.GosiRates
import com.saudi.salarycalculator.feature.calculator.R
import com.saudi.salarycalculator.feature.calculator.WizardFieldsState
import com.saudi.salarycalculator.feature.calculator.WizardStep

/** The step-by-step calculator wizard: a [HorizontalPager] of the six steps from [WizardSteps.kt],
 * kept in sync with [wizardStep] (the source of truth, owned by SalaryViewModel) in both
 * directions — swiping the pager advances [onGoToStep], and tapping Next/Back or jumping via the
 * progress indicator animates the pager to match. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalculatorWizardScreen(
  wizard: WizardFieldsState,
  wizardStep: WizardStep,
  gosiRates: GosiRates,
  isCalculating: Boolean,
  darkMode: Boolean,
  currencySymbol: String,
  onUpdateWizard: ((WizardFieldsState) -> WizardFieldsState) -> Unit,
  onGoToStep: (WizardStep) -> Unit,
  onCalculate: () -> Unit,
  onExitWizard: () -> Unit,
  modifier: Modifier = Modifier
) {
  val steps = WizardStep.entries
  val pagerState = rememberPagerState(pageCount = { steps.size })
  val currentIndex = steps.indexOf(wizardStep)

  LaunchedEffect(currentIndex) {
    if (pagerState.currentPage != currentIndex) {
      pagerState.animateScrollToPage(currentIndex)
    }
  }
  LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
    if (!pagerState.isScrollInProgress) {
      val settledStep = steps[pagerState.currentPage]
      if (settledStep != wizardStep) onGoToStep(settledStep)
    }
  }

  val stepTitle = when (wizardStep) {
    WizardStep.BASIC_SALARY -> stringResource(R.string.step_basic_salary)
    WizardStep.ALLOWANCES -> stringResource(R.string.step_allowances)
    WizardStep.DEDUCTIONS -> stringResource(R.string.step_deductions)
    WizardStep.EMPLOYMENT_DETAILS -> stringResource(R.string.step_employment_details)
    WizardStep.GOSI_EOSB -> stringResource(R.string.step_gosi_eosb)
    WizardStep.REVIEW -> stringResource(R.string.step_review)
  }
  val stepOfLabel = stringResource(R.string.common_step_of, currentIndex + 1, steps.size)

  Column(modifier = modifier.fillMaxSize().padding(horizontal = 20.dp)) {
    AppTopBar(
      title = stepTitle,
      darkMode = darkMode,
      onBack = {
        if (currentIndex == 0) onExitWizard() else onGoToStep(steps[currentIndex - 1])
      }
    )
    StepProgressIndicator(
      currentStep = currentIndex,
      totalSteps = steps.size,
      stepLabel = stepTitle,
      stepOfLabel = stepOfLabel,
      darkMode = darkMode,
      modifier = Modifier.padding(bottom = 16.dp)
    )

    HorizontalPager(
      state = pagerState,
      modifier = Modifier.weight(1f).fillMaxWidth()
    ) { page ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(bottom = 24.dp)
      ) {
        when (steps[page]) {
          WizardStep.BASIC_SALARY -> StepBasicSalaryContent(wizard, darkMode, currencySymbol, onUpdateWizard)
          WizardStep.ALLOWANCES -> StepAllowancesContent(wizard, darkMode, currencySymbol, onUpdateWizard)
          WizardStep.DEDUCTIONS -> StepDeductionsContent(wizard, darkMode, currencySymbol, onUpdateWizard)
          WizardStep.EMPLOYMENT_DETAILS -> StepEmploymentDetailsContent(wizard, darkMode, onUpdateWizard)
          WizardStep.GOSI_EOSB -> StepGosiEosbContent(wizard, darkMode, currencySymbol, gosiRates, onUpdateWizard)
          WizardStep.REVIEW -> StepReviewContent(wizard, darkMode, currencySymbol, isCalculating, onCalculate)
        }
      }
    }

    if (wizardStep != WizardStep.REVIEW) {
      Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        if (currentIndex > 0) {
          SecondaryButton(
            text = stringResource(R.string.common_back),
            darkMode = darkMode,
            modifier = Modifier.weight(1f),
            onClick = { onGoToStep(steps[currentIndex - 1]) }
          )
        }
        PrimaryButton(
          text = stringResource(R.string.common_next),
          modifier = Modifier.weight(1f),
          onClick = {
            val nextIndex = currentIndex + 1
            if (nextIndex < steps.size) {
              onGoToStep(steps[nextIndex])
            }
          }
        )
      }
    }
  }
}
