package com.saudi.salarycalculator.feature.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saudi.salarycalculator.core.data.SalaryRepository
import com.saudi.salarycalculator.core.model.CalculationRecord
import com.saudi.salarycalculator.core.model.CalculationType
import com.saudi.salarycalculator.core.model.EmployeeType
import com.saudi.salarycalculator.core.model.EndOfServiceInput
import com.saudi.salarycalculator.core.model.EndOfServiceResult
import com.saudi.salarycalculator.core.model.GosiInput
import com.saudi.salarycalculator.core.model.GosiRates
import com.saudi.salarycalculator.core.model.NetSalaryInput
import com.saudi.salarycalculator.core.model.NetSalaryResult
import com.saudi.salarycalculator.core.model.OfferComparisonResult
import com.saudi.salarycalculator.core.model.OfferInput
import com.saudi.salarycalculator.core.model.SavingsInput
import com.saudi.salarycalculator.core.model.SavingsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class CalculatorViewModel @Inject constructor(
  private val repository: SalaryRepository
) : ViewModel() {
  private val _state = MutableStateFlow(CalculatorUiState())
  val state: StateFlow<CalculatorUiState> = _state.asStateFlow()

  init {
    repository.observeHistory().onEach { history ->
      _state.value = _state.value.copy(history = history)
    }.launchIn(viewModelScope)

    repository.observeSelectedTab().onEach { tab ->
      _state.value = _state.value.copy(selectedTab = tab.coerceIn(0, 7))
    }.launchIn(viewModelScope)

    repository.observeLanguage().onEach { language ->
      _state.value = _state.value.copy(language = language)
    }.launchIn(viewModelScope)
  }

  fun onSelectedTab(index: Int) {
    _state.value = _state.value.copy(selectedTab = index)
    viewModelScope.launch { repository.setSelectedTab(index) }
  }

  fun onFieldChange(field: CalculatorField, value: String) {
    _state.value = _state.value.updateField(field, value)
    if (field == CalculatorField.LANGUAGE) {
      viewModelScope.launch { repository.setLanguage(value) }
    }
  }

  fun setEmployeeType(type: EmployeeType) {
    _state.value = _state.value.copy(employeeType = type)
  }

  fun calculateNetSalary() {
    val s = _state.value
    viewModelScope.launch {
      val input = s.toNetSalaryInput()
      val result = repository.calculateNetSalary(input)
      val gosi = repository.calculateGosi(
        GosiInput(
          baseAmount = input.basicSalary + input.housingAllowance,
          employeeType = input.employeeType,
          rates = input.gosiRates
        )
      )
      _state.value = _state.value.copy(
        selectedTab = 2,
        netSalaryResult = result,
        calculationAck = "Take-home salary calculated",
        reportPreview = buildReportPreview(result, _state.value.offerComparisonResult, _state.value.savingsResult)
      )
      repository.setSelectedTab(2)
      saveRecord(CalculationType.NET_SALARY, "Salary Dashboard", "Gross=${result.grossSalary}", "Net=${result.netSalary}, GOSI=${gosi.employeeContribution}")
    }
  }

  fun calculateEndOfService() {
    val s = _state.value
    viewModelScope.launch {
      val result = repository.calculateEndOfService(
        EndOfServiceInput(
          lastBasicSalary = s.eosLastSalary.toDoubleValue(),
          yearsOfService = s.eosYears.toDoubleValue(),
          resigned = s.eosResigned
        )
      )
      _state.value = _state.value.copy(endOfServiceResult = result)
      saveRecord(CalculationType.END_OF_SERVICE, "End of Service", "Years=${s.eosYears}", "Reward=${result.rewardAmount}")
    }
  }

  fun compareOffers() {
    val s = _state.value
    viewModelScope.launch {
      val result = repository.compareOffers(
        offerA = s.toOfferInput(isNewOffer = false),
        offerB = s.toOfferInput(isNewOffer = true)
      )
      _state.value = _state.value.copy(
        offerComparisonResult = result,
        reportPreview = buildReportPreview(_state.value.netSalaryResult, result, _state.value.savingsResult)
      )
      saveRecord(CalculationType.OFFER_COMPARISON, "Offer Comparison", "${result.offerA.title} vs ${result.offerB.title}", "Score=${result.acceptanceScore}")
    }
  }

  fun calculateSavings() {
    val s = _state.value
    viewModelScope.launch {
      val net = s.netSalaryResult?.netSalary ?: s.savingsNetSalary.toDoubleValue()
      val result = repository.calculateSavings(
        SavingsInput(
          netSalary = net,
          rent = s.rentExpense.toDoubleValue(),
          food = s.foodExpense.toDoubleValue(),
          transport = s.transportExpense.toDoubleValue(),
          familyExpense = s.familyExpense.toDoubleValue(),
          otherExpense = s.otherExpense.toDoubleValue()
        )
      )
      _state.value = _state.value.copy(
        savingsResult = result,
        reportPreview = buildReportPreview(_state.value.netSalaryResult, _state.value.offerComparisonResult, result)
      )
      saveRecord(CalculationType.SAVINGS_PLAN, "Savings Planner", "Expenses=${result.totalExpenses}", "Savings=${result.monthlySavings}")
    }
  }

  fun toggleYearlyMode() {
    _state.value = _state.value.copy(showYearly = !_state.value.showYearly)
  }

  fun toggleTheme() {
    _state.value = _state.value.copy(darkMode = !_state.value.darkMode)
  }

  fun clearHistory() {
    viewModelScope.launch { repository.clearHistory() }
  }

  private suspend fun saveRecord(type: CalculationType, title: String, input: String, result: String) {
    repository.saveRecord(
      CalculationRecord(
        type = type,
        title = title,
        inputSummary = input,
        resultSummary = result,
        createdAtMillis = System.currentTimeMillis()
      )
    )
  }
}

