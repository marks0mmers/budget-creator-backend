package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory

data class ExpenseCategoryDto(
    val id: Int,
    val name: String,
    val description: String,
    val subCategories: List<ExpenseSubCategoryDto>
) {
    constructor(expenseCategory: ExpenseCategory) : this(
        expenseCategory.id.value,
        expenseCategory.name,
        expenseCategory.description,
        expenseCategory.subCategories.map { ExpenseSubCategoryDto(it) }
    )
}