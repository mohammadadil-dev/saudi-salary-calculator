package com.saudi.salarycalculator.core.data.repository;

import com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService;
import com.saudi.salarycalculator.core.calculator.GosiCalculatorService;
import com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService;
import com.saudi.salarycalculator.core.calculator.OfferComparisonCalculatorService;
import com.saudi.salarycalculator.core.calculator.OvertimeCalculatorService;
import com.saudi.salarycalculator.core.calculator.SavingsCalculatorService;
import com.saudi.salarycalculator.core.database.dao.CalculationRecordDao;
import com.saudi.salarycalculator.core.preferences.UserPreferencesStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class OfflineFirstSalaryRepository_Factory implements Factory<OfflineFirstSalaryRepository> {
  private final Provider<NetSalaryCalculatorService> netSalaryCalculatorServiceProvider;

  private final Provider<GosiCalculatorService> gosiCalculatorServiceProvider;

  private final Provider<OvertimeCalculatorService> overtimeCalculatorServiceProvider;

  private final Provider<EndOfServiceCalculatorService> endOfServiceCalculatorServiceProvider;

  private final Provider<OfferComparisonCalculatorService> offerComparisonCalculatorServiceProvider;

  private final Provider<SavingsCalculatorService> savingsCalculatorServiceProvider;

  private final Provider<CalculationRecordDao> calculationRecordDaoProvider;

  private final Provider<UserPreferencesStore> preferencesStoreProvider;

  public OfflineFirstSalaryRepository_Factory(
      Provider<NetSalaryCalculatorService> netSalaryCalculatorServiceProvider,
      Provider<GosiCalculatorService> gosiCalculatorServiceProvider,
      Provider<OvertimeCalculatorService> overtimeCalculatorServiceProvider,
      Provider<EndOfServiceCalculatorService> endOfServiceCalculatorServiceProvider,
      Provider<OfferComparisonCalculatorService> offerComparisonCalculatorServiceProvider,
      Provider<SavingsCalculatorService> savingsCalculatorServiceProvider,
      Provider<CalculationRecordDao> calculationRecordDaoProvider,
      Provider<UserPreferencesStore> preferencesStoreProvider) {
    this.netSalaryCalculatorServiceProvider = netSalaryCalculatorServiceProvider;
    this.gosiCalculatorServiceProvider = gosiCalculatorServiceProvider;
    this.overtimeCalculatorServiceProvider = overtimeCalculatorServiceProvider;
    this.endOfServiceCalculatorServiceProvider = endOfServiceCalculatorServiceProvider;
    this.offerComparisonCalculatorServiceProvider = offerComparisonCalculatorServiceProvider;
    this.savingsCalculatorServiceProvider = savingsCalculatorServiceProvider;
    this.calculationRecordDaoProvider = calculationRecordDaoProvider;
    this.preferencesStoreProvider = preferencesStoreProvider;
  }

  @Override
  public OfflineFirstSalaryRepository get() {
    return newInstance(netSalaryCalculatorServiceProvider.get(), gosiCalculatorServiceProvider.get(), overtimeCalculatorServiceProvider.get(), endOfServiceCalculatorServiceProvider.get(), offerComparisonCalculatorServiceProvider.get(), savingsCalculatorServiceProvider.get(), calculationRecordDaoProvider.get(), preferencesStoreProvider.get());
  }

  public static OfflineFirstSalaryRepository_Factory create(
      Provider<NetSalaryCalculatorService> netSalaryCalculatorServiceProvider,
      Provider<GosiCalculatorService> gosiCalculatorServiceProvider,
      Provider<OvertimeCalculatorService> overtimeCalculatorServiceProvider,
      Provider<EndOfServiceCalculatorService> endOfServiceCalculatorServiceProvider,
      Provider<OfferComparisonCalculatorService> offerComparisonCalculatorServiceProvider,
      Provider<SavingsCalculatorService> savingsCalculatorServiceProvider,
      Provider<CalculationRecordDao> calculationRecordDaoProvider,
      Provider<UserPreferencesStore> preferencesStoreProvider) {
    return new OfflineFirstSalaryRepository_Factory(netSalaryCalculatorServiceProvider, gosiCalculatorServiceProvider, overtimeCalculatorServiceProvider, endOfServiceCalculatorServiceProvider, offerComparisonCalculatorServiceProvider, savingsCalculatorServiceProvider, calculationRecordDaoProvider, preferencesStoreProvider);
  }

  public static OfflineFirstSalaryRepository newInstance(
      NetSalaryCalculatorService netSalaryCalculatorService,
      GosiCalculatorService gosiCalculatorService,
      OvertimeCalculatorService overtimeCalculatorService,
      EndOfServiceCalculatorService endOfServiceCalculatorService,
      OfferComparisonCalculatorService offerComparisonCalculatorService,
      SavingsCalculatorService savingsCalculatorService, CalculationRecordDao calculationRecordDao,
      UserPreferencesStore preferencesStore) {
    return new OfflineFirstSalaryRepository(netSalaryCalculatorService, gosiCalculatorService, overtimeCalculatorService, endOfServiceCalculatorService, offerComparisonCalculatorService, savingsCalculatorService, calculationRecordDao, preferencesStore);
  }
}
