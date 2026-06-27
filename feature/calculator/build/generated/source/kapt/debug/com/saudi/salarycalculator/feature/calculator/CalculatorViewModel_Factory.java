package com.saudi.salarycalculator.feature.calculator;

import com.saudi.salarycalculator.core.data.SalaryRepository;
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
public final class CalculatorViewModel_Factory implements Factory<CalculatorViewModel> {
  private final Provider<SalaryRepository> repositoryProvider;

  public CalculatorViewModel_Factory(Provider<SalaryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CalculatorViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static CalculatorViewModel_Factory create(Provider<SalaryRepository> repositoryProvider) {
    return new CalculatorViewModel_Factory(repositoryProvider);
  }

  public static CalculatorViewModel newInstance(SalaryRepository repository) {
    return new CalculatorViewModel(repository);
  }
}
