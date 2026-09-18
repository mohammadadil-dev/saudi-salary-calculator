package com.saudi.salarycalculator.core.data

import com.saudi.salarycalculator.core.model.CalculationRecord
import com.saudi.salarycalculator.core.model.CostOfLivingInput
import com.saudi.salarycalculator.core.model.CostOfLivingResult
import com.saudi.salarycalculator.core.model.EndOfServiceInput
import com.saudi.salarycalculator.core.model.EndOfServiceResult
import com.saudi.salarycalculator.core.model.EosbTrackerInput
import com.saudi.salarycalculator.core.model.EosbTrackerProfile
import com.saudi.salarycalculator.core.model.EosbTrackerResult
import com.saudi.salarycalculator.core.model.ExpatCostInput
import com.saudi.salarycalculator.core.model.ExpatCostResult
import com.saudi.salarycalculator.core.model.GosiInput
import com.saudi.salarycalculator.core.model.GosiResult
import com.saudi.salarycalculator.core.model.GosiSystem
import com.saudi.salarycalculator.core.model.LeaveTrackerInput
import com.saudi.salarycalculator.core.model.LeaveTrackerProfile
import com.saudi.salarycalculator.core.model.LeaveTrackerResult
import com.saudi.salarycalculator.core.model.NetSalaryInput
import com.saudi.salarycalculator.core.model.NetSalaryResult
import com.saudi.salarycalculator.core.model.OfferComparisonResult
import com.saudi.salarycalculator.core.model.OfferInput
import com.saudi.salarycalculator.core.model.OfferRedFlagInput
import com.saudi.salarycalculator.core.model.OfferRedFlagResult
import com.saudi.salarycalculator.core.model.PaydayInput
import com.saudi.salarycalculator.core.model.PaydayResult
import com.saudi.salarycalculator.core.model.OvertimeInput
import com.saudi.salarycalculator.core.model.ReverseSalaryInput
import com.saudi.salarycalculator.core.model.ReverseSalaryResult
import com.saudi.salarycalculator.core.model.OvertimeResult
import com.saudi.salarycalculator.core.model.SavingsInput
import com.saudi.salarycalculator.core.model.SavingsResult
import kotlinx.coroutines.flow.Flow

interface SalaryRepository {
  suspend fun calculateNetSalary(input: NetSalaryInput): NetSalaryResult
  suspend fun calculateGosi(input: GosiInput): GosiResult
  suspend fun calculateOvertime(input: OvertimeInput): OvertimeResult
  suspend fun calculateEndOfService(input: EndOfServiceInput): EndOfServiceResult
  suspend fun compareOffers(offerA: OfferInput, offerB: OfferInput): OfferComparisonResult
  suspend fun calculateSavings(input: SavingsInput): SavingsResult
  suspend fun calculateExpatCosts(input: ExpatCostInput): ExpatCostResult

  fun observeHistory(): Flow<List<CalculationRecord>>
  suspend fun saveRecord(record: CalculationRecord)
  suspend fun clearHistory()
  suspend fun deleteRecord(id: String)

  fun observeLanguage(): Flow<String>
  suspend fun setLanguage(language: String)
  fun observeSelectedTab(): Flow<Int>
  suspend fun setSelectedTab(index: Int)
  fun observeDarkMode(): Flow<Boolean>
  suspend fun setDarkMode(enabled: Boolean)

  fun observeGosiSystem(): Flow<GosiSystem>
  suspend fun setGosiSystem(system: GosiSystem)
  fun observeEosbTrackerProfile(): Flow<EosbTrackerProfile>
  suspend fun setEosbTrackerProfile(joiningDateMillis: Long, lastBasicSalary: Double)
  suspend fun clearEosbTrackerProfile()
  suspend fun calculateEosbTracker(input: EosbTrackerInput): EosbTrackerResult


  fun observePaydayDayOfMonth(): Flow<Int?>
  suspend fun setPaydayDayOfMonth(dayOfMonth: Int)
  suspend fun clearPaydayDayOfMonth()
  fun calculatePayday(input: PaydayInput): PaydayResult

  fun observeLeaveTrackerProfile(): Flow<LeaveTrackerProfile>
  suspend fun setLeaveTrackerProfile(joiningDateMillis: Long, daysTakenThisYear: Int)
  suspend fun clearLeaveTrackerProfile()
  fun calculateLeaveTracker(input: LeaveTrackerInput): LeaveTrackerResult

  suspend fun scanOfferForRedFlags(input: OfferRedFlagInput): OfferRedFlagResult

  suspend fun calculateReverseSalary(input: ReverseSalaryInput): ReverseSalaryResult

  suspend fun estimateCostOfLiving(input: CostOfLivingInput): CostOfLivingResult
}
