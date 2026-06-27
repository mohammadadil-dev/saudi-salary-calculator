package com.saudi.salarycalculator.feature.calculator;

import androidx.lifecycle.ViewModel;
import com.saudi.salarycalculator.core.data.SalaryRepository;
import com.saudi.salarycalculator.core.model.CalculationRecord;
import com.saudi.salarycalculator.core.model.CalculationType;
import com.saudi.salarycalculator.core.model.EmployeeType;
import com.saudi.salarycalculator.core.model.EndOfServiceInput;
import com.saudi.salarycalculator.core.model.EndOfServiceResult;
import com.saudi.salarycalculator.core.model.GosiInput;
import com.saudi.salarycalculator.core.model.GosiRates;
import com.saudi.salarycalculator.core.model.NetSalaryInput;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import com.saudi.salarycalculator.core.model.OfferComparisonResult;
import com.saudi.salarycalculator.core.model.OfferInput;
import com.saudi.salarycalculator.core.model.SavingsInput;
import com.saudi.salarycalculator.core.model.SavingsResult;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rJ\u0006\u0010\u000f\u001a\u00020\rJ\u0006\u0010\u0010\u001a\u00020\rJ\u0006\u0010\u0011\u001a\u00020\rJ\u0016\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u0019J.\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u00162\u0006\u0010\u001f\u001a\u00020\u0016H\u0082@\u00a2\u0006\u0002\u0010 J\u000e\u0010!\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\"J\u0006\u0010#\u001a\u00020\rJ\u0006\u0010$\u001a\u00020\rR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006%"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/CalculatorViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/saudi/salarycalculator/core/data/SalaryRepository;", "(Lcom/saudi/salarycalculator/core/data/SalaryRepository;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/saudi/salarycalculator/feature/calculator/CalculatorUiState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "calculateEndOfService", "", "calculateNetSalary", "calculateSavings", "clearHistory", "compareOffers", "onFieldChange", "field", "Lcom/saudi/salarycalculator/feature/calculator/CalculatorField;", "value", "", "onSelectedTab", "index", "", "saveRecord", "type", "Lcom/saudi/salarycalculator/core/model/CalculationType;", "title", "input", "result", "(Lcom/saudi/salarycalculator/core/model/CalculationType;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setEmployeeType", "Lcom/saudi/salarycalculator/core/model/EmployeeType;", "toggleTheme", "toggleYearlyMode", "calculator_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class CalculatorViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.data.SalaryRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.saudi.salarycalculator.feature.calculator.CalculatorUiState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.saudi.salarycalculator.feature.calculator.CalculatorUiState> state = null;
    
    @javax.inject.Inject()
    public CalculatorViewModel(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.data.SalaryRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.saudi.salarycalculator.feature.calculator.CalculatorUiState> getState() {
        return null;
    }
    
    public final void onSelectedTab(int index) {
    }
    
    public final void onFieldChange(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.CalculatorField field, @org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void setEmployeeType(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.EmployeeType type) {
    }
    
    public final void calculateNetSalary() {
    }
    
    public final void calculateEndOfService() {
    }
    
    public final void compareOffers() {
    }
    
    public final void calculateSavings() {
    }
    
    public final void toggleYearlyMode() {
    }
    
    public final void toggleTheme() {
    }
    
    public final void clearHistory() {
    }
    
    private final java.lang.Object saveRecord(com.saudi.salarycalculator.core.model.CalculationType type, java.lang.String title, java.lang.String input, java.lang.String result, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}