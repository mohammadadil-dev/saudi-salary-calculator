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
 * Raw, string-backed form state for the wizard. Kept as [String] (not [Double]) so text fields
 * can hold transient/partial input (e.g. "12." while typing) without losing the user's place;
 * conversion to the domain [NetSalaryInput] happens only at calculate-time via [toNetSalaryInput].
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b<\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u00ef\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0016\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001a\u0012\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u001a\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u001d\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u001d\u00a2\u0006\u0002\u0010\u001fJ\t\u0010>\u001a\u00020\u0003H\u00c6\u0003J\t\u0010?\u001a\u00020\u0003H\u00c6\u0003J\t\u0010@\u001a\u00020\u0003H\u00c6\u0003J\t\u0010A\u001a\u00020\u0003H\u00c6\u0003J\t\u0010B\u001a\u00020\u0003H\u00c6\u0003J\t\u0010C\u001a\u00020\u0003H\u00c6\u0003J\t\u0010D\u001a\u00020\u0003H\u00c6\u0003J\t\u0010E\u001a\u00020\u0003H\u00c6\u0003J\t\u0010F\u001a\u00020\u0003H\u00c6\u0003J\t\u0010G\u001a\u00020\u0016H\u00c6\u0003J\t\u0010H\u001a\u00020\u0018H\u00c6\u0003J\t\u0010I\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010J\u001a\u0004\u0018\u00010\u001aH\u00c6\u0003\u00a2\u0006\u0002\u0010%J\u0010\u0010K\u001a\u0004\u0018\u00010\u001aH\u00c6\u0003\u00a2\u0006\u0002\u0010%J\t\u0010L\u001a\u00020\u001dH\u00c6\u0003J\t\u0010M\u001a\u00020\u001dH\u00c6\u0003J\t\u0010N\u001a\u00020\u0003H\u00c6\u0003J\t\u0010O\u001a\u00020\u0007H\u00c6\u0003J\t\u0010P\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Q\u001a\u00020\u0003H\u00c6\u0003J\t\u0010R\u001a\u00020\u0003H\u00c6\u0003J\t\u0010S\u001a\u00020\u0003H\u00c6\u0003J\t\u0010T\u001a\u00020\u0003H\u00c6\u0003J\u00f8\u0001\u0010U\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u00182\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u001a2\b\b\u0002\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001dH\u00c6\u0001\u00a2\u0006\u0002\u0010VJ\u0013\u0010W\u001a\u00020\u001d2\b\u0010X\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010Y\u001a\u00020ZH\u00d6\u0001J\u0006\u0010[\u001a\u00020\\J\t\u0010]\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010!R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010!R\u0015\u0010\u001b\u001a\u0004\u0018\u00010\u001a\u00a2\u0006\n\n\u0002\u0010&\u001a\u0004\b$\u0010%R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010!R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010!R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0011\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010!R\u0011\u0010\u001c\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010!R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010!R\u0015\u0010\u0019\u001a\u0004\u0018\u00010\u001a\u00a2\u0006\n\n\u0002\u0010&\u001a\u0004\b4\u0010%R\u0011\u0010\u0011\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010!R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010!R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010!R\u0011\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010!R\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010!R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010!R\u0011\u0010\u001e\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u00101R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010!R\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010!\u00a8\u0006^"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/WizardFieldsState;", "", "employeeName", "", "jobTitle", "basicSalary", "employeeType", "Lcom/saudi/salarycalculator/core/model/EmployeeType;", "housingAllowance", "transportAllowance", "foodAllowance", "mobileAllowance", "otherAllowances", "bonus", "commission", "overtimeHours", "overtimeHourlyRateOverride", "loanDeduction", "absenceDeduction", "unpaidLeaveDays", "otherDeductions", "employmentSector", "Lcom/saudi/salarycalculator/core/model/EmploymentSector;", "contractType", "Lcom/saudi/salarycalculator/core/model/ContractType;", "joiningDateMillis", "", "calculationMonthMillis", "gosiIncluded", "", "resigned", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/saudi/salarycalculator/core/model/EmployeeType;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/saudi/salarycalculator/core/model/EmploymentSector;Lcom/saudi/salarycalculator/core/model/ContractType;Ljava/lang/Long;Ljava/lang/Long;ZZ)V", "getAbsenceDeduction", "()Ljava/lang/String;", "getBasicSalary", "getBonus", "getCalculationMonthMillis", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getCommission", "getContractType", "()Lcom/saudi/salarycalculator/core/model/ContractType;", "getEmployeeName", "getEmployeeType", "()Lcom/saudi/salarycalculator/core/model/EmployeeType;", "getEmploymentSector", "()Lcom/saudi/salarycalculator/core/model/EmploymentSector;", "getFoodAllowance", "getGosiIncluded", "()Z", "getHousingAllowance", "getJobTitle", "getJoiningDateMillis", "getLoanDeduction", "getMobileAllowance", "getOtherAllowances", "getOtherDeductions", "getOvertimeHourlyRateOverride", "getOvertimeHours", "getResigned", "getTransportAllowance", "getUnpaidLeaveDays", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/saudi/salarycalculator/core/model/EmployeeType;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/saudi/salarycalculator/core/model/EmploymentSector;Lcom/saudi/salarycalculator/core/model/ContractType;Ljava/lang/Long;Ljava/lang/Long;ZZ)Lcom/saudi/salarycalculator/feature/calculator/WizardFieldsState;", "equals", "other", "hashCode", "", "toNetSalaryInput", "Lcom/saudi/salarycalculator/core/model/NetSalaryInput;", "toString", "calculator_debug"})
public final class WizardFieldsState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String employeeName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String jobTitle = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String basicSalary = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.model.EmployeeType employeeType = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String housingAllowance = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String transportAllowance = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String foodAllowance = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String mobileAllowance = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String otherAllowances = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String bonus = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String commission = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String overtimeHours = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String overtimeHourlyRateOverride = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String loanDeduction = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String absenceDeduction = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String unpaidLeaveDays = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String otherDeductions = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.model.EmploymentSector employmentSector = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.model.ContractType contractType = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long joiningDateMillis = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long calculationMonthMillis = null;
    private final boolean gosiIncluded = false;
    private final boolean resigned = false;
    
    public WizardFieldsState(@org.jetbrains.annotations.NotNull()
    java.lang.String employeeName, @org.jetbrains.annotations.NotNull()
    java.lang.String jobTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String basicSalary, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.EmployeeType employeeType, @org.jetbrains.annotations.NotNull()
    java.lang.String housingAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String transportAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String foodAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String mobileAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String otherAllowances, @org.jetbrains.annotations.NotNull()
    java.lang.String bonus, @org.jetbrains.annotations.NotNull()
    java.lang.String commission, @org.jetbrains.annotations.NotNull()
    java.lang.String overtimeHours, @org.jetbrains.annotations.NotNull()
    java.lang.String overtimeHourlyRateOverride, @org.jetbrains.annotations.NotNull()
    java.lang.String loanDeduction, @org.jetbrains.annotations.NotNull()
    java.lang.String absenceDeduction, @org.jetbrains.annotations.NotNull()
    java.lang.String unpaidLeaveDays, @org.jetbrains.annotations.NotNull()
    java.lang.String otherDeductions, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.EmploymentSector employmentSector, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.ContractType contractType, @org.jetbrains.annotations.Nullable()
    java.lang.Long joiningDateMillis, @org.jetbrains.annotations.Nullable()
    java.lang.Long calculationMonthMillis, boolean gosiIncluded, boolean resigned) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmployeeName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getJobTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBasicSalary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.EmployeeType getEmployeeType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHousingAllowance() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTransportAllowance() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFoodAllowance() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMobileAllowance() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOtherAllowances() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBonus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCommission() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOvertimeHours() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOvertimeHourlyRateOverride() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLoanDeduction() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAbsenceDeduction() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUnpaidLeaveDays() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOtherDeductions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.EmploymentSector getEmploymentSector() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.ContractType getContractType() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getJoiningDateMillis() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getCalculationMonthMillis() {
        return null;
    }
    
    public final boolean getGosiIncluded() {
        return false;
    }
    
    public final boolean getResigned() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.NetSalaryInput toNetSalaryInput() {
        return null;
    }
    
    public WizardFieldsState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.EmploymentSector component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.ContractType component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component20() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component21() {
        return null;
    }
    
    public final boolean component22() {
        return false;
    }
    
    public final boolean component23() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.EmployeeType component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.WizardFieldsState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String employeeName, @org.jetbrains.annotations.NotNull()
    java.lang.String jobTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String basicSalary, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.EmployeeType employeeType, @org.jetbrains.annotations.NotNull()
    java.lang.String housingAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String transportAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String foodAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String mobileAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String otherAllowances, @org.jetbrains.annotations.NotNull()
    java.lang.String bonus, @org.jetbrains.annotations.NotNull()
    java.lang.String commission, @org.jetbrains.annotations.NotNull()
    java.lang.String overtimeHours, @org.jetbrains.annotations.NotNull()
    java.lang.String overtimeHourlyRateOverride, @org.jetbrains.annotations.NotNull()
    java.lang.String loanDeduction, @org.jetbrains.annotations.NotNull()
    java.lang.String absenceDeduction, @org.jetbrains.annotations.NotNull()
    java.lang.String unpaidLeaveDays, @org.jetbrains.annotations.NotNull()
    java.lang.String otherDeductions, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.EmploymentSector employmentSector, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.ContractType contractType, @org.jetbrains.annotations.Nullable()
    java.lang.Long joiningDateMillis, @org.jetbrains.annotations.Nullable()
    java.lang.Long calculationMonthMillis, boolean gosiIncluded, boolean resigned) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}