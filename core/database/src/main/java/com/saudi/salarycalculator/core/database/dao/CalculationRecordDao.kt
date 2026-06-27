package com.saudi.salarycalculator.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saudi.salarycalculator.core.database.entity.CalculationRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationRecordDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(record: CalculationRecordEntity)

  @Query("SELECT * FROM calculation_records ORDER BY createdAtMillis DESC")
  fun observeAll(): Flow<List<CalculationRecordEntity>>

  @Query("DELETE FROM calculation_records")
  suspend fun clearAll()
}
