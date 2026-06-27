package com.saudi.salarycalculator.feature.calculator.screens;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.saudi.salarycalculator.core.model.EmployeeType;
import com.saudi.salarycalculator.core.model.OfferComparisonResult;
import com.saudi.salarycalculator.feature.calculator.OfferFormState;
import com.saudi.salarycalculator.feature.calculator.R;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a:\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u0006H\u0003\u001a\u008a\u0001\u0010\n\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u001e\u0010\u0010\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f0\u0011\u0012\u0004\u0012\u00020\u00010\u00112\u001e\u0010\u0012\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f0\u0011\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0007\u001aH\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\f2\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00062\u001e\u0010\u001a\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f0\u0011\u0012\u0004\u0012\u00020\u00010\u0011H\u0003\u00a8\u0006\u001b"}, d2 = {"ComparisonDetailRow", "", "label", "", "value", "isPositive", "", "darkMode", "currencySymbol", "isCurrency", "ComparisonScreen", "offerCurrent", "Lcom/saudi/salarycalculator/feature/calculator/OfferFormState;", "offerNew", "comparisonResult", "Lcom/saudi/salarycalculator/core/model/OfferComparisonResult;", "onUpdateCurrent", "Lkotlin/Function1;", "onUpdateNew", "onCompare", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "OfferFormCard", "title", "offer", "onUpdate", "calculator_debug"})
public final class ComparisonScreenKt {
    
    /**
     * Offer Comparison: two editable [OfferFormState] cards feeding [SalaryViewModel.compareOffers],
     * plus the resulting [OfferComparisonResult] once available. Mirrors the wizard's pattern of
     * passing raw state + an `onUpdate` transform callback rather than owning any state itself.
     */
    @androidx.compose.runtime.Composable()
    public static final void ComparisonScreen(boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String currencySymbol, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.OfferFormState offerCurrent, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.OfferFormState offerNew, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.OfferComparisonResult comparisonResult, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.OfferFormState, com.saudi.salarycalculator.feature.calculator.OfferFormState>, kotlin.Unit> onUpdateCurrent, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.OfferFormState, com.saudi.salarycalculator.feature.calculator.OfferFormState>, kotlin.Unit> onUpdateNew, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCompare, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OfferFormCard(java.lang.String title, com.saudi.salarycalculator.feature.calculator.OfferFormState offer, java.lang.String currencySymbol, boolean darkMode, kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.OfferFormState, com.saudi.salarycalculator.feature.calculator.OfferFormState>, kotlin.Unit> onUpdate) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ComparisonDetailRow(java.lang.String label, java.lang.String value, boolean isPositive, boolean darkMode, java.lang.String currencySymbol, boolean isCurrency) {
    }
}