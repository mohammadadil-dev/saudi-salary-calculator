package com.saudi.salarycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.saudi.salarycalculator.feature.calculator.CalculatorRoute
import com.saudi.salarycalculator.ui.theme.SalaryCalculatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SalaryCalculatorTheme {
        CalculatorRoute(adBanner = { AdMobBanner() })
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
        adUnitId = "ca-app-pub-3940256099942544/9214589741"
        setAdSize(AdSize.BANNER)
        loadAd(AdRequest.Builder().build())
      }
    }
  )
}
