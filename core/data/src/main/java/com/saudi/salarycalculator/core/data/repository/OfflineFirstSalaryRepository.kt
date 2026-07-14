package com.saudi.salarycalculator.core.data.repository

import com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService
import com.saudi.salarycalculator.core.calculator.ExpatCostCalculatorService
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
import com.saudi.salarycalculator.core.model.ContractType
import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.EmploymentSector
import com.saudi.salarycalculator.core.model.EndOfServiceInput
import com.saudi.salarycalculator.core.model.EndOfServiceResult
import com.saudi.salarycalculator.core.model.ExpatCostInput
import com.saudi.salarycalculator.core.model.ExpatCostResult
import com.saudi.salarycalculator.core.model.GosiInput
import com.saudi.salarycalculator.core.model.GosiRates
import com.saudi.salarycalculator.core.model.GosiResult
import com.saudi.salarycalculator.core.model.GosiSystem
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
  private val expatCostCalculatorService: ExpatCostCalculatorService,
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

  override suspend fun calculateExpatCosts(input: ExpatCostInput): ExpatCostResult =
    expatCostCalculatorService.calculate(input)

  override fun observeHistory(): Flow<List<CalculationRecord>> =
    calculationRecordDao.observeAll().map { records -> records.map { it.toModel() } }

  override suspend fun saveRecord(record: CalculationRecord) {
    calculationRecordDao.insert(record.toEntity())
  }

  override suspend fun clearHistory() {
    calculationRecordDao.clearAll()
  }

  override suspend fun deleteRecord(id: String) {
    calculationRecordDao.deleteById(id)
  }

  override fun observeLanguage(): Flow<String> = preferencesStore.selectedLanguage

  override suspend fun setLanguage(language: String) {
    preferencesStore.setLanguage(language)
  }

  override fun observeSelectedTab(): Flow<Int> = preferencesStore.selectedTabIndex

  override suspend fun setSelectedTab(index: Int) {
    preferencesStore.setTabIndex(index)
  }

  override fun observeDarkMode(): Flow<Boolean> = preferencesStore.darkModeEnabled

  override suspend fun setDarkMode(enabled: Boolean) {
    preferencesStore.setDarkMode(enabled)
  }

  override fun observeGosiSystem(): Flow<GosiSystem> = preferencesStore.gosiSystem.map { raw ->
    runCatching { GosiSystem.valueOf(raw) }.getOrDefault(GosiSystem.NEW)
  }

  override suspend fun setGosiSystem(system: GosiSystem) {
    preferencesStore.setGosiSystem(system.name)
  }
}

private fun CalculationRecordEntity.toModel(): CalculationRecord =
  CalculationRecord(
    id = id,
    type = CalculationType.valueOf(type),
    title = title,
    inputSummary = inputSummary,
    resultSummary = resultSummary,
    createdAtMillis = createdAtMillis,
    netSalaryInput = netSalaryInputSnapshot?.decodeNetSalaryInputSnapshot()
  )

private fun CalculationRecord.toEntity(): CalculationRecordEntity =
  CalculationRecordEntity(
    id = id,
    type = type.name,
    title = title,
    inputSummary = inputSummary,
    resultSummary = resultSummary,
    createdAtMillis = createdAtMillis,
    netSalaryInputSnapshot = netSalaryInput?.encodeAsSnapshot()
  )

// ---- NetSalaryInput <-> flat string snapshot --------------------------------------------
// Room has no TypeConverter set up for nested data classes, and pulling in a JSON library for
// one optional column is overkill, so this hand-rolls a plain-text delimited encoding instead.
// Any accidental occurrence of the delimiter inside free-text fields (employeeName/jobTitle) is
// neutralized by stripping it out at encode time, so the field count never shifts on decode.
private const val SNAPSHOT_DELIMITER = "<<#>>"
private const val SNAPSHOT_NULL = "<<NULL>>"
// Records written before the GOSI contribution cap / Ramadan hours fields were added only have 27
// fields; decode treats the 28th (contributionCapSar) and 29th (ramadanReducedHours) as optional
// so those old snapshots still decode instead of being silently dropped. New snapshots always
// write all 29.
private const val SNAPSHOT_FIELD_COUNT_LEGACY = 27
private const val SNAPSHOT_FIELD_COUNT = 29

private fun NetSalaryInput.encodeAsSnapshot(): String {
  fun Double?.encoded() = this?.toString() ?: SNAPSHOT_NULL
  fun Long?.encoded() = this?.toString() ?: SNAPSHOT_NULL
  fun String.escaped() = replace(SNAPSHOT_DELIMITER, " ")
  return listOf(
    employeeName.escaped(),
    jobTitle.escaped(),
    basicSalary.toString(),
    housingAllowance.toString(),
    transportAllowance.toString(),
    foodAllowance.toString(),
    mobileAllowance.toString(),
    otherAllowances.toString(),
    bonus.toString(),
    commission.toString(),
    overtimeHours.toString(),
    overtimeHourlyRateOverride.encoded(),
    loanDeduction.toString(),
    absenceDeduction.toString(),
    unpaidLeaveDays.toString(),
    deductions.toString(),
    employeeType.name,
    employmentSector.name,
    contractType.name,
    gosiIncluded.toString(),
    resigned.toString(),
    joiningDateMillis.encoded(),
    calculationMonthMillis.encoded(),
    gosiRates.saudiEmployeeRate.toString(),
    gosiRates.saudiEmployerRate.toString(),
    gosiRates.expatEmployeeRate.toString(),
    gosiRates.expatEmployerHazardRate.toString(),
    gosiRates.contributionCapSar.toString(),
    ramadanReducedHours.toString()
  ).joinToString(SNAPSHOT_DELIMITER)
}

private fun String.decodeNetSalaryInputSnapshot(): NetSalaryInput? {
  val parts = split(SNAPSHOT_DELIMITER)
  if (parts.size < SNAPSHOT_FIELD_COUNT_LEGACY) return null

  fun String.nullableDouble() = if (this == SNAPSHOT_NULL) null else toDoubleOrNull()
  fun String.nullableLong() = if (this == SNAPSHOT_NULL) null else toLongOrNull()

  return runCatching {
    NetSalaryInput(
      employeeName = parts[0],
      jobTitle = parts[1],
      basicSalary = parts[2].toDoubleOrNull() ?: 0.0,
      housingAllowance = parts[3].toDoubleOrNull() ?: 0.0,
      transportAllowance = parts[4].toDoubleOrNull() ?: 0.0,
      foodAllowance = parts[5].toDoubleOrNull() ?: 0.0,
      mobileAllowance = parts[6].toDoubleOrNull() ?: 0.0,
      otherAllowances = parts[7].toDoubleOrNull() ?: 0.0,
      bonus = parts[8].toDoubleOrNull() ?: 0.0,
      commission = parts[9].toDoubleOrNull() ?: 0.0,
      overtimeHours = parts[10].toDoubleOrNull() ?: 0.0,
      overtimeHourlyRateOverride = parts[11].nullableDouble(),
      loanDeduction = parts[12].toDoubleOrNull() ?: 0.0,
      absenceDeduction = parts[13].toDoubleOrNull() ?: 0.0,
      unpaidLeaveDays = parts[14].toDoubleOrNull() ?: 0.0,
      deductions = parts[15].toDoubleOrNull() ?: 0.0,
      employeeType = runCatching { EmployeeType.valueOf(parts[16]) }.getOrDefault(EmployeeType.SAUDI),
      employmentSector = runCatching { EmploymentSector.valueOf(parts[17]) }.getOrDefault(EmploymentSector.PRIVATE),
      contractType = runCatching { ContractType.valueOf(parts[18]) }.getOrDefault(ContractType.UNLIMITED),
      gosiIncluded = parts[19].toBooleanStrictOrNull() ?: true,
      resigned = parts[20].toBooleanStrictOrNull() ?: false,
      joiningDateMillis = parts[21].nullableLong(),
      calculationMonthMillis = parts[22].nullableLong(),
      gosiRates = GosiRates(
        saudiEmployeeRate = parts[23].toDoubleOrNull() ?: 0.0975,
        saudiEmployerRate = parts[24].toDoubleOrNull() ?: 0.1175,
        expatEmployeeRate = parts[25].toDoubleOrNull() ?: 0.0,
        expatEmployerHazardRate = parts[26].toDoubleOrNull() ?: 0.02,
        contributionCapSar = parts.getOrNull(27)?.toDoubleOrNull() ?: 45000.0
      ),
      ramadanReducedHours = parts.getOrNull(28)?.toBooleanStrictOrNull() ?: false
    )
  }.getOrNull()
}
