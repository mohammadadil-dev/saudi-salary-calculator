package com.saudi.salarycalculator.feature.calculator.screens;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.SheetState;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.unit.LayoutDirection;
import com.saudi.salarycalculator.core.calculator.DefaultEndOfServiceCalculatorService;
import com.saudi.salarycalculator.core.calculator.DefaultGosiCalculatorService;
import com.saudi.salarycalculator.core.model.ContractType;
import com.saudi.salarycalculator.core.model.EmployeeType;
import com.saudi.salarycalculator.core.model.EmploymentSector;
import com.saudi.salarycalculator.core.model.EndOfServiceInput;
import com.saudi.salarycalculator.core.model.GosiInput;
import com.saudi.salarycalculator.core.model.GosiRates;
import com.saudi.salarycalculator.feature.calculator.R;
import com.saudi.salarycalculator.feature.calculator.WizardFieldsState;
import java.text.SimpleDateFormat;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000T\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aM\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0003\u00a2\u0006\u0002\u0010\r\u001aA\u0010\u000e\u001a\u00020\u00012\b\u0010\u000f\u001a\u0004\u0018\u00010\u00062\u0006\u0010\n\u001a\u00020\t2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0012H\u0003\u00a2\u0006\u0002\u0010\u0013\u001a.\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00032\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u0018H\u0003\u001aJ\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00032\u001e\u0010\u001d\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001c0\u0012\u0012\u0004\u0012\u00020\u00010\u00122\b\b\u0002\u0010\u001e\u001a\u00020\u001fH\u0007\u001aJ\u0010 \u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00032\u001e\u0010\u001d\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001c0\u0012\u0012\u0004\u0012\u00020\u00010\u00122\b\b\u0002\u0010\u001e\u001a\u00020\u001fH\u0007\u001aJ\u0010!\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00032\u001e\u0010\u001d\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001c0\u0012\u0012\u0004\u0012\u00020\u00010\u00122\b\b\u0002\u0010\u001e\u001a\u00020\u001fH\u0007\u001aB\u0010\"\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\n\u001a\u00020\t2\u001e\u0010\u001d\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001c0\u0012\u0012\u0004\u0012\u00020\u00010\u00122\b\b\u0002\u0010\u001e\u001a\u00020\u001fH\u0007\u001aR\u0010#\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00032\u0006\u0010$\u001a\u00020%2\u001e\u0010\u001d\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001c0\u0012\u0012\u0004\u0012\u00020\u00010\u00122\b\b\u0002\u0010\u001e\u001a\u00020\u001fH\u0007\u001a@\u0010&\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00032\u0006\u0010\'\u001a\u00020\t2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\b\b\u0002\u0010\u001e\u001a\u00020\u001fH\u0007\u00a8\u0006)"}, d2 = {"DateField", "", "label", "", "actionLabel", "millis", "", "pattern", "monthOnly", "", "darkMode", "onClick", "Lkotlin/Function0;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/String;ZZLkotlin/jvm/functions/Function0;)V", "DatePickerSheet", "initialMillis", "onDismiss", "onConfirm", "Lkotlin/Function1;", "(Ljava/lang/Long;ZLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "ReviewSection", "title", "currencySymbol", "rows", "", "Lcom/saudi/salarycalculator/feature/calculator/screens/ReviewRow;", "StepAllowancesContent", "wizard", "Lcom/saudi/salarycalculator/feature/calculator/WizardFieldsState;", "onUpdate", "modifier", "Landroidx/compose/ui/Modifier;", "StepBasicSalaryContent", "StepDeductionsContent", "StepEmploymentDetailsContent", "StepGosiEosbContent", "gosiRates", "Lcom/saudi/salarycalculator/core/model/GosiRates;", "StepReviewContent", "isCalculating", "onCalculate", "calculator_debug"})
public final class WizardStepsKt {
    
    /**
     * The six step bodies of the calculator wizard. Each takes the raw [WizardFieldsState] plus an
     * [onUpdate] transform-callback so every field edit flows back through [SalaryViewModel.updateWizard]
     * without these composables needing to know about the ViewModel itself.
     */
    @androidx.compose.runtime.Composable()
    public static final void StepBasicSalaryContent(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String currencySymbol, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.WizardFieldsState, com.saudi.salarycalculator.feature.calculator.WizardFieldsState>, kotlin.Unit> onUpdate, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void StepAllowancesContent(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String currencySymbol, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.WizardFieldsState, com.saudi.salarycalculator.feature.calculator.WizardFieldsState>, kotlin.Unit> onUpdate, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void StepDeductionsContent(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String currencySymbol, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.WizardFieldsState, com.saudi.salarycalculator.feature.calculator.WizardFieldsState>, kotlin.Unit> onUpdate, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void StepEmploymentDetailsContent(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, boolean darkMode, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.WizardFieldsState, com.saudi.salarycalculator.feature.calculator.WizardFieldsState>, kotlin.Unit> onUpdate, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DateField(java.lang.String label, java.lang.String actionLabel, java.lang.Long millis, java.lang.String pattern, boolean monthOnly, boolean darkMode, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void DatePickerSheet(java.lang.Long initialMillis, boolean darkMode, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onConfirm) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void StepGosiEosbContent(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String currencySymbol, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.GosiRates gosiRates, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.WizardFieldsState, com.saudi.salarycalculator.feature.calculator.WizardFieldsState>, kotlin.Unit> onUpdate, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void StepReviewContent(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String currencySymbol, boolean isCalculating, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCalculate, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReviewSection(java.lang.String title, boolean darkMode, java.lang.String currencySymbol, java.util.List<com.saudi.salarycalculator.feature.calculator.screens.ReviewRow> rows) {
    }
}