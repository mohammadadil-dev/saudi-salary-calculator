package com.saudi.salarycalculator.core.model

import java.util.UUID

enum class CalculationType {
  NET_SALARY,
  GOSI,
  OVERTIME,
  END_OF_SERVICE,
  OFFER_COMPARISON,
  SAVINGS_PLAN,
  PDF_REPORT,
  EXPAT_COSTS,
  OFFER_RED_FLAG_SCAN,
  REVERSE_SALARY,
  COST_OF_LIVING
}

enum class EmployeeType {
  SAUDI,
  EXPAT
}

enum class EmploymentSector {
  PRIVATE,
  GOVERNMENT
}

enum class ContractType {
  LIMITED,
  UNLIMITED
}

/** Which GOSI contribution-rate track a Saudi employee falls under, per the Social Insurance Law
 * (Royal Decree, effective 3 July 2024). This is a factual attribute of the employee (when they
 * were first registered with GOSI), not a preference — see [GosiRateSchedule] for the sourced
 * rate numbers each track maps to. */
enum class GosiSystem {
  /** First registered with GOSI before 3 July 2024. Stays on the flat pre-reform rate. */
  EXISTING,
  /** First registered with GOSI on/after 3 July 2024. Rate rises in scheduled annual steps. */
  NEW
}

/** Defaults reflect the "New system" current rate (see [GosiRateSchedule]) since that applies to
 * anyone newly registered with GOSI; [GosiRateSchedule.defaultRatesFor] should be used to seed
 * the correct rates once the employee's actual [GosiSystem] is known. All fields remain
 * user-editable in Settings since exact figures should always be confirmed against gosi.gov.sa. */
data class GosiRates(
  val saudiEmployeeRate: Double = 0.1075,
  val saudiEmployerRate: Double = 0.1275,
  val expatEmployeeRate: Double = 0.0,
  val expatEmployerHazardRate: Double = 0.02,
  /** Monthly wage base (basic + housing) above which GOSI contributions are no longer assessed. */
  val contributionCapSar: Double = 45000.0
)

/**
 * Full set of inputs collected across the step-by-step calculator wizard
 * (Basic Salary -> Allowances -> Deductions -> Employment Details -> GOSI & EOSB -> Review).
 */
data class NetSalaryInput(
  val employeeName: String = "",
  val jobTitle: String = "",
  val basicSalary: Double,
  val housingAllowance: Double = 0.0,
  val transportAllowance: Double = 0.0,
  val foodAllowance: Double = 0.0,
  val mobileAllowance: Double = 0.0,
  val otherAllowances: Double = 0.0,
  val bonus: Double = 0.0,
  val commission: Double = 0.0,
  val overtimeHours: Double = 0.0,
  val overtimeHourlyRateOverride: Double? = null,
  val loanDeduction: Double = 0.0,
  val absenceDeduction: Double = 0.0,
  val unpaidLeaveDays: Double = 0.0,
  val deductions: Double = 0.0,
  val employeeType: EmployeeType = EmployeeType.SAUDI,
  val employmentSector: EmploymentSector = EmploymentSector.PRIVATE,
  val contractType: ContractType = ContractType.UNLIMITED,
  val gosiIncluded: Boolean = true,
  val resigned: Boolean = false,
  val joiningDateMillis: Long? = null,
  val calculationMonthMillis: Long? = null,
  val gosiRates: GosiRates = GosiRates(),
  /** Whether this calculation should use the Saudi Labor Law Article 98 reduced Ramadan working
   * hours (6 hrs/day / 36 hrs/week for fasting Muslim employees) instead of the standard 8 hrs/day
   * baseline when deriving the hourly rate for overtime pay. Left to the user to set (see
   * [WorkingHoursSchedule]) rather than auto-detected from [calculationMonthMillis], since exact
   * Ramadan start/end dates are only confirmed close to the time by moon sighting. */
  val ramadanReducedHours: Boolean = false
)

