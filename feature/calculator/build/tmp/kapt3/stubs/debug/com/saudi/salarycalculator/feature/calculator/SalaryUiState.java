package com.saudi.salarycalculator.feature.calculator;

import com.saudi.salarycalculator.core.model.CalculationRecord;
import com.saudi.salarycalculator.core.model.ContractType;
import com.saudi.salarycalculator.core.model.EmployeeType;
import com.saudi.salarycalculator.core.model.EmploymentSector;
import com.saudi.salarycalculator.core.model.GosiRates;
import com.saudi.salarycalculator.core.model.NetSalaryInput;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import com.saudi.salarycalculator.core.model.OfferComparisonResult;
import com.saudi.salarycalculator.core.model.OfferInput;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0010\b\n\u0002\b\u0018\b\u0086\b\u0018\u00002\u00020\u0001B\u0095\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0012\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0015\u0012\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u001aJ\t\u00108\u001a\u00020\u0003H\u00c6\u0003J\t\u00109\u001a\u00020\u0012H\u00c6\u0003J\u000b\u0010:\u001a\u0004\u0018\u00010\u0015H\u00c6\u0003J\u000f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u00c6\u0003J\u000b\u0010<\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010=\u001a\u00020\u0005H\u00c6\u0003J\t\u0010>\u001a\u00020\u0007H\u00c6\u0003J\t\u0010?\u001a\u00020\tH\u00c6\u0003J\t\u0010@\u001a\u00020\u000bH\u00c6\u0003J\t\u0010A\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010B\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003J\u0010\u0010C\u001a\u0004\u0018\u00010\u0010H\u00c6\u0003\u00a2\u0006\u0002\u0010$J\t\u0010D\u001a\u00020\u0012H\u00c6\u0003J\u009e\u0001\u0010E\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00122\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001\u00a2\u0006\u0002\u0010FJ\u0013\u0010G\u001a\u00020\u00032\b\u0010H\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010I\u001a\u000203H\u00d6\u0001J\t\u0010J\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u001cR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0015\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u00a2\u0006\n\n\u0002\u0010%\u001a\u0004\b#\u0010$R\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0013\u0010\u0014\u001a\u0004\u0018\u00010\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0011\u0010\u0013\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010+R\u0013\u0010\u0019\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\"R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0011\u00102\u001a\u0002038F\u00a2\u0006\u0006\u001a\u0004\b4\u00105R\u0011\u00106\u001a\u0002038F\u00a2\u0006\u0006\u001a\u0004\b7\u00105\u00a8\u0006K"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/SalaryUiState;", "", "darkMode", "", "language", "", "gosiRates", "Lcom/saudi/salarycalculator/core/model/GosiRates;", "wizard", "Lcom/saudi/salarycalculator/feature/calculator/WizardFieldsState;", "wizardStep", "Lcom/saudi/salarycalculator/feature/calculator/WizardStep;", "isCalculating", "netSalaryResult", "Lcom/saudi/salarycalculator/core/model/NetSalaryResult;", "lastCalculatedAtMillis", "", "offerCurrent", "Lcom/saudi/salarycalculator/feature/calculator/OfferFormState;", "offerNew", "offerComparisonResult", "Lcom/saudi/salarycalculator/core/model/OfferComparisonResult;", "history", "", "Lcom/saudi/salarycalculator/core/model/CalculationRecord;", "toastMessage", "(ZLjava/lang/String;Lcom/saudi/salarycalculator/core/model/GosiRates;Lcom/saudi/salarycalculator/feature/calculator/WizardFieldsState;Lcom/saudi/salarycalculator/feature/calculator/WizardStep;ZLcom/saudi/salarycalculator/core/model/NetSalaryResult;Ljava/lang/Long;Lcom/saudi/salarycalculator/feature/calculator/OfferFormState;Lcom/saudi/salarycalculator/feature/calculator/OfferFormState;Lcom/saudi/salarycalculator/core/model/OfferComparisonResult;Ljava/util/List;Ljava/lang/String;)V", "getDarkMode", "()Z", "getGosiRates", "()Lcom/saudi/salarycalculator/core/model/GosiRates;", "getHistory", "()Ljava/util/List;", "getLanguage", "()Ljava/lang/String;", "getLastCalculatedAtMillis", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getNetSalaryResult", "()Lcom/saudi/salarycalculator/core/model/NetSalaryResult;", "getOfferComparisonResult", "()Lcom/saudi/salarycalculator/core/model/OfferComparisonResult;", "getOfferCurrent", "()Lcom/saudi/salarycalculator/feature/calculator/OfferFormState;", "getOfferNew", "getToastMessage", "getWizard", "()Lcom/saudi/salarycalculator/feature/calculator/WizardFieldsState;", "getWizardStep", "()Lcom/saudi/salarycalculator/feature/calculator/WizardStep;", "wizardStepCount", "", "getWizardStepCount", "()I", "wizardStepIndex", "getWizardStepIndex", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(ZLjava/lang/String;Lcom/saudi/salarycalculator/core/model/GosiRates;Lcom/saudi/salarycalculator/feature/calculator/WizardFieldsState;Lcom/saudi/salarycalculator/feature/calculator/WizardStep;ZLcom/saudi/salarycalculator/core/model/NetSalaryResult;Ljava/lang/Long;Lcom/saudi/salarycalculator/feature/calculator/OfferFormState;Lcom/saudi/salarycalculator/feature/calculator/OfferFormState;Lcom/saudi/salarycalculator/core/model/OfferComparisonResult;Ljava/util/List;Ljava/lang/String;)Lcom/saudi/salarycalculator/feature/calculator/SalaryUiState;", "equals", "other", "hashCode", "toString", "calculator_debug"})
public final class SalaryUiState {
    private final boolean darkMode = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String language = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.model.GosiRates gosiRates = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.feature.calculator.WizardStep wizardStep = null;
    private final boolean isCalculating = false;
    @org.jetbrains.annotations.Nullable()
    private final com.saudi.salarycalculator.core.model.NetSalaryResult netSalaryResult = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long lastCalculatedAtMillis = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.feature.calculator.OfferFormState offerCurrent = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.feature.calculator.OfferFormState offerNew = null;
    @org.jetbrains.annotations.Nullable()
    private final com.saudi.salarycalculator.core.model.OfferComparisonResult offerComparisonResult = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> history = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String toastMessage = null;
    
