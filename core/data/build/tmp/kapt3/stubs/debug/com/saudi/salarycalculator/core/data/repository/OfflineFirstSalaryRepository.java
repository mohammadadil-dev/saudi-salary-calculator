package com.saudi.salarycalculator.core.data.repository;

import com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService;
import com.saudi.salarycalculator.core.calculator.GosiCalculatorService;
import com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService;
import com.saudi.salarycalculator.core.calculator.OfferComparisonCalculatorService;
import com.saudi.salarycalculator.core.calculator.OvertimeCalculatorService;
import com.saudi.salarycalculator.core.calculator.SavingsCalculatorService;
import com.saudi.salarycalculator.core.data.SalaryRepository;
import com.saudi.salarycalculator.core.database.dao.CalculationRecordDao;
import com.saudi.salarycalculator.core.database.entity.CalculationRecordEntity;
import com.saudi.salarycalculator.core.model.CalculationRecord;
import com.saudi.salarycalculator.core.model.CalculationType;
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
import com.saudi.salarycalculator.core.preferences.UserPreferencesStore;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ac\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\n\u0018\u00002\u00020\u0001BG\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0015\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0015\u001a\u00020\u001eH\u0096@\u00a2\u0006\u0002\u0010\u001fJ\u0016\u0010 \u001a\u00020!2\u0006\u0010\u0015\u001a\u00020\"H\u0096@\u00a2\u0006\u0002\u0010#J\u0016\u0010$\u001a\u00020%2\u0006\u0010\u0015\u001a\u00020&H\u0096@\u00a2\u0006\u0002\u0010\'J\u000e\u0010(\u001a\u00020)H\u0096@\u00a2\u0006\u0002\u0010*J\u001e\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020.H\u0096@\u00a2\u0006\u0002\u00100J\u0014\u00101\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002040302H\u0016J\u000e\u00105\u001a\b\u0012\u0004\u0012\u00020602H\u0016J\u000e\u00107\u001a\b\u0012\u0004\u0012\u00020802H\u0016J\u0016\u00109\u001a\u00020)2\u0006\u0010:\u001a\u000204H\u0096@\u00a2\u0006\u0002\u0010;J\u0016\u0010<\u001a\u00020)2\u0006\u0010=\u001a\u000206H\u0096@\u00a2\u0006\u0002\u0010>J\u0016\u0010?\u001a\u00020)2\u0006\u0010@\u001a\u000208H\u0096@\u00a2\u0006\u0002\u0010AR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006B"}, d2 = {"Lcom/saudi/salarycalculator/core/data/repository/OfflineFirstSalaryRepository;", "Lcom/saudi/salarycalculator/core/data/SalaryRepository;", "netSalaryCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/NetSalaryCalculatorService;", "gosiCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/GosiCalculatorService;", "overtimeCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/OvertimeCalculatorService;", "endOfServiceCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/EndOfServiceCalculatorService;", "offerComparisonCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/OfferComparisonCalculatorService;", "savingsCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/SavingsCalculatorService;", "calculationRecordDao", "Lcom/saudi/salarycalculator/core/database/dao/CalculationRecordDao;", "preferencesStore", "Lcom/saudi/salarycalculator/core/preferences/UserPreferencesStore;", "(Lcom/saudi/salarycalculator/core/calculator/NetSalaryCalculatorService;Lcom/saudi/salarycalculator/core/calculator/GosiCalculatorService;Lcom/saudi/salarycalculator/core/calculator/OvertimeCalculatorService;Lcom/saudi/salarycalculator/core/calculator/EndOfServiceCalculatorService;Lcom/saudi/salarycalculator/core/calculator/OfferComparisonCalculatorService;Lcom/saudi/salarycalculator/core/calculator/SavingsCalculatorService;Lcom/saudi/salarycalculator/core/database/dao/CalculationRecordDao;Lcom/saudi/salarycalculator/core/preferences/UserPreferencesStore;)V", "calculateEndOfService", "Lcom/saudi/salarycalculator/core/model/EndOfServiceResult;", "input", "Lcom/saudi/salarycalculator/core/model/EndOfServiceInput;", "(Lcom/saudi/salarycalculator/core/model/EndOfServiceInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "calculateGosi", "Lcom/saudi/salarycalculator/core/model/GosiResult;", "Lcom/saudi/salarycalculator/core/model/GosiInput;", "(Lcom/saudi/salarycalculator/core/model/GosiInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "calculateNetSalary", "Lcom/saudi/salarycalculator/core/model/NetSalaryResult;", "Lcom/saudi/salarycalculator/core/model/NetSalaryInput;", "(Lcom/saudi/salarycalculator/core/model/NetSalaryInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "calculateOvertime", "Lcom/saudi/salarycalculator/core/model/OvertimeResult;", "Lcom/saudi/salarycalculator/core/model/OvertimeInput;", "(Lcom/saudi/salarycalculator/core/model/OvertimeInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "calculateSavings", "Lcom/saudi/salarycalculator/core/model/SavingsResult;", "Lcom/saudi/salarycalculator/core/model/SavingsInput;", "(Lcom/saudi/salarycalculator/core/model/SavingsInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearHistory", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "compareOffers", "Lcom/saudi/salarycalculator/core/model/OfferComparisonResult;", "offerA", "Lcom/saudi/salarycalculator/core/model/OfferInput;", "offerB", "(Lcom/saudi/salarycalculator/core/model/OfferInput;Lcom/saudi/salarycalculator/core/model/OfferInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeHistory", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/saudi/salarycalculator/core/model/CalculationRecord;", "observeLanguage", "", "observeSelectedTab", "", "saveRecord", "record", "(Lcom/saudi/salarycalculator/core/model/CalculationRecord;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setLanguage", "language", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setSelectedTab", "index", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "data_debug"})
public final class OfflineFirstSalaryRepository implements com.saudi.salarycalculator.core.data.SalaryRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService netSalaryCalculatorService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.calculator.GosiCalculatorService gosiCalculatorService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.calculator.OvertimeCalculatorService overtimeCalculatorService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService endOfServiceCalculatorService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.calculator.OfferComparisonCalculatorService offerComparisonCalculatorService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.calculator.SavingsCalculatorService savingsCalculatorService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.database.dao.CalculationRecordDao calculationRecordDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.preferences.UserPreferencesStore preferencesStore = null;
    
    @javax.inject.Inject()
    public OfflineFirstSalaryRepository(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService netSalaryCalculatorService, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.calculator.GosiCalculatorService gosiCalculatorService, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.calculator.OvertimeCalculatorService overtimeCalculatorService, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService endOfServiceCalculatorService, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.calculator.OfferComparisonCalculatorService offerComparisonCalculatorService, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.calculator.SavingsCalculatorService savingsCalculatorService, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.database.dao.CalculationRecordDao calculationRecordDao, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.preferences.UserPreferencesStore preferencesStore) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object calculateNetSalary(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.NetSalaryInput input, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.NetSalaryResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object calculateGosi(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.GosiInput input, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.GosiResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object calculateOvertime(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.OvertimeInput input, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.OvertimeResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object calculateEndOfService(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.EndOfServiceInput input, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.EndOfServiceResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object compareOffers(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.OfferInput offerA, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.OfferInput offerB, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.OfferComparisonResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object calculateSavings(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.SavingsInput input, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.saudi.salarycalculator.core.model.SavingsResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord>> observeHistory() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveRecord(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.CalculationRecord record, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearHistory(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.String> observeLanguage() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setLanguage(@org.jetbrains.annotations.NotNull()
    java.lang.String language, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.Integer> observeSelectedTab() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setSelectedTab(int index, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}