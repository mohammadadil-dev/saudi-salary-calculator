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

data class GosiRates(
  val saudiEmployeeRate: Double = 0.0975,
  val saudiEmployerRate: Double = 0.1175,
  val expatEmployeeRate: Double = 0.0,
  val expatEmployerHazardRate: Double = 0.02
)

data class NetSalaryInput(
  val basicSalary: Double,
  val housingAllowance: Double,
  val transportAllowance: Double,
  val foodAllowance: Double,
  val mobileAllowance: Double,
  val otherAllowances: Double,
  val deductions: Double,
  val employeeType: EmployeeType = EmployeeType.SAUDI,
  val gosiRates: GosiRates = GosiRates()
)

data class NetSalaryResult(
  val grossSalary: Double,
  val employeeGosiAmount: Double,
  val employerGosiAmount: Double,
  val employerMonthlyCost: Double,
  val netSalary: Double,
  val yearlyGrossSalary: Double,
  val yearlyNetSalary: Double,
  val yearlyEmployerCost: Double,
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
  val gosiRates: GosiRates = GosiRates()
)

data class OfferComparisonResult(
  val offerA: OfferScore,
  val offerB: OfferScore,
  val betterOfferTitle: String,
  val monthlyDifference: Double,
  val yearlyDifference: Double,
  val acceptanceScore: Int,
  val scoreLabel: String
)

data class OfferScore(
  val title: String,
  val netMonthlySalary: Double,
  val totalYearlyCompensation: Double,
  val employerMonthlyCost: Double
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
  val createdAtMillis: Long
)
