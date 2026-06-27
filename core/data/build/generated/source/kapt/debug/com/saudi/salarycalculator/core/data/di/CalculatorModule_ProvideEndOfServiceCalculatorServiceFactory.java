package com.saudi.salarycalculator.core.data.di;

import com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService;
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
public final class CalculatorModule_ProvideEndOfServiceCalculatorServiceFactory implements Factory<EndOfServiceCalculatorService> {
  @Override
  public EndOfServiceCalculatorService get() {
    return provideEndOfServiceCalculatorService();
  }

  public static CalculatorModule_ProvideEndOfServiceCalculatorServiceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static EndOfServiceCalculatorService provideEndOfServiceCalculatorService() {
    return Preconditions.checkNotNullFromProvides(CalculatorModule.INSTANCE.provideEndOfServiceCalculatorService());
  }

  private static final class InstanceHolder {
    private static final CalculatorModule_ProvideEndOfServiceCalculatorServiceFactory INSTANCE = new CalculatorModule_ProvideEndOfServiceCalculatorServiceFactory();
  }
}
