package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.ExpenseCategoryDto
import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import com.marks0mmers.budgetcreator.repositories.ExpenseCategoryRepository
import com.marks0mmers.budgetcreator.util.fail
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.http.HttpStatus.NOT_FOUND

class ExpenseSubCategoryService(private val expenseCategoryRepository: ExpenseCategoryRepository) {
    suspend fun addExpenseSubCategoryToExpenseCategory(
        expenseCategoryId: String,
        expenseSubCategory: ExpenseCategorySubmissionView
    ): ExpenseCategoryDto {
        val expenseCategory = expenseCategoryRepository
            .findById(expenseCategoryId)
            .awaitFirstOrElse { fail("Cannot find Expense Category $expenseCategoryId", NOT_FOUND) }
        return expenseCategoryRepository
            .save(expenseCategory.addExpenseSubCategory(expenseSubCategory))
            .awaitFirstOrElse { fail("Failed to add Expense Sub Category") }
            .toDto()
    }

    suspend fun removeExpenseSubCategoryFromExpenseCategory(
        expenseCategoryId: String,
        expenseSubCategoryId: String
    ): ExpenseCategoryDto {
        val expenseCategory = expenseCategoryRepository
            .findById(expenseCategoryId)
            .awaitFirstOrElse { fail("Cannot find Expense Category $expenseCategoryId", NOT_FOUND) }
        return expenseCategoryRepository
            .save(expenseCategory.removeExpenseSubCategory(expenseSubCategoryId))
            .awaitFirstOrElse { fail("Failed to add Expense Sub Category") }
            .toDto()
    }

    suspend fun updateExpenseSubCategory(
        expenseCategoryId: String,
        expenseSubCategoryId: String,
        expenseSubCategory: ExpenseCategorySubmissionView
    ): ExpenseCategoryDto {
        val expenseCategory = expenseCategoryRepository
            .findById(expenseCategoryId)
            .awaitFirstOrElse { fail("Cannot find Expense Category $expenseCategoryId", NOT_FOUND) }
        val expenseSubCategoryDto = expenseCategory.subCategories
            .first { s -> s.id == expenseSubCategoryId }
            .toDto()
        return expenseCategoryRepository
            .save(
                expenseCategory.updateExpenseSubCategory(
                    expenseSubCategoryDto.copy(
                        name = expenseSubCategory.name,
                        description = expenseSubCategory.description
                    )
                )
            )
            .awaitFirstOrElse { fail("Failed to add Expense Sub Category") }
            .toDto()
    }
}