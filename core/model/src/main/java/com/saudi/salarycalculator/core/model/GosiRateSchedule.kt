package com.saudi.salarycalculator.core.model

import java.util.Calendar
import java.util.TimeZone

/**
 * Known, sourced GOSI contribution-rate checkpoints for Saudi nationals under the two parallel
 * systems introduced by the Social Insurance Law (Royal Decree, effective 3 July 2024):
 *  - [GosiSystem.EXISTING]: employees first registered with GOSI before 3 July 2024. Stays flat
 *    at the pre-reform rate (11.75% employer + 9.75% employee = 21.5%).
 *  - [GosiSystem.NEW]: employees first registered on/after 3 July 2024. The New System launches
 *    at the SAME rate as the Existing system (21.5% total) — it does not diverge in year one.
 *    Only the annuity/pension component then rises by 0.5 points (both sides) on each subsequent
 *    3 July, through 2028: 9% (2024) -> 9.5% (2025) -> 10% (2026) -> 10.5% (2027) -> 11% (2028,
 *    final). SANED unemployment insurance (0.75% both sides) and the 2% employer-only
 *    occupational-hazard share never change — only the annuity slice steps up.
 *
 * All five steps through 2028 are already fixed by the enacted law (not a projection), so all of
 * them are encoded here rather than stopping at the most recently confirmed one.
 * [LAST_VERIFIED_LABEL] tracks when this table was last cross-checked against public sources; the
 * Settings screen surfaces it with a pointer back to gosi.gov.sa — this is a convenience default,
 * not a compliance guarantee, and every rate stays user-editable.
 *
 * IMPORTANT: an earlier version of this table incorrectly had the New System starting at
 * 10.25%/12.25% from launch (conflating the 2024 launch rate with the first 2025 step-up), based
 * on secondary compliance-tracker summaries (Mercans, Saudi Compliance Institute) that skip the
 * flat 2024-2025 period. Corrected using more precise, detail-level sources: Oracle Fusion Cloud
 * Payroll's GOSI legislative-update notes (which must encode exact effective dates to run real
 * customer payroll) and ZenHR's payroll-update notice, both of which break out the
 * annuity/SANED/hazard components per year and cite GOSI's own awareness portal
 * (awareness.gosi.gov.sa/journey3.html).
 *
 * Sourced from GOSI's own contributor FAQ (gosi.gov.sa) on the SANED unemployment contribution,
 * cross-referenced against Oracle's and ZenHR's payroll-legislation notes on the New System
 * rollout, as of the verification date below.
 */
object GosiRateSchedule {

  /** Human-readable label for when these numbers were last cross-checked against public sources.
   * Shown in the in-app disclaimer; update this whenever the checkpoints below are revisited. */
  const val LAST_VERIFIED_LABEL = "July 2026"

  const val CONTRIBUTION_CAP_SAR = 45000.0

  private const val EXISTING_EMPLOYEE_RATE = 0.0975
  private const val EXISTING_EMPLOYER_RATE = 0.1175

  private const val EXPAT_EMPLOYEE_RATE = 0.0
  private const val EXPAT_EMPLOYER_HAZARD_RATE = 0.02

  /** New-system checkpoints as (effective-from UTC millis, employeeRate, employerRate), oldest
   * first. [defaultRatesFor] picks the latest checkpoint at or before the given date, so any date
   * after the last entry keeps returning that entry rather than extrapolating. */
  private val newSystemCheckpoints: List<Triple<Long, Double, Double>> = listOf(
    // 21.5% total — New System launch, identical to the Existing-system rate for its first year.
    Triple(utcMillis(2024, Calendar.JULY, 3), 0.0975, 0.1175),
    // 22.5% total — first scheduled annual uplift (annuity 9% -> 9.5%).
    Triple(utcMillis(2025, Calendar.JULY, 3), 0.1025, 0.1225),
    // 23.5% total (annuity 9.5% -> 10%).
    Triple(utcMillis(2026, Calendar.JULY, 3), 0.1075, 0.1275),
    // 24.5% total (annuity 10% -> 10.5%).
    Triple(utcMillis(2027, Calendar.JULY, 3), 0.1125, 0.1325),
    // 25.5% total (annuity 10.5% -> 11%, final step).
    Triple(utcMillis(2028, Calendar.JULY, 3), 0.1175, 0.1375)
  )

  fun defaultRatesFor(system: GosiSystem, atMillis: Long = System.currentTimeMillis()): GosiRates =
    when (system) {
      GosiSystem.EXISTING -> GosiRates(
        saudiEmployeeRate = EXISTING_EMPLOYEE_RATE,
        saudiEmployerRate = EXISTING_EMPLOYER_RATE,
        expatEmployeeRate = EXPAT_EMPLOYEE_RATE,
        expatEmployerHazardRate = EXPAT_EMPLOYER_HAZARD_RATE,
        contributionCapSar = CONTRIBUTION_CAP_SAR
      )
      GosiSystem.NEW -> {
        val checkpoint = newSystemCheckpoints.lastOrNull { it.first <= atMillis }
          ?: newSystemCheckpoints.first()
        GosiRates(
          saudiEmployeeRate = checkpoint.second,
          saudiEmployerRate = checkpoint.third,
          expatEmployeeRate = EXPAT_EMPLOYEE_RATE,
          expatEmployerHazardRate = EXPAT_EMPLOYER_HAZARD_RATE,
          contributionCapSar = CONTRIBUTION_CAP_SAR
        )
      }
    }

  private fun utcMillis(year: Int, month: Int, day: Int): Long {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.clear()
    calendar.set(year, month, day)
    return calendar.timeInMillis
  }
}
