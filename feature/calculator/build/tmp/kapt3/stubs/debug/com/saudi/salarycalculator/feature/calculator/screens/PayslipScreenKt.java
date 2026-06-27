package com.saudi.salarycalculator.feature.calculator.screens;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.saudi.salarycalculator.core.designsystem.components.PayslipLine;
import com.saudi.salarycalculator.core.model.ContractType;
import com.saudi.salarycalculator.core.model.EmployeeType;
import com.saudi.salarycalculator.core.model.EmploymentSector;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import com.saudi.salarycalculator.feature.calculator.R;
import com.saudi.salarycalculator.feature.calculator.WizardFieldsState;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0003\u001a\u001a\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\tH\u0003\u001a^\u0010\n\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u00112\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u00a8\u0006\u0014"}, d2 = {"PayslipDetailRow", "", "label", "", "value", "darkMode", "", "PayslipEmptyState", "modifier", "Landroidx/compose/ui/Modifier;", "PayslipScreen", "currencySymbol", "wizard", "Lcom/saudi/salarycalculator/feature/calculator/WizardFieldsState;", "result", "Lcom/saudi/salarycalculator/core/model/NetSalaryResult;", "onExportPdf", "Lkotlin/Function0;", "onSaveCalculation", "onShare", "calculator_debug"})
public final class PayslipScreenKt {
    
    /**
     * Read-only payslip rendering of the last [NetSalaryResult], grouped the same way a printed
     * Saudi payslip would be (employee details, earnings, allowances, deductions, GOSI, net). The
     * actual PDF/share/save side effects are the caller's responsibility (see [onExportPdf],
     * [onSaveCalculation], [onShare]) so this screen stays a pure function of [wizard] + [result],
     * consistent with every other screen in the feature.
     */
    @androidx.compose.runtime.Composable()
    public static final void PayslipScreen(boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String currencySymbol, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.NetSalaryResult result, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onExportPdf, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSaveCalculation, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onShare, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PayslipDetailRow(java.lang.String label, java.lang.String value, boolean darkMode) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PayslipEmptyState(boolean darkMode, androidx.compose.ui.Modifier modifier) {
    }
}