data class NetSalaryResult(
  val grossSalary: Double,
  val totalAllowances: Double,
  val totalEarningsAddOns: Double,
  val overtimePay: Double,
  val totalDeductions: Double,
  val employeeGosiAmount: Double,
  val employerGosiAmount: Double,
  val employerMonthlyCost: Double,
  val netSalary: Double,
  val yearlyGrossSalary: Double,
  val yearlyNetSalary: Double,
  val yearlyEmployerCost: Double,
  val estimatedEosb: Double,
  val yearsOfService: Double,
  val smartSummary: String,
  val breakdown: List<SalaryBreakdownItem>
)

data class GosiInput(
  val baseAmount: Double,
  val employeeType: EmployeeType = EmployeeType.SAUDI,
  val rates: GosiRates = GosiRates()
)

data class GosiResult(
  val employeeContribution: Double,
  val employerContribution: Double,
  val totalContribution: Double
)

data class OvertimeInput(
  val monthlySalary: Double,
  val overtimeHours: Double,
  val overtimeMultiplier: Double = 1.5,
  val monthlyWorkingHours: Double = 240.0
)

data class OvertimeResult(
  val hourlyRate: Double,
  val overtimeRate: Double,
  val overtimePay: Double
)

data class EndOfServiceInput(
  val lastBasicSalary: Double,
  val yearsOfService: Double,
  val resigned: Boolean
)

data class EndOfServiceResult(
  val rewardAmount: Double,
  val eligibleYears: Double
)

data class OfferInput(
  val title: String,
  val basicSalary: Double,
  val housingAllowance: Double,
  val transportAllowance: Double,
  val foodAllowance: Double,
  val mobileAllowance: Double,
  val otherAllowances: Double,
  val deductions: Double,
  val employeeType: EmployeeType = EmployeeType.SAUDI,
  val gosiRates: GosiRates = GosiRates(),
  val yearsOfService: Double = 0.0,
  val resigned: Boolean = false
)

data class OfferComparisonResult(
  val offerA: OfferScore,
  val offerB: OfferScore,
  val betterOfferTitle: String,
  val monthlyDifference: Double,
  val yearlyDifference: Double,
  val percentageIncrease: Double,
  val gosiMonthlyDifference: Double,
  val eosbDifference: Double,
  val acceptanceScore: Int,
  val scoreLabel: String
)

data class OfferScore(
  val title: String,
  val netMonthlySalary: Double,
  val totalYearlyCompensation: Double,
  val employerMonthlyCost: Double,
  val employeeGosiMonthly: Double,
  val estimatedEosb: Double
)

data class SavingsInput(
  val netSalary: Double,
  val rent: Double,
  val food: Double,
  val transport: Double,
  val familyExpense: Double,
  val otherExpense: Double
)

data class SavingsResult(
  val totalExpenses: Double,
  val monthlySavings: Double,
  val yearlySavings: Double,
  val savingsRate: Double
)

/**
 * Recurring residency-related costs a non-Saudi employee personally carries, distinct from the
 * employer's own iqama/work-permit levy obligations (Saudi Labor Law Article 40 makes the
 * employee's own iqama renewal and work-permit fees the employer's legal responsibility — this
 * tool intentionally does NOT bundle those employer-side costs into the total, since presenting
 * them as "your cost" could normalize unlawfully passing them onto the employee). See
 * [ExpatCostSchedule] for sourced default fee amounts.
 */
data class ExpatCostInput(
  /** Family members (spouse, children, other sponsored dependents) whose residency the employee
   * personally sponsors and pays the monthly dependent levy for. */
  val dependentCount: Int = 0,
  val dependentMonthlyLevySar: Double = 400.0,
  /** Off by default: only include this if the employee is actually the one paying it, which per
   * Article 40 shouldn't be the normal case — see the class doc above. */
  val includeOwnIqamaRenewal: Boolean = false,
  val ownIqamaRenewalFeeSar: Double = 650.0,
  val exitReentryTripsPerYear: Int = 0,
  val exitReentryFeePerTripSar: Double = 200.0,
  /** Health insurance is market-priced by insurers, not government-fixed, so this has no sourced
   * default — left at 0 until the user enters their own premium. */
  val monthlyHealthInsurancePremiumSar: Double = 0.0
)

