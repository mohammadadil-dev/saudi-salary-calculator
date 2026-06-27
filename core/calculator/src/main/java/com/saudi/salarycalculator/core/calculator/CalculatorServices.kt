package com.saudi.salarycalculator.core.calculator

import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.EndOfServiceInput
import com.saudi.salarycalculator.core.model.EndOfServiceResult
import com.saudi.salarycalculator.core.model.GosiInput
import com.saudi.salarycalculator.core.model.GosiResult
import com.saudi.salarycalculator.core.model.NetSalaryInput
import com.saudi.salarycalculator.core.model.NetSalaryResult
import com.saudi.salarycalculator.core.model.OfferComparisonResult
import com.saudi.salarycalculator.core.model.OfferInput
import com.saudi.salarycalculator.core.model.OfferScore
import com.saudi.salarycalculator.core.model.OvertimeInput
import com.saudi.salarycalculator.core.model.OvertimeResult
import com.saudi.salarycalculator.core.model.SalaryBreakdownItem
import com.saudi.salarycalculator.core.model.SavingsInput
import com.saudi.salarycalculator.core.model.SavingsResult
import kotlin.math.roundToInt
import kotlin.math.roundToLong

interface NetSalaryCalculatorService {
  fun calculate(input: NetSalaryInput): NetSalaryResult
}

interface GosiCalculatorService {
  fun calculate(input: GosiInput): GosiResult
}

interface OvertimeCalculatorService {
  fun calculate(input: OvertimeInput): OvertimeResult
}

interface EndOfServiceCalculatorService {
  fun calculate(input: EndOfServiceInput): EndOfServiceResult
}

interface OfferComparisonCalculatorService {
  fun calculate(offerA: OfferInput, offerB: OfferInput): OfferComparisonResult
}

interface SavingsCalculatorService {
  fun calculate(input: SavingsInput): SavingsResult
}

class DefaultNetSalaryCalculatorService : NetSalaryCalculatorService {
  override fun calculate(input: NetSalaryInput): NetSalaryResult {
    val gross = input.basicSalary +
      input.housingAllowance +
      input.transportAllowance +
      input.foodAllowance +
      input.mobileAllowance +
      input.otherAllowances

    val gosiInput = GosiInput(
      baseAmount = input.basicSalary + input.housingAllowance,
      employeeType = input.employeeType,
      rates = input.gosiRates
    )
    val gosi = DefaultGosiCalculatorService().calculate(gosiInput)
    val net = gross - gosi.employeeContribution - input.deductions
    val employerCost = gross + gosi.employerContribution

    return NetSalaryResult(
      grossSalary = gross.toCurrency(),
      employeeGosiAmount = gosi.employeeContribution,
      employerGosiAmount = gosi.employerContribution,
      employerMonthlyCost = employerCost.toCurrency(),
      netSalary = net.toCurrency(),
      yearlyGrossSalary = (gross * 12.0).toCurrency(),
      yearlyNetSalary = (net * 12.0).toCurrency(),
      yearlyEmployerCost = (employerCost * 12.0).toCurrency(),
      smartSummary = "Your monthly take-home salary is SAR ${net.toCurrency().formatSar()}",
      breakdown = listOf(
        SalaryBreakdownItem("Basic", input.basicSalary.toCurrency(), 0xFF2563EB),
        SalaryBreakdownItem("Housing", input.housingAllowance.toCurrency(), 0xFFFF8A3D),
        SalaryBreakdownItem("Transport", input.transportAllowance.toCurrency(), 0xFF14B8A6),
        SalaryBreakdownItem("Food", input.foodAllowance.toCurrency(), 0xFFEAB308),
        SalaryBreakdownItem("Mobile", input.mobileAllowance.toCurrency(), 0xFF7C3AED),
        SalaryBreakdownItem("Other", input.otherAllowances.toCurrency(), 0xFF0EA5E9),
        SalaryBreakdownItem("GOSI", gosi.employeeContribution, 0xFFEF4444, isDeduction = true),
        SalaryBreakdownItem("Deductions", input.deductions.toCurrency(), 0xFFB91C1C, isDeduction = true)
      )
    )
  }
}

class DefaultGosiCalculatorService : GosiCalculatorService {
  override fun calculate(input: GosiInput): GosiResult {
    val employeeRate = when (input.employeeType) {
      EmployeeType.SAUDI -> input.rates.saudiEmployeeRate
      EmployeeType.EXPAT -> input.rates.expatEmployeeRate
    }
    val employerRate = when (input.employeeType) {
      EmployeeType.SAUDI -> input.rates.saudiEmployerRate
      EmployeeType.EXPAT -> input.rates.expatEmployerHazardRate
    }
    val employee = (input.baseAmount * employeeRate).toCurrency()
    val employer = (input.baseAmount * employerRate).toCurrency()
    return GosiResult(
      employeeContribution = employee,
      employerContribution = employer,
      totalContribution = (employee + employer).toCurrency()
    )
  }
}

class DefaultOvertimeCalculatorService : OvertimeCalculatorService {
  override fun calculate(input: OvertimeInput): OvertimeResult {
    val hourlyRate = input.monthlySalary / input.monthlyWorkingHours.coerceAtLeast(1.0)
    val overtimeRate = hourlyRate * input.overtimeMultiplier
    val overtimePay = overtimeRate * input.overtimeHours
    return OvertimeResult(
      hourlyRate = hourlyRate.toCurrency(),
      overtimeRate = overtimeRate.toCurrency(),
      overtimePay = overtimePay.toCurrency()
    )
  }
}

class DefaultEndOfServiceCalculatorService : EndOfServiceCalculatorService {
  override fun calculate(input: EndOfServiceInput): EndOfServiceResult {
    val firstFiveYears = minOf(input.yearsOfService, 5.0)
    val remainingYears = maxOf(input.yearsOfService - 5.0, 0.0)
    val fullReward = (firstFiveYears * 0.5 * input.lastBasicSalary) +
      (remainingYears * input.lastBasicSalary)
    val adjusted = if (!input.resigned) {
      fullReward
    } else {
      when {
        input.yearsOfService < 2.0 -> 0.0
        input.yearsOfService < 5.0 -> fullReward / 3.0
        input.yearsOfService < 10.0 -> (fullReward * 2.0) / 3.0
        else -> fullReward
      }
    }
    return EndOfServiceResult(
      rewardAmount = adjusted.toCurrency(),
      eligibleYears = input.yearsOfService
    )
  }
}

class DefaultOfferComparisonCalculatorService(
  private val netSalaryCalculatorService: NetSalaryCalculatorService
) : OfferComparisonCalculatorService {
  override fun calculate(offerA: OfferInput, offerB: OfferInput): OfferComparisonResult {
    val scoreA = offerA.toScore(netSalaryCalculatorService)
    val scoreB = offerB.toScore(netSalaryCalculatorService)
    val better = if (scoreB.netMonthlySalary >= scoreA.netMonthlySalary) scoreB else scoreA
    val diffMonthly = (scoreB.netMonthlySalary - scoreA.netMonthlySalary).toCurrency()
    val acceptanceScore = ((scoreB.netMonthlySalary / scoreA.netMonthlySalary.coerceAtLeast(1.0)) * 65.0)
      .coerceIn(0.0, 100.0)
      .roundToInt()

    return OfferComparisonResult(
      offerA = scoreA,
      offerB = scoreB,
      betterOfferTitle = better.title,
      monthlyDifference = diffMonthly,
      yearlyDifference = (diffMonthly * 12.0).toCurrency(),
      acceptanceScore = acceptanceScore,
      scoreLabel = when {
        acceptanceScore >= 85 -> "Strong offer"
        acceptanceScore >= 70 -> "Worth considering"
        acceptanceScore >= 55 -> "Neutral"
        else -> "Weak offer"
      }
    )
  }
}

class DefaultSavingsCalculatorService : SavingsCalculatorService {
  override fun calculate(input: SavingsInput): SavingsResult {
    val expenses = input.rent + input.food + input.transport + input.familyExpense + input.otherExpense
    val savings = input.netSalary - expenses
    return SavingsResult(
      totalExpenses = expenses.toCurrency(),
      monthlySavings = savings.toCurrency(),
      yearlySavings = (savings * 12.0).toCurrency(),
      savingsRate = if (input.netSalary <= 0.0) 0.0 else ((savings / input.netSalary) * 100.0).toCurrency()
    )
  }
}

private fun OfferInput.toScore(netSalaryCalculatorService: NetSalaryCalculatorService): OfferScore {
  val result = netSalaryCalculatorService.calculate(
    NetSalaryInput(
      basicSalary = basicSalary,
      housingAllowance = housingAllowance,
      transportAllowance = transportAllowance,
      foodAllowance = foodAllowance,
      mobileAllowance = mobileAllowance,
      otherAllowances = otherAllowances,
      deductions = deductions,
      employeeType = employeeType,
      gosiRates = gosiRates
    )
  )

  return OfferScore(
    title = title,
    netMonthlySalary = result.netSalary,
    totalYearlyCompensation = result.yearlyNetSalary,
    employerMonthlyCost = result.employerMonthlyCost
  )
}

private fun Double.toCurrency(): Double = ((this * 100.0).roundToLong() / 100.0)

private fun Double.formatSar(): String = "%,.2f".format(this)
