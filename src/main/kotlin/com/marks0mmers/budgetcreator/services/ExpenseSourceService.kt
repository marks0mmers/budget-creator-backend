package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.ExpenseSourceDto
import com.marks0mmers.budgetcreator.models.persistent.ExpenseSource
import com.marks0mmers.budgetcreator.models.views.ExpenseSourceSubmissionView
import com.marks0mmers.budgetcreator.repositories.ExpenseSourceRepository
import com.marks0mmers.budgetcreator.util.fail
import com.marks0mmers.budgetcreator.util.BudgetCreatorException
import org.springframework.http.HttpStatus.NOT_FOUND

/**
 * The Service layer responsible for handling [ExpenseSource] functionality
 *
 * @author Mark Sommers
 */
class ExpenseSourceService {

    /** The expense source Postgres Repository */
    private val expenseSourceRepository = ExpenseSourceRepository

    /**
     * Adds an expense source to a budget
     *
     * @param budgetId The budget to add the expense source to
     * @param expenseSource The expense source to add
     * @return The added expense source
     * @throws BudgetCreatorException If the budget cannot be found
     */
    suspend fun addExpenseSourceToBudget(budgetId: Int, expenseSource: ExpenseSourceSubmissionView): ExpenseSourceDto {
        return expenseSourceRepository.create(budgetId, expenseSource)
            ?: fail("Cannot find budget $budgetId", NOT_FOUND)
    }

    /**
     * Removes an expense source from a budget
     *
     * @param expenseSourceId The expense source to remove
     * @return The removed expense source
     * @throws BudgetCreatorException If the expense source cannot be found
     */
    suspend fun removeExpenseSourceFromBudget(expenseSourceId: Int): ExpenseSourceDto {
        return expenseSourceRepository.delete(expenseSourceId)
            ?: fail("Cannot find Expense Source $expenseSourceId", NOT_FOUND)
    }

    /**
     * Updates an expense source
     *
     * @param expenseSourceId The expense source to update
     * @param expenseSource The new values to set on an expense source
     * @return The updated expense source
     * @throws BudgetCreatorException If the expense source cannot be found
     */
    suspend fun updateExpenseSource(
        expenseSourceId: Int,
        expenseSource: ExpenseSourceSubmissionView
    ): ExpenseSourceDto {
        return expenseSourceRepository.update(expenseSourceId, expenseSource)
            ?: fail("Cannot fid Expense Source $expenseSourceId", NOT_FOUND)
    }
}