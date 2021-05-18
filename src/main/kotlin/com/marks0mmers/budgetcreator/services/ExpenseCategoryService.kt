package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.ExpenseCategoryDto
import com.marks0mmers.budgetcreator.models.views.DeletedObjectView
import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import com.marks0mmers.budgetcreator.repositories.ExpenseCategoryRepository
import com.marks0mmers.budgetcreator.util.fail
import kotlinx.coroutines.flow.Flow

class ExpenseCategoryService {

    private val expenseCategoryRepository = ExpenseCategoryRepository

    suspend fun getExpenseCategories(): Flow<ExpenseCategoryDto> {
        return expenseCategoryRepository
            .findAll()
    }

    suspend fun createExpenseCategory(ec: ExpenseCategorySubmissionView): ExpenseCategoryDto {
        return expenseCategoryRepository
            .create(ec)
    }

    suspend fun updateExpenseCategory(
        expenseCategoryId: Int,
        expenseCategorySubmission: ExpenseCategorySubmissionView
    ): ExpenseCategoryDto {
        return expenseCategoryRepository
            .update(expenseCategoryId, expenseCategorySubmission)
            ?: fail("Failed to update Expense Category")
    }

    suspend fun deleteExpenseCategory(expenseCategoryId: Int): DeletedObjectView {
        expenseCategoryRepository.delete(expenseCategoryId)
        return DeletedObjectView(expenseCategoryId)
    }
}