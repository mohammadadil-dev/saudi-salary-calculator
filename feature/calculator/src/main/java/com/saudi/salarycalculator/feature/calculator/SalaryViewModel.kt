package com.saudi.salarycalculator.feature.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saudi.salarycalculator.core.data.SalaryRepository
import com.saudi.salarycalculator.core.model.CalculationRecord
import com.saudi.salarycalculator.core.model.CalculationType
import com.saudi.salarycalculator.core.model.GosiRates
import com.saudi.salarycalculator.core.model.NetSalaryInput
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/** Single shared ViewModel for the whole calculator feature: one instance is obtained at the
 * NavHost level (see SalaryNavGraph) and handed to every destination, so wizard progress,
 * the last result, and the offer comparison all survive back-stack navigation between the
 * bottom-nav tabs without being recomputed or lost. */
@HiltViewModel
class SalaryViewModel @Inject constructor(
  private val repository: SalaryRepository
) : ViewModel() {
  private val _state = MutableStateFlow(SalaryUiState())
  val state: StateFlow<SalaryUiState> = _state.asStateFlow()

  init {
    repository.observeDarkMode().onEach { enabled ->
      _state.value = _state.value.copy(darkMode = enabled)
    }.launchIn(viewModelScope)

    repository.observeLanguage().onEach { language ->
      _state.value = _state.value.copy(language = language)
    }.launchIn(viewModelScope)

    repository.observeHistory().onEach { history ->
      _state.value = _state.value.copy(history = history)
    }.launchIn(viewModelScope)
  }

  // ---- Wizard ----------------------------------------------------------

  fun updateWizard(transform: (WizardFieldsState) -> WizardFieldsState) {
    _state.value = _state.value.copy(wizard = transform(_state.value.wizard))
  }

  fun goToWizardStep(step: WizardStep) {
    _state.value = _state.value.copy(wizardStep = step)
  }

  fun nextWizardStep(): Boolean {
    val steps = WizardStep.entries
    val nextIndex = _state.value.wizardStepIndex + 1
    return if (nextIndex < steps.size) {
      _state.value = _state.value.copy(wizardStep = steps[nextIndex])
      true
    } else {
      false
    }
  }

  fun previousWizardStep(): Boolean {
    val steps = WizardStep.entries
    val prevIndex = _state.value.wizardStepIndex - 1
    return if (prevIndex >= 0) {
      _state.value = _state.value.copy(wizardStep = steps[prevIndex])
      true
    } else {
      false
    }
  }

  fun resetWizard() {
    _state.value = _state.value.copy(wizard = WizardFieldsState(), wizardStep = WizardStep.BASIC_SALARY)
  }

  /** Runs the net-salary calculation from the current wizard state. [onComplete] lets the
   * Review screen navigate to the Result destination only after the result is actually ready. */
  fun calculateNetSalary(onComplete: () -> Unit = {}) {
    val wizard = _state.value.wizard
    viewModelScope.launch {
      _state.value = _state.value.copy(isCalculating = true)
      val input = wizard.toNetSalaryInput().copy(gosiRates = _state.value.gosiRates)
      val result = repository.calculateNetSalary(input)
      _state.value = _state.value.copy(
        isCalculating = false,
        netSalaryResult = result,
        lastCalculatedAtMillis = System.currentTimeMillis()
      )
      saveRecord(
        type = CalculationType.NET_SALARY,
        title = wizard.jobTitle.ifBlank { "Salary calculation" },
        inputSummary = "Basic=${input.basicSalary}",
        resultSummary = "Net=${result.netSalary}",
        netSalaryInput = input
      )
      onComplete()
    }
  }

  /** Reopens a saved [CalculationRecord] into the wizard for editing. Only records that carry a
   * structured [NetSalaryInput] snapshot (currently NET_SALARY records) support this. */
  fun editRecord(record: CalculationRecord) {
    val input = record.netSalaryInput ?: return
    _state.value = _state.value.copy(
      wizard = input.toWizardFieldsState(),
      wizardStep = WizardStep.BASIC_SALARY
    )
  }

  fun deleteRecord(id: String) {
    viewModelScope.launch { repository.deleteRecord(id) }
  }

  // ---- Offer comparison -------------------------------------------------

  fun updateOfferCurrent(transform: (OfferFormState) -> OfferFormState) {
    _state.value = _state.value.copy(offerCurrent = transform(_state.value.offerCurrent))
  }

  fun updateOfferNew(transform: (OfferFormState) -> OfferFormState) {
    _state.value = _state.value.copy(offerNew = transform(_state.value.offerNew))
  }

  /** No-ops if either offer is missing a real basic salary, so a stray tap can't produce a
   * meaningless 0-vs-0 "result" card. The Compare Offers CTA mirrors this same condition via its
   * `enabled` param (see ComparisonScreen) — this is just defense-in-depth. */
  fun compareOffers() {
    val s = _state.value
    val offerCurrentValid = (s.offerCurrent.basicSalary.toDoubleOrNull() ?: 0.0) > 0.0
    val offerNewValid = (s.offerNew.basicSalary.toDoubleOrNull() ?: 0.0) > 0.0
    if (!offerCurrentValid || !offerNewValid) return
    viewModelScope.launch {
      val result = repository.compareOffers(
        offerA = s.offerCurrent.toOfferInput(),
        offerB = s.offerNew.toOfferInput()
      )
      _state.value = _state.value.copy(offerComparisonResult = result)
      saveRecord(
        type = CalculationType.OFFER_COMPARISON,
        title = "${result.offerA.title} vs ${result.offerB.title}",
        inputSummary = "${s.offerCurrent.title} / ${s.offerNew.title}",
        resultSummary = "Score=${result.acceptanceScore}"
      )
    }
  }

  // ---- Settings ----------------------------------------------------------

  fun toggleDarkMode() {
    val newValue = !_state.value.darkMode
    _state.value = _state.value.copy(darkMode = newValue)
    viewModelScope.launch { repository.setDarkMode(newValue) }
  }

  fun setLanguage(language: String) {
    _state.value = _state.value.copy(language = language)
    viewModelScope.launch { repository.setLanguage(language) }
  }

  fun updateGosiRates(transform: (GosiRates) -> GosiRates) {
    _state.value = _state.value.copy(gosiRates = transform(_state.value.gosiRates))
  }

  fun clearHistory() {
    viewModelScope.launch { repository.clearHistory() }
  }

  fun dismissToast() {
    _state.value = _state.value.copy(toastMessage = null)
  }

  /** Surfaces a one-off confirmation message (e.g. "PDF exported"). Consumed by a Snackbar at
   * the NavGraph level, which calls [dismissToast] once it has been shown. */
  fun showToast(message: String) {
    _state.value = _state.value.copy(toastMessage = message)
  }

  private suspend fun saveRecord(
    type: CalculationType,
    title: String,
    inputSummary: String,
    resultSummary: String,
    netSalaryInput: NetSalaryInput? = null
  ) {
    repository.saveRecord(
      CalculationRecord(
        type = type,
        title = title,
        inputSummary = inputSummary,
        resultSummary = resultSummary,
        createdAtMillis = System.currentTimeMillis(),
        netSalaryInput = netSalaryInput
      )
    )
  }
}
