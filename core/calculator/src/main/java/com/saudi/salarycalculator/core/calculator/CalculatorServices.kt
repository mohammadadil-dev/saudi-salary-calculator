package com.saudi.salarycalculator.core.calculator

import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.EndOfServiceInput
import com.saudi.salarycalculator.core.model.EndOfServiceResult
import com.saudi.salarycalculator.core.model.EosbMilestone
import com.saudi.salarycalculator.core.model.EosbResignationEligibility
import com.saudi.salarycalculator.core.model.EosbTrackerInput
import com.saudi.salarycalculator.core.model.EosbTrackerResult
import com.saudi.salarycalculator.core.model.ExpatCostInput
import com.saudi.salarycalculator.core.model.ExpatCostResult
import com.saudi.salarycalculator.core.model.CostOfLivingInput
import com.saudi.salarycalculator.core.model.CostOfLivingResult
import com.saudi.salarycalculator.core.model.CostOfLivingSchedule
import com.saudi.salarycalculator.core.model.GosiInput
import com.saudi.salarycalculator.core.model.GosiResult
import com.saudi.salarycalculator.core.model.LeaveTrackerInput
import com.saudi.salarycalculator.core.model.LeaveTrackerResult
import com.saudi.salarycalculator.core.model.NetSalaryInput
import com.saudi.salarycalculator.core.model.NetSalaryResult
import com.saudi.salarycalculator.core.model.OfferComparisonResult
import com.saudi.salarycalculator.core.model.OfferFlagSeverity
import com.saudi.salarycalculator.core.model.OfferFlagType
import com.saudi.salarycalculator.core.model.OfferInput
import com.saudi.salarycalculator.core.model.OfferRedFlagFinding
import com.saudi.salarycalculator.core.model.OfferRedFlagInput
import com.saudi.salarycalculator.core.model.OfferRedFlagResult
import com.saudi.salarycalculator.core.model.OfferScore
import com.saudi.salarycalculator.core.model.OvertimeInput
import com.saudi.salarycalculator.core.model.ReverseSalaryInput
import com.saudi.salarycalculator.core.model.ReverseSalaryResult
import com.saudi.salarycalculator.core.model.OvertimeResult
import com.saudi.salarycalculator.core.model.PaydayInput
import com.saudi.salarycalculator.core.model.PaydayResult
import com.saudi.salarycalculator.core.model.SalaryBreakdownItem
import com.saudi.salarycalculator.core.model.SavingsInput
import com.saudi.salarycalculator.core.model.SavingsResult
import com.saudi.salarycalculator.core.model.WorkingHoursSchedule
import java.util.Calendar
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

interface EosbTrackerCalculatorService {
  fun calculate(input: EosbTrackerInput): EosbTrackerResult
}

interface PaydayCalculatorService {
  fun calculate(input: PaydayInput): PaydayResult
}

interface LeaveTrackerCalculatorService {
  fun calculate(input: LeaveTrackerInput): LeaveTrackerResult
}

interface OfferComparisonCalculatorService {
  fun calculate(offerA: OfferInput, offerB: OfferInput): OfferComparisonResult
}

interface OfferRedFlagCalculatorService {
  fun scan(input: OfferRedFlagInput): OfferRedFlagResult
}

