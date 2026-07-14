package com.saudi.salarycalculator.core.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
  }
}
