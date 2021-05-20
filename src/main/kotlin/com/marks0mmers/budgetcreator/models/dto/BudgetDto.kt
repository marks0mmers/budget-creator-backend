package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.Budget

/**
 * The DTO that represents a Budget
 *
 * @property id The id of the budget
 * @property title The title of the budget
 * @property primaryUserId The primary user of the budget
 * @property incomeSources The list of income sources for the budget
 * @property expenseSources The list of expense sources for the budget
 */
data class BudgetDto(
    val id: Int,
    val title: String,
    val primaryUserId: Int,
    val incomeSources: List<IncomeSourceDto>,
    val expenseSources: List<ExpenseSourceDto>
) {
    /**
     * Creates a DTO based on the budget entity
     * @param budget The budget entity to create from
     */
    constructor(budget: Budget) : this(
        budget.id.value,
        budget.title,
        budget.primaryUser.id.value,
        budget.incomeSources.map(::IncomeSourceDto),
        budget.expenseSources.map(::ExpenseSourceDto)
    )
}