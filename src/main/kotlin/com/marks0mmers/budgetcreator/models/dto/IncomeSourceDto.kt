package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.IncomeSource
import com.marks0mmers.budgetcreator.util.fail

data class IncomeSourceDto(
    val id: String,
    val name: String,
    val amount: Double
) {
    constructor(incomeSource: IncomeSource) : this(
        incomeSource.id,
        incomeSource.name,
        incomeSource.amount
    )
}