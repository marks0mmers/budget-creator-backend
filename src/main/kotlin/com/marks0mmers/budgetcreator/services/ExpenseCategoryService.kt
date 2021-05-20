package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.ExpenseCategoryDto
import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory
import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import com.marks0mmers.budgetcreator.repositories.ExpenseCategoryRepository
import com.marks0mmers.budgetcreator.util.fail
import com.marks0mmers.budgetcreator.util.BudgetCreatorException
import kotlinx.coroutines.flow.Flow

/**
 * The Service layer responsible for handling [ExpenseCategory] functionality
 *
 * @author Mark Sommers
 */
class ExpenseCategoryService {

    /** The expense category Postgres Repository */
    private val expenseCategoryRepository = ExpenseCategoryRepository

    /**
     * Gets all the expense categories
     *
     * @return The list of expense categories
     */
    suspend fun getExpenseCategories(): Flow<ExpenseCategoryDto> {
        return expenseCategoryRepository
            .findAll()
    }

    /**
     * Creates an expense category
     *
     * @param ec The expense category to create
     * @return The created expense category
     */
    suspend fun createExpenseCategory(ec: ExpenseCategorySubmissionView): ExpenseCategoryDto {
        return expenseCategoryRepository
            .create(ec)
    }

    /**
     * Updates the expense category
     *
     * @param expenseCategoryId The expense category ID to update
     * @param expenseCategorySubmission The new values to set on the expense category
     * @return The updated expense category
     * @throws BudgetCreatorException If the expense category cannot be found
     */
    suspend fun updateExpenseCategory(
        expenseCategoryId: Int,
        expenseCategorySubmission: ExpenseCategorySubmissionView
    ): ExpenseCategoryDto {
        return expenseCategoryRepository
            .update(expenseCategoryId, expenseCategorySubmission)
            ?: fail("Failed to update Expense Category $expenseCategoryId")
    }

    /**
     * Removes the expense category
     *
     * @param expenseCategoryId The expense category ID to remove
     * @return The removed expense category
     */
    suspend fun deleteExpenseCategory(expenseCategoryId: Int): ExpenseCategoryDto {
        return expenseCategoryRepository
            .delete(expenseCategoryId)
            ?: fail("Failed to remove Expense Category $expenseCategoryId")
    }
}