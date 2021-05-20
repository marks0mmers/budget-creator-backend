package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.ExpenseSource

/**
 * The DTO that represents an ExpenseSource
 *
 * @property id The id of the expense source
 * @property name The name of the expense source
 * @property amount The amount of the expense source
 * @property category The category of the expense source
 * @property subCategory The sub-category of the expense source
 */
data class ExpenseSourceDto(
    val id: Int,
    val name: String,
    val amount: Double,
    val category: ExpenseCategoryDto,
    val subCategory: ExpenseSubCategoryDto?
) {
    /**
     * Creates a DTO based on the expense source entity
     * @param expenseSource The expense source entity to create from
     */
    constructor(expenseSource: ExpenseSource): this(
        expenseSource.id.value,
        expenseSource.name,
        expenseSource.amount,
        expenseSource.category.toDto(),
        expenseSource.subCategory?.toDto(),
    )
}