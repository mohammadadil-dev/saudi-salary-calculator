package com.saudi.salarycalculator.feature.calculator.screens;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.KeyboardType;
import com.saudi.salarycalculator.core.model.CalculationRecord;
import com.saudi.salarycalculator.core.model.CalculationType;
import com.saudi.salarycalculator.core.model.GosiRates;
import com.saudi.salarycalculator.feature.calculator.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a4\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a\u00b0\u0001\u0010\n\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\t2\u001e\u0010\u0014\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\r0\t\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00010\u00122\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\t2\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\u0010\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\u00a8\u0006\u001b"}, d2 = {"GosiRatePercentField", "", "label", "", "rate", "", "darkMode", "", "onRateChange", "Lkotlin/Function1;", "SettingsScreen", "language", "gosiRates", "Lcom/saudi/salarycalculator/core/model/GosiRates;", "history", "", "Lcom/saudi/salarycalculator/core/model/CalculationRecord;", "onToggleDarkMode", "Lkotlin/Function0;", "onSetLanguage", "onUpdateGosiRates", "onClearHistory", "onEditRecord", "onDeleteRecord", "modifier", "Landroidx/compose/ui/Modifier;", "formatPercent", "calculator_debug"})
public final class SettingsScreenKt {
    
    /**
     * Settings: language, dark mode, editable GOSI contribution rates, and calculation history.
     * GOSI rates are edited as plain percentages (e.g. "9.75") and converted to/from the fractional
     * [GosiRates] doubles the calculator services expect (e.g. 0.0975) at the edges of this screen.
     */
    @androidx.compose.runtime.Composable()
    public static final void SettingsScreen(boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String language, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.GosiRates gosiRates, @org.jetbrains.annotations.NotNull()
    java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> history, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToggleDarkMode, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSetLanguage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.core.model.GosiRates, com.saudi.salarycalculator.core.model.GosiRates>, kotlin.Unit> onUpdateGosiRates, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClearHistory, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.core.model.CalculationRecord, kotlin.Unit> onEditRecord, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDeleteRecord, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void GosiRatePercentField(java.lang.String label, double rate, boolean darkMode, kotlin.jvm.functions.Function1<? super java.lang.Double, kotlin.Unit> onRateChange) {
    }
    
    private static final java.lang.String formatPercent(double rate) {
        return null;
    }
}