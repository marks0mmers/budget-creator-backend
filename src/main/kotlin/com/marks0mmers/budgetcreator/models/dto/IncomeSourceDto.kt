package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.IncomeSource

data class IncomeSourceDto(
    val id: Int,
    val name: String,
    val amount: Double
) {
    constructor(incomeSource: IncomeSource) : this(
        incomeSource.id.value,
        incomeSource.name,
        incomeSource.amount
    )
}