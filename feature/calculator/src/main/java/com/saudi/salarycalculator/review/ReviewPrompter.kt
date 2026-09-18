package com.saudi.salarycalculator.review

import android.app.Activity
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

/**
 * Wraps Google Play's In-App Review API with sensible gating so we don't nag users on first
 * launch or ask again right after a dismissal — and with a sentiment check in front of it so an
 * unhappy user is routed away from the public review dialog instead of into it.
 *
 * Two things can make a user *eligible* to be asked, tracked independently so both kinds of user
 * get a fair shot at being asked:
 *  - [onSuccessEvent]: called right after a moment of clear value delivered — a payslip export,
 *    a completed offer comparison, a payroll summary export. Never call this on a raw tap or
 *    right after an error.
 *  - [onAppOpen]: called once per app session. Catches the (very common) user who only ever uses
 *    the quick calculator and never triggers a success event, so they'd otherwise never be asked.
 *
 * Both return `true` when this is a good moment to show the caller's own sentiment-gate UI (an
 * "Enjoying the app?" prompt) — they never touch the UI or the Play API themselves. It's the
 * caller's job to show that UI and then, only on a positive response, call [launchReviewFlow]. On
 * either response the caller should call [markPrompted] so the cooldown below applies regardless
 * of whether the user said yes or no.
 */

private val Context.reviewDataStore by preferencesDataStore(name = "review_prompt_prefs")

object ReviewPrompter {

    // Tune these to taste.
    private const val MIN_SUCCESS_EVENTS_BEFORE_ASKING = 3
    private const val MIN_SESSIONS_BEFORE_ASKING = 10
    private const val MIN_DAYS_SINCE_INSTALL_BEFORE_ASKING = 2
    private const val COOLDOWN_DAYS_BETWEEN_PROMPTS = 90

    private val KEY_SUCCESS_COUNT = intPreferencesKey("success_event_count")
    private val KEY_SESSION_COUNT = intPreferencesKey("session_open_count")
    private val KEY_LAST_PROMPT_AT = longPreferencesKey("last_prompt_at_millis")
    private val KEY_FIRST_SEEN_AT = longPreferencesKey("first_seen_at_millis")

    /** Call after a genuine "value delivered" moment. Returns true if the sentiment-gate UI
     * should be shown now. */
    suspend fun onSuccessEvent(context: Context): Boolean {
        ensureFirstSeen(context)
        context.reviewDataStore.edit { prefs ->
            prefs[KEY_SUCCESS_COUNT] = (prefs[KEY_SUCCESS_COUNT] ?: 0) + 1
        }
        return isEligible(context)
    }

    /** Call once per app session/open. Returns true if the sentiment-gate UI should be shown
     * now — this is what catches loyal users who never hit a specific success event. */
    suspend fun onAppOpen(context: Context): Boolean {
        ensureFirstSeen(context)
        context.reviewDataStore.edit { prefs ->
            prefs[KEY_SESSION_COUNT] = (prefs[KEY_SESSION_COUNT] ?: 0) + 1
        }
        return isEligible(context)
    }

    /** Marks the cooldown as consumed for this window. Call this after the sentiment-gate UI is
     * answered either way (yes or no) — an unhappy "no" shouldn't be re-asked again in a week. */
    suspend fun markPrompted(context: Context) {
        context.reviewDataStore.edit { prefs ->
            prefs[KEY_LAST_PROMPT_AT] = System.currentTimeMillis()
        }
    }

    /** Actually fires Google Play's native in-app review dialog. Only call this after the
     * caller's own sentiment-gate UI got a positive response — never unconditionally. Whether
     * the dialog actually appears is entirely up to Play's own internal quota; this app has no
     * way to observe that outcome, by design. */
    fun launchReviewFlow(context: Context, activity: Activity) {
        val manager = ReviewManagerFactory.create(context)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                manager.launchReviewFlow(activity, task.result)
            }
            // On failure, just silently skip — the explicit "Rate this app" row in Settings
            // (which deep-links straight to the Play Store listing) is always available as a
            // fallback and doesn't depend on this API succeeding.
        }
    }

    private suspend fun ensureFirstSeen(context: Context) {
        val alreadySeen = context.reviewDataStore.data.first()[KEY_FIRST_SEEN_AT] != null
        if (!alreadySeen) {
            context.reviewDataStore.edit { prefs ->
                prefs[KEY_FIRST_SEEN_AT] = System.currentTimeMillis()
            }
        }
    }

    private suspend fun isEligible(context: Context): Boolean {
        val prefs = context.reviewDataStore.data.first()
        val now = System.currentTimeMillis()

        val firstSeenAt = prefs[KEY_FIRST_SEEN_AT] ?: now
        val lastPromptAt = prefs[KEY_LAST_PROMPT_AT] ?: 0L
        val successCount = prefs[KEY_SUCCESS_COUNT] ?: 0
        val sessionCount = prefs[KEY_SESSION_COUNT] ?: 0

        val daysSinceFirstSeen = TimeUnit.MILLISECONDS.toDays(now - firstSeenAt)
        val daysSinceLastPrompt = TimeUnit.MILLISECONDS.toDays(now - lastPromptAt)

        val meetsActivityBar = successCount >= MIN_SUCCESS_EVENTS_BEFORE_ASKING ||
            sessionCount >= MIN_SESSIONS_BEFORE_ASKING

        return meetsActivityBar &&
            daysSinceFirstSeen >= MIN_DAYS_SINCE_INSTALL_BEFORE_ASKING &&
            (lastPromptAt == 0L || daysSinceLastPrompt >= COOLDOWN_DAYS_BETWEEN_PROMPTS)
    }
}
