package com.saudi.salarycalculator.feature.calculator.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.saudi.salarycalculator.core.designsystem.components.BrandSplash
import com.saudi.salarycalculator.feature.calculator.R

/** Splash destination: the very first screen in the nav graph, shown right after the system
 * splash screen dismisses. Purely presentational — navigation onward to Home is the caller's
 * responsibility via [onFinished]. */
@Composable
fun SplashScreen(
  darkMode: Boolean,
  onFinished: () -> Unit
) {
  BrandSplash(
    appName = stringResource(R.string.splash_app_name),
    tagline = stringResource(R.string.splash_tagline),
    darkMode = darkMode,
    onFinished = onFinished
  )
}
