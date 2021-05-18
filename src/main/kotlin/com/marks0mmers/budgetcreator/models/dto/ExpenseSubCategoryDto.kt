package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.ExpenseSubCategory

data class ExpenseSubCategoryDto(
    val id: Int,
    val name: String,
    val description: String
) {
    constructor(expenseSubCategory: ExpenseSubCategory) : this(
        expenseSubCategory.id.value,
        expenseSubCategory.name,
        expenseSubCategory.description
    )
}