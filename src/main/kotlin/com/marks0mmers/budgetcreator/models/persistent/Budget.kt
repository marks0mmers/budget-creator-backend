package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import com.marks0mmers.budgetcreator.models.dto.IncomeSourceDto
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.util.fail

data class Budget(
    val title: String,
    val expenseCategoryId: String,
    val expenseSubCategoryId: String,
    val primaryUserId: String,
    var incomeSources: List<IncomeSource>,
): DtoConvertible<BudgetDto> {
    var id: String? = null

    constructor(budget: BudgetSubmissionView, primaryUserId: String) : this(
        budget.title,
        budget.expenseCategoryId,
        budget.expenseSubCategoryId,
        primaryUserId,
        emptyList(),
    )

    constructor(budget: BudgetDto) : this(
        budget.title,
        budget.expenseCategoryId,
        budget.expenseSubCategoryId,
        budget.primaryUserId,
        budget.incomeSources.map { IncomeSource(it) }
    ) {
        id = budget.id
    }

    fun addIncomeSource(incomeSource: IncomeSourceSubmissionView): Budget {
        incomeSources = listOf(*incomeSources.toTypedArray(), IncomeSource(incomeSource))
        return this
    }

    fun updateIncomeSource(incomeSource: IncomeSourceDto): Budget {
        incomeSources = listOf(
            *incomeSources.filter { it.id != incomeSource.id }.toTypedArray(),
            incomeSources.find { it.id == incomeSource.id }
                ?.let { IncomeSource(incomeSource) } ?: fail("Income Source not found")
        )
        return this
    }

    fun removeIncomeSource(incomeSourceId: String): Budget {
        incomeSources = incomeSources.filter { it.id != incomeSourceId }
        return this
    }

    override fun toDto() = BudgetDto(this)
}