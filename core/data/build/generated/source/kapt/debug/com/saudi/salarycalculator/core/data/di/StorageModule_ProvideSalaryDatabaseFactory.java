package com.saudi.salarycalculator.core.data.di;

import android.content.Context;
import com.saudi.salarycalculator.core.database.SalaryDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class StorageModule_ProvideSalaryDatabaseFactory implements Factory<SalaryDatabase> {
  private final Provider<Context> contextProvider;

  public StorageModule_ProvideSalaryDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SalaryDatabase get() {
    return provideSalaryDatabase(contextProvider.get());
  }

  public static StorageModule_ProvideSalaryDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new StorageModule_ProvideSalaryDatabaseFactory(contextProvider);
  }

  public static SalaryDatabase provideSalaryDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(StorageModule.INSTANCE.provideSalaryDatabase(context));
  }
}
