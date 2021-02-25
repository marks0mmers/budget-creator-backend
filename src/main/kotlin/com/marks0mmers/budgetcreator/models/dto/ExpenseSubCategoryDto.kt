package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.ExpenseSubCategory
import com.marks0mmers.budgetcreator.util.fail

data class ExpenseSubCategoryDto(
    val id: String,
    val name: String,
    val description: String
) {
    constructor(expenseSubCategory: ExpenseSubCategory) : this(
        expenseSubCategory.id,
        expenseSubCategory.name,
        expenseSubCategory.description
    )
}