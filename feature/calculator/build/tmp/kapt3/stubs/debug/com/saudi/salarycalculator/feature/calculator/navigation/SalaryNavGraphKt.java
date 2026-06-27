package com.saudi.salarycalculator.feature.calculator.navigation;

import android.content.Context;
import android.content.Intent;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import com.saudi.salarycalculator.core.designsystem.components.BottomNavItem;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import com.saudi.salarycalculator.feature.calculator.PdfReportExporter;
import com.saudi.salarycalculator.feature.calculator.R;
import com.saudi.salarycalculator.feature.calculator.SalaryViewModel;
import com.saudi.salarycalculator.feature.calculator.WizardFieldsState;
import java.io.File;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\'\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0013\b\u0002\u0010\u0004\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\u0006H\u0007\u001a \u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0002\u001a \u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\bH\u0002\u001a(\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0002\u001a\u0014\u0010\u0015\u001a\u00020\u0001*\u00020\u00162\u0006\u0010\u0017\u001a\u00020\bH\u0002\u00a8\u0006\u0018"}, d2 = {"SalaryNavGraph", "", "modifier", "Landroidx/compose/ui/Modifier;", "adBanner", "Lkotlin/Function0;", "Landroidx/compose/runtime/Composable;", "buildPayslipReportText", "", "wizard", "Lcom/saudi/salarycalculator/feature/calculator/WizardFieldsState;", "result", "Lcom/saudi/salarycalculator/core/model/NetSalaryResult;", "currencySymbol", "sharePdfFile", "context", "Landroid/content/Context;", "file", "Ljava/io/File;", "chooserTitle", "writePayslipPdf", "navigateToTab", "Landroidx/navigation/NavController;", "route", "calculator_debug"})
public final class SalaryNavGraphKt {
    
    /**
     * Single shared-ViewModel NavHost for the whole app. [SalaryViewModel] is obtained once here
     * (scoped to whichever ViewModelStoreOwner hosts this composable, normally the Activity) and
     * handed to every destination, so wizard/result/comparison state survives tab switches. Hosts
     * the bottom navigation bar for [Screen.bottomNavScreens] and a slim back-only [AppTopBar] for
     * [Screen.Payslip], which is pushed from Result rather than being a tab itself. [adBanner] is an
     * injection point for the existing AdMob banner so this graph stays free of Activity-level setup.
     */
    @androidx.compose.runtime.Composable()
    public static final void SalaryNavGraph(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> adBanner) {
    }
    
    /**
     * Single-top, non-stacking navigation between bottom-nav tabs (mirrors the standard
     * bottom-navigation pattern) so repeatedly tapping tabs doesn't grow the back stack.
     */
    private static final void navigateToTab(androidx.navigation.NavController $this$navigateToTab, java.lang.String route) {
    }
    
    private static final java.io.File writePayslipPdf(android.content.Context context, com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, com.saudi.salarycalculator.core.model.NetSalaryResult result, java.lang.String currencySymbol) {
        return null;
    }
    
    private static final void sharePdfFile(android.content.Context context, java.io.File file, java.lang.String chooserTitle) {
    }
    
    private static final java.lang.String buildPayslipReportText(com.saudi.salarycalculator.feature.calculator.WizardFieldsState wizard, com.saudi.salarycalculator.core.model.NetSalaryResult result, java.lang.String currencySymbol) {
        return null;
    }
}