data class ExpatCostResult(
  val monthlyDependentLevy: Double,
  val monthlyOwnIqamaRenewal: Double,
  val monthlyExitReentry: Double,
  val monthlyHealthInsurance: Double,
  val totalMonthlyCost: Double,
  val totalYearlyCost: Double
)

data class SalaryBreakdownItem(
  val label: String,
  val amount: Double,
  val colorHex: Long,
  val isDeduction: Boolean = false
)

data class CalculationRecord(
  val id: String = UUID.randomUUID().toString(),
  val type: CalculationType,
  val title: String,
  val inputSummary: String,
  val resultSummary: String,
  val createdAtMillis: Long,
  /** Structured snapshot of the wizard inputs that produced this record, present only for
   * [CalculationType.NET_SALARY]. Lets the history UI reopen the wizard pre-filled for editing
   * instead of only offering delete. */
  val netSalaryInput: NetSalaryInput? = null
)

// ---- EOSB accrual tracker -----------------------------------------------------------------
// Distinct from the one-shot wizard's EOSB estimate (see NetSalaryResult.estimatedEosb /
// EndOfServiceInput above): this is a standing, persisted "my employment" profile — joining date
// plus last basic salary — that the Home tool re-reads live against *today's* date every time the
// tracker screen is opened, rather than a snapshot taken once at calculation time. It reuses
// EndOfServiceCalculatorService for the actual reward math so both features share one
// source of truth for the Article 84-85 formula.

/** Persisted profile backing the tracker. Both fields are null until the user completes setup;
 * [UserPreferencesStore] is the source of truth, not [CalculationRecord] history, since this is a
 * standing fact about the user's job rather than a one-off calculation to log. */
data class EosbTrackerProfile(
  val joiningDateMillis: Long? = null,
  val lastBasicSalary: Double? = null
)

data class EosbTrackerInput(
  val joiningDateMillis: Long,
  val lastBasicSalary: Double,
  /** Defaults to now; overridable for testing/preview so results are reproducible. */
  val asOfMillis: Long = System.currentTimeMillis()
)

/** Mirrors the resignation fraction ladder in [DefaultEndOfServiceCalculatorService] (Article 85):
 * NONE below 2 years, rising to FULL at 10+. Kept as its own enum (rather than reusing a raw
 * fraction) so the UI can render a plain-language label without re-deriving the thresholds. */
enum class EosbResignationEligibility {
  NONE, ONE_THIRD, TWO_THIRDS, FULL
}

/** The next service-length cliff ahead of the employee, where either the accrual rate changes or
 * the resignation fraction jumps. Null once past the 10-year mark, since Article 85's ladder tops
 * out at "full award" and there's no further threshold to count down to. */
data class EosbMilestone(
  val yearsThreshold: Double,
  val dateMillis: Long,
  val daysRemaining: Long,
  /** Resource-key-style tag (e.g. "2_YEAR", "5_YEAR", "10_YEAR") the UI maps to a localized
   * label + description, rather than baking English text into this data-layer model. */
  val milestoneKey: String
)

data class EosbTrackerResult(
  val yearsOfService: Double,
  val accruedIfTerminated: Double,
  val accruedIfResignedToday: Double,
  val resignationEligibility: EosbResignationEligibility,
  val nextMilestone: EosbMilestone?
)

/** Input for the home-screen "days until payday" widget/countdown. [dayOfMonth] is the day the
 * user gets paid (1-31); for months shorter than that (e.g. 31 in February), the payday clamps to
 * the last day of that month rather than rolling into the next one — see
 * [DefaultPaydayCalculatorService]. */
data class PaydayInput(
  val dayOfMonth: Int,
  /** Defaults to now; overridable for testing/preview so results are reproducible. */
  val asOfMillis: Long = System.currentTimeMillis()
)

data class PaydayResult(
  val nextPaydayMillis: Long,
  /** 0 means today is payday. */
  val daysRemaining: Long
)

