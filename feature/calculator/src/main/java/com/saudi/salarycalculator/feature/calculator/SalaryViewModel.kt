package com.saudi.salarycalculator.feature.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saudi.salarycalculator.core.data.SalaryRepository
import com.saudi.salarycalculator.core.model.CalculationRecord
import com.saudi.salarycalculator.core.model.CalculationType
import com.saudi.salarycalculator.core.model.EosbTrackerInput
import com.saudi.salarycalculator.core.model.GosiRateSchedule
import com.saudi.salarycalculator.core.model.GosiRates
import com.saudi.salarycalculator.core.model.GosiSystem
import com.saudi.salarycalculator.core.model.NetSalaryInput
import com.saudi.salarycalculator.core.model.OfferRedFlagInput
import com.saudi.salarycalculator.core.model.PaydayInput
import com.saudi.salarycalculator.core.model.ReverseSalaryInput
import com.saudi.salarycalculator.core.model.LeaveTrackerInput
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

    // Seeds gosiRates from the persisted GOSI system's current sourced default (see
    // GosiRateSchedule) on every launch. Any manual rate tweaks made in Settings are not
    // separately persisted (same as before this field existed) — only which system the employee
    // is on survives restarts, so the seeded default is always correct even if a stale manual
    // edit isn't.
    repository.observeGosiSystem().onEach { system ->
      _state.value = _state.value.copy(
        gosiSystem = system,
        gosiRates = GosiRateSchedule.defaultRatesFor(system)
      )
    }.launchIn(viewModelScope)

    // Seeds the tracker form from the persisted profile and recomputes the live result whenever
    // that profile changes (first load, save, or clear) — see refreshEosbTracker() for the other
    // trigger (re-derive against *today's* date each time the tracker screen is opened, since
    // years-of-service otherwise goes stale the moment real time passes without a profile edit).
    repository.observeEosbTrackerProfile().onEach { profile ->
      val joining = profile.joiningDateMillis
      val basic = profile.lastBasicSalary
      val hasProfile = joining != null && basic != null && basic > 0.0
      _state.value = _state.value.copy(
        eosbTrackerForm = EosbTrackerFormState(
          joiningDateMillis = joining,
          lastBasicSalary = basic?.let { if (it == it.toLong().toDouble()) it.toLong().toString() else it.toString() } ?: ""
        ),
        eosbTrackerSaved = hasProfile,
        eosbTrackerResult = null
      )
      if (hasProfile) refreshEosbTracker()
    }.launchIn(viewModelScope)
    // Seeds the payday countdown from the persisted day-of-month and recomputes it against
    // "now" every time that setting changes (first load, save, or clear) — mirrors the EOSB
    // tracker's refresh pattern above, minus a separate manual refresh trigger since this is a
    // cheap synchronous calculation (see SalaryRepository.calculatePayday) rather than one worth
    // deferring to screen-open time.
    repository.observePaydayDayOfMonth().onEach { dayOfMonth ->
      _state.value = _state.value.copy(
        paydayDayOfMonth = dayOfMonth,
        paydayResult = dayOfMonth?.let { repository.calculatePayday(PaydayInput(dayOfMonth = it)) }
      )
    }.launchIn(viewModelScope)
    // Seeds the leave tracker form from the persisted profile and recomputes the live balance
    // whenever that profile changes (first load, save, or clear) — mirrors the EOSB tracker's
    // refresh pattern above, including the same "re-derive on screen open" trigger via
    // refreshLeaveTracker() for years-of-service/accrual drifting as real time passes.
    repository.observeLeaveTrackerProfile().onEach { profile ->
      val joining = profile.joiningDateMillis
      val daysTaken = profile.daysTakenThisYear
      val hasProfile = joining != null && daysTaken != null && daysTaken >= 0
      _state.value = _state.value.copy(
        leaveTrackerForm = LeaveTrackerFormState(
          joiningDateMillis = joining,
          daysTakenThisYear = daysTaken?.toString() ?: ""
        ),
        leaveTrackerSaved = hasProfile,
        leaveTrackerResult = null
      )
      if (hasProfile) refreshLeaveTracker()
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
    // Also clears netSalaryResult: it's a persistent bottom-nav tab, so without this a user who
    // resets the wizard and taps Result directly (before recalculating) would see a leftover
    // number from whatever was calculated before, no longer matching the now-empty wizard.
    _state.value = _state.value.copy(
      wizard = WizardFieldsState(),
      wizardStep = WizardStep.BASIC_SALARY,
      netSalaryResult = null
    )
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
    // Same reasoning as resetWizard(): the wizard now reflects a different record's inputs, so
    // the old netSalaryResult (from whatever was last calculated, possibly a different record
    // entirely) must not keep showing on the Result tab until the user recalculates.
    _state.value = _state.value.copy(
      wizard = input.toWizardFieldsState(),
      wizardStep = WizardStep.BASIC_SALARY,
      netSalaryResult = null
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
      // toOfferInput() leaves gosiRates at its bare default; without this override, Offer
      // Comparison would silently ignore whatever GOSI system/rates are configured in Settings
      // (both offers would score against the OfferInput default instead) — copy the current
      // configured rates onto both sides so the comparison stays consistent with the rest of the
      // app's GOSI handling.
      val result = repository.compareOffers(
        offerA = s.offerCurrent.toOfferInput().copy(gosiRates = s.gosiRates),
        offerB = s.offerNew.toOfferInput().copy(gosiRates = s.gosiRates)
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

  // ---- Expat residency cost estimator ------------------------------------

  /** Unlike the other one-shot calculators in the app, this one keeps its result live once a
   * first estimate exists: the underlying math is pure, instant arithmetic (no network/database
   * cost), so there's no good reason to make the user re-tap "Estimate Costs" after every stepper
   * nudge just to see an already-visible number catch up (e.g. increasing exit/re-entry trips
   * silently left the old monthly/yearly figures on screen until Estimate was tapped again).
   * Before a first tap, the empty "enter values and tap Estimate" state is left alone — only an
   * existing result gets kept in sync. This does not touch calculation history: only the explicit
   * [calculateExpatCosts] tap logs a [CalculationRecord], so idle form nudges don't spam it. */
  fun updateExpatCostForm(transform: (ExpatCostFormState) -> ExpatCostFormState) {
    val updatedForm = transform(_state.value.expatCostForm)
    _state.value = _state.value.copy(expatCostForm = updatedForm)
    if (_state.value.expatCostResult != null) {
      viewModelScope.launch {
        val result = repository.calculateExpatCosts(updatedForm.toExpatCostInput())
        _state.value = _state.value.copy(expatCostResult = result)
      }
    }
  }

  fun calculateExpatCosts() {
    val s = _state.value
    viewModelScope.launch {
      val result = repository.calculateExpatCosts(s.expatCostForm.toExpatCostInput())
      _state.value = _state.value.copy(expatCostResult = result)
      saveRecord(
        type = CalculationType.EXPAT_COSTS,
        title = "Expat residency costs",
        inputSummary = "${s.expatCostForm.dependentCount} dependent(s)",
        resultSummary = "Monthly=${result.totalMonthlyCost}"
      )
    }
  }

  // ---- EOSB accrual tracker ----------------------------------------------

  fun updateEosbTrackerForm(transform: (EosbTrackerFormState) -> EosbTrackerFormState) {
    _state.value = _state.value.copy(eosbTrackerForm = transform(_state.value.eosbTrackerForm))
  }

  /** Persists the current form as the standing tracker profile. No-ops (rather than saving a
   * half-filled profile) if the joining date is missing or the salary doesn't parse to a
   * positive number — the Save CTA mirrors this same condition via its `enabled` param on the
   * tracker screen, same defense-in-depth pattern as [compareOffers]. */
  fun saveEosbTrackerProfile() {
    val form = _state.value.eosbTrackerForm
    val joiningDateMillis = form.joiningDateMillis ?: return
    val lastBasicSalary = form.lastBasicSalary.toDoubleOrNull() ?: return
    if (lastBasicSalary <= 0.0) return
    viewModelScope.launch { repository.setEosbTrackerProfile(joiningDateMillis, lastBasicSalary) }
  }

  fun clearEosbTrackerProfile() {
    viewModelScope.launch { repository.clearEosbTrackerProfile() }
  }

  /** Re-derives [SalaryUiState.eosbTrackerResult] against the current moment. Call whenever the
   * tracker screen is (re)composed, not just on save — years-of-service, the milestone countdown,
   * and which resignation fraction currently applies all depend on "now", which moves forward
   * even when the saved profile itself hasn't changed. */
  fun refreshEosbTracker() {
    val form = _state.value.eosbTrackerForm
    val joiningDateMillis = form.joiningDateMillis ?: return
    val lastBasicSalary = form.lastBasicSalary.toDoubleOrNull() ?: return
    if (lastBasicSalary <= 0.0) return
    viewModelScope.launch {
      val result = repository.calculateEosbTracker(
        EosbTrackerInput(joiningDateMillis = joiningDateMillis, lastBasicSalary = lastBasicSalary)
      )
      _state.value = _state.value.copy(eosbTrackerResult = result)
    }
  }

  // ---- Leave-balance tracker ----------------------------------------------

  fun updateLeaveTrackerForm(transform: (LeaveTrackerFormState) -> LeaveTrackerFormState) {
    _state.value = _state.value.copy(leaveTrackerForm = transform(_state.value.leaveTrackerForm))
  }

  /** Persists the current form as the standing leave profile. No-ops if the joining date is
   * missing or days-taken doesn't parse to a non-negative whole number — mirrors
   * [saveEosbTrackerProfile]'s guard, enforced again by the Save CTA's `enabled` param on the
   * tracker screen. */
  fun saveLeaveTrackerProfile() {
    val form = _state.value.leaveTrackerForm
    val joiningDateMillis = form.joiningDateMillis ?: return
    val daysTaken = form.daysTakenThisYear.toIntOrNull() ?: return
    if (daysTaken < 0) return
    viewModelScope.launch { repository.setLeaveTrackerProfile(joiningDateMillis, daysTaken) }
  }

  fun clearLeaveTrackerProfile() {
    viewModelScope.launch { repository.clearLeaveTrackerProfile() }
  }

  /** Re-derives [SalaryUiState.leaveTrackerResult] against the current moment — see
   * [refreshEosbTracker] for why this needs to run on every screen open, not just on save. */
  fun refreshLeaveTracker() {
    val form = _state.value.leaveTrackerForm
    val joiningDateMillis = form.joiningDateMillis ?: return
    val daysTaken = form.daysTakenThisYear.toIntOrNull() ?: return
    if (daysTaken < 0) return
    viewModelScope.launch {
      val result = repository.calculateLeaveTracker(
        LeaveTrackerInput(joiningDateMillis = joiningDateMillis, daysTakenThisYear = daysTaken)
      )
      _state.value = _state.value.copy(leaveTrackerResult = result)
    }
  }

  // ---- Offer red-flag scanner ---------------------------------------------

  fun updateOfferScanForm(transform: (OfferScanFormState) -> OfferScanFormState) {
    _state.value = _state.value.copy(offerScanForm = transform(_state.value.offerScanForm))
  }

  /** No-ops if basic salary isn't a real positive number, so a stray tap on an empty form can't
   * produce a meaningless all-zero-input scan — mirrors [compareOffers]'s validity guard. Total
   * monthly salary falls back to basic salary when left blank (the field is optional), which
   * simply means the basic-salary-ratio check never fires rather than firing on a bad default. */
  fun scanOffer() {
    val form = _state.value.offerScanForm
    val basicSalary = form.basicSalary.toDoubleOrNull() ?: 0.0
    if (basicSalary <= 0.0) return
    viewModelScope.launch {
      val totalMonthlySalary = form.totalMonthlySalary.toDoubleOrNull()?.takeIf { it > 0.0 } ?: basicSalary
      // Falls back to 30 (the form's own default) rather than 0 on a blank/invalid entry, so a
      // field the user hasn't touched yet doesn't read as a 30-day asymmetric-notice red flag.
      val employeeNoticeDays = form.employeeNoticeDays.toIntOrNull()?.coerceIn(0, 365) ?: 30
      val employerNoticeDays = form.employerNoticeDays.toIntOrNull()?.coerceIn(0, 365) ?: 30
      val result = repository.scanOfferForRedFlags(
        OfferRedFlagInput(
          basicSalary = basicSalary,
          totalMonthlySalary = totalMonthlySalary,
          probationMonths = form.probationMonths,
          probationExtendedInWriting = form.probationExtendedInWriting,
          employeeNoticeDays = employeeNoticeDays,
          employerNoticeDays = employerNoticeDays,
          isGosiRegistered = form.isGosiRegistered,
          hasWrittenContract = form.hasWrittenContract,
          recruitmentFeesCharged = form.recruitmentFeesCharged
        )
      )
      _state.value = _state.value.copy(offerScanResult = result)
      saveRecord(
        type = CalculationType.OFFER_RED_FLAG_SCAN,
        title = "Offer red-flag scan",
        inputSummary = "Basic ${basicSalary.toLong()} / Total ${totalMonthlySalary.toLong()}",
        resultSummary = if (result.violationCount == 0 && result.cautionCount == 0) {
          "No red flags found"
        } else {
          "${result.violationCount} violation(s), ${result.cautionCount} caution(s)"
        }
      )
    }
  }

  // ---- Reverse salary calculator ------------------------------------------

  fun updateReverseSalaryForm(transform: (ReverseSalaryFormState) -> ReverseSalaryFormState) {
    _state.value = _state.value.copy(reverseSalaryForm = transform(_state.value.reverseSalaryForm))
  }

  /** No-ops if the target net salary isn't a real positive number — mirrors [scanOffer]'s and
   * [compareOffers]'s validity guards. Reuses the currently configured GOSI rates (Settings),
   * same override [compareOffers] applies, so the reverse calculation stays consistent with the
   * rest of the app's GOSI handling rather than silently falling back to defaults. */
  fun calculateReverseSalary() {
    val form = _state.value.reverseSalaryForm
    val target = form.targetNetMonthlySalary.toDoubleOrNull() ?: 0.0
    if (target <= 0.0) return
    viewModelScope.launch {
      val result = repository.calculateReverseSalary(
        ReverseSalaryInput(
          targetNetMonthlySalary = target,
          housingAllowancePercent = form.housingAllowancePercent,
          transportAllowancePercent = form.transportAllowancePercent,
          employeeType = form.employeeType,
          gosiRates = _state.value.gosiRates
        )
      )
      _state.value = _state.value.copy(reverseSalaryResult = result)
      saveRecord(
        type = CalculationType.REVERSE_SALARY,
        title = "Salary needed calculator",
        inputSummary = "Target net ${target.toLong()}",
        resultSummary = if (result.isAchievable) {
          "Basic ${result.requiredBasicSalary.toLong()}"
        } else {
          "Target not achievable within modeled range"
        }
      )
    }
  }

  // ---- Cost-of-living estimator -------------------------------------------

  fun updateCostOfLivingForm(transform: (CostOfLivingFormState) -> CostOfLivingFormState) {
    _state.value = _state.value.copy(costOfLivingForm = transform(_state.value.costOfLivingForm))
  }

  fun estimateCostOfLiving() {
    val s = _state.value
    viewModelScope.launch {
      val result = repository.estimateCostOfLiving(s.costOfLivingForm.toCostOfLivingInput())
      _state.value = _state.value.copy(costOfLivingResult = result)
      saveRecord(
        type = CalculationType.COST_OF_LIVING,
        title = "Cost-of-living estimate",
        inputSummary = "${s.costOfLivingForm.city.name}, ${s.costOfLivingForm.apartmentSize.name}",
        resultSummary = "Monthly SAR ${result.totalMonthlyRangeSar.first}-${result.totalMonthlyRangeSar.last}"
      )
    }
  }

  /** The "Use this as my target salary" hand-off: seeds the Reverse Salary Calculator's target
   * field from the current [CostOfLivingResult] before the caller navigates there, so the two
   * screens read as one flow (estimate a budget, then solve for the salary that covers it)
   * instead of requiring the user to re-type the number themselves. No-ops if no estimate has
   * been run yet. */
  fun useCostOfLivingResultAsReverseSalaryTarget() {
    val result = _state.value.costOfLivingResult ?: return
    updateReverseSalaryForm {
      it.copy(targetNetMonthlySalary = result.suggestedTargetNetSalarySar.toString())
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

  /** Switches which GOSI contribution track applies (see [GosiSystem]) and re-seeds [gosiRates]
   * from [GosiRateSchedule]'s current sourced default for that track. Persisted so the choice
   * survives app restarts; the resulting rates remain editable afterward like any other GOSI
   * rate. */
  fun setGosiSystem(system: GosiSystem) {
    _state.value = _state.value.copy(
      gosiSystem = system,
      gosiRates = GosiRateSchedule.defaultRatesFor(system)
    )
    viewModelScope.launch { repository.setGosiSystem(system) }
  }

  // ---- Payday countdown --------------------------------------------------

  fun setPaydayDayOfMonth(dayOfMonth: Int) {
    viewModelScope.launch { repository.setPaydayDayOfMonth(dayOfMonth) }
  }

  fun clearPaydayDayOfMonth() {
    viewModelScope.launch { repository.clearPaydayDayOfMonth() }
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
