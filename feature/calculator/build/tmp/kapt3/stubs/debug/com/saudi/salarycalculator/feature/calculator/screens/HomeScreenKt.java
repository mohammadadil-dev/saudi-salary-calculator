package com.saudi.salarycalculator.feature.calculator.screens;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.saudi.salarycalculator.core.model.CalculationRecord;
import com.saudi.salarycalculator.core.model.CalculationType;
import com.saudi.salarycalculator.feature.calculator.R;
import java.util.Calendar;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0083\u0001\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\u0012H\u0007\u00a2\u0006\u0002\u0010\u0013\u00a8\u0006\u0014"}, d2 = {"HomeScreen", "", "darkMode", "", "lastResultNetSalary", "", "history", "", "Lcom/saudi/salarycalculator/core/model/CalculationRecord;", "currencySymbol", "", "onStartCalculation", "Lkotlin/Function0;", "onViewAllHistory", "onEditRecord", "Lkotlin/Function1;", "onDeleteRecord", "modifier", "Landroidx/compose/ui/Modifier;", "(ZLjava/lang/Double;Ljava/util/List;Ljava/lang/String;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;)V", "calculator_debug"})
public final class HomeScreenKt {
    
    /**
     * Root "Home" tab: greeting, last-result summary, recent calculation history (capped to 3,
     * "View all" hands off to Settings where the full history lives), and three educational insight
     * cards. This is the screen users land on after Splash and whenever they tap the Home nav item.
     */
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(boolean darkMode, @org.jetbrains.annotations.Nullable()
    java.lang.Double lastResultNetSalary, @org.jetbrains.annotations.NotNull()
    java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> history, @org.jetbrains.annotations.NotNull()
    java.lang.String currencySymbol, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onStartCalculation, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onViewAllHistory, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.core.model.CalculationRecord, kotlin.Unit> onEditRecord, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDeleteRecord, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}