package com.saudi.salarycalculator.core.data.di;

import com.saudi.salarycalculator.core.calculator.SavingsCalculatorService;
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
public final class CalculatorModule_ProvideSavingsCalculatorServiceFactory implements Factory<SavingsCalculatorService> {
  @Override
  public SavingsCalculatorService get() {
    return provideSavingsCalculatorService();
  }

  public static CalculatorModule_ProvideSavingsCalculatorServiceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SavingsCalculatorService provideSavingsCalculatorService() {
    return Preconditions.checkNotNullFromProvides(CalculatorModule.INSTANCE.provideSavingsCalculatorService());
  }

  private static final class InstanceHolder {
    private static final CalculatorModule_ProvideSavingsCalculatorServiceFactory INSTANCE = new CalculatorModule_ProvideSavingsCalculatorServiceFactory();
  }
}
