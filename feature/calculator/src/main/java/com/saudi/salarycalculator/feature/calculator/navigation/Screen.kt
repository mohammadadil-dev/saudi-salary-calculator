package com.saudi.salarycalculator.feature.calculator.navigation

/** All top-level destinations in the app. [Splash], [Payslip], [ExpatCosts], [EosbTracker],
 * [LeaveTracker], [OfferScan], [ReverseSalary] and [CostOfLiving] are not part of the bottom
 * navigation bar; the other five map 1:1 to [bottomNavScreens] in display order. */
sealed class Screen(val route: String) {
  object Splash : Screen("splash")
  object Home : Screen("home")
  object Calculator : Screen("calculator")
  object Result : Screen("result")
  object Payslip : Screen("payslip")
  object Comparison : Screen("comparison")
  object Settings : Screen("settings")
  object ExpatCosts : Screen("expat_costs")
  object EosbTracker : Screen("eosb_tracker")
  object LeaveTracker : Screen("leave_tracker")
  object OfferScan : Screen("offer_scan")
  object ReverseSalary : Screen("reverse_salary")
  object CostOfLiving : Screen("cost_of_living")

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
      EosbTracker.route -> EosbTracker
      LeaveTracker.route -> LeaveTracker
      OfferScan.route -> OfferScan
      ReverseSalary.route -> ReverseSalary
      CostOfLiving.route -> CostOfLiving
      else -> Splash
    }
  }
}
