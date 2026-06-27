package com.saudi.salarycalculator.core.data.repository

import com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService
import com.saudi.salarycalculator.core.calculator.GosiCalculatorService
import com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService
import com.saudi.salarycalculator.core.calculator.OfferComparisonCalculatorService
import com.saudi.salarycalculator.core.calculator.OvertimeCalculatorService
import com.saudi.salarycalculator.core.calculator.SavingsCalculatorService
import com.saudi.salarycalculator.core.data.SalaryRepository
import com.saudi.salarycalculator.core.database.dao.CalculationRecordDao
import com.saudi.salarycalculator.core.database.entity.CalculationRecordEntity
import com.saudi.salarycalculator.core.model.CalculationRecord
import com.saudi.salarycalculator.core.model.CalculationType
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
import com.saudi.salarycalculator.core.preferences.UserPreferencesStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstSalaryRepository @Inject constructor(
  private val netSalaryCalculatorService: NetSalaryCalculatorService,
  private val gosiCalculatorService: GosiCalculatorService,
  private val overtimeCalculatorService: OvertimeCalculatorService,
  private val endOfServiceCalculatorService: EndOfServiceCalculatorService,
  private val offerComparisonCalculatorService: OfferComparisonCalculatorService,
  private val savingsCalculatorService: SavingsCalculatorService,
  private val calculationRecordDao: CalculationRecordDao,
  private val preferencesStore: UserPreferencesStore
) : SalaryRepository {
  override suspend fun calculateNetSalary(input: NetSalaryInput): NetSalaryResult =
    netSalaryCalculatorService.calculate(input)

  override suspend fun calculateGosi(input: GosiInput): GosiResult =
    gosiCalculatorService.calculate(input)

  override suspend fun calculateOvertime(input: OvertimeInput): OvertimeResult =
    overtimeCalculatorService.calculate(input)

  override suspend fun calculateEndOfService(input: EndOfServiceInput): EndOfServiceResult =
    endOfServiceCalculatorService.calculate(input)

  override suspend fun compareOffers(offerA: OfferInput, offerB: OfferInput): OfferComparisonResult =
    offerComparisonCalculatorService.calculate(offerA, offerB)

  override suspend fun calculateSavings(input: SavingsInput): SavingsResult =
    savingsCalculatorService.calculate(input)

  override fun observeHistory(): Flow<List<CalculationRecord>> =
    calculationRecordDao.observeAll().map { records -> records.map { it.toModel() } }

  override suspend fun saveRecord(record: CalculationRecord) {
    calculationRecordDao.insert(record.toEntity())
  }

  override suspend fun clearHistory() {
    calculationRecordDao.clearAll()
  }

  override fun observeLanguage(): Flow<String> = preferencesStore.selectedLanguage

  override suspend fun setLanguage(language: String) {
    preferencesStore.setLanguage(language)
  }

  override fun observeSelectedTab(): Flow<Int> = preferencesStore.selectedTabIndex

  override suspend fun setSelectedTab(index: Int) {
    preferencesStore.setTabIndex(index)
  }
}

private fun CalculationRecordEntity.toModel(): CalculationRecord =
  CalculationRecord(
    id = id,
    type = CalculationType.valueOf(type),
    title = title,
    inputSummary = inputSummary,
    resultSummary = resultSummary,
    createdAtMillis = createdAtMillis
  )

private fun CalculationRecord.toEntity(): CalculationRecordEntity =
  CalculationRecordEntity(
    id = id,
    type = type.name,
    title = title,
    inputSummary = inputSummary,
    resultSummary = resultSummary,
    createdAtMillis = createdAtMillis
  )
