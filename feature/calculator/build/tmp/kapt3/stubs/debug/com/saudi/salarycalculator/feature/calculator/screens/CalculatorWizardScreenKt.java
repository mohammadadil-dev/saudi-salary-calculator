package com.saudi.salarycalculator.feature.calculator.screens;

import androidx.compose.foundation.ExperimentalFoundationApi;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import com.saudi.salarycalculator.core.model.GosiRates;
import com.saudi.salarycalculator.feature.calculator.R;
import com.saudi.salarycalculator.feature.calculator.WizardFieldsState;
import com.saudi.salarycalculator.feature.calculator.WizardStep;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0092\u0001\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u001e\u0010\r\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u000e\u0012\u0004\u0012\u00020\u00010\u000e2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u00112\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0007\u00a8\u0006\u0015"}, d2 = {"CalculatorWizardScreen", "", "wizard", "Lcom/saudi/salarycalculator/feature/calculator/WizardFieldsState;", "wizardStep", "Lcom/saudi/salarycalculator/feature/calculator/WizardStep;", "gosiRates", "Lcom/saudi/salarycalculator/core/model/GosiRates;", "isCalculating", "", "darkMode", "currencySymbol", "", "onUpdateWizard", "Lkotlin/Function1;", "onGoToStep", "onCalculate", "Lkotlin/Function0;", "onExitWizard", "modifier", "Landroidx/compose/ui/Modifier;", "calculator_debug"})
public final class CalculatorWizardScreenKt {
    
    /**
     * The step-by-step calculator wizard: a [HorizontalPager] of the six steps from [WizardSteps.kt],
     * kept in sync with [wizardStep] (the source of truth, owned by SalaryViewModel) in both
     * directions — swiping the pager advances [onGoToStep], and tapping Next/Back or jumping via the
     * progress indicator animates the pager to match.
     */
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    public static final void CalculatorWizardScreen(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardStep wizardStep, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.GosiRates gosiRates, boolean isCalculating, boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String currencySymbol, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.WizardFieldsState, com.saudi.salarycalculator.feature.calculator.WizardFieldsState>, kotlin.Unit> onUpdateWizard, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.WizardStep, kotlin.Unit> onGoToStep, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCalculate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onExitWizard, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}