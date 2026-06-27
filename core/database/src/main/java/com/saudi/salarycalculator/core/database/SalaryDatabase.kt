package com.saudi.salarycalculator.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saudi.salarycalculator.core.database.dao.CalculationRecordDao
import com.saudi.salarycalculator.core.database.entity.CalculationRecordEntity

@Database(
  entities = [CalculationRecordEntity::class],
  version = 2,
  exportSchema = false
)
abstract class SalaryDatabase : RoomDatabase() {
  abstract fun calculationRecordDao(): CalculationRecordDao
}