/** Standing "my leave balance" profile for the leave-balance tracker (mirrors [EosbTrackerProfile]
 * — a persisted fact about the employee's own job, re-derived against "now" each time the tracker
 * screen opens, rather than a one-off wizard snapshot). */
data class LeaveTrackerProfile(
  val joiningDateMillis: Long? = null,
  val daysTakenThisYear: Int? = null
)

data class LeaveTrackerInput(
  val joiningDateMillis: Long,
  val daysTakenThisYear: Int,
  /** Defaults to now; overridable for testing/preview so results are reproducible. */
  val asOfMillis: Long = System.currentTimeMillis()
)

/** [currentLeaveYearStartMillis] is the most recent anniversary of [LeaveTrackerInput.joiningDateMillis]
 * at or before [LeaveTrackerInput.asOfMillis] — the start of the employee's current "leave year"
 * under Saudi Labor Law Article 109, which measures annual leave against service anniversaries
 * rather than the calendar year. */
data class LeaveTrackerResult(
  val yearsOfService: Double,
  /** 21 or 30 (Article 109's 5-year cliff — the same threshold [EndOfServiceCalculatorService]
   * uses for the EOSB accrual rate). */
  val annualEntitlementDays: Int,
  val currentLeaveYearStartMillis: Long,
  val daysAccruedSoFarThisYear: Double,
  val daysTakenThisYear: Int,
  /** Accrued minus taken; can go negative if more leave was taken than has accrued so far. */
  val daysRemainingBalance: Double,
  val nextAnniversaryMillis: Long,
  val daysUntilNextAnniversary: Long
)


/** Inputs for the offer red-flag scanner: a handful of contract terms the user reports from a
 * job offer, checked against Saudi Labor Law norms and common expat-hiring risk patterns. Purely
 * a client-side screening heuristic against user-entered numbers/booleans — nothing here is
 * personal data, and nothing leaves the device (see [DefaultOfferRedFlagCalculatorService]). */
data class OfferRedFlagInput(
  val basicSalary: Double,
  /** Total monthly salary including all allowances — used only to compute [basicSalary]'s share
   * of the total, since EOSB/GOSI are commonly calculated on basic (+ housing), not gross pay. */
  val totalMonthlySalary: Double,
  val probationMonths: Int,
  /** Only meaningful when [probationMonths] exceeds the 90-day statutory default. */
  val probationExtendedInWriting: Boolean,
  val employeeNoticeDays: Int,
  val employerNoticeDays: Int,
  val isGosiRegistered: Boolean,
  val hasWrittenContract: Boolean,
  val recruitmentFeesCharged: Boolean
)

/** How seriously a given [OfferRedFlagFinding] should be treated. VIOLATION means the reported
 * term appears to conflict with a specific Labor Law provision; CAUTION means it's legal but
 * risky or unusual and worth clarifying before signing; INFO is a structural fact worth knowing
 * that isn't a violation on its own. This is a screening heuristic, not legal advice — every
 * surfaced result is paired with a disclaimer pointing the user to MOL/a lawyer for anything that
 * actually matters to them. */
enum class OfferFlagSeverity { VIOLATION, CAUTION, INFO }

/** Resource-key-style tag (mirrors [EosbMilestone.milestoneKey]) the UI maps to a localized
 * title + explanation, rather than baking English text into this data-layer model. */
