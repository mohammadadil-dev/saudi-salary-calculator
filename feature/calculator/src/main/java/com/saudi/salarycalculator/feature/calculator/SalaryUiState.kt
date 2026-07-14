package com.saudi.salarycalculator.feature.calculator

import com.saudi.salarycalculator.core.model.CalculationRecord
import com.saudi.salarycalculator.core.model.CityCostOfLiving
import com.saudi.salarycalculator.core.model.ContractType
import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.EmploymentSector
import com.saudi.salarycalculator.core.model.ExpatCostInput
import com.saudi.salarycalculator.core.model.ExpatCostResult
import com.saudi.salarycalculator.core.model.ExpatCostSchedule
import com.saudi.salarycalculator.core.model.GosiRates
import com.saudi.salarycalculator.core.model.GosiSystem
import com.saudi.salarycalculator.core.model.NetSalaryInput
import com.saudi.salarycalculator.core.model.NetSalaryResult
import com.saudi.salarycalculator.core.model.OfferComparisonResult
import com.saudi.salarycalculator.core.model.OfferInput
import com.saudi.salarycalculator.core.model.SaudiCityTier

/** Six steps of the calculator wizard, in display order. The Review step doubles as the
 * "Calculate" trigger; the Result screen itself is a separate nav destination. */
enum class WizardStep {
  BASIC_SALARY,
  ALLOWANCES,
  DEDUCTIONS,
  EMPLOYMENT_DETAILS,
  GOSI_EOSB,
  REVIEW
}

/** Raw, string-backed form state for the wizard. Kept as [String] (not [Double]) so text fields
 * can hold transient/partial input (e.g. "12." while typing) without losing the user's place;
 * conversion to the domain [NetSalaryInput] happens only at calculate-time via [toNetSalaryInput]. */
data class WizardFieldsState(
  val employeeName: String = "",
  val jobTitle: String = "",
  val basicSalary: String = "",
  val employeeType: EmployeeType = EmployeeType.SAUDI,
  val housingAllowance: String = "",
  val transportAllowance: String = "",
  val foodAllowance: String = "",
  val mobileAllowance: String = "",
  val otherAllowances: String = "",
  val bonus: String = "",
  val commission: String = "",
  val overtimeHours: String = "",
  val overtimeHourlyRateOverride: String = "",
  val loanDeduction: String = "",
  val absenceDeduction: String = "",
  val unpaidLeaveDays: String = "",
  val otherDeductions: String = "",
  val employmentSector: EmploymentSector = EmploymentSector.PRIVATE,
  val contractType: ContractType = ContractType.UNLIMITED,
  val joiningDateMillis: Long? = null,
  val calculationMonthMillis: Long? = null,
  val gosiIncluded: Boolean = true,
  val resigned: Boolean = false,
  val ramadanReducedHours: Boolean = false
) {
  fun toNetSalaryInput(): NetSalaryInput = NetSalaryInput(
    employeeName = employeeName,
    jobTitle = jobTitle,
    basicSalary = basicSalary.toDoubleOrNull() ?: 0.0,
    housingAllowance = housingAllowance.toDoubleOrNull() ?: 0.0,
    transportAllowance = transportAllowance.toDoubleOrNull() ?: 0.0,
    foodAllowance = foodAllowance.toDoubleOrNull() ?: 0.0,
    mobileAllowance = mobileAllowance.toDoubleOrNull() ?: 0.0,
    otherAllowances = otherAllowances.toDoubleOrNull() ?: 0.0,
    bonus = bonus.toDoubleOrNull() ?: 0.0,
    commission = commission.toDoubleOrNull() ?: 0.0,
    overtimeHours = overtimeHours.toDoubleOrNull() ?: 0.0,
    overtimeHourlyRateOverride = overtimeHourlyRateOverride.toDoubleOrNull(),
    loanDeduction = loanDeduction.toDoubleOrNull() ?: 0.0,
    absenceDeduction = absenceDeduction.toDoubleOrNull() ?: 0.0,
    unpaidLeaveDays = unpaidLeaveDays.toDoubleOrNull() ?: 0.0,
    deductions = otherDeductions.toDoubleOrNull() ?: 0.0,
    employeeType = employeeType,
    employmentSector = employmentSector,
    contractType = contractType,
    gosiIncluded = gosiIncluded,
    resigned = resigned,
    joiningDateMillis = joiningDateMillis,
    calculationMonthMillis = calculationMonthMillis,
    ramadanReducedHours = ramadanReducedHours
  )
}

/** Reverse of [WizardFieldsState.toNetSalaryInput] — rebuilds editable wizard text fields from a
 * persisted [NetSalaryInput] snapshot so a history record can be reopened into the wizard.
 * Optional numeric fields that are exactly 0 are rendered blank (matching how an untouched wizard
 * field looks) rather than as a literal "0". */
