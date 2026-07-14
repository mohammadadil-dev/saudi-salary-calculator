package com.saudi.salarycalculator.core.model

/**
 * Coarse, non-authoritative cost-of-living reference used only to give context alongside an
 * Offer Comparison entry (e.g. "this city tends to run higher/lower") — it never feeds into any
 * calculated number. Unlike [GosiRateSchedule] or [ExpatCostSchedule], these figures are NOT
 * government-set: they're market data (rent listings, cost-of-living surveys) that vary widely by
 * source, neighborhood, and property quality, and change with market conditions. Treat this as a
 * rough pointer for the user's own judgment, not a benchmark to calculate against — this app is
 * not a source of financial advice.
 *
 * Descriptive tier labels intentionally live in the UI layer as localized string resources (see
 * ComparisonScreen), not here, so this stays presentation-agnostic.
 */
enum class SaudiCityTier {
  RIYADH,
  JEDDAH,
  /** Khobar, Dhahran, and Dammam grouped together: sources differ on ranking within the Eastern
   * Province, but broadly agree it sits at or near the top alongside Riyadh. */
  EASTERN_PROVINCE,
  /** Catch-all for every other Saudi city; generally reported as lower-cost than the three major
   * metro clusters above, though this varies a lot city to city. */
  OTHER
}

object CityCostOfLiving {
  const val LAST_VERIFIED_LABEL = "July 2026"

  /** Indicative monthly rent range (SAR) for a modest 1-bedroom apartment. Deliberately wide —
   * source figures for the same city routinely differ by 2x depending on neighborhood, and this
   * is meant to convey a ballpark, not a quote. */
  fun rentRangeSar(city: SaudiCityTier): IntRange = when (city) {
    SaudiCityTier.RIYADH -> 1800..4400
    SaudiCityTier.EASTERN_PROVINCE -> 2000..4500
    SaudiCityTier.JEDDAH -> 1500..2800
    SaudiCityTier.OTHER -> 1000..2200
  }
}
