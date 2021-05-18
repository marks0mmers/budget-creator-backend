package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.ExpenseSource

data class ExpenseSourceDto(
    val id: Int,
    val name: String,
    val amount: Double,
    val category: ExpenseCategoryDto,
    val subCategory: ExpenseSubCategoryDto?
) {
    constructor(expenseSource: ExpenseSource): this(
        expenseSource.id.value,
        expenseSource.name,
        expenseSource.amount,
        expenseSource.category.toDto(),
        expenseSource.subCategory?.toDto(),
    )
}