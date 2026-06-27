package com.saudi.salarycalculator.core.data.di;

import com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class CalculatorModule_ProvideNetSalaryCalculatorServiceFactory implements Factory<NetSalaryCalculatorService> {
  @Override
  public NetSalaryCalculatorService get() {
    return provideNetSalaryCalculatorService();
  }

  public static CalculatorModule_ProvideNetSalaryCalculatorServiceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NetSalaryCalculatorService provideNetSalaryCalculatorService() {
    return Preconditions.checkNotNullFromProvides(CalculatorModule.INSTANCE.provideNetSalaryCalculatorService());
  }

  private static final class InstanceHolder {
    private static final CalculatorModule_ProvideNetSalaryCalculatorServiceFactory INSTANCE = new CalculatorModule_ProvideNetSalaryCalculatorServiceFactory();
  }
}
