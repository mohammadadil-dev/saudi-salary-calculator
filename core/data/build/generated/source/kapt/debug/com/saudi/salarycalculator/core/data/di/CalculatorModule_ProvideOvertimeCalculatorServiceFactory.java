package com.saudi.salarycalculator.core.data.di;

import com.saudi.salarycalculator.core.calculator.OvertimeCalculatorService;
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
public final class CalculatorModule_ProvideOvertimeCalculatorServiceFactory implements Factory<OvertimeCalculatorService> {
  @Override
  public OvertimeCalculatorService get() {
    return provideOvertimeCalculatorService();
  }

  public static CalculatorModule_ProvideOvertimeCalculatorServiceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OvertimeCalculatorService provideOvertimeCalculatorService() {
    return Preconditions.checkNotNullFromProvides(CalculatorModule.INSTANCE.provideOvertimeCalculatorService());
  }

  private static final class InstanceHolder {
    private static final CalculatorModule_ProvideOvertimeCalculatorServiceFactory INSTANCE = new CalculatorModule_ProvideOvertimeCalculatorServiceFactory();
  }
}
