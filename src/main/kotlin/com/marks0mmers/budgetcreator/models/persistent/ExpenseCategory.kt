package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.ExpenseCategoryDto
import com.marks0mmers.budgetcreator.models.dto.ExpenseSubCategoryDto
import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import com.marks0mmers.budgetcreator.util.fail
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ExpenseCategory(
    val name: String,
    val description: String,
    val subCategories: List<ExpenseSubCategory>
) {
    @Id var id: String? = null

    constructor(expenseCategorySubmission: ExpenseCategorySubmissionView) : this(
        expenseCategorySubmission.name,
        expenseCategorySubmission.description,
        emptyList()
    )

    constructor(expenseCategoryDto: ExpenseCategoryDto) : this(
        expenseCategoryDto.name,
        expenseCategoryDto.description,
        expenseCategoryDto.subCategories.map { ExpenseSubCategory(it) }
    ) {
        id = expenseCategoryDto.id
    }

    fun addExpenseSubCategory(expenseSubCategory: ExpenseCategorySubmissionView): ExpenseCategory {
        return copy(
            subCategories = listOf(*subCategories.toTypedArray(), ExpenseSubCategory(expenseSubCategory))
        )
    }

    fun updateExpenseSubCategory(expenseSubCategory: ExpenseSubCategoryDto): ExpenseCategory {
        return copy(
            subCategories = listOf(
                *subCategories.filter { it.id != expenseSubCategory.id }.toTypedArray(),
                subCategories.find { it.id == expenseSubCategory.id }
                    ?.let { ExpenseSubCategory(expenseSubCategory) } ?: fail("Expense Sub Category not found")
            )
        )
    }

    fun removeExpenseSubCategory(expenseSubCategoryId: String): ExpenseCategory {
        return copy(
            subCategories = subCategories.filter { it.id != expenseSubCategoryId }
        )
    }
}