package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.Budget

data class BudgetDto (
        val id: String? = null,
        val title: String,
        val primaryUserId: String?,
        val incomeSources: List<IncomeSourceDto>
) {
    constructor(budget: Budget) : this(
            budget.id,
            budget.title,
            budget.primaryUserId,
            budget.incomeSources.map(::IncomeSourceDto)
    )
}