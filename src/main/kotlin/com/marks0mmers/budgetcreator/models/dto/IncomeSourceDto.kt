package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.IncomeSource

data class IncomeSourceDto(
        val id: String? = null,
        val name: String,
        val amount: Double
) {
    constructor(incomeSource: IncomeSource): this(
            incomeSource.id,
            incomeSource.name,
            incomeSource.amount
    )
}