package com.saudi.salarycalculator.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_records")
data class CalculationRecordEntity(
  @PrimaryKey val id: String,
  val type: String,
  val title: String,
  val inputSummary: String,
  val resultSummary: String,
  val createdAtMillis: Long,
  /** Delimited encoding of a NetSalaryInput snapshot (see OfflineFirstSalaryRepository), or null
   * for records that don't support reopening into the wizard. */
  val netSalaryInputSnapshot: String? = null
)
