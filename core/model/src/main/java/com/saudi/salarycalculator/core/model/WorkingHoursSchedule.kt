package com.saudi.salarycalculator.core.model

/**
 * Monthly working-hours baselines used to derive an hourly rate from a monthly basic salary
 * (hourly rate = basic salary / baseline hours), per Saudi Labor Law Article 98:
 *  - Standard: up to 8 hours/day (48 hours/week) -> this app uses a flat 240-hour/month
 *    convention (30 days x 8 hours) as the divisor.
 *  - Ramadan: actual working hours for fasting Muslim employees are capped at 6 hours/day
 *    (36 hours/week) -> scaled down proportionally (6/8 of standard) to 180 hours/month.
 *
 * Whether a given calculation falls inside Ramadan is intentionally NOT auto-detected from a
 * date here. The Hijri calendar shifts ~11 days earlier every Gregorian year, and even Saudi
 * Arabia's own Umm al-Qura tabulated calendar can differ by a day from the moon-sighting-
 * confirmed start announced close to the time. Silently guessing a date range risked being
 * wrong in a way the user wouldn't notice, so this stays a user-set toggle (see
 * `ramadanReducedHours` on [NetSalaryInput]) — [RAMADAN_HINT_LABEL] just gives an approximate
 * pointer in the UI, it doesn't drive any calculation.
 */
object WorkingHoursSchedule {
  const val STANDARD_MONTHLY_HOURS = 240.0
  const val RAMADAN_MONTHLY_HOURS = 180.0

  /** Approximate, non-authoritative hint shown next to the Ramadan toggle. Sourced from the
   * Umm al-Qura calendar; the actual start/end each year is confirmed by moon sighting and can
   * shift by a day. Update (or remove) this once it's stale. */
  const val RAMADAN_HINT_LABEL = "~18 Feb – 19 Mar 2026 (1447H)"

  fun monthlyHoursFor(ramadanReducedHours: Boolean): Double =
    if (ramadanReducedHours) RAMADAN_MONTHLY_HOURS else STANDARD_MONTHLY_HOURS
}