fun NetSalaryInput.toWizardFieldsState(): WizardFieldsState {
  fun Double.required() = if (this == toLong().toDouble()) toLong().toString() else toString()
  fun Double.optional() = if (this == 0.0) "" else required()
  fun Double?.optional() = this?.let { if (it == 0.0) "" else it.required() } ?: ""

  return WizardFieldsState(
    employeeName = employeeName,
    jobTitle = jobTitle,
    basicSalary = basicSalary.required(),
    employeeType = employeeType,
    housingAllowance = housingAllowance.optional(),
    transportAllowance = transportAllowance.optional(),
    foodAllowance = foodAllowance.optional(),
    mobileAllowance = mobileAllowance.optional(),
    otherAllowances = otherAllowances.optional(),
    bonus = bonus.optional(),
    commission = commission.optional(),
    overtimeHours = overtimeHours.optional(),
    overtimeHourlyRateOverride = overtimeHourlyRateOverride.optional(),
    loanDeduction = loanDeduction.optional(),
    absenceDeduction = absenceDeduction.optional(),
    unpaidLeaveDays = unpaidLeaveDays.optional(),
    otherDeductions = deductions.optional(),
    employmentSector = employmentSector,
    contractType = contractType,
    joiningDateMillis = joiningDateMillis,
    calculationMonthMillis = calculationMonthMillis,
    gosiIncluded = gosiIncluded,
    resigned = resigned,
    ramadanReducedHours = ramadanReducedHours
  )
}

/** One side of the Offer Comparison screen. [title] defaults differ for the two sides so the
 * UI can pre-fill "Current offer" / "New offer" without extra plumbing. [city] is purely
 * informational context (see [CityCostOfLiving]) — it's never sent through [toOfferInput] since
 * it doesn't affect the actual GOSI/net-salary calculation, only what reference info is shown
 * alongside this offer. */
data class OfferFormState(
  val title: String = "",
  val basicSalary: String = "",
  val housingAllowance: String = "",
  val transportAllowance: String = "",
  val foodAllowance: String = "",
  val mobileAllowance: String = "",
  val otherAllowances: String = "",
  val deductions: String = "",
  val employeeType: EmployeeType = EmployeeType.SAUDI,
  val city: SaudiCityTier? = null
) {
  fun toOfferInput(): OfferInput = OfferInput(
    title = title,
    basicSalary = basicSalary.toDoubleOrNull() ?: 0.0,
    housingAllowance = housingAllowance.toDoubleOrNull() ?: 0.0,
    transportAllowance = transportAllowance.toDoubleOrNull() ?: 0.0,
    foodAllowance = foodAllowance.toDoubleOrNull() ?: 0.0,
    mobileAllowance = mobileAllowance.toDoubleOrNull() ?: 0.0,
    otherAllowances = otherAllowances.toDoubleOrNull() ?: 0.0,
    deductions = deductions.toDoubleOrNull() ?: 0.0,
    employeeType = employeeType
  )
}

/** Form state for the expat residency-cost estimator (see [ExpatCostSchedule] for the sourced
 * defaults). String-backed money/rate fields follow the same convention as [WizardFieldsState]
 * so text entry can hold partial input without losing the user's place. */
data class ExpatCostFormState(
  val dependentCount: Int = 0,
  val dependentMonthlyLevy: String = ExpatCostSchedule.DEPENDENT_MONTHLY_LEVY_SAR.toLong().toString(),
  val includeOwnIqamaRenewal: Boolean = false,
  val ownIqamaRenewalFee: String = ExpatCostSchedule.OWN_IQAMA_RENEWAL_FEE_SAR.toLong().toString(),
  val exitReentryTripsPerYear: Int = 0,
  /** false = single-trip visa (SAR 200), true = multiple-trip visa (SAR 500). */
  val exitReentryMultipleTrip: Boolean = false,
  val monthlyHealthInsurancePremium: String = ""
) {
  val exitReentryFeePerTrip: Double
    get() = if (exitReentryMultipleTrip) {
      ExpatCostSchedule.EXIT_REENTRY_MULTIPLE_FEE_SAR
    } else {
      ExpatCostSchedule.EXIT_REENTRY_SINGLE_FEE_SAR
    }

  fun toExpatCostInput(): ExpatCostInput = ExpatCostInput(
    dependentCount = dependentCount,
    dependentMonthlyLevySar = dependentMonthlyLevy.toDoubleOrNull() ?: ExpatCostSchedule.DEPENDENT_MONTHLY_LEVY_SAR,
    includeOwnIqamaRenewal = includeOwnIqamaRenewal,
    ownIqamaRenewalFeeSar = ownIqamaRenewalFee.toDoubleOrNull() ?: ExpatCostSchedule.OWN_IQAMA_RENEWAL_FEE_SAR,
    exitReentryTripsPerYear = exitReentryTripsPerYear,
    exitReentryFeePerTripSar = exitReentryFeePerTrip,
    monthlyHealthInsurancePremiumSar = monthlyHealthInsurancePremium.toDoubleOrNull() ?: 0.0
  )
}

data class SalaryUiState(
  val darkMode: Boolean = false,
  val language: String = "en",
  val gosiRates: GosiRates = GosiRates(),
  val gosiSystem: GosiSystem = GosiSystem.NEW,
  val wizard: WizardFieldsState = WizardFieldsState(),
  val wizardStep: WizardStep = WizardStep.BASIC_SALARY,
  val isCalculating: Boolean = false,
  val netSalaryResult: NetSalaryResult? = null,
  val lastCalculatedAtMillis: Long? = null,
  val offerCurrent: OfferFormState = OfferFormState(title = "Current offer"),
  val offerNew: OfferFormState = OfferFormState(title = "New offer"),
  val offerComparisonResult: OfferComparisonResult? = null,
  val expatCostForm: ExpatCostFormState = ExpatCostFormState(),
  val expatCostResult: ExpatCostResult? = null,
  val history: List<CalculationRecord> = emptyList(),
  val toastMessage: String? = null
) {
  val wizardStepIndex: Int get() = WizardStep.entries.indexOf(wizardStep)
  val wizardStepCount: Int get() = WizardStep.entries.size
}
