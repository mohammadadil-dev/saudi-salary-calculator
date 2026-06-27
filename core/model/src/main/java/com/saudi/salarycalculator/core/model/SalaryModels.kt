package com.saudi.salarycalculator.core.model

import java.util.UUID

enum class CalculationType {
  NET_SALARY,
  GOSI,
  OVERTIME,
  END_OF_SERVICE,
  OFFER_COMPARISON,
  SAVINGS_PLAN,
  PDF_REPORT
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

data class GosiRates(
  val saudiEmployeeRate: Double = 0.0975,
  val saudiEmployerRate: Double = 0.1175,
  val expatEmployeeRate: Double = 0.0,
  val expatEmployerHazardRate: Double = 0.02
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
  val gosiRates: GosiRates = GosiRates()
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