enum class OfferFlagType {
  /** Article 53 caps probation at 90 days, extendable once to 180 days only by a distinct
   * written agreement. Reported probation beyond 180 days has no legal basis at all. */
  PROBATION_EXCEEDS_MAX,
  /** Reported probation is 91-180 days but the user didn't confirm a separate written extension
   * agreement exists — the default 90-day cap otherwise applies. */
  PROBATION_EXTENSION_NOT_WRITTEN,
  /** GOSI registration (at minimum the occupational-hazards branch) is mandatory for private
   * sector employees regardless of nationality. */
  GOSI_NOT_REGISTERED,
  /** No signed/Qiwa-authenticated contract — leaves the employee with nothing to fall back on
   * if the employer later disputes the agreed terms. */
  NO_WRITTEN_CONTRACT,
  /** Recruitment, visa, and hiring costs are ordinarily the employer's responsibility, not the
   * worker's. */
  RECRUITMENT_FEES_CHARGED,
  /** The employee would owe substantially more notice than the employer — a one-sided term
   * worth negotiating before signing. */
  ASYMMETRIC_NOTICE_PERIOD,
  /** Basic salary is less than half of total monthly pay. Not a violation, but since EOSB and
   * GOSI are commonly calculated on basic (+ housing) rather than gross pay, a low basic can
   * shrink those benefits even when take-home pay looks competitive. */
  LOW_BASIC_RATIO,
  /** None of the above triggered — shown as a positive result, not absence of findings. */
  ALL_CLEAR
}

data class OfferRedFlagFinding(
  val type: OfferFlagType,
  val severity: OfferFlagSeverity
)

data class OfferRedFlagResult(
  val findings: List<OfferRedFlagFinding>,
  val violationCount: Int,
  val cautionCount: Int
)


/** Input for "what salary do I need" — the reverse of [NetSalaryInput]: given a desired net
 * take-home, solves for the basic salary (and resulting package) that would produce it. Housing
 * and transport allowances are assumed to scale with basic salary at the given percentages (a
 * common Saudi compensation structure) rather than being independently specified — keeps the
 * form to one number and two adjustable assumptions instead of the full wizard. */
data class ReverseSalaryInput(
  val targetNetMonthlySalary: Double,
  val housingAllowancePercent: Int = 25,
  val transportAllowancePercent: Int = 10,
  val employeeType: EmployeeType = EmployeeType.SAUDI,
  val gosiRates: GosiRates = GosiRates()
)

data class ReverseSalaryResult(
  val requiredBasicSalary: Double,
  val requiredHousingAllowance: Double,
  val requiredTransportAllowance: Double,
  val requiredTotalMonthlyPackage: Double,
  /** What plugging [requiredBasicSalary] back through the real net-salary formula actually
   * yields — should equal the requested target to within a rounding cent when [isAchievable]. */
  val achievedNetSalary: Double,
  /** False when even a very high basic salary can't reach the target net pay within the modeled
   * search range — surfaced so the UI can show a warning instead of a silently wrong number. */
  val isAchievable: Boolean
)


/** Input for the cost-of-living estimator: a rough monthly household budget check for
 * expatriates weighing a job offer, built entirely from [CostOfLivingSchedule]'s sourced
 * reference ranges. Deliberately simple (city + apartment size + household composition) rather
 * than itemized line-by-line, since an itemized form would imply a precision the underlying
 * market data doesn't support. */
data class CostOfLivingInput(
  val city: SaudiCityTier = SaudiCityTier.RIYADH,
  val apartmentSize: ApartmentSize = ApartmentSize.TWO_BR,
  /** Additional adults sharing the household beyond the person filling out the form (e.g. a
   * spouse) — does not include the user themselves, who is always counted as the first adult. */
  val additionalAdults: Int = 0,
  val childrenCount: Int = 0,
  /** How many of [childrenCount] attend a private/international school; the calculator clamps
   * this to [childrenCount] so it can never exceed the reported number of children. */
  val childrenInSchool: Int = 0,
  val schoolFeeTier: SchoolFeeTier = SchoolFeeTier.MID_RANGE
)

data class CostOfLivingResult(
  val rentRangeSar: IntRange,
  val nonRentRangeSar: IntRange,
  val monthlySchoolFeeRangeSar: IntRange,
  val totalMonthlyRangeSar: IntRange,
  /** Top of [totalMonthlyRangeSar] — a single, deliberately conservative "aim for at least this
   * much net pay" figure, used to hand off into [ReverseSalaryInput.targetNetMonthlySalary] on
   * the Reverse Salary Calculator (which needs one number, not a range). */
  val suggestedTargetNetSalarySar: Int
)
