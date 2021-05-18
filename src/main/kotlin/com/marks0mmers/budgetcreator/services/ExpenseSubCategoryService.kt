package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.ExpenseSubCategoryDto
import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import com.marks0mmers.budgetcreator.repositories.ExpenseSubCategoryRepository
import com.marks0mmers.budgetcreator.util.fail
import org.springframework.http.HttpStatus.NOT_FOUND

class ExpenseSubCategoryService(private val expenseSubCategoryRepository: ExpenseSubCategoryRepository) {
    suspend fun addExpenseSubCategoryToExpenseCategory(
        expenseCategoryId: Int,
        expenseSubCategory: ExpenseCategorySubmissionView
    ): ExpenseSubCategoryDto {
        return expenseSubCategoryRepository.create(expenseCategoryId, expenseSubCategory)
            ?: fail("Cannot find Expense Category $expenseCategoryId", NOT_FOUND)
    }

    suspend fun removeExpenseSubCategoryFromExpenseCategory(expenseSubCategoryId: Int): ExpenseSubCategoryDto {
        return expenseSubCategoryRepository.delete(expenseSubCategoryId)
            ?: fail("Cannot find Expense Sub-Category $expenseSubCategoryId", NOT_FOUND)
    }

    suspend fun updateExpenseSubCategory(
        expenseSubCategoryId: Int,
        expenseSubCategory: ExpenseCategorySubmissionView
    ): ExpenseSubCategoryDto {
        return expenseSubCategoryRepository.update(expenseSubCategoryId, expenseSubCategory)
            ?: fail("Cannot find Expense Sub-Category $expenseSubCategoryId", NOT_FOUND)
    }
}