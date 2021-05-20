package com.marks0mmers.budgetcreator.models.views

/**
 * The JSON object that is used to Upsert Income Sources
 *
 * @property name The name of the income source
 * @property amount The amount of the income source
 */
data class IncomeSourceSubmissionView(
    val name: String,
    val amount: Double
)