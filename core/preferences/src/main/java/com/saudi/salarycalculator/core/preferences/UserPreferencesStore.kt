package com.saudi.salarycalculator.core.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "salary_preferences")

class UserPreferencesStore(private val context: Context) {
  val selectedLanguage: Flow<String> = context.dataStore.data.map {
    it[KEY_LANGUAGE] ?: "system"
  }

  val selectedTabIndex: Flow<Int> = context.dataStore.data.map {
    it[KEY_TAB_INDEX] ?: 0
  }

  val darkModeEnabled: Flow<Boolean> = context.dataStore.data.map {
    it[KEY_DARK_MODE] ?: false
  }

  /** "EXISTING" or "NEW" — mirrors [com.saudi.salarycalculator.core.model.GosiSystem].name.
   * Kept as a plain string here since :core:preferences doesn't depend on :core:model; callers in
   * :core:data map it to/from the enum. Defaults to "NEW" since that's the track anyone freshly
   * registering with GOSI falls under. */
  val gosiSystem: Flow<String> = context.dataStore.data.map {
    it[KEY_GOSI_SYSTEM] ?: "NEW"
  }

  suspend fun setGosiSystem(system: String) {
    context.dataStore.edit { prefs ->
      prefs[KEY_GOSI_SYSTEM] = system
    }
  }

  // ---- EOSB accrual tracker profile -----------------------------------------------------
  // Absent (null) until the user completes the tracker's one-time setup; unlike the wizard's
  // per-calculation snapshots, this is a standing fact about the user's own job, so it lives
  // here rather than in a CalculationRecord.

  val eosbJoiningDateMillis: Flow<Long?> = context.dataStore.data.map { it[KEY_EOSB_JOINING_DATE] }
  val eosbLastBasicSalary: Flow<Double?> = context.dataStore.data.map { it[KEY_EOSB_BASIC_SALARY] }

  suspend fun setEosbTrackerProfile(joiningDateMillis: Long, lastBasicSalary: Double) {
    context.dataStore.edit { prefs ->
      prefs[KEY_EOSB_JOINING_DATE] = joiningDateMillis
      prefs[KEY_EOSB_BASIC_SALARY] = lastBasicSalary
    }
  }

  suspend fun clearEosbTrackerProfile() {
    context.dataStore.edit { prefs ->
      prefs.remove(KEY_EOSB_JOINING_DATE)
      prefs.remove(KEY_EOSB_BASIC_SALARY)
    }
  }

  // ---- Payday countdown (home-screen widget + Settings preview) ------------------------
  // Absent (null) until the user sets it in Settings; the widget shows a setup prompt instead
  // of a countdown when this is null.

  val paydayDayOfMonth: Flow<Int?> = context.dataStore.data.map { it[KEY_PAYDAY_DAY] }

  suspend fun setPaydayDayOfMonth(dayOfMonth: Int) {
    context.dataStore.edit { prefs ->
      prefs[KEY_PAYDAY_DAY] = dayOfMonth
    }
  }

  suspend fun clearPaydayDayOfMonth() {
    context.dataStore.edit { prefs ->
      prefs.remove(KEY_PAYDAY_DAY)
    }
  }

  // ---- Leave-balance tracker profile -----------------------------------------------------
  // Absent (null) until the user completes the tracker's one-time setup, same convention as the
  // EOSB tracker profile above.

  val leaveJoiningDateMillis: Flow<Long?> = context.dataStore.data.map { it[KEY_LEAVE_JOINING_DATE] }
  val leaveDaysTakenThisYear: Flow<Int?> = context.dataStore.data.map { it[KEY_LEAVE_DAYS_TAKEN] }

  suspend fun setLeaveTrackerProfile(joiningDateMillis: Long, daysTakenThisYear: Int) {
    context.dataStore.edit { prefs ->
      prefs[KEY_LEAVE_JOINING_DATE] = joiningDateMillis
      prefs[KEY_LEAVE_DAYS_TAKEN] = daysTakenThisYear
    }
  }

  suspend fun clearLeaveTrackerProfile() {
    context.dataStore.edit { prefs ->
      prefs.remove(KEY_LEAVE_JOINING_DATE)
      prefs.remove(KEY_LEAVE_DAYS_TAKEN)
    }
  }

  suspend fun setLanguage(language: String) {
    context.dataStore.edit { prefs ->
      prefs[KEY_LANGUAGE] = language
    }
  }

  suspend fun setTabIndex(index: Int) {
    context.dataStore.edit { prefs ->
      prefs[KEY_TAB_INDEX] = index
    }
  }

  suspend fun setDarkMode(enabled: Boolean) {
    context.dataStore.edit { prefs ->
      prefs[KEY_DARK_MODE] = enabled
    }
  }

  companion object {
    private val KEY_LANGUAGE: Preferences.Key<String> = stringPreferencesKey("language")
    private val KEY_TAB_INDEX: Preferences.Key<Int> = intPreferencesKey("tab_index")
    private val KEY_DARK_MODE: Preferences.Key<Boolean> = booleanPreferencesKey("dark_mode")
    private val KEY_GOSI_SYSTEM: Preferences.Key<String> = stringPreferencesKey("gosi_system")
    private val KEY_EOSB_JOINING_DATE: Preferences.Key<Long> = longPreferencesKey("eosb_joining_date_millis")
    private val KEY_EOSB_BASIC_SALARY: Preferences.Key<Double> = doublePreferencesKey("eosb_last_basic_salary")
    private val KEY_PAYDAY_DAY: Preferences.Key<Int> = intPreferencesKey("payday_day_of_month")
    private val KEY_LEAVE_JOINING_DATE: Preferences.Key<Long> = longPreferencesKey("leave_joining_date_millis")
    private val KEY_LEAVE_DAYS_TAKEN: Preferences.Key<Int> = intPreferencesKey("leave_days_taken_this_year")
  }
}
