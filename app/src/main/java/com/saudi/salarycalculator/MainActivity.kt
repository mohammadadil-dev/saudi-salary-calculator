package com.saudi.salarycalculator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.saudi.salarycalculator.core.designsystem.theme.SalaryCalculatorTheme
import com.saudi.salarycalculator.feature.calculator.SalaryViewModel
import com.saudi.salarycalculator.feature.calculator.navigation.SalaryNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    val splashScreen = installSplashScreen()
    super.onCreate(savedInstanceState)

    // Animate the system splash icon out (scale + fade) instead of an abrupt cut, so the
    // hand-off into the in-app Splash destination (see SalaryNavGraph) feels continuous.
    splashScreen.setOnExitAnimationListener { provider ->
      val scaleX = ObjectAnimator.ofFloat(provider.iconView, View.SCALE_X, 1f, 1.15f, 0f)
      val scaleY = ObjectAnimator.ofFloat(provider.iconView, View.SCALE_Y, 1f, 1.15f, 0f)
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
