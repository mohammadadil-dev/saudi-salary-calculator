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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\n\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n\u00a8\u0006\u000b"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/AppTab;", "", "(Ljava/lang/String;I)V", "HOME", "SALARY", "RESULT", "COMPARE", "EOS", "SAVINGS", "REPORT", "SETTINGS", "calculator_debug"})
enum AppTab {
    /*public static final*/ HOME /* = new HOME() */,
    /*public static final*/ SALARY /* = new SALARY() */,
    /*public static final*/ RESULT /* = new RESULT() */,
    /*public static final*/ COMPARE /* = new COMPARE() */,
    /*public static final*/ EOS /* = new EOS() */,
    /*public static final*/ SAVINGS /* = new SAVINGS() */,
    /*public static final*/ REPORT /* = new REPORT() */,
    /*public static final*/ SETTINGS /* = new SETTINGS() */;
    
    AppTab() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.saudi.salarycalculator.feature.calculator.AppTab> getEntries() {
        return null;
    }
}