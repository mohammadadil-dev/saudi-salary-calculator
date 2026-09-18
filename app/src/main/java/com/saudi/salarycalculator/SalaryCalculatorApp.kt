package com.saudi.salarycalculator

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.saudi.salarycalculator.core.data.SalaryRepository
import com.saudi.salarycalculator.widget.PaydayWidgetProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltAndroidApp
class SalaryCalculatorApp : Application() {

  @Inject lateinit var salaryRepository: SalaryRepository

  private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

  override fun onCreate() {
    super.onCreate()
    MobileAds.initialize(this)

    // Nudges the home-screen Payday widget (see widget/PaydayWidgetProvider.kt) to refresh as
    // soon as the user changes the setting in Settings, rather than waiting on the system's own
    // 30-minute-minimum widget update schedule. drop(1) skips the initial emission on process
    // start — onUpdate/onEnabled already render the widget correctly on first placement, so this
    // observer only matters for a value that changes while the process is already running.
    salaryRepository.observePaydayDayOfMonth()
      .distinctUntilChanged()
      .drop(1)
      .onEach { PaydayWidgetProvider.requestUpdate(this@SalaryCalculatorApp) }
      .launchIn(applicationScope)
  }
}
