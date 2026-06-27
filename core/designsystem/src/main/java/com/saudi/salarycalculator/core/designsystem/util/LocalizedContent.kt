package com.saudi.salarycalculator.core.designsystem.util

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.LayoutDirection
import java.util.Locale

/** Forces every [androidx.compose.ui.res.stringResource] call below this point in the tree to
 * resolve against [languageCode] instead of the device's system locale, and flips text direction
 * to RTL for Arabic. The app stores the user's chosen language in [languageCode] (Settings ->
 * language toggle) but never changes the device locale, so without this wrapper `stringResource`
 * keeps reading the original `values/` resources no matter what the user picks. Wrapping
 * [LocalContext] with a configuration-overridden context is what actually makes the switch take
 * effect immediately, with no Activity recreation needed. */
@Composable
fun LocalizedContent(languageCode: String, content: @Composable () -> Unit) {
  val baseContext = LocalContext.current
  val localizedContext = remember(baseContext, languageCode) {
    val locale = Locale(languageCode)
    val config = Configuration(baseContext.resources.configuration)
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    baseContext.createConfigurationContext(config)
  }
  val layoutDirection = if (languageCode == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr

  CompositionLocalProvider(
    LocalContext provides localizedContext,
    LocalLayoutDirection provides layoutDirection
  ) {
    content()
  }
}
