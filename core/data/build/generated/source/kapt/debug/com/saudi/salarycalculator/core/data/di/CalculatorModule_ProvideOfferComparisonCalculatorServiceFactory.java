package com.saudi.salarycalculator.core.data.di;

import com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService;
import com.saudi.salarycalculator.core.calculator.OfferComparisonCalculatorService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class CalculatorModule_ProvideOfferComparisonCalculatorServiceFactory implements Factory<OfferComparisonCalculatorService> {
  private final Provider<NetSalaryCalculatorService> netSalaryCalculatorServiceProvider;

  public CalculatorModule_ProvideOfferComparisonCalculatorServiceFactory(
      Provider<NetSalaryCalculatorService> netSalaryCalculatorServiceProvider) {
    this.netSalaryCalculatorServiceProvider = netSalaryCalculatorServiceProvider;
  }

  @Override
  public OfferComparisonCalculatorService get() {
    return provideOfferComparisonCalculatorService(netSalaryCalculatorServiceProvider.get());
  }

  public static CalculatorModule_ProvideOfferComparisonCalculatorServiceFactory create(
      Provider<NetSalaryCalculatorService> netSalaryCalculatorServiceProvider) {
    return new CalculatorModule_ProvideOfferComparisonCalculatorServiceFactory(netSalaryCalculatorServiceProvider);
  }

  public static OfferComparisonCalculatorService provideOfferComparisonCalculatorService(
      NetSalaryCalculatorService netSalaryCalculatorService) {
    return Preconditions.checkNotNullFromProvides(CalculatorModule.INSTANCE.provideOfferComparisonCalculatorService(netSalaryCalculatorService));
  }
}
