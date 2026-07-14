package com.saudi.salarycalculator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.saudi.salarycalculator.core.designsystem.theme.SalaryCalculatorTheme
import com.saudi.salarycalculator.feature.calculator.SalaryViewModel
import com.saudi.salarycalculator.feature.calculator.navigation.SalaryNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  // Force-update via Google Play's In-App Update API, IMMEDIATE flow: if Play reports a newer
  // versionCode is live for this install, Play shows its own full-screen "update required" UI on
  // top of the app that the user can't dismiss back into the app without updating (or leaving).
  // No-op for sideloaded/adb-installed APKs (no Play Store install record to check against), and
  // no-op until a version newer than what's currently live gets published — see the dependency
  // comment in app/build.gradle.kts.
  private lateinit var appUpdateManager: AppUpdateManager
  private lateinit var updateResultLauncher: ActivityResultLauncher<IntentSenderRequest>

  override fun onCreate(savedInstanceState: Bundle?) {
    val splashScreen = installSplashScreen()
    super.onCreate(savedInstanceState)

    appUpdateManager = AppUpdateManagerFactory.create(this)
    updateResultLauncher = registerForActivityResult(
      ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
      if (result.resultCode != Activity.RESULT_OK) {
        // Declined, canceled, or failed mid-flow. onResume() re-checks every time the user
        // returns to the app and re-prompts while the update is still DEVELOPER_TRIGGERED, so no
        // separate retry logic is needed here.
        Log.w("AppUpdate", "In-app update flow did not complete, result code: ${result.resultCode}")
      }
    }
    checkForImmediateUpdate()

    // Animate the system splash icon out (scale + fade) instead of an abrupt cut, so the
    // hand-off into the in-app Splash destination (see SalaryNavGraph) feels continuous.
    splashScreen.setOnExitAnimationListener { provider ->
      // provider.iconView is nullable: some OEM splash-screen implementations (seen on certain
      // Samsung/Xiaomi/Vivo builds) don't populate it, and ObjectAnimator.ofFloat NPEs if handed
      // a null target. Fall back to a straight fade-and-remove when that happens.
      val icon = provider.iconView
      if (icon == null) {
        val fade = ObjectAnimator.ofFloat(provider.view, View.ALPHA, 1f, 0f).apply {
          duration = 260
        }
        fade.addListener(object : AnimatorListenerAdapter() {
          override fun onAnimationEnd(animation: Animator) {
            provider.remove()
          }
        })
        fade.start()
        return@setOnExitAnimationListener
      }

      val scaleX = ObjectAnimator.ofFloat(icon, View.SCALE_X, 1f, 1.15f, 0f)
      val scaleY = ObjectAnimator.ofFloat(icon, View.SCALE_Y, 1f, 1.15f, 0f)
      val fade = ObjectAnimator.ofFloat(provider.view, View.ALPHA, 1f, 0f)
      scaleX.duration = 420
      scaleY.duration = 420
      fade.duration = 260
      fade.startDelay = 200
      AnimatorSet().apply {
        playTogether(scaleX, scaleY, fade)
        addListener(object : AnimatorListenerAdapter() {
          override fun onAnimationEnd(animation: Animator) {
            provider.remove()
          }
        })
        start()
      }
    }

    setContent {
      // Obtained here (Activity-scoped) purely to drive the theme before the NavGraph's first
      // composition; SalaryNavGraph resolves its own hiltViewModel() call to this same shared
      // instance, so there is exactly one SalaryViewModel for the whole app.
      val viewModel: SalaryViewModel = hiltViewModel()
      val state by viewModel.state.collectAsStateWithLifecycle()

      SalaryCalculatorTheme(darkTheme = state.darkMode) {
        SalaryNavGraph(adBanner = { AdMobBanner() })
      }
    }
  }

  override fun onResume() {
    super.onResume()
    // Resumes an IMMEDIATE update that was already in progress (e.g. the user rotated the
    // screen, or the process was recreated mid-flow) instead of leaving it stalled.
    appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
      if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
        startImmediateUpdate(info)
      }
    }
  }

  private fun checkForImmediateUpdate() {
    appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
      if (
        info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
        info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
      ) {
        startImmediateUpdate(info)
      }
    }.addOnFailureListener { error ->
      // No network, Play Store app missing/outdated, sideloaded install, etc. Fails silently —
      // this is a nice-to-have gate, not something that should ever block app startup.
      Log.w("AppUpdate", "Could not check for app update", error)
    }
  }

  private fun startImmediateUpdate(info: AppUpdateInfo) {
    appUpdateManager.startUpdateFlowForResult(
      info,
      updateResultLauncher,
      AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
    )
  }
}

@Composable
private fun AdMobBanner() {
  val context = LocalContext.current
  AndroidView(
    modifier = Modifier
      .fillMaxWidth()
      .height(50.dp),
    factory = {
      AdView(context).apply {
        // Resolved per build type in app/build.gradle.kts: Google's sample unit for debug,
        // the (to-be-replaced) release value for release. See PLAY_STORE_RELEASE.md.
        adUnitId = BuildConfig.BANNER_AD_UNIT_ID
        setAdSize(AdSize.BANNER)
        loadAd(AdRequest.Builder().build())
      }
    }
  )
}