    public SalaryUiState(boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String language, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.GosiRates gosiRates, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardStep wizardStep, boolean isCalculating, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.NetSalaryResult netSalaryResult, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastCalculatedAtMillis, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.OfferFormState offerCurrent, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.OfferFormState offerNew, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.OfferComparisonResult offerComparisonResult, @org.jetbrains.annotations.NotNull()
    java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> history, @org.jetbrains.annotations.Nullable()
    java.lang.String toastMessage) {
        super();
    }
    
    public final boolean getDarkMode() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLanguage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.GosiRates getGosiRates() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.WizardFieldsState getWizard() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.WizardStep getWizardStep() {
        return null;
    }
    
    public final boolean isCalculating() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.NetSalaryResult getNetSalaryResult() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getLastCalculatedAtMillis() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.OfferFormState getOfferCurrent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.OfferFormState getOfferNew() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.OfferComparisonResult getOfferComparisonResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> getHistory() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getToastMessage() {
        return null;
    }
    
    public final int getWizardStepIndex() {
        return 0;
    }
    
    public final int getWizardStepCount() {
        return 0;
    }
    
    public SalaryUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.OfferFormState component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.OfferComparisonResult component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.GosiRates component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.WizardFieldsState component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.WizardStep component5() {
        return null;
    }
    
    public final boolean component6() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.NetSalaryResult component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.OfferFormState component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.SalaryUiState copy(boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String language, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.GosiRates gosiRates, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardStep wizardStep, boolean isCalculating, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.NetSalaryResult netSalaryResult, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastCalculatedAtMillis, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.OfferFormState offerCurrent, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.OfferFormState offerNew, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.OfferComparisonResult offerComparisonResult, @org.jetbrains.annotations.NotNull()
    java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> history, @org.jetbrains.annotations.Nullable()
    java.lang.String toastMessage) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}