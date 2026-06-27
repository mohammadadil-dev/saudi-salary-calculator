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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/screens/DateTarget;", "", "(Ljava/lang/String;I)V", "JOINING", "CALCULATION_MONTH", "calculator_debug"})
enum DateTarget {
    /*public static final*/ JOINING /* = new JOINING() */,
    /*public static final*/ CALCULATION_MONTH /* = new CALCULATION_MONTH() */;
    
    DateTarget() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.saudi.salarycalculator.feature.calculator.screens.DateTarget> getEntries() {
        return null;
    }
}