data class CalculatorUiState(
  val selectedTab: Int = 0,
  val showYearly: Boolean = false,
  val darkMode: Boolean = false,
  val language: String = "en",
  val employeeType: EmployeeType = EmployeeType.SAUDI,
  val basicSalary: String = "",
  val housingAllowance: String = "",
  val transportAllowance: String = "",
  val foodAllowance: String = "",
  val mobileAllowance: String = "",
  val otherAllowances: String = "",
  val deductions: String = "",
  val saudiEmployeeGosiRate: String = "0.0975",
  val saudiEmployerGosiRate: String = "0.1175",
  val expatEmployeeGosiRate: String = "0.0",
  val expatEmployerHazardRate: String = "0.02",
  val eosLastSalary: String = "",
  val eosYears: String = "",
  val eosResigned: Boolean = false,
  val offerATitle: String = "Current offer",
  val offerABasic: String = "",
  val offerAHousing: String = "",
  val offerATransport: String = "",
  val offerAFood: String = "",
  val offerAMobile: String = "",
  val offerAOther: String = "",
  val offerADeduction: String = "",
  val offerBTitle: String = "New offer",
  val offerBBasic: String = "",
  val offerBHousing: String = "",
  val offerBTransport: String = "",
  val offerBFood: String = "",
  val offerBMobile: String = "",
  val offerBOther: String = "",
  val offerBDeduction: String = "",
  val savingsNetSalary: String = "",
  val rentExpense: String = "",
  val foodExpense: String = "",
  val transportExpense: String = "",
  val familyExpense: String = "",
  val otherExpense: String = "",
  val netSalaryResult: NetSalaryResult? = null,
  val endOfServiceResult: EndOfServiceResult? = null,
  val offerComparisonResult: OfferComparisonResult? = null,
  val savingsResult: SavingsResult? = null,
  val calculationAck: String? = null,
  val reportPreview: String = "Calculate a salary to generate a PDF-ready report preview.",
  val history: List<CalculationRecord> = emptyList()
)

