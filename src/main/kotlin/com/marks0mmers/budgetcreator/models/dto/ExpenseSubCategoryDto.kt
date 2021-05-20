package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.ExpenseSubCategory

/**
 * The DTO that represents an Expense Sub-category
 *
 * @property id The id of the sub-category
 * @property name The name of the sub-category
 * @property description The description of the sub-category
 */
data class ExpenseSubCategoryDto(
    val id: Int,
    val name: String,
    val description: String
) {
    /**
     * Creates a DTO based on the expense sub-category entity
     */
    constructor(expenseSubCategory: ExpenseSubCategory) : this(
        expenseSubCategory.id.value,
        expenseSubCategory.name,
        expenseSubCategory.description
    )
}