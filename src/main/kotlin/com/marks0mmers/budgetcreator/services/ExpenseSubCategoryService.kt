package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.ExpenseSubCategoryDto
import com.marks0mmers.budgetcreator.models.persistent.ExpenseSubCategory
import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import com.marks0mmers.budgetcreator.repositories.ExpenseSubCategoryRepository
import com.marks0mmers.budgetcreator.util.BudgetCreatorException
import com.marks0mmers.budgetcreator.util.fail
import org.springframework.http.HttpStatus.NOT_FOUND

/**
 * The service layer responsible for handling [ExpenseSubCategory] functionality
 *
 * @author Mark Sommers
 */
class ExpenseSubCategoryService {

    /** The expense sub-category Postgres Repository */
    private val expenseSubCategoryRepository = ExpenseSubCategoryRepository

    /**
     * Add an expense sub-category to an expense category
     *
     * @param expenseCategoryId The expense category to add the sub-category to
     * @param expenseSubCategory The expense sub-category to add
     * @return The added expense sub-category
     * @throws BudgetCreatorException If the expense category cannot be found
     */
    suspend fun addExpenseSubCategoryToExpenseCategory(
        expenseCategoryId: Int,
        expenseSubCategory: ExpenseCategorySubmissionView
    ): ExpenseSubCategoryDto {
        return expenseSubCategoryRepository.create(expenseCategoryId, expenseSubCategory)
            ?: fail("Cannot find Expense Category $expenseCategoryId", NOT_FOUND)
    }

    /**
     * Remove an expense sub-category from an expense category
     *
     * @param expenseSubCategoryId The sub-category to remove
     * @return The removed expense sub-category
     * @throws BudgetCreatorException If the expense sub-category cannot be found
     */
    suspend fun removeExpenseSubCategoryFromExpenseCategory(expenseSubCategoryId: Int): ExpenseSubCategoryDto {
        return expenseSubCategoryRepository.delete(expenseSubCategoryId)
            ?: fail("Cannot find Expense Sub-Category $expenseSubCategoryId", NOT_FOUND)
    }

    /**
     * Update an expense sub-category on an expense category
     *
     * @param expenseSubCategoryId The expense sub-category to update
     * @param expenseSubCategory The values to update on the sub-category
     * @return The updated sub-category
     * @throws BudgetCreatorException If the expense sub-category cannot be found
     */
    suspend fun updateExpenseSubCategory(
        expenseSubCategoryId: Int,
        expenseSubCategory: ExpenseCategorySubmissionView
    ): ExpenseSubCategoryDto {
        return expenseSubCategoryRepository.update(expenseSubCategoryId, expenseSubCategory)
            ?: fail("Cannot find Expense Sub-Category $expenseSubCategoryId", NOT_FOUND)
    }
}