enum class CalculatorField {
  BASIC_SALARY,
  HOUSING_ALLOWANCE,
  TRANSPORT_ALLOWANCE,
  FOOD_ALLOWANCE,
  MOBILE_ALLOWANCE,
  OTHER_ALLOWANCES,
  DEDUCTIONS,
  SAUDI_EMPLOYEE_GOSI_RATE,
  SAUDI_EMPLOYER_GOSI_RATE,
  EXPAT_EMPLOYEE_GOSI_RATE,
  EXPAT_EMPLOYER_HAZARD_RATE,
  EOS_LAST_SALARY,
  EOS_YEARS,
  EOS_RESIGNED,
  OFFER_A_TITLE,
  OFFER_A_BASIC,
  OFFER_A_HOUSING,
  OFFER_A_TRANSPORT,
  OFFER_A_FOOD,
  OFFER_A_MOBILE,
  OFFER_A_OTHER,
  OFFER_A_DEDUCTION,
  OFFER_B_TITLE,
  OFFER_B_BASIC,
  OFFER_B_HOUSING,
  OFFER_B_TRANSPORT,
  OFFER_B_FOOD,
  OFFER_B_MOBILE,
  OFFER_B_OTHER,
  OFFER_B_DEDUCTION,
  SAVINGS_NET_SALARY,
  RENT_EXPENSE,
  FOOD_EXPENSE,
  TRANSPORT_EXPENSE,
  FAMILY_EXPENSE,
  OTHER_EXPENSE,
  LANGUAGE
}

private fun CalculatorUiState.updateField(field: CalculatorField, value: String): CalculatorUiState = when (field) {
  CalculatorField.BASIC_SALARY -> copy(basicSalary = value)
  CalculatorField.HOUSING_ALLOWANCE -> copy(housingAllowance = value)
  CalculatorField.TRANSPORT_ALLOWANCE -> copy(transportAllowance = value)
  CalculatorField.FOOD_ALLOWANCE -> copy(foodAllowance = value)
  CalculatorField.MOBILE_ALLOWANCE -> copy(mobileAllowance = value)
  CalculatorField.OTHER_ALLOWANCES -> copy(otherAllowances = value)
  CalculatorField.DEDUCTIONS -> copy(deductions = value)
  CalculatorField.SAUDI_EMPLOYEE_GOSI_RATE -> copy(saudiEmployeeGosiRate = value)
  CalculatorField.SAUDI_EMPLOYER_GOSI_RATE -> copy(saudiEmployerGosiRate = value)
  CalculatorField.EXPAT_EMPLOYEE_GOSI_RATE -> copy(expatEmployeeGosiRate = value)
  CalculatorField.EXPAT_EMPLOYER_HAZARD_RATE -> copy(expatEmployerHazardRate = value)
  CalculatorField.EOS_LAST_SALARY -> copy(eosLastSalary = value)
  CalculatorField.EOS_YEARS -> copy(eosYears = value)
  CalculatorField.EOS_RESIGNED -> copy(eosResigned = value.toBoolean())
  CalculatorField.OFFER_A_TITLE -> copy(offerATitle = value)
  CalculatorField.OFFER_A_BASIC -> copy(offerABasic = value)
  CalculatorField.OFFER_A_HOUSING -> copy(offerAHousing = value)
  CalculatorField.OFFER_A_TRANSPORT -> copy(offerATransport = value)
  CalculatorField.OFFER_A_FOOD -> copy(offerAFood = value)
  CalculatorField.OFFER_A_MOBILE -> copy(offerAMobile = value)
  CalculatorField.OFFER_A_OTHER -> copy(offerAOther = value)
  CalculatorField.OFFER_A_DEDUCTION -> copy(offerADeduction = value)
  CalculatorField.OFFER_B_TITLE -> copy(offerBTitle = value)
  CalculatorField.OFFER_B_BASIC -> copy(offerBBasic = value)
  CalculatorField.OFFER_B_HOUSING -> copy(offerBHousing = value)
  CalculatorField.OFFER_B_TRANSPORT -> copy(offerBTransport = value)
  CalculatorField.OFFER_B_FOOD -> copy(offerBFood = value)
  CalculatorField.OFFER_B_MOBILE -> copy(offerBMobile = value)
  CalculatorField.OFFER_B_OTHER -> copy(offerBOther = value)
  CalculatorField.OFFER_B_DEDUCTION -> copy(offerBDeduction = value)
  CalculatorField.SAVINGS_NET_SALARY -> copy(savingsNetSalary = value)
  CalculatorField.RENT_EXPENSE -> copy(rentExpense = value)
  CalculatorField.FOOD_EXPENSE -> copy(foodExpense = value)
  CalculatorField.TRANSPORT_EXPENSE -> copy(transportExpense = value)
  CalculatorField.FAMILY_EXPENSE -> copy(familyExpense = value)
  CalculatorField.OTHER_EXPENSE -> copy(otherExpense = value)
  CalculatorField.LANGUAGE -> copy(language = value)
}

