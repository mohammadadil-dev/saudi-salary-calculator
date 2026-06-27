package com.saudi.salarycalculator.core.data.di

import android.content.Context
import androidx.room.Room
import com.saudi.salarycalculator.core.data.SalaryRepository
import com.saudi.salarycalculator.core.data.repository.OfflineFirstSalaryRepository
import com.saudi.salarycalculator.core.database.SalaryDatabase
import com.saudi.salarycalculator.core.database.dao.CalculationRecordDao
import com.saudi.salarycalculator.core.preferences.UserPreferencesStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
  @Binds
  @Singleton
  abstract fun bindSalaryRepository(
    implementation: OfflineFirstSalaryRepository
  ): SalaryRepository
}

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
  @Provides
  @Singleton
  fun provideSalaryDatabase(@ApplicationContext context: Context): SalaryDatabase =
    Room.databaseBuilder(context, SalaryDatabase::class.java, "salary_database").build()

  @Provides
  fun provideCalculationRecordDao(database: SalaryDatabase): CalculationRecordDao =
    database.calculationRecordDao()

  @Provides
  @Singleton
  fun provideUserPreferencesStore(@ApplicationContext context: Context): UserPreferencesStore =
    UserPreferencesStore(context)
}
