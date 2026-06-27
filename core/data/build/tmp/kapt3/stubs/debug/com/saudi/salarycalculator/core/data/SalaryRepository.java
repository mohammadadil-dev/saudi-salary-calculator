package com.saudi.salarycalculator.core.data;

import com.saudi.salarycalculator.core.model.CalculationRecord;
import com.saudi.salarycalculator.core.model.EndOfServiceInput;
import com.saudi.salarycalculator.core.model.EndOfServiceResult;
import com.saudi.salarycalculator.core.model.GosiInput;
import com.saudi.salarycalculator.core.model.GosiResult;
import com.saudi.salarycalculator.core.model.NetSalaryInput;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import com.saudi.salarycalculator.core.model.OfferComparisonResult;
import com.saudi.salarycalculator.core.model.OfferInput;
import com.saudi.salarycalculator.core.model.OvertimeInput;
import com.saudi.salarycalculator.core.model.OvertimeResult;
import com.saudi.salarycalculator.core.model.SavingsInput;
import com.saudi.salarycalculator.core.model.SavingsResult;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\f\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\rH\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0004\u001a\u00020\u0011H\u00a6@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0004\u001a\u00020\u0015H\u00a6@\u00a2\u0006\u0002\u0010\u0016J\u000e\u0010\u0017\u001a\u00020\u0018H\u00a6@\u00a2\u0006\u0002\u0010\u0019J\u001e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001dH\u00a6@\u00a2\u0006\u0002\u0010\u001fJ\u0016\u0010 \u001a\u00020\u00182\u0006\u0010!\u001a\u00020\"H\u00a6@\u00a2\u0006\u0002\u0010#J\u000e\u0010$\u001a\b\u0012\u0004\u0012\u00020&0%H&J\u0014\u0010\'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020)0(0%H&J\u000e\u0010*\u001a\b\u0012\u0004\u0012\u00020\"0%H&J\u000e\u0010+\u001a\b\u0012\u0004\u0012\u00020,0%H&J\u0016\u0010-\u001a\u00020\u00182\u0006\u0010.\u001a\u00020)H\u00a6@\u00a2\u0006\u0002\u0010/J\u0016\u00100\u001a\u00020\u00182\u0006\u00101\u001a\u00020&H\u00a6@\u00a2\u0006\u0002\u00102J\u0016\u00103\u001a\u00020\u00182\u0006\u00104\u001a\u00020\"H\u00a6@\u00a2\u0006\u0002\u0010#J\u0016\u00105\u001a\u00020\u00182\u0006\u00106\u001a\u00020,H\u00a6@\u00a2\u0006\u0002\u00107\u00a8\u00068"}, d2 = {"Lcom/saudi/salarycalculator/core/data/SalaryRepository;", "", "calculateEndOfService", "Lcom/saudi/salarycalculator/core/model/EndOfServiceResult;", "input", "Lcom/saudi/salarycalculator/core/model/EndOfServiceInput;", "(Lcom/saudi/salarycalculator/core/model/EndOfServiceInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "calculateGosi", "Lcom/saudi/salarycalculator/core/model/GosiResult;", "Lcom/saudi/salarycalculator/core/model/GosiInput;", "(Lcom/saudi/salarycalculator/core/model/GosiInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "calculateNetSalary", "Lcom/saudi/salarycalculator/core/model/NetSalaryResult;", "Lcom/saudi/salarycalculator/core/model/NetSalaryInput;", "(Lcom/saudi/salarycalculator/core/model/NetSalaryInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "calculateOvertime", "Lcom/saudi/salarycalculator/core/model/OvertimeResult;", "Lcom/saudi/salarycalculator/core/model/OvertimeInput;", "(Lcom/saudi/salarycalculator/core/model/OvertimeInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "calculateSavings", "Lcom/saudi/salarycalculator/core/model/SavingsResult;", "Lcom/saudi/salarycalculator/core/model/SavingsInput;", "(Lcom/saudi/salarycalculator/core/model/SavingsInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearHistory", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "compareOffers", "Lcom/saudi/salarycalculator/core/model/OfferComparisonResult;", "offerA", "Lcom/saudi/salarycalculator/core/model/OfferInput;", "offerB", "(Lcom/saudi/salarycalculator/core/model/OfferInput;Lcom/saudi/salarycalculator/core/model/OfferInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRecord", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeDarkMode", "Lkotlinx/coroutines/flow/Flow;", "", "observeHistory", "", "Lcom/saudi/salarycalculator/core/model/CalculationRecord;", "observeLanguage", "observeSelectedTab", "", "saveRecord", "record", "(Lcom/saudi/salarycalculator/core/model/CalculationRecord;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setDarkMode", "enabled", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setLanguage", "language", "setSelectedTab", "index", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "data_debug"})
public abstract interface SalaryRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object calculateNetSalary(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.NetSalaryInput input, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.NetSalaryResult> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object calculateGosi(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.GosiInput input, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.GosiResult> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object calculateOvertime(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.OvertimeInput input, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.OvertimeResult> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object calculateEndOfService(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.EndOfServiceInput input, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.EndOfServiceResult> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object compareOffers(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.OfferInput offerA, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.OfferInput offerB, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.OfferComparisonResult> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object calculateSavings(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.SavingsInput input, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.SavingsResult> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord>> observeHistory();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveRecord(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.CalculationRecord record, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearHistory(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteRecord(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.String> observeLanguage();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setLanguage(@org.jetbrains.annotations.NotNull()
    java.lang.String language, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> observeSelectedTab();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setSelectedTab(int index, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> observeDarkMode();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setDarkMode(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}