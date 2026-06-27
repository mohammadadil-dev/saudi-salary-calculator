package com.saudi.salarycalculator.core.data.di

import com.saudi.salarycalculator.core.calculator.DefaultEndOfServiceCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultGosiCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultNetSalaryCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultOfferComparisonCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultOvertimeCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultSavingsCalculatorService
import com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService
import com.saudi.salarycalculator.core.calculator.GosiCalculatorService
import com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService
import com.saudi.salarycalculator.core.calculator.OfferComparisonCalculatorService
import com.saudi.salarycalculator.core.calculator.OvertimeCalculatorService
import com.saudi.salarycalculator.core.calculator.SavingsCalculatorService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalculatorModule {
  @Provides
  @Singleton
  fun provideNetSalaryCalculatorService(): NetSalaryCalculatorService =
    DefaultNetSalaryCalculatorService()

  @Provides
  @Singleton
  fun provideGosiCalculatorService(): GosiCalculatorService =
    DefaultGosiCalculatorService()

  @Provides
  @Singleton
  fun provideOvertimeCalculatorService(): OvertimeCalculatorService =
    DefaultOvertimeCalculatorService()

  @Provides
  @Singleton
  fun provideEndOfServiceCalculatorService(): EndOfServiceCalculatorService =
    DefaultEndOfServiceCalculatorService()

  @Provides
  @Singleton
  fun provideOfferComparisonCalculatorService(
    netSalaryCalculatorService: NetSalaryCalculatorService
  ): OfferComparisonCalculatorService =
    DefaultOfferComparisonCalculatorService(netSalaryCalculatorService)

  @Provides
  @Singleton
  fun provideSavingsCalculatorService(): SavingsCalculatorService =
    DefaultSavingsCalculatorService()
}
