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
  EXPAT_COSTS
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
