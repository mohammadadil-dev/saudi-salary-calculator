package com.saudi.salarycalculator.core.data.di;

import android.content.Context;
import androidx.room.Room;
import com.saudi.salarycalculator.core.data.SalaryRepository;
import com.saudi.salarycalculator.core.data.repository.OfflineFirstSalaryRepository;
import com.saudi.salarycalculator.core.database.SalaryDatabase;
import com.saudi.salarycalculator.core.database.dao.CalculationRecordDao;
import com.saudi.salarycalculator.core.preferences.UserPreferencesStore;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\b\u001a\u00020\tH\u0007J\u0012\u0010\n\u001a\u00020\u000b2\b\b\u0001\u0010\b\u001a\u00020\tH\u0007\u00a8\u0006\f"}, d2 = {"Lcom/saudi/salarycalculator/core/data/di/StorageModule;", "", "()V", "provideCalculationRecordDao", "Lcom/saudi/salarycalculator/core/database/dao/CalculationRecordDao;", "database", "Lcom/saudi/salarycalculator/core/database/SalaryDatabase;", "provideSalaryDatabase", "context", "Landroid/content/Context;", "provideUserPreferencesStore", "Lcom/saudi/salarycalculator/core/preferences/UserPreferencesStore;", "data_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class StorageModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.saudi.salarycalculator.core.data.di.StorageModule INSTANCE = null;
    
    private StorageModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.database.SalaryDatabase provideSalaryDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.database.dao.CalculationRecordDao provideCalculationRecordDao(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.database.SalaryDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.preferences.UserPreferencesStore provideUserPreferencesStore(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
}