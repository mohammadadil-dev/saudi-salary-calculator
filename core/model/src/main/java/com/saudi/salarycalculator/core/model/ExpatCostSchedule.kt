package com.saudi.salarycalculator.core.model

/**
 * Sourced default fee amounts for [ExpatCostInput], current as of [LAST_VERIFIED_LABEL]. All
 * government fees here are revised periodically (most recently by Ministry of Interior / Jawazat
 * circulars) and should be confirmed at absher.sa or qiwa.sa before relying on this for real
 * budgeting — the fields these seed remain fully user-editable for that reason.
 *
 * Deliberately excluded: the employer-paid "expat levy" on the employee's own work permit
 * (SAR 700-800/month depending on the company's Saudization ratio). That cost is the employer's
 * legal responsibility under Labor Law Article 40, not the employee's — bundling it into a
 * personal cost estimate would misrepresent whose obligation it is.
 */
object ExpatCostSchedule {
  const val LAST_VERIFIED_LABEL = "July 2026"

  /** Monthly government levy per sponsored dependent/companion (spouse, children, other family
   * members living with the employee in Saudi Arabia). */
  const val DEPENDENT_MONTHLY_LEVY_SAR = 400.0

  /** Annual iqama renewal fee for the worker themselves. Per Article 40 this is the employer's
   * responsibility to pay, not the employee's — see [ExpatCostInput.includeOwnIqamaRenewal]. */
  const val OWN_IQAMA_RENEWAL_FEE_SAR = 650.0

  /** Single exit/re-entry visa: one trip, valid up to ~2 months. */
  const val EXIT_REENTRY_SINGLE_FEE_SAR = 200.0

  /** Multiple exit/re-entry visa: valid up to ~3 months, usable for repeat trips in that window. */
  const val EXIT_REENTRY_MULTIPLE_FEE_SAR = 500.0
}
