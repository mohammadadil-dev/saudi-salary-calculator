package com.saudi.salarycalculator.core.data.di;

import com.saudi.salarycalculator.core.calculator.GosiCalculatorService;
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
public final class CalculatorModule_ProvideGosiCalculatorServiceFactory implements Factory<GosiCalculatorService> {
  @Override
  public GosiCalculatorService get() {
    return provideGosiCalculatorService();
  }

  public static CalculatorModule_ProvideGosiCalculatorServiceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GosiCalculatorService provideGosiCalculatorService() {
    return Preconditions.checkNotNullFromProvides(CalculatorModule.INSTANCE.provideGosiCalculatorService());
  }

  private static final class InstanceHolder {
    private static final CalculatorModule_ProvideGosiCalculatorServiceFactory INSTANCE = new CalculatorModule_ProvideGosiCalculatorServiceFactory();
  }
}
