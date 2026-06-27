package com.saudi.salarycalculator.feature.calculator;

import androidx.lifecycle.ViewModel;
import com.saudi.salarycalculator.core.data.SalaryRepository;
import com.saudi.salarycalculator.core.model.CalculationRecord;
import com.saudi.salarycalculator.core.model.CalculationType;
import com.saudi.salarycalculator.core.model.EmployeeType;
import com.saudi.salarycalculator.core.model.EndOfServiceInput;
import com.saudi.salarycalculator.core.model.EndOfServiceResult;
import com.saudi.salarycalculator.core.model.GosiInput;
import com.saudi.salarycalculator.core.model.GosiRates;
import com.saudi.salarycalculator.core.model.NetSalaryInput;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import com.saudi.salarycalculator.core.model.OfferComparisonResult;
import com.saudi.salarycalculator.core.model.OfferInput;
import com.saudi.salarycalculator.core.model.SavingsInput;
import com.saudi.salarycalculator.core.model.SavingsResult;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\'\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014j\u0002\b\u0015j\u0002\b\u0016j\u0002\b\u0017j\u0002\b\u0018j\u0002\b\u0019j\u0002\b\u001aj\u0002\b\u001bj\u0002\b\u001cj\u0002\b\u001dj\u0002\b\u001ej\u0002\b\u001fj\u0002\b j\u0002\b!j\u0002\b\"j\u0002\b#j\u0002\b$j\u0002\b%j\u0002\b&j\u0002\b\'\u00a8\u0006("}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/CalculatorField;", "", "(Ljava/lang/String;I)V", "BASIC_SALARY", "HOUSING_ALLOWANCE", "TRANSPORT_ALLOWANCE", "FOOD_ALLOWANCE", "MOBILE_ALLOWANCE", "OTHER_ALLOWANCES", "DEDUCTIONS", "SAUDI_EMPLOYEE_GOSI_RATE", "SAUDI_EMPLOYER_GOSI_RATE", "EXPAT_EMPLOYEE_GOSI_RATE", "EXPAT_EMPLOYER_HAZARD_RATE", "EOS_LAST_SALARY", "EOS_YEARS", "EOS_RESIGNED", "OFFER_A_TITLE", "OFFER_A_BASIC", "OFFER_A_HOUSING", "OFFER_A_TRANSPORT", "OFFER_A_FOOD", "OFFER_A_MOBILE", "OFFER_A_OTHER", "OFFER_A_DEDUCTION", "OFFER_B_TITLE", "OFFER_B_BASIC", "OFFER_B_HOUSING", "OFFER_B_TRANSPORT", "OFFER_B_FOOD", "OFFER_B_MOBILE", "OFFER_B_OTHER", "OFFER_B_DEDUCTION", "SAVINGS_NET_SALARY", "RENT_EXPENSE", "FOOD_EXPENSE", "TRANSPORT_EXPENSE", "FAMILY_EXPENSE", "OTHER_EXPENSE", "LANGUAGE", "calculator_debug"})
public enum CalculatorField {
    /*public static final*/ BASIC_SALARY /* = new BASIC_SALARY() */,
    /*public static final*/ HOUSING_ALLOWANCE /* = new HOUSING_ALLOWANCE() */,
    /*public static final*/ TRANSPORT_ALLOWANCE /* = new TRANSPORT_ALLOWANCE() */,
    /*public static final*/ FOOD_ALLOWANCE /* = new FOOD_ALLOWANCE() */,
    /*public static final*/ MOBILE_ALLOWANCE /* = new MOBILE_ALLOWANCE() */,
    /*public static final*/ OTHER_ALLOWANCES /* = new OTHER_ALLOWANCES() */,
    /*public static final*/ DEDUCTIONS /* = new DEDUCTIONS() */,
    /*public static final*/ SAUDI_EMPLOYEE_GOSI_RATE /* = new SAUDI_EMPLOYEE_GOSI_RATE() */,
    /*public static final*/ SAUDI_EMPLOYER_GOSI_RATE /* = new SAUDI_EMPLOYER_GOSI_RATE() */,
    /*public static final*/ EXPAT_EMPLOYEE_GOSI_RATE /* = new EXPAT_EMPLOYEE_GOSI_RATE() */,
    /*public static final*/ EXPAT_EMPLOYER_HAZARD_RATE /* = new EXPAT_EMPLOYER_HAZARD_RATE() */,
    /*public static final*/ EOS_LAST_SALARY /* = new EOS_LAST_SALARY() */,
    /*public static final*/ EOS_YEARS /* = new EOS_YEARS() */,
    /*public static final*/ EOS_RESIGNED /* = new EOS_RESIGNED() */,
    /*public static final*/ OFFER_A_TITLE /* = new OFFER_A_TITLE() */,
    /*public static final*/ OFFER_A_BASIC /* = new OFFER_A_BASIC() */,
    /*public static final*/ OFFER_A_HOUSING /* = new OFFER_A_HOUSING() */,
    /*public static final*/ OFFER_A_TRANSPORT /* = new OFFER_A_TRANSPORT() */,
    /*public static final*/ OFFER_A_FOOD /* = new OFFER_A_FOOD() */,
    /*public static final*/ OFFER_A_MOBILE /* = new OFFER_A_MOBILE() */,
    /*public static final*/ OFFER_A_OTHER /* = new OFFER_A_OTHER() */,
    /*public static final*/ OFFER_A_DEDUCTION /* = new OFFER_A_DEDUCTION() */,
    /*public static final*/ OFFER_B_TITLE /* = new OFFER_B_TITLE() */,
    /*public static final*/ OFFER_B_BASIC /* = new OFFER_B_BASIC() */,
    /*public static final*/ OFFER_B_HOUSING /* = new OFFER_B_HOUSING() */,
    /*public static final*/ OFFER_B_TRANSPORT /* = new OFFER_B_TRANSPORT() */,
    /*public static final*/ OFFER_B_FOOD /* = new OFFER_B_FOOD() */,
    /*public static final*/ OFFER_B_MOBILE /* = new OFFER_B_MOBILE() */,
    /*public static final*/ OFFER_B_OTHER /* = new OFFER_B_OTHER() */,
    /*public static final*/ OFFER_B_DEDUCTION /* = new OFFER_B_DEDUCTION() */,
    /*public static final*/ SAVINGS_NET_SALARY /* = new SAVINGS_NET_SALARY() */,
    /*public static final*/ RENT_EXPENSE /* = new RENT_EXPENSE() */,
    /*public static final*/ FOOD_EXPENSE /* = new FOOD_EXPENSE() */,
    /*public static final*/ TRANSPORT_EXPENSE /* = new TRANSPORT_EXPENSE() */,
    /*public static final*/ FAMILY_EXPENSE /* = new FAMILY_EXPENSE() */,
    /*public static final*/ OTHER_EXPENSE /* = new OTHER_EXPENSE() */,
    /*public static final*/ LANGUAGE /* = new LANGUAGE() */;
    
    CalculatorField() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.saudi.salarycalculator.feature.calculator.CalculatorField> getEntries() {
        return null;
    }
}