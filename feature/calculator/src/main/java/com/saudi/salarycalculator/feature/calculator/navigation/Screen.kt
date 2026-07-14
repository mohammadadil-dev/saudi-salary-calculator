package com.saudi.salarycalculator.feature.calculator.navigation

/** All top-level destinations in the app. [Splash], [Payslip], and [ExpatCosts] are not part of
 * the bottom navigation bar; the other five map 1:1 to [bottomNavScreens] in display order. */
sealed class Screen(val route: String) {
  object Splash : Screen("splash")
  object Home : Screen("home")
  object Calculator : Screen("calculator")
  object Result : Screen("result")
  object Payslip : Screen("payslip")
  object Comparison : Screen("comparison")
  object Settings : Screen("settings")
  object ExpatCosts : Screen("expat_costs")

  companion object {
    val bottomNavScreens = listOf(Home, Calculator, Result, Comparison, Settings)

    fun fromRoute(route: String?): Screen = when (route) {
      Home.route -> Home
      Calculator.route -> Calculator
      Result.route -> Result
      Payslip.route -> Payslip
      Comparison.route -> Comparison
      Settings.route -> Settings
      ExpatCosts.route -> ExpatCosts
      else -> Splash
    }
  }
}
