package com.saudi.salarycalculator.core.data.di;

import com.saudi.salarycalculator.core.database.SalaryDatabase;
import com.saudi.salarycalculator.core.database.dao.CalculationRecordDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class StorageModule_ProvideCalculationRecordDaoFactory implements Factory<CalculationRecordDao> {
  private final Provider<SalaryDatabase> databaseProvider;

  public StorageModule_ProvideCalculationRecordDaoFactory(
      Provider<SalaryDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public CalculationRecordDao get() {
    return provideCalculationRecordDao(databaseProvider.get());
  }

  public static StorageModule_ProvideCalculationRecordDaoFactory create(
      Provider<SalaryDatabase> databaseProvider) {
    return new StorageModule_ProvideCalculationRecordDaoFactory(databaseProvider);
  }

  public static CalculationRecordDao provideCalculationRecordDao(SalaryDatabase database) {
    return Preconditions.checkNotNullFromProvides(StorageModule.INSTANCE.provideCalculationRecordDao(database));
  }
}
