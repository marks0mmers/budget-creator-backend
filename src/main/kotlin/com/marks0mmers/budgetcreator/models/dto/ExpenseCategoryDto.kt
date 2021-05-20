package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory

/**
 * The DTO that represents an ExpenseCategory
 *
 * @property id The id of the category
 * @property name The name of the category
 * @property description The description of the category
 * @property subCategories The list of sub-categories
 */
data class ExpenseCategoryDto(
    val id: Int,
    val name: String,
    val description: String,
    val subCategories: List<ExpenseSubCategoryDto>
) {
    /**
     * Creates a DTO based on the expense category entity
     * @param expenseCategory The expense category entity to create from
     */
    constructor(expenseCategory: ExpenseCategory) : this(
        expenseCategory.id.value,
        expenseCategory.name,
        expenseCategory.description,
        expenseCategory.subCategories.map { ExpenseSubCategoryDto(it) }
    )
}