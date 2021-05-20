package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.IncomeSource

/**
 * The DTO that represents an Income Source
 *
 * @property id The id of the income source
 * @property name The name of the income source
 * @property amount The amount of the income source
 */
data class IncomeSourceDto(
    val id: Int,
    val name: String,
    val amount: Double
) {
    /**
     * Creates a DTO based on the income source entity
     * @param incomeSource The income source entity to create from
     */
    constructor(incomeSource: IncomeSource) : this(
        incomeSource.id.value,
        incomeSource.name,
        incomeSource.amount
    )
}