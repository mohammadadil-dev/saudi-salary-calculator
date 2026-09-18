package com.saudi.salarycalculator.core.model

/**
 * Sourced monthly cost-of-living reference data backing the Cost-of-Living Estimator, current as
 * of [LAST_VERIFIED_LABEL]. Like [CityCostOfLiving] (whose 1-bedroom rent figures this reuses as
 * its base case), this is market data — rent listings, cost-of-living surveys, private-school fee
 * schedules — not a government-set figure. It varies by source, neighborhood, and lifestyle, and
 * is meant as a ballpark for the user's own planning, not a quote or financial advice. Ranges are
 * deliberately wide for the same reason [CityCostOfLiving] keeps its ranges wide.
 *
 * Sources (accessed September 2026): Numbeo's cost-of-living indices for Riyadh, Jeddah, and Al
 * Khobar (rent and "excluding rent" monthly baskets); Expatica's Saudi Arabia cost-of-living
 * guide; Edarabia's Saudi international school fee survey.
 *
 * The Eastern Province non-rent baseline is anchored close to the Jeddah/Riyadh figures rather
 * than Numbeo's raw Al Khobar sample, which reports an "excluding rent" figure far below every
 * other city despite similar rent levels — treated as an unreliable outlier rather than used
 * directly.
 */
enum class ApartmentSize {
  STUDIO_OR_ONE_BR,
  TWO_BR,
  THREE_BR_PLUS
}

enum class SchoolFeeTier {
  BUDGET,
  MID_RANGE,
  PREMIUM
}

object CostOfLivingSchedule {
  const val LAST_VERIFIED_LABEL = "September 2026"

  /** Indicative monthly rent range (SAR) by city tier and apartment size. [ApartmentSize.TWO_BR]
   * and [ApartmentSize.THREE_BR_PLUS] scale [CityCostOfLiving.rentRangeSar]'s sourced 1-bedroom
   * figures by a flat multiplier rather than separately sourced data per bedroom count — the
   * multiplier itself is this app's own estimate, called out here rather than presented as an
   * independently sourced number. */
  fun rentRangeSar(city: SaudiCityTier, size: ApartmentSize): IntRange {
    val base = CityCostOfLiving.rentRangeSar(city)
    return when (size) {
      ApartmentSize.STUDIO_OR_ONE_BR -> base
      ApartmentSize.TWO_BR -> (base.first * 13 / 10)..(base.last * 13 / 10)
      ApartmentSize.THREE_BR_PLUS -> (base.first * 17 / 10)..(base.last * 17 / 10)
    }
  }

  /** Indicative monthly non-rent baseline (SAR) for a single adult — groceries, utilities,
   * transport, mobile/internet, and incidental spending. */
  fun nonRentBaselineSar(city: SaudiCityTier): IntRange = when (city) {
    SaudiCityTier.RIYADH -> 2800..3900
    SaudiCityTier.JEDDAH -> 2600..3500
    SaudiCityTier.EASTERN_PROVINCE -> 2700..3800
    SaudiCityTier.OTHER -> 2000..2900
  }

  /** Multiplier applied to the single-adult non-rent baseline for each additional adult sharing
   * the household (spouse/partner, another working-age adult) — reflects that some costs
   * (utilities, internet) are shared while others (groceries, transport) scale per person. */
  const val ADDITIONAL_ADULT_MULTIPLIER = 0.6

  /** Multiplier applied per child — lower than an adult's since children add less to the
   * groceries/transport basket but still meaningfully more than zero. */
  const val ADDITIONAL_CHILD_MULTIPLIER = 0.35

  /** Indicative ANNUAL international/private school fee range (SAR) per child, by tier. Saudi
   * public schooling is free but not taught in a curriculum most expatriate families use, so this
   * only covers the private/international segment expatriates typically have to budget for. */
  fun annualSchoolFeeRangeSar(tier: SchoolFeeTier): IntRange = when (tier) {
    SchoolFeeTier.BUDGET -> 12000..25000
    SchoolFeeTier.MID_RANGE -> 25000..45000
    SchoolFeeTier.PREMIUM -> 45000..90000
  }
}
