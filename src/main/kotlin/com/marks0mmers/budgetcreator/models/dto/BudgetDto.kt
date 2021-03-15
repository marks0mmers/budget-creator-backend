package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.util.fail

data class BudgetDto(
    val id: String,
    val title: String,
    val expenseCategoryId: String,
    val expenseSubCategoryId: String,
    val primaryUserId: String,
    val incomeSources: List<IncomeSourceDto>
) {
    constructor(budget: Budget) : this(
        budget.id ?: fail("Budget id is null"),
        budget.title,
        budget.expenseCategoryId,
        budget.expenseSubCategoryId,
        budget.primaryUserId,
        budget.incomeSources.map(::IncomeSourceDto)
    )
}