package com.saudi.salarycalculator.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.saudi.salarycalculator.MainActivity
import com.saudi.salarycalculator.R
import com.saudi.salarycalculator.core.calculator.DefaultPaydayCalculatorService
import com.saudi.salarycalculator.core.model.PaydayInput
import com.saudi.salarycalculator.core.preferences.UserPreferencesStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/** Home-screen widget showing a countdown to the payday set in Settings (see
 * [com.saudi.salarycalculator.feature.calculator.screens.SettingsScreen]).
 *
 * Reads [UserPreferencesStore] and calculates via [DefaultPaydayCalculatorService] directly
 * rather than through Hilt/[com.saudi.salarycalculator.core.data.SalaryRepository] — both are
 * plain, dependency-free classes, and that keeps this receiver free of Hilt's BroadcastReceiver
 * injection setup for what is, underneath, the exact same calculation the in-app Settings preview
 * already runs. Both call through [DefaultPaydayCalculatorService], so the two can never
 * silently disagree.
 *
 * [scope] and [paydayCalculatorService] live on the companion object rather than as instance
 * fields: Android may create and discard a new [PaydayWidgetProvider] instance for every
 * broadcast dispatch, but the companion object's state is per-process, so an update requested via
 * [requestUpdate] shares the same coroutine machinery as one dispatched through [onUpdate]. */
class PaydayWidgetProvider : AppWidgetProvider() {

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    // goAsync() keeps the process eligible to finish this coroutine after onUpdate returns —
    // without it, reading DataStore (a suspend call) from onUpdate risks the app being frozen or
    // killed mid-read on some OEMs' aggressive background-execution limits.
    val pendingResult = goAsync()
    val appContext = context.applicationContext
    scope.launch {
      try {
        updateWidgets(appContext, appWidgetManager, appWidgetIds)
      } finally {
        pendingResult.finish()
      }
    }
  }

  companion object {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val paydayCalculatorService = DefaultPaydayCalculatorService()

    /** Called from [com.saudi.salarycalculator.SalaryCalculatorApp] whenever the persisted payday
     * setting changes, so an already-placed widget updates within moments instead of waiting for
     * the system's own (30-minute-minimum) [android.appwidget.AppWidgetProviderInfo.updatePeriodMillis]
     * schedule. No-ops if no widget instance is currently placed. */
    fun requestUpdate(context: Context) {
      val appContext = context.applicationContext
      val manager = AppWidgetManager.getInstance(appContext)
      val ids = manager.getAppWidgetIds(ComponentName(appContext, PaydayWidgetProvider::class.java))
      if (ids.isEmpty()) return
      scope.launch { updateWidgets(appContext, manager, ids) }
    }

    private suspend fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
      val dayOfMonth = UserPreferencesStore(context).paydayDayOfMonth.first()
      val views = buildRemoteViews(context, dayOfMonth)
      appWidgetIds.forEach { id -> appWidgetManager.updateAppWidget(id, views) }
    }

    private fun buildRemoteViews(context: Context, dayOfMonth: Int?): RemoteViews {
      val views = RemoteViews(context.packageName, R.layout.widget_payday)

      val valueText = if (dayOfMonth == null) {
        context.getString(R.string.widget_payday_setup_prompt)
      } else {
        val result = paydayCalculatorService.calculate(PaydayInput(dayOfMonth = dayOfMonth))
        if (result.daysRemaining <= 0L) {
          context.getString(R.string.widget_payday_today)
        } else {
          context.getString(R.string.widget_payday_days_remaining, result.daysRemaining)
        }
      }
      views.setTextViewText(R.id.widget_payday_value, valueText)

      val openAppIntent = Intent(context, MainActivity::class.java)
      val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        openAppIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
      )
      views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

      return views
    }
  }
}
