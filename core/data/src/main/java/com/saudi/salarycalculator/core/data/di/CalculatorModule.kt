package com.saudi.salarycalculator.core.data.di

import com.saudi.salarycalculator.core.calculator.DefaultEndOfServiceCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultExpatCostCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultCostOfLivingCalculatorService
import com.saudi.salarycalculator.core.calculator.CostOfLivingCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultGosiCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultNetSalaryCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultOfferComparisonCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultOvertimeCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultSavingsCalculatorService
import com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService
import com.saudi.salarycalculator.core.calculator.EosbTrackerCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultEosbTrackerCalculatorService
import com.saudi.salarycalculator.core.calculator.ExpatCostCalculatorService
import com.saudi.salarycalculator.core.calculator.GosiCalculatorService
import com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultOfferRedFlagCalculatorService
import com.saudi.salarycalculator.core.calculator.OfferComparisonCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultReverseSalaryCalculatorService
import com.saudi.salarycalculator.core.calculator.OfferRedFlagCalculatorService
import com.saudi.salarycalculator.core.calculator.OvertimeCalculatorService
import com.saudi.salarycalculator.core.calculator.ReverseSalaryCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultPaydayCalculatorService
import com.saudi.salarycalculator.core.calculator.PaydayCalculatorService
import com.saudi.salarycalculator.core.calculator.DefaultLeaveTrackerCalculatorService
import com.saudi.salarycalculator.core.calculator.LeaveTrackerCalculatorService
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

  @Provides
  @Singleton
  fun provideExpatCostCalculatorService(): ExpatCostCalculatorService =
    DefaultExpatCostCalculatorService()

  @Provides
  @Singleton
  fun provideEosbTrackerCalculatorService(
    endOfServiceCalculatorService: EndOfServiceCalculatorService
  ): EosbTrackerCalculatorService =
    DefaultEosbTrackerCalculatorService(endOfServiceCalculatorService)

  @Provides
  @Singleton
  fun providePaydayCalculatorService(): PaydayCalculatorService =
    DefaultPaydayCalculatorService()

  @Provides
  @Singleton
  fun provideLeaveTrackerCalculatorService(): LeaveTrackerCalculatorService =
    DefaultLeaveTrackerCalculatorService()

  @Provides
  @Singleton
  fun provideOfferRedFlagCalculatorService(): OfferRedFlagCalculatorService =
    DefaultOfferRedFlagCalculatorService()

  @Provides
  @Singleton
  fun provideReverseSalaryCalculatorService(
    netSalaryCalculatorService: NetSalaryCalculatorService
  ): ReverseSalaryCalculatorService =
    DefaultReverseSalaryCalculatorService(netSalaryCalculatorService)

  @Provides
  @Singleton
  fun provideCostOfLivingCalculatorService(): CostOfLivingCalculatorService =
    DefaultCostOfLivingCalculatorService()
}

