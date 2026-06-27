package com.saudi.salarycalculator.core.data.di;

import com.saudi.salarycalculator.core.calculator.DefaultEndOfServiceCalculatorService;
import com.saudi.salarycalculator.core.calculator.DefaultGosiCalculatorService;
import com.saudi.salarycalculator.core.calculator.DefaultNetSalaryCalculatorService;
import com.saudi.salarycalculator.core.calculator.DefaultOfferComparisonCalculatorService;
import com.saudi.salarycalculator.core.calculator.DefaultOvertimeCalculatorService;
import com.saudi.salarycalculator.core.calculator.DefaultSavingsCalculatorService;
import com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService;
import com.saudi.salarycalculator.core.calculator.GosiCalculatorService;
import com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService;
import com.saudi.salarycalculator.core.calculator.OfferComparisonCalculatorService;
import com.saudi.salarycalculator.core.calculator.OvertimeCalculatorService;
import com.saudi.salarycalculator.core.calculator.SavingsCalculatorService;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\b\u0010\u0005\u001a\u00020\u0006H\u0007J\b\u0010\u0007\u001a\u00020\bH\u0007J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bH\u0007J\b\u0010\f\u001a\u00020\rH\u0007J\b\u0010\u000e\u001a\u00020\u000fH\u0007\u00a8\u0006\u0010"}, d2 = {"Lcom/saudi/salarycalculator/core/data/di/CalculatorModule;", "", "()V", "provideEndOfServiceCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/EndOfServiceCalculatorService;", "provideGosiCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/GosiCalculatorService;", "provideNetSalaryCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/NetSalaryCalculatorService;", "provideOfferComparisonCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/OfferComparisonCalculatorService;", "netSalaryCalculatorService", "provideOvertimeCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/OvertimeCalculatorService;", "provideSavingsCalculatorService", "Lcom/saudi/salarycalculator/core/calculator/SavingsCalculatorService;", "data_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class CalculatorModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.saudi.salarycalculator.core.data.di.CalculatorModule INSTANCE = null;
    
    private CalculatorModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService provideNetSalaryCalculatorService() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.calculator.GosiCalculatorService provideGosiCalculatorService() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.calculator.OvertimeCalculatorService provideOvertimeCalculatorService() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService provideEndOfServiceCalculatorService() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.calculator.OfferComparisonCalculatorService provideOfferComparisonCalculatorService(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService netSalaryCalculatorService) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.calculator.SavingsCalculatorService provideSavingsCalculatorService() {
        return null;
    }
}