package com.saudi.salarycalculator.feature.calculator.screens;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.saudi.salarycalculator.core.designsystem.components.BreakdownSlice;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import com.saudi.salarycalculator.feature.calculator.R;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001a\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0003\u001aH\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u00a8\u0006\u000e"}, d2 = {"ResultEmptyState", "", "darkMode", "", "modifier", "Landroidx/compose/ui/Modifier;", "ResultScreen", "currencySymbol", "", "result", "Lcom/saudi/salarycalculator/core/model/NetSalaryResult;", "onViewPayslip", "Lkotlin/Function0;", "onCompareOffer", "calculator_debug"})
public final class ResultScreenKt {
    
    /**
     * Result dashboard: shown right after [com.saudi.salarycalculator.feature.calculator.SalaryViewModel.calculateNetSalary]
     * completes, and reachable again any time from Home/history while [result] is non-null. A
     * Monthly/Yearly [SegmentedControl] re-scales every figure on the screen from the same
     * [NetSalaryResult] without re-running the calculation.
     */
    @androidx.compose.runtime.Composable()
    public static final void ResultScreen(boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String currencySymbol, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.NetSalaryResult result, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onViewPayslip, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCompareOffer, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ResultEmptyState(boolean darkMode, androidx.compose.ui.Modifier modifier) {
    }
}