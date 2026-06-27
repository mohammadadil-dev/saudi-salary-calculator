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

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000H\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a&\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0002\u001a\f\u0010\b\u001a\u00020\u0001*\u00020\tH\u0002\u001a\u0016\u0010\n\u001a\u00020\t*\u00020\u00012\b\b\u0002\u0010\u000b\u001a\u00020\tH\u0002\u001a\f\u0010\f\u001a\u00020\r*\u00020\u000eH\u0002\u001a\f\u0010\u000f\u001a\u00020\u0010*\u00020\u000eH\u0002\u001a\u0014\u0010\u0011\u001a\u00020\u0012*\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u0014H\u0002\u001a\u001c\u0010\u0015\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0001H\u0002\u00a8\u0006\u0019"}, d2 = {"buildReportPreview", "", "salary", "Lcom/saudi/salarycalculator/core/model/NetSalaryResult;", "offer", "Lcom/saudi/salarycalculator/core/model/OfferComparisonResult;", "savings", "Lcom/saudi/salarycalculator/core/model/SavingsResult;", "formatSar", "", "toDoubleValue", "default", "toGosiRates", "Lcom/saudi/salarycalculator/core/model/GosiRates;", "Lcom/saudi/salarycalculator/feature/calculator/CalculatorUiState;", "toNetSalaryInput", "Lcom/saudi/salarycalculator/core/model/NetSalaryInput;", "toOfferInput", "Lcom/saudi/salarycalculator/core/model/OfferInput;", "isNewOffer", "", "updateField", "field", "Lcom/saudi/salarycalculator/feature/calculator/CalculatorField;", "value", "calculator_debug"})
public final class CalculatorViewModelKt {
    
    private static final com.saudi.salarycalculator.feature.calculator.CalculatorUiState updateField(com.saudi.salarycalculator.feature.calculator.CalculatorUiState $this$updateField, com.saudi.salarycalculator.feature.calculator.CalculatorField field, java.lang.String value) {
        return null;
    }
    
    private static final com.saudi.salarycalculator.core.model.GosiRates toGosiRates(com.saudi.salarycalculator.feature.calculator.CalculatorUiState $this$toGosiRates) {
        return null;
    }
    
    private static final com.saudi.salarycalculator.core.model.NetSalaryInput toNetSalaryInput(com.saudi.salarycalculator.feature.calculator.CalculatorUiState $this$toNetSalaryInput) {
        return null;
    }
    
    private static final com.saudi.salarycalculator.core.model.OfferInput toOfferInput(com.saudi.salarycalculator.feature.calculator.CalculatorUiState $this$toOfferInput, boolean isNewOffer) {
        return null;
    }
    
    private static final java.lang.String buildReportPreview(com.saudi.salarycalculator.core.model.NetSalaryResult salary, com.saudi.salarycalculator.core.model.OfferComparisonResult offer, com.saudi.salarycalculator.core.model.SavingsResult savings) {
        return null;
    }
    
    private static final double toDoubleValue(java.lang.String $this$toDoubleValue, double p1_772401952) {
        return 0.0;
    }
    
    private static final java.lang.String formatSar(double $this$formatSar) {
        return null;
    }
}