private fun CalculatorUiState.toGosiRates(): GosiRates =
  GosiRates(
    saudiEmployeeRate = saudiEmployeeGosiRate.toDoubleValue(0.0975),
    saudiEmployerRate = saudiEmployerGosiRate.toDoubleValue(0.1175),
    expatEmployeeRate = expatEmployeeGosiRate.toDoubleValue(0.0),
    expatEmployerHazardRate = expatEmployerHazardRate.toDoubleValue(0.02)
  )

private fun CalculatorUiState.toNetSalaryInput(): NetSalaryInput =
  NetSalaryInput(
    basicSalary = basicSalary.toDoubleValue(),
    housingAllowance = housingAllowance.toDoubleValue(),
    transportAllowance = transportAllowance.toDoubleValue(),
    foodAllowance = foodAllowance.toDoubleValue(),
    mobileAllowance = mobileAllowance.toDoubleValue(),
    otherAllowances = otherAllowances.toDoubleValue(),
    deductions = deductions.toDoubleValue(),
    employeeType = employeeType,
    gosiRates = toGosiRates()
  )

private fun CalculatorUiState.toOfferInput(isNewOffer: Boolean): OfferInput =
  OfferInput(
    title = if (isNewOffer) "New offer" else "Current offer",
    basicSalary = if (isNewOffer) offerBBasic.toDoubleValue() else offerABasic.toDoubleValue(),
    housingAllowance = if (isNewOffer) offerBHousing.toDoubleValue() else offerAHousing.toDoubleValue(),
    transportAllowance = if (isNewOffer) offerBTransport.toDoubleValue() else offerATransport.toDoubleValue(),
    foodAllowance = if (isNewOffer) offerBFood.toDoubleValue() else offerAFood.toDoubleValue(),
    mobileAllowance = if (isNewOffer) offerBMobile.toDoubleValue() else offerAMobile.toDoubleValue(),
    otherAllowances = if (isNewOffer) offerBOther.toDoubleValue() else offerAOther.toDoubleValue(),
    deductions = if (isNewOffer) offerBDeduction.toDoubleValue() else offerADeduction.toDoubleValue(),
    employeeType = employeeType,
    gosiRates = toGosiRates()
  )

private fun buildReportPreview(
  salary: NetSalaryResult?,
  offer: OfferComparisonResult?,
  savings: SavingsResult?
): String = buildString {
  appendLine("Saudi Salary Calculator Report")
  appendLine()
  salary?.let {
    appendLine("Take-home: SAR ${it.netSalary.formatSar()}")
    appendLine("Gross salary: SAR ${it.grossSalary.formatSar()}")
    appendLine("Employee GOSI: SAR ${it.employeeGosiAmount.formatSar()}")
    appendLine("Employer monthly cost: SAR ${it.employerMonthlyCost.formatSar()}")
    appendLine("Yearly net salary: SAR ${it.yearlyNetSalary.formatSar()}")
  }
  offer?.let {
    appendLine()
    appendLine("Offer score: ${it.acceptanceScore}/100 (${it.scoreLabel})")
    appendLine("Monthly difference: SAR ${it.monthlyDifference.formatSar()}")
  }
  savings?.let {
    appendLine()
    appendLine("Monthly savings: SAR ${it.monthlySavings.formatSar()}")
    appendLine("Savings rate: ${it.savingsRate}%")
  }
}

private fun String.toDoubleValue(default: Double = 0.0): Double = toDoubleOrNull() ?: default

private fun Double.formatSar(): String = "%,.2f".format(this)
