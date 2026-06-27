package com.saudi.salarycalculator.feature.calculator;

import androidx.lifecycle.ViewModel;
import com.saudi.salarycalculator.core.data.SalaryRepository;
import com.saudi.salarycalculator.core.model.CalculationRecord;
import com.saudi.salarycalculator.core.model.CalculationType;
import com.saudi.salarycalculator.core.model.GosiRates;
import com.saudi.salarycalculator.core.model.NetSalaryInput;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import kotlinx.coroutines.flow.StateFlow;

/**
 * Single shared ViewModel for the whole calculator feature: one instance is obtained at the
 * NavHost level (see SalaryNavGraph) and handed to every destination, so wizard progress,
 * the last result, and the offer comparison all survive back-stack navigation between the
 * bottom-nav tabs without being recomputed or lost.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\f\u001a\u00020\r2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000fJ\u0006\u0010\u0010\u001a\u00020\rJ\u0006\u0010\u0011\u001a\u00020\rJ\u000e\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u0014J\u0006\u0010\u0015\u001a\u00020\rJ\u000e\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u0010\u001c\u001a\u00020\u001dJ\u0006\u0010\u001e\u001a\u00020\u001dJ\u0006\u0010\u001f\u001a\u00020\rJ:\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u00142\u0006\u0010$\u001a\u00020\u00142\u0006\u0010%\u001a\u00020\u00142\n\b\u0002\u0010&\u001a\u0004\u0018\u00010\'H\u0082@\u00a2\u0006\u0002\u0010(J\u000e\u0010)\u001a\u00020\r2\u0006\u0010*\u001a\u00020\u0014J\u000e\u0010+\u001a\u00020\r2\u0006\u0010,\u001a\u00020\u0014J\u0006\u0010-\u001a\u00020\rJ\u001a\u0010.\u001a\u00020\r2\u0012\u0010/\u001a\u000e\u0012\u0004\u0012\u000201\u0012\u0004\u0012\u00020100J\u001a\u00102\u001a\u00020\r2\u0012\u0010/\u001a\u000e\u0012\u0004\u0012\u000203\u0012\u0004\u0012\u00020300J\u001a\u00104\u001a\u00020\r2\u0012\u0010/\u001a\u000e\u0012\u0004\u0012\u000203\u0012\u0004\u0012\u00020300J\u001a\u00105\u001a\u00020\r2\u0012\u0010/\u001a\u000e\u0012\u0004\u0012\u000206\u0012\u0004\u0012\u00020600R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u00067"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/SalaryViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/saudi/salarycalculator/core/data/SalaryRepository;", "(Lcom/saudi/salarycalculator/core/data/SalaryRepository;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/saudi/salarycalculator/feature/calculator/SalaryUiState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "calculateNetSalary", "", "onComplete", "Lkotlin/Function0;", "clearHistory", "compareOffers", "deleteRecord", "id", "", "dismissToast", "editRecord", "record", "Lcom/saudi/salarycalculator/core/model/CalculationRecord;", "goToWizardStep", "step", "Lcom/saudi/salarycalculator/feature/calculator/WizardStep;", "nextWizardStep", "", "previousWizardStep", "resetWizard", "saveRecord", "type", "Lcom/saudi/salarycalculator/core/model/CalculationType;", "title", "inputSummary", "resultSummary", "netSalaryInput", "Lcom/saudi/salarycalculator/core/model/NetSalaryInput;", "(Lcom/saudi/salarycalculator/core/model/CalculationType;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/saudi/salarycalculator/core/model/NetSalaryInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setLanguage", "language", "showToast", "message", "toggleDarkMode", "updateGosiRates", "transform", "Lkotlin/Function1;", "Lcom/saudi/salarycalculator/core/model/GosiRates;", "updateOfferCurrent", "Lcom/saudi/salarycalculator/feature/calculator/OfferFormState;", "updateOfferNew", "updateWizard", "Lcom/saudi/salarycalculator/feature/calculator/WizardFieldsState;", "calculator_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SalaryViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.data.SalaryRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.saudi.salarycalculator.feature.calculator.SalaryUiState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.saudi.salarycalculator.feature.calculator.SalaryUiState> state = null;
    
    @javax.inject.Inject()
    public SalaryViewModel(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.data.SalaryRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.saudi.salarycalculator.feature.calculator.SalaryUiState> getState() {
        return null;
    }
    
    public final void updateWizard(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.WizardFieldsState, com.saudi.salarycalculator.feature.calculator.WizardFieldsState> transform) {
    }
    
    public final void goToWizardStep(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardStep step) {
    }
    
    public final boolean nextWizardStep() {
        return false;
    }
    
    public final boolean previousWizardStep() {
        return false;
    }
    
    public final void resetWizard() {
    }
    
    /**
     * Runs the net-salary calculation from the current wizard state. [onComplete] lets the
     * Review screen navigate to the Result destination only after the result is actually ready.
     */
    public final void calculateNetSalary(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onComplete) {
    }
    
    /**
     * Reopens a saved [CalculationRecord] into the wizard for editing. Only records that carry a
     * structured [NetSalaryInput] snapshot (currently NET_SALARY records) support this.
     */
    public final void editRecord(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.CalculationRecord record) {
    }
    
    public final void deleteRecord(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }
    
    public final void updateOfferCurrent(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.OfferFormState, com.saudi.salarycalculator.feature.calculator.OfferFormState> transform) {
    }
    
    public final void updateOfferNew(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.OfferFormState, com.saudi.salarycalculator.feature.calculator.OfferFormState> transform) {
    }
    
    public final void compareOffers() {
    }
    
    public final void toggleDarkMode() {
    }
    
    public final void setLanguage(@org.jetbrains.annotations.NotNull()
    java.lang.String language) {
    }
    
    public final void updateGosiRates(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.core.model.GosiRates, com.saudi.salarycalculator.core.model.GosiRates> transform) {
    }
    
    public final void clearHistory() {
    }
    
    public final void dismissToast() {
    }
    
    /**
     * Surfaces a one-off confirmation message (e.g. "PDF exported"). Consumed by a Snackbar at
     * the NavGraph level, which calls [dismissToast] once it has been shown.
     */
    public final void showToast(@org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    private final java.lang.Object saveRecord(com.saudi.salarycalculator.core.model.CalculationType type, java.lang.String title, java.lang.String inputSummary, java.lang.String resultSummary, com.saudi.salarycalculator.core.model.NetSalaryInput netSalaryInput, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}