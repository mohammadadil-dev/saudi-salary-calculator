package com.saudi.salarycalculator.core.calculator

import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.EndOfServiceInput
import com.saudi.salarycalculator.core.model.EndOfServiceResult
import com.saudi.salarycalculator.core.model.ExpatCostInput
import com.saudi.salarycalculator.core.model.ExpatCostResult
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
import com.saudi.salarycalculator.core.model.WorkingHoursSchedule
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

/**
 * Placeholder, production-shaped calculation logic for Saudi net salary.
 * Approximates GOSI (per GOSI rate inputs), unpaid leave / absence / loan deductions,
 * overtime pay (1.5x hourly), and an End-of-Service (EOSB) estimate derived from the
 * employee's joining date through the selected calculation month. Intended to be swapped
 * for verified, law-accurate logic later without touching the UI layer.
 */
class DefaultNetSalaryCalculatorService(
  private val endOfServiceCalculatorService: EndOfServiceCalculatorService = DefaultEndOfServiceCalculatorService()
) : NetSalaryCalculatorService {
  override fun calculate(input: NetSalaryInput): NetSalaryResult {
    val allowances = input.housingAllowance + input.transportAllowance +
      input.foodAllowance + input.mobileAllowance + input.otherAllowances

    val monthlyWorkingHours = WorkingHoursSchedule.monthlyHoursFor(input.ramadanReducedHours)
    val hourlyRate = input.overtimeHourlyRateOverride ?: (input.basicSalary / monthlyWorkingHours)
    val overtimePay = (hourlyRate * 1.5 * input.overtimeHours).toCurrency()
    val earningsAddOns = (input.bonus + input.commission + overtimePay).toCurrency()

    val gross = (input.basicSalary + allowances + earningsAddOns).toCurrency()

    val gosi = if (input.gosiIncluded) {
      DefaultGosiCalculatorService().calculate(
        GosiInput(
          baseAmount = input.basicSalary + input.housingAllowance,
          employeeType = input.employeeType,
          rates = input.gosiRates
        )
      )
    } else {
      GosiResult(employeeContribution = 0.0, employerContribution = 0.0, totalContribution = 0.0)
    }

    val unpaidLeaveDeduction = ((input.basicSalary / 30.0) * input.unpaidLeaveDays).toCurrency()
    val totalDeductions = (
      input.loanDeduction + input.absenceDeduction + unpaidLeaveDeduction +
        input.deductions + gosi.employeeContribution
      ).toCurrency()

    val net = (gross - totalDeductions).toCurrency()
    val employerCost = (gross + gosi.employerContribution).toCurrency()

    val yearsOfService = yearsBetween(input.joiningDateMillis, input.calculationMonthMillis)
    val eosb = endOfServiceCalculatorService.calculate(
      EndOfServiceInput(
        lastBasicSalary = input.basicSalary,
        yearsOfService = yearsOfService,
        resigned = input.resigned
      )
    ).rewardAmount

    return NetSalaryResult(
      grossSalary = gross,
      totalAllowances = allowances.toCurrency(),
      totalEarningsAddOns = earningsAddOns,
      overtimePay = overtimePay,
      totalDeductions = totalDeductions,
      employeeGosiAmount = gosi.employeeContribution,
      employerGosiAmount = gosi.employerContribution,
      employerMonthlyCost = employerCost,
      netSalary = net,
      yearlyGrossSalary = (gross * 12.0).toCurrency(),
      yearlyNetSalary = (net * 12.0).toCurrency(),
      yearlyEmployerCost = (employerCost * 12.0).toCurrency(),
      estimatedEosb = eosb,
      yearsOfService = yearsOfService,
      smartSummary = "Your monthly take-home salary is SAR ${net.formatSar()}",
      breakdown = listOf(
        SalaryBreakdownItem("Basic salary", input.basicSalary.toCurrency(), 0xFF0F7A4D),
        SalaryBreakdownItem("Housing allowance", input.housingAllowance.toCurrency(), 0xFF34C28E),
        SalaryBreakdownItem("Transport allowance", input.transportAllowance.toCurrency(), 0xFF2DA8A8),
        SalaryBreakdownItem("Food & other allowances", (input.foodAllowance + input.mobileAllowance + input.otherAllowances).toCurrency(), 0xFFC8932B),
        SalaryBreakdownItem("Bonus & commission", (input.bonus + input.commission).toCurrency(), 0xFF8A6BD6),
        SalaryBreakdownItem("Overtime pay", overtimePay, 0xFF1F9D8A),
        SalaryBreakdownItem("GOSI (employee)", gosi.employeeContribution, 0xFFE5484D, isDeduction = true),
        SalaryBreakdownItem("Loan, absence & leave", (input.loanDeduction + input.absenceDeduction + unpaidLeaveDeduction + input.deductions).toCurrency(), 0xFFB23A3A, isDeduction = true)
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
    // GOSI only assesses contributions on wages up to the monthly contribution cap (SAR 45,000 as
    // of mid-2026 — see GosiRateSchedule); anything above it is excluded from the base, not just
    // taxed at a different rate.
    val cap = input.rates.contributionCapSar
    val contributionBase = if (cap > 0.0) minOf(input.baseAmount, cap) else input.baseAmount
    val employee = (contributionBase * employeeRate).toCurrency()
    val employer = (contributionBase * employerRate).toCurrency()
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
  private val netSalaryCalculatorService: NetSalaryCalculatorService,
  private val endOfServiceCalculatorService: EndOfServiceCalculatorService = DefaultEndOfServiceCalculatorService()
) : OfferComparisonCalculatorService {
  override fun calculate(offerA: OfferInput, offerB: OfferInput): OfferComparisonResult {
    val scoreA = offerA.toScore(netSalaryCalculatorService, endOfServiceCalculatorService)
    val scoreB = offerB.toScore(netSalaryCalculatorService, endOfServiceCalculatorService)
    // Was ">=", which silently named offer B (the "new" offer) the winner on an exact tie —
    // e.g. basic 15000/HRA 5000 vs basic 14000/HRA 6000 both net to the same take-home pay
    // (GOSI only depends on basic+HRA combined, not the split), so every tie was misreported
    // as "new offer is better". ">" resolves ties to A instead; the UI also overrides the
    // headline to a neutral "about the same" message below the 1% gap threshold (see
    // ComparisonScreen) so neither offer's name is shown as a false "winner".
    val better = if (scoreB.netMonthlySalary > scoreA.netMonthlySalary) scoreB else scoreA
    val diffMonthly = (scoreB.netMonthlySalary - scoreA.netMonthlySalary).toCurrency()
    val percentageIncrease = if (scoreA.netMonthlySalary > 0.0) {
      (((scoreB.netMonthlySalary - scoreA.netMonthlySalary) / scoreA.netMonthlySalary) * 100.0).toCurrency()
    } else 0.0

    // Magnitude of the gap between the two offers, independent of which one wins — this is what
    // scoreLabel below describes, paired with betterOfferTitle (e.g. "Better offer: New offer —
    // Clearly better"). The previous version scored offer B's ratio to A directly: any realistic
    // raise (a few percent) landed at ~65-70, the same band as "no change at all", which is why a
    // genuine +3.7% increase rendered as "Neutral" — indistinguishable from a 0% difference. Using
    // the gap's magnitude (already shown on screen as the % increase/decrease) keeps the label
    // consistent with the number right next to it.
    val percentageGap = kotlin.math.abs(percentageIncrease)
    val acceptanceScore = (50.0 + percentageGap * 2.5).coerceIn(0.0, 100.0).roundToInt()
    val scoreLabel = when {
      percentageGap < 1.0 -> "About the same"
      percentageGap < 5.0 -> "Slightly better"
      percentageGap < 15.0 -> "Clearly better"
      else -> "Significantly better"
    }

    return OfferComparisonResult(
      offerA = scoreA,
      offerB = scoreB,
      betterOfferTitle = better.title,
      monthlyDifference = diffMonthly,
      yearlyDifference = (diffMonthly * 12.0).toCurrency(),
      percentageIncrease = percentageIncrease,
      gosiMonthlyDifference = (scoreB.employeeGosiMonthly - scoreA.employeeGosiMonthly).toCurrency(),
      eosbDifference = (scoreB.estimatedEosb - scoreA.estimatedEosb).toCurrency(),
      acceptanceScore = acceptanceScore,
      scoreLabel = scoreLabel
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

interface ExpatCostCalculatorService {
  fun calculate(input: ExpatCostInput): ExpatCostResult
}

class DefaultExpatCostCalculatorService : ExpatCostCalculatorService {
  override fun calculate(input: ExpatCostInput): ExpatCostResult {
    val monthlyDependentLevy = (input.dependentCount.coerceAtLeast(0) * input.dependentMonthlyLevySar).toCurrency()
    val monthlyOwnIqamaRenewal = if (input.includeOwnIqamaRenewal) {
      (input.ownIqamaRenewalFeeSar / 12.0).toCurrency()
    } else {
      0.0
    }
    val monthlyExitReentry = (
      (input.exitReentryTripsPerYear.coerceAtLeast(0) * input.exitReentryFeePerTripSar) / 12.0
      ).toCurrency()
    val monthlyHealthInsurance = input.monthlyHealthInsurancePremiumSar.coerceAtLeast(0.0).toCurrency()

    val totalMonthly = (
      monthlyDependentLevy + monthlyOwnIqamaRenewal + monthlyExitReentry + monthlyHealthInsurance
      ).toCurrency()

    return ExpatCostResult(
      monthlyDependentLevy = monthlyDependentLevy,
      monthlyOwnIqamaRenewal = monthlyOwnIqamaRenewal,
      monthlyExitReentry = monthlyExitReentry,
      monthlyHealthInsurance = monthlyHealthInsurance,
      totalMonthlyCost = totalMonthly,
      totalYearlyCost = (totalMonthly * 12.0).toCurrency()
    )
  }
}

private fun OfferInput.toScore(
  netSalaryCalculatorService: NetSalaryCalculatorService,
  endOfServiceCalculatorService: EndOfServiceCalculatorService
): OfferScore {
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
  val eosb = endOfServiceCalculatorService.calculate(
    EndOfServiceInput(lastBasicSalary = basicSalary, yearsOfService = yearsOfService, resigned = resigned)
  ).rewardAmount

  return OfferScore(
    title = title,
    netMonthlySalary = result.netSalary,
    totalYearlyCompensation = result.yearlyNetSalary,
    employerMonthlyCost = result.employerMonthlyCost,
    employeeGosiMonthly = result.employeeGosiAmount,
    estimatedEosb = eosb
  )
}

private fun Double.toCurrency(): Double = ((this * 100.0).roundToLong() / 100.0)

private fun Double.formatSar(): String = "%,.2f".format(this)

private fun yearsBetween(joiningMillis: Long?, calculationMonthMillis: Long?): Double {
  if (joiningMillis == null) return 0.0
  val end = calculationMonthMillis ?: System.currentTimeMillis()
  val diffMillis = (end - joiningMillis).coerceAtLeast(0L)
  return diffMillis / (1000.0 * 60.0 * 60.0 * 24.0 * 365.25)
}
