package com.saudi.salarycalculator.core.data

import com.saudi.salarycalculator.core.model.CalculationRecord
import com.saudi.salarycalculator.core.model.EndOfServiceInput
import com.saudi.salarycalculator.core.model.EndOfServiceResult
import com.saudi.salarycalculator.core.model.GosiInput
import com.saudi.salarycalculator.core.model.GosiResult
import com.saudi.salarycalculator.core.model.NetSalaryInput
import com.saudi.salarycalculator.core.model.NetSalaryResult
import com.saudi.salarycalculator.core.model.OfferComparisonResult
import com.saudi.salarycalculator.core.model.OfferInput
import com.saudi.salarycalculator.core.model.OvertimeInput
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
}