interface ReverseSalaryCalculatorService {
  fun calculate(input: ReverseSalaryInput): ReverseSalaryResult
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

/** Standing "my employment" tracker: reuses [EndOfServiceCalculatorService] for the actual
 * Article 84-85 reward math (see that class for the formula) so the tracker and the one-shot
 * wizard estimate can never silently disagree, and layers on top: years of service computed
 * against a live "as of" timestamp rather than a fixed calculation month, both the
 * resign-today and terminated-today figures side by side, and a countdown to the next
 * service-length milestone where the resignation fraction or accrual rate changes. */
class DefaultEosbTrackerCalculatorService(
  private val endOfServiceCalculatorService: EndOfServiceCalculatorService = DefaultEndOfServiceCalculatorService()
) : EosbTrackerCalculatorService {
  override fun calculate(input: EosbTrackerInput): EosbTrackerResult {
    val yearsOfService = yearsBetween(input.joiningDateMillis, input.asOfMillis)

    val terminated = endOfServiceCalculatorService.calculate(
      EndOfServiceInput(
        lastBasicSalary = input.lastBasicSalary,
        yearsOfService = yearsOfService,
        resigned = false
      )
    )
    val resigned = endOfServiceCalculatorService.calculate(
      EndOfServiceInput(
        lastBasicSalary = input.lastBasicSalary,
        yearsOfService = yearsOfService,
        resigned = true
      )
    )

    val eligibility = when {
      yearsOfService < 2.0 -> EosbResignationEligibility.NONE
      yearsOfService < 5.0 -> EosbResignationEligibility.ONE_THIRD
      yearsOfService < 10.0 -> EosbResignationEligibility.TWO_THIRDS
      else -> EosbResignationEligibility.FULL
    }

    return EosbTrackerResult(
      yearsOfService = yearsOfService,
      accruedIfTerminated = terminated.rewardAmount,
      accruedIfResignedToday = resigned.rewardAmount,
      resignationEligibility = eligibility,
      nextMilestone = nextEosbMilestone(input.joiningDateMillis, yearsOfService, input.asOfMillis)
    )
  }
}

private val EOSB_MILESTONE_THRESHOLDS = listOf(2.0 to "2_YEAR", 5.0 to "5_YEAR", 10.0 to "10_YEAR")

/** Null once [yearsOfService] is past the last threshold (10 years) — Article 85's resignation
 * ladder tops out at "full award" there, so there's no further cliff to count down to. */
private fun nextEosbMilestone(joiningDateMillis: Long, yearsOfService: Double, asOfMillis: Long): EosbMilestone? {
  val (thresholdYears, key) = EOSB_MILESTONE_THRESHOLDS.firstOrNull { (years, _) -> years > yearsOfService } ?: return null
  val millisPerYear = 1000.0 * 60.0 * 60.0 * 24.0 * 365.25
  val targetMillis = joiningDateMillis + (thresholdYears * millisPerYear).roundToLong()
  val daysRemaining = (targetMillis - asOfMillis).coerceAtLeast(0L) / (1000L * 60 * 60 * 24)
  return EosbMilestone(
    yearsThreshold = thresholdYears,
    dateMillis = targetMillis,
    daysRemaining = daysRemaining,
    milestoneKey = key
  )
}

/** Reuses [Calendar] month-length awareness so a payday set to the 31st correctly lands on the
 * 28th/29th in February rather than overflowing into March. Compares against the start of
 * [PaydayInput.asOfMillis]'s day (not the raw timestamp) so "today is payday" reads as 0 days
 * remaining all day, not a few hours' worth of a fractional day. */
class DefaultPaydayCalculatorService : PaydayCalculatorService {
  override fun calculate(input: PaydayInput): PaydayResult {
    val now = Calendar.getInstance().apply { timeInMillis = input.asOfMillis }
    val todayStart = (now.clone() as Calendar).apply {
      set(Calendar.HOUR_OF_DAY, 0)
      set(Calendar.MINUTE, 0)
      set(Calendar.SECOND, 0)
      set(Calendar.MILLISECOND, 0)
    }

    fun paydayInMonth(monthOffset: Int): Calendar {
      val cal = todayStart.clone() as Calendar
      cal.add(Calendar.MONTH, monthOffset)
      val lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
      cal.set(Calendar.DAY_OF_MONTH, minOf(input.dayOfMonth, lastDayOfMonth))
      return cal
    }

    var candidate = paydayInMonth(0)
    if (candidate.timeInMillis < todayStart.timeInMillis) {
      candidate = paydayInMonth(1)
    }

    val daysRemaining = (candidate.timeInMillis - todayStart.timeInMillis) / (1000L * 60 * 60 * 24)
    return PaydayResult(nextPaydayMillis = candidate.timeInMillis, daysRemaining = daysRemaining)
  }
}

/** Saudi Labor Law Article 109: annual leave is at least 21 days per year of service, rising to
 * at least 30 days/year once the employee passes 5 years — the same 5-year cliff
 * [DefaultEndOfServiceCalculatorService] uses for the EOSB accrual rate, so this mirrors that
 * threshold rather than inventing a new one. The "leave year" is anchored to the joining-date
 * anniversary (not the calendar year), since that is what the law measures service against;
 * accrual within the current leave year is prorated by elapsed days over the *actual* length of
 * that specific anniversary-to-anniversary span (365 or 366 days) rather than a fixed 365, so a
 * leap year doesn't quietly overstate the accrual rate. */
class DefaultLeaveTrackerCalculatorService : LeaveTrackerCalculatorService {
  override fun calculate(input: LeaveTrackerInput): LeaveTrackerResult {
    val yearsOfService = yearsBetween(input.joiningDateMillis, input.asOfMillis)
    val annualEntitlementDays = if (yearsOfService >= 5.0) 30 else 21

    fun Calendar.toDayStart(): Calendar = (clone() as Calendar).apply {
      set(Calendar.HOUR_OF_DAY, 0)
      set(Calendar.MINUTE, 0)
      set(Calendar.SECOND, 0)
      set(Calendar.MILLISECOND, 0)
    }

    val joining = (Calendar.getInstance().apply { timeInMillis = input.joiningDateMillis }).toDayStart()
    val asOf = (Calendar.getInstance().apply { timeInMillis = input.asOfMillis }).toDayStart()

    var anniversary = (joining.clone() as Calendar).apply { set(Calendar.YEAR, asOf.get(Calendar.YEAR)) }
    if (anniversary.timeInMillis > asOf.timeInMillis) {
      anniversary = (anniversary.clone() as Calendar).apply { add(Calendar.YEAR, -1) }
    }
    // Guards a just-joined employee (under a year of service): the step above can otherwise land
    // on a fictional anniversary before the real joining date, which would understate service.
    if (anniversary.timeInMillis < joining.timeInMillis) {
      anniversary = joining
    }

    val nextAnniversary = (anniversary.clone() as Calendar).apply { add(Calendar.YEAR, 1) }
    val totalDaysThisLeaveYear = (nextAnniversary.timeInMillis - anniversary.timeInMillis) / (1000L * 60 * 60 * 24)
    val elapsedDaysThisYear = (asOf.timeInMillis - anniversary.timeInMillis) / (1000L * 60 * 60 * 24)
    val daysAccruedSoFarThisYear = if (totalDaysThisLeaveYear > 0) {
      annualEntitlementDays * (elapsedDaysThisYear.toDouble() / totalDaysThisLeaveYear.toDouble())
    } else {
      0.0
    }

    return LeaveTrackerResult(
      yearsOfService = yearsOfService,
      annualEntitlementDays = annualEntitlementDays,
      currentLeaveYearStartMillis = anniversary.timeInMillis,
      daysAccruedSoFarThisYear = daysAccruedSoFarThisYear,
      daysTakenThisYear = input.daysTakenThisYear,
      daysRemainingBalance = daysAccruedSoFarThisYear - input.daysTakenThisYear,
      nextAnniversaryMillis = nextAnniversary.timeInMillis,
      daysUntilNextAnniversary = (nextAnniversary.timeInMillis - asOf.timeInMillis) / (1000L * 60 * 60 * 24)
    )
  }
}

/** Pure rule-based screening against Saudi Labor Law norms and common expat-hiring risk
 * patterns — see [OfferFlagType] for what each rule checks and why. Deliberately conservative:
 * every rule is grounded in a specific, well-documented provision or a plainly one-sided term,
 * and the screen surfacing these results always pairs them with a "this isn't legal advice"
 * disclaimer rather than a bare verdict. */
class DefaultOfferRedFlagCalculatorService : OfferRedFlagCalculatorService {
  override fun scan(input: OfferRedFlagInput): OfferRedFlagResult {
    val findings = mutableListOf<OfferRedFlagFinding>()

    // Article 53: probation caps at 90 days, extendable once to 180 days only by a distinct
    // written agreement between employer and employee.
    if (input.probationMonths > 6) {
      findings += OfferRedFlagFinding(OfferFlagType.PROBATION_EXCEEDS_MAX, OfferFlagSeverity.VIOLATION)
    } else if (input.probationMonths > 3 && !input.probationExtendedInWriting) {
      findings += OfferRedFlagFinding(OfferFlagType.PROBATION_EXTENSION_NOT_WRITTEN, OfferFlagSeverity.CAUTION)
    }

    if (!input.isGosiRegistered) {
      findings += OfferRedFlagFinding(OfferFlagType.GOSI_NOT_REGISTERED, OfferFlagSeverity.VIOLATION)
    }

    if (!input.hasWrittenContract) {
      findings += OfferRedFlagFinding(OfferFlagType.NO_WRITTEN_CONTRACT, OfferFlagSeverity.CAUTION)
    }

    if (input.recruitmentFeesCharged) {
      findings += OfferRedFlagFinding(OfferFlagType.RECRUITMENT_FEES_CHARGED, OfferFlagSeverity.VIOLATION)
    }

    // Flags a lopsided notice term (employee owes meaningfully more than the employer) rather
    // than any asymmetry at all, so a routine 30/30 or even 45/30 split doesn't trigger this.
    if (input.employeeNoticeDays - input.employerNoticeDays >= 15) {
      findings += OfferRedFlagFinding(OfferFlagType.ASYMMETRIC_NOTICE_PERIOD, OfferFlagSeverity.CAUTION)
    }

    if (input.totalMonthlySalary > 0.0 && input.basicSalary / input.totalMonthlySalary < 0.5) {
      findings += OfferRedFlagFinding(OfferFlagType.LOW_BASIC_RATIO, OfferFlagSeverity.INFO)
    }

    if (findings.isEmpty()) {
      findings += OfferRedFlagFinding(OfferFlagType.ALL_CLEAR, OfferFlagSeverity.INFO)
    }

    return OfferRedFlagResult(
      findings = findings,
      violationCount = findings.count { it.severity == OfferFlagSeverity.VIOLATION },
      cautionCount = findings.count { it.severity == OfferFlagSeverity.CAUTION }
    )
  }
}

/** Solves for the basic salary that produces a target net take-home by binary-searching against
 * the real [NetSalaryCalculatorService] (never a re-derived approximation of it), so this can
 * never drift out of sync with however net salary is actually calculated. Net pay is monotonic
 * non-decreasing in basic salary (GOSI's contribution cap only ever plateaus the deduction, it
 * never reduces net pay as gross rises), so binary search is guaranteed to converge. */
class DefaultReverseSalaryCalculatorService(
  private val netSalaryCalculatorService: NetSalaryCalculatorService
) : ReverseSalaryCalculatorService {
  private companion object {
    const val SEARCH_CEILING_SAR = 300000.0
    const val ITERATIONS = 60
  }

  private fun netFor(input: ReverseSalaryInput, basicSalary: Double): Double {
    val housing = basicSalary * input.housingAllowancePercent / 100.0
    val transport = basicSalary * input.transportAllowancePercent / 100.0
    return netSalaryCalculatorService.calculate(
      NetSalaryInput(
        basicSalary = basicSalary,
        housingAllowance = housing,
        transportAllowance = transport,
        employeeType = input.employeeType,
        gosiRates = input.gosiRates
      )
    ).netSalary
  }

  override fun calculate(input: ReverseSalaryInput): ReverseSalaryResult {
    if (input.targetNetMonthlySalary <= 0.0) {
      return ReverseSalaryResult(0.0, 0.0, 0.0, 0.0, netFor(input, 0.0), true)
    }

    val isAchievable = netFor(input, SEARCH_CEILING_SAR) >= input.targetNetMonthlySalary
    var low = 0.0
    var high = SEARCH_CEILING_SAR
    repeat(ITERATIONS) {
      val mid = (low + high) / 2.0
      if (netFor(input, mid) < input.targetNetMonthlySalary) low = mid else high = mid
    }

    val basicSalary = high
    val housing = basicSalary * input.housingAllowancePercent / 100.0
    val transport = basicSalary * input.transportAllowancePercent / 100.0
    return ReverseSalaryResult(
      requiredBasicSalary = basicSalary,
      requiredHousingAllowance = housing,
      requiredTransportAllowance = transport,
      requiredTotalMonthlyPackage = basicSalary + housing + transport,
      achievedNetSalary = netFor(input, basicSalary),
      isAchievable = isAchievable
    )
  }
}

class DefaultOfferComparisonCalculatorService(
  private val netSalaryCalculatorService: NetSalaryCalculatorService,
  private val endOfServiceCalculatorService: EndOfServiceCalculatorService = DefaultEndOfServiceCalculatorService()
) : OfferComparisonCalculatorService {
  private companion object {
    // OfferInput.yearsOfService defaults to 0 and this screen never collects a real tenure for
    // either side (these are hypothetical/prospective offers, not a standing job) — projecting
    // EOSB at 0 years of service made the reward, and therefore eosbDifference, always exactly
    // 0.00 no matter what the two offers' salaries were. Comparing both offers at a fixed
    // reference tenure instead makes the gap reflect the actual salary difference between them;
    // 5 years is just a representative mid-career reference point, called out in the UI label
    // (see comparison_eosb_difference) so it doesn't read as either offer's real service length.
    const val EOSB_REFERENCE_YEARS_OF_SERVICE = 5.0
  }

  override fun calculate(offerA: OfferInput, offerB: OfferInput): OfferComparisonResult {
    val scoreA = offerA.toScore(netSalaryCalculatorService, endOfServiceCalculatorService, EOSB_REFERENCE_YEARS_OF_SERVICE)
    val scoreB = offerB.toScore(netSalaryCalculatorService, endOfServiceCalculatorService, EOSB_REFERENCE_YEARS_OF_SERVICE)
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

interface CostOfLivingCalculatorService {
  fun estimate(input: CostOfLivingInput): CostOfLivingResult
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

/**
 * Combines [CostOfLivingSchedule]'s sourced rent, non-rent, and school-fee ranges into one
 * household budget range. Every input scales a wide reference range rather than producing a
 * single precise figure, since the underlying market data doesn't support that precision (see
 * [CostOfLivingSchedule]'s class doc) — the UI is expected to present this as a range, not a
 * point estimate.
 */
class DefaultCostOfLivingCalculatorService : CostOfLivingCalculatorService {
  override fun estimate(input: CostOfLivingInput): CostOfLivingResult {
    val rentRange = CostOfLivingSchedule.rentRangeSar(input.city, input.apartmentSize)
    val baseline = CostOfLivingSchedule.nonRentBaselineSar(input.city)

    val additionalAdults = input.additionalAdults.coerceAtLeast(0)
    val childrenCount = input.childrenCount.coerceAtLeast(0)
    val childrenInSchool = input.childrenInSchool.coerceIn(0, childrenCount)

    val householdMultiplier = 1.0 +
      additionalAdults * CostOfLivingSchedule.ADDITIONAL_ADULT_MULTIPLIER +
      childrenCount * CostOfLivingSchedule.ADDITIONAL_CHILD_MULTIPLIER

    val nonRentRange = (baseline.first * householdMultiplier).roundToInt()..
      (baseline.last * householdMultiplier).roundToInt()

    val monthlySchoolFeeRange = if (childrenInSchool > 0) {
      val annualFee = CostOfLivingSchedule.annualSchoolFeeRangeSar(input.schoolFeeTier)
      ((annualFee.first * childrenInSchool) / 12)..((annualFee.last * childrenInSchool) / 12)
    } else {
      0..0
    }

    val totalRange = (rentRange.first + nonRentRange.first + monthlySchoolFeeRange.first)..
      (rentRange.last + nonRentRange.last + monthlySchoolFeeRange.last)

    return CostOfLivingResult(
      rentRangeSar = rentRange,
      nonRentRangeSar = nonRentRange,
      monthlySchoolFeeRangeSar = monthlySchoolFeeRange,
      totalMonthlyRangeSar = totalRange,
      suggestedTargetNetSalarySar = totalRange.last
    )
  }
}

private fun OfferInput.toScore(
  netSalaryCalculatorService: NetSalaryCalculatorService,
  endOfServiceCalculatorService: EndOfServiceCalculatorService,
  eosbReferenceYearsOfService: Double = yearsOfService
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
    EndOfServiceInput(lastBasicSalary = basicSalary, yearsOfService = eosbReferenceYearsOfService, resigned = resigned)
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
