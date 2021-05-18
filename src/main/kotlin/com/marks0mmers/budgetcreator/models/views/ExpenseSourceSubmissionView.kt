package com.marks0mmers.budgetcreator.models.views

data class ExpenseSourceSubmissionView(
    val name: String,
    val amount: Double,
    val categoryId: Int,
    val subCategoryId: Int?
)