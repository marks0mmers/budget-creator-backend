package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory
import com.marks0mmers.budgetcreator.util.fail

data class ExpenseCategoryDto(
    val id: String,
    val name: String,
    val description: String,
    val subCategories: List<ExpenseSubCategoryDto>
) {
    constructor(expenseCategory: ExpenseCategory) : this(
        expenseCategory.id ?: fail("Expense Category id is null"),
        expenseCategory.name,
        expenseCategory.description,
        expenseCategory.subCategories.map { ExpenseSubCategoryDto(it) }
    )
}