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
 * One side of the Offer Comparison screen. [title] defaults differ for the two sides so the
 * UI can pre-fill "Current offer" / "New offer" without extra plumbing.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B_\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\t\u0010!\u001a\u00020\fH\u00c6\u0003Jc\u0010\"\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\fH\u00c6\u0001J\u0013\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010&\u001a\u00020\'H\u00d6\u0001J\u0006\u0010(\u001a\u00020)J\t\u0010*\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000fR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000fR\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u000f\u00a8\u0006+"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/OfferFormState;", "", "title", "", "basicSalary", "housingAllowance", "transportAllowance", "foodAllowance", "mobileAllowance", "otherAllowances", "deductions", "employeeType", "Lcom/saudi/salarycalculator/core/model/EmployeeType;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/saudi/salarycalculator/core/model/EmployeeType;)V", "getBasicSalary", "()Ljava/lang/String;", "getDeductions", "getEmployeeType", "()Lcom/saudi/salarycalculator/core/model/EmployeeType;", "getFoodAllowance", "getHousingAllowance", "getMobileAllowance", "getOtherAllowances", "getTitle", "getTransportAllowance", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "", "toOfferInput", "Lcom/saudi/salarycalculator/core/model/OfferInput;", "toString", "calculator_debug"})
public final class OfferFormState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String basicSalary = null;
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
    private final java.lang.String deductions = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.model.EmployeeType employeeType = null;
    
    public OfferFormState(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String basicSalary, @org.jetbrains.annotations.NotNull()
    java.lang.String housingAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String transportAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String foodAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String mobileAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String otherAllowances, @org.jetbrains.annotations.NotNull()
    java.lang.String deductions, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.EmployeeType employeeType) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBasicSalary() {
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
    public final java.lang.String getDeductions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.EmployeeType getEmployeeType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.OfferInput toOfferInput() {
        return null;
    }
    
    public OfferFormState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
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
    public final com.saudi.salarycalculator.core.model.EmployeeType component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.OfferFormState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String basicSalary, @org.jetbrains.annotations.NotNull()
    java.lang.String housingAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String transportAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String foodAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String mobileAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String otherAllowances, @org.jetbrains.annotations.NotNull()
    java.lang.String deductions, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.EmployeeType employeeType) {
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