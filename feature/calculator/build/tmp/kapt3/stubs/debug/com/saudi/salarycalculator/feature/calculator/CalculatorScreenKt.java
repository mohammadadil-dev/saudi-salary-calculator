package com.saudi.salarycalculator.feature.calculator;

import android.content.Context;
import android.content.Intent;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.SegmentedButtonDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.StrokeCap;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.compose.ui.unit.LayoutDirection;
import androidx.core.content.FileProvider;
import com.saudi.salarycalculator.core.model.EmployeeType;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u009c\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u001c\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0006\u001a\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0003\u001a\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0003\u001a,\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a1\u0010\u0015\u001a\u00020\u00052\b\b\u0002\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u00192\u0013\b\u0002\u0010\u001a\u001a\r\u0012\u0004\u0012\u00020\u00050\u001b\u00a2\u0006\u0002\b\u001cH\u0007\u001a\u00d1\u0001\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0018\u0010\u001e\u001a\u0014\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00050\u001f2\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00050\u00132\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00050\u00132\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\u0011\u0010\u001a\u001a\r\u0012\u0004\u0012\u00020\u00050\u001b\u00a2\u0006\u0002\b\u001c2\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0007\u001a.\u0010*\u001a\u00020\u00052\u0006\u0010+\u001a\u00020\u00072\u0006\u0010,\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00050\u001bH\u0003\u001a\u0018\u0010.\u001a\u00020\u00052\u0006\u0010/\u001a\u0002002\u0006\u0010\u000b\u001a\u00020\fH\u0003\u001a*\u00101\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u00072\u0006\u00103\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0003\u001a:\u00104\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\f\u00105\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\u0012\u00106\u001a\u000e\u0012\u0004\u0012\u000207\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a(\u00108\u001a\u00020\u00052\u0006\u0010/\u001a\u0002002\u0006\u00109\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0003\u001a,\u0010:\u001a\u00020\u00052\u0006\u0010;\u001a\u00020\"2\u0006\u0010\u0010\u001a\u00020\u00112\u0012\u0010<\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001aT\u0010=\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0018\u0010\u001e\u001a\u0014\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00050\u001f2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\u0012\u00106\u001a\u000e\u0012\u0004\u0012\u000207\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a#\u0010?\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\f2\u0011\u0010@\u001a\r\u0012\u0004\u0012\u00020\u00050\u001b\u00a2\u0006\u0002\b\u001cH\u0003\u001a:\u0010A\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\u0012\u0010B\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a,\u0010C\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a,\u0010D\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a<\u0010E\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u00072\u0006\u00103\u001a\u00020\u00072\u0006\u0010F\u001a\u00020 2\u0006\u0010\u000b\u001a\u00020\f2\u0012\u00106\u001a\u000e\u0012\u0004\u0012\u000207\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a:\u0010G\u001a\u00020\u00052\u0006\u0010H\u001a\u00020\u00072\u0006\u00103\u001a\u00020\u00072\u0012\u0010I\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00050\u00132\f\u0010J\u001a\b\u0012\u0004\u0012\u00020\u00050\u001bH\u0003\u001a,\u0010K\u001a\u00020\u00052\u0006\u0010L\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\f2\u0012\u0010B\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a4\u0010M\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u00072\u0006\u00103\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010N\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\bO\u0010P\u001a:\u0010Q\u001a\u00020\u00052\u0006\u0010R\u001a\u00020\u00072\f\u0010S\u001a\b\u0012\u0004\u0012\u00020U0T2\u0006\u0010\u000b\u001a\u00020\f2\u0012\u00106\u001a\u000e\u0012\u0004\u0012\u000207\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a\u001e\u0010V\u001a\u00020\u00052\u0006\u0010+\u001a\u00020\u00072\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00050\u001bH\u0003\u001a\u0018\u0010W\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0003\u001a.\u0010X\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010Y\u001a\u00020Z2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00050\u001bH\u0003\u001a:\u0010[\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a \u0010\\\u001a\u00020\u00052\u0006\u0010/\u001a\u0002002\u0006\u00109\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0003\u001aN\u0010]\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00050\u00132\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\u0012\u00106\u001a\u000e\u0012\u0004\u0012\u000207\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a:\u0010^\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\u0012\u00106\u001a\u000e\u0012\u0004\u0012\u000207\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a\u0018\u0010_\u001a\u00020\u00052\u0006\u0010+\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fH\u0003\u001aT\u0010`\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0018\u0010\u001e\u001a\u0014\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00050\u001f2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00050\u001b2\u0012\u00106\u001a\u000e\u0012\u0004\u0012\u000207\u0012\u0004\u0012\u00020\u00050\u0013H\u0003\u001a\u0018\u0010a\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0003\u001a(\u0010b\u001a\u00020\u00052\u0006\u0010/\u001a\u0002002\u0006\u00109\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0003\u001a\u0015\u0010c\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\fH\u0002\u00a2\u0006\u0002\u0010d\u001a\u0010\u0010e\u001a\u00020f2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\u0010\u0010g\u001a\u00020\u00072\u0006\u0010h\u001a\u00020iH\u0002\u001a\u0015\u0010j\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\fH\u0002\u00a2\u0006\u0002\u0010d\u001a\u0018\u0010k\u001a\u00020\u00052\u0006\u0010Y\u001a\u00020Z2\u0006\u0010l\u001a\u00020\u0007H\u0002\u001a\u0010\u0010m\u001a\u00020\u00112\u0006\u0010L\u001a\u00020\u0007H\u0002\u001a\f\u0010n\u001a\u00020\u0007*\u00020\nH\u0002\"\u0010\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0003\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006o"}, d2 = {"accentColor", "Landroidx/compose/ui/graphics/Color;", "J", "secondaryAccent", "AckBanner", "", "message", "", "AnimatedSarCounter", "amount", "", "darkMode", "", "AppTabs", "state", "Lcom/saudi/salarycalculator/feature/calculator/CalculatorUiState;", "copy", "Lcom/saudi/salarycalculator/feature/calculator/UiCopy;", "onSelectedTab", "Lkotlin/Function1;", "", "CalculatorRoute", "modifier", "Landroidx/compose/ui/Modifier;", "viewModel", "Lcom/saudi/salarycalculator/feature/calculator/CalculatorViewModel;", "adBanner", "Lkotlin/Function0;", "Landroidx/compose/runtime/Composable;", "CalculatorScreen", "onFieldChange", "Lkotlin/Function2;", "Lcom/saudi/salarycalculator/feature/calculator/CalculatorField;", "onEmployeeTypeChange", "Lcom/saudi/salarycalculator/core/model/EmployeeType;", "onCalculateSalary", "onCompareOffers", "onCalculateEos", "onCalculateSavings", "onToggleYearly", "onToggleTheme", "onClearHistory", "ChoiceChip", "text", "selected", "onClick", "CircularNetSalaryChart", "result", "Lcom/saudi/salarycalculator/core/model/NetSalaryResult;", "CompactMetricCard", "label", "value", "ComparisonScreen", "onCompare", "openInput", "Lcom/saudi/salarycalculator/feature/calculator/InputTarget;", "DashboardMetrics", "yearly", "EmployeeTypeToggle", "type", "onChange", "EndOfServiceScreen", "onCalculate", "GlassCard", "content", "Header", "onLanguageChange", "HeroCard", "HomeScreen", "InputRow", "field", "KeypadSheet", "title", "onValueChange", "onDone", "LanguageToggle", "language", "MetricRow", "marker", "MetricRow-g2O1Hgs", "(Ljava/lang/String;Ljava/lang/String;ZJ)V", "OfferForm", "heading", "inputs", "", "Lcom/saudi/salarycalculator/feature/calculator/InputSpec;", "PrimaryButton", "ReportPreviewCard", "ReportScreen", "context", "Landroid/content/Context;", "ResultDashboard", "ResultHero", "SalaryInputScreen", "SavingsScreen", "SectionTitle", "SettingsScreen", "SmartSummaryCard", "WpsBreakdown", "contentColor", "(Z)J", "fintechBackground", "Landroidx/compose/ui/graphics/Brush;", "formatDate", "millis", "", "glassColor", "sharePdf", "report", "uiCopy", "sar", "calculator_debug"})
public final class CalculatorScreenKt {
    private static final long accentColor = 0L;
    private static final long secondaryAccent = 0L;
    
    @androidx.compose.runtime.Composable()
    public static final void CalculatorRoute(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.CalculatorViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> adBanner) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CalculatorScreen(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super com.saudi.salarycalculator.feature.calculator.CalculatorField, ? super java.lang.String, kotlin.Unit> onFieldChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onSelectedTab, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.core.model.EmployeeType, kotlin.Unit> onEmployeeTypeChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCalculateSalary, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCompareOffers, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCalculateEos, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCalculateSavings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToggleYearly, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToggleTheme, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClearHistory, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> adBanner, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void Header(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy, kotlin.jvm.functions.Function0<kotlin.Unit> onToggleTheme, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onLanguageChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AppTabs(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onSelectedTab) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HomeScreen(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onSelectedTab) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HeroCard(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onSelectedTab) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SalaryInputScreen(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy, kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.core.model.EmployeeType, kotlin.Unit> onEmployeeTypeChange, kotlin.jvm.functions.Function0<kotlin.Unit> onCalculate, kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.InputTarget, kotlin.Unit> openInput) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ResultDashboard(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy, kotlin.jvm.functions.Function0<kotlin.Unit> onToggleYearly, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onSelectedTab) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ComparisonScreen(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy, kotlin.jvm.functions.Function0<kotlin.Unit> onCompare, kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.InputTarget, kotlin.Unit> openInput) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EndOfServiceScreen(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy, kotlin.jvm.functions.Function2<? super com.saudi.salarycalculator.feature.calculator.CalculatorField, ? super java.lang.String, kotlin.Unit> onFieldChange, kotlin.jvm.functions.Function0<kotlin.Unit> onCalculate, kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.InputTarget, kotlin.Unit> openInput) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SavingsScreen(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy, kotlin.jvm.functions.Function0<kotlin.Unit> onCalculate, kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.InputTarget, kotlin.Unit> openInput) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SettingsScreen(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy, kotlin.jvm.functions.Function2<? super com.saudi.salarycalculator.feature.calculator.CalculatorField, ? super java.lang.String, kotlin.Unit> onFieldChange, kotlin.jvm.functions.Function0<kotlin.Unit> onToggleTheme, kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.InputTarget, kotlin.Unit> openInput) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReportScreen(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy, android.content.Context context, kotlin.jvm.functions.Function0<kotlin.Unit> onClearHistory) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SmartSummaryCard(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void EmployeeTypeToggle(com.saudi.salarycalculator.core.model.EmployeeType type, com.saudi.salarycalculator.feature.calculator.UiCopy copy, kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.core.model.EmployeeType, kotlin.Unit> onChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OfferForm(java.lang.String heading, java.util.List<com.saudi.salarycalculator.feature.calculator.InputSpec> inputs, boolean darkMode, kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.InputTarget, kotlin.Unit> openInput) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void InputRow(java.lang.String label, java.lang.String value, com.saudi.salarycalculator.feature.calculator.CalculatorField field, boolean darkMode, kotlin.jvm.functions.Function1<? super com.saudi.salarycalculator.feature.calculator.InputTarget, kotlin.Unit> openInput) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void KeypadSheet(java.lang.String title, java.lang.String value, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, kotlin.jvm.functions.Function0<kotlin.Unit> onDone) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LanguageToggle(java.lang.String language, boolean darkMode, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onLanguageChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AckBanner(java.lang.String message) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ResultHero(com.saudi.salarycalculator.core.model.NetSalaryResult result, boolean yearly, com.saudi.salarycalculator.feature.calculator.UiCopy copy) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DashboardMetrics(com.saudi.salarycalculator.core.model.NetSalaryResult result, boolean yearly, boolean darkMode, com.saudi.salarycalculator.feature.calculator.UiCopy copy) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CompactMetricCard(java.lang.String label, java.lang.String value, boolean darkMode, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReportPreviewCard(com.saudi.salarycalculator.feature.calculator.CalculatorUiState state, com.saudi.salarycalculator.feature.calculator.UiCopy copy) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AnimatedSarCounter(double amount, boolean darkMode) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CircularNetSalaryChart(com.saudi.salarycalculator.core.model.NetSalaryResult result, boolean darkMode) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void WpsBreakdown(com.saudi.salarycalculator.core.model.NetSalaryResult result, boolean yearly, boolean darkMode, com.saudi.salarycalculator.feature.calculator.UiCopy copy) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SectionTitle(java.lang.String text, boolean darkMode) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ChoiceChip(java.lang.String text, boolean selected, boolean darkMode, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PrimaryButton(java.lang.String text, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void GlassCard(boolean darkMode, kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    private static final androidx.compose.ui.graphics.Brush fintechBackground(boolean darkMode) {
        return null;
    }
    
    private static final long glassColor(boolean darkMode) {
        return 0L;
    }
    
    private static final long contentColor(boolean darkMode) {
        return 0L;
    }
    
    private static final java.lang.String sar(double $this$sar) {
        return null;
    }
    
    private static final java.lang.String formatDate(long millis) {
        return null;
    }
    
    private static final void sharePdf(android.content.Context context, java.lang.String report) {
    }
    
    private static final com.saudi.salarycalculator.feature.calculator.UiCopy uiCopy(java.lang.String language) {
        return null;
    }
}