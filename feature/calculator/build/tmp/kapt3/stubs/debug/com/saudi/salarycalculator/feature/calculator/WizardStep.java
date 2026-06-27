package com.saudi.salarycalculator.feature.calculator;

import com.saudi.salarycalculator.core.model.CalculationRecord;
import com.saudi.salarycalculator.core.model.ContractType;
import com.saudi.salarycalculator.core.model.EmployeeType;
import com.saudi.salarycalculator.core.model.EmploymentSector;
import com.saudi.salarycalculator.core.model.GosiRates;
import com.saudi.salarycalculator.core.model.NetSalaryInput;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import com.saudi.salarycalculator.core.model.OfferComparisonResult;
import com.saudi.salarycalculator.core.model.OfferInput;

/**
 * Six steps of the calculator wizard, in display order. The Review step doubles as the
 * "Calculate" trigger; the Result screen itself is a separate nav destination.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/WizardStep;", "", "(Ljava/lang/String;I)V", "BASIC_SALARY", "ALLOWANCES", "DEDUCTIONS", "EMPLOYMENT_DETAILS", "GOSI_EOSB", "REVIEW", "calculator_debug"})
public enum WizardStep {
    /*public static final*/ BASIC_SALARY /* = new BASIC_SALARY() */,
    /*public static final*/ ALLOWANCES /* = new ALLOWANCES() */,
    /*public static final*/ DEDUCTIONS /* = new DEDUCTIONS() */,
    /*public static final*/ EMPLOYMENT_DETAILS /* = new EMPLOYMENT_DETAILS() */,
    /*public static final*/ GOSI_EOSB /* = new GOSI_EOSB() */,
    /*public static final*/ REVIEW /* = new REVIEW() */;
    
    WizardStep() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.saudi.salarycalculator.feature.calculator.WizardStep> getEntries() {
        return null;
    }
}