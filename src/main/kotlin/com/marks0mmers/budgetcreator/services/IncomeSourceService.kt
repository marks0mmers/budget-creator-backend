package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.IncomeSourceDto
import com.marks0mmers.budgetcreator.models.persistent.IncomeSource
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.repositories.IncomeSourceRepository
import com.marks0mmers.budgetcreator.util.BudgetCreatorException
import com.marks0mmers.budgetcreator.util.fail
import org.springframework.http.HttpStatus.NOT_FOUND

/**
 * The Service layer responsible for handling [IncomeSource] functionality
 *
 * @author Mark Sommers
 */
class IncomeSourceService {

    /** The income source Postgres Repository */
    private val incomeSourceRepository = IncomeSourceRepository

    /**
     * Add an income source to the budget
     *
     * @param budgetId The budget to add the income source to
     * @param incomeSource The income source to ddd
     * @return The added income source
     * @throws BudgetCreatorException If the budget cannot be found
     */
    suspend fun addIncomeSourceToBudget(budgetId: Int, incomeSource: IncomeSourceSubmissionView): IncomeSourceDto {
        return incomeSourceRepository.create(budgetId, incomeSource)
            ?: fail("Cannot find budget $budgetId", NOT_FOUND)
    }

    /**
     * Removes the income source from a budget
     *
     * @param incomeSourceId The income source to remove
     * @return The removed income source
     * @throws BudgetCreatorException If the income source cannot be found
     */
    suspend fun removeIncomeSourceFromBudget(incomeSourceId: Int): IncomeSourceDto {
        return incomeSourceRepository.delete(incomeSourceId)
            ?: fail("Cannot find income source $incomeSourceId")
    }

    /**
     * Updates the income source on a budget
     *
     * @param incomeSourceId The income source to update
     * @param incomeSource The updated values of an income source
     * @return The updated income source
     * @throws BudgetCreatorException If the income source cannot be found
     */
    suspend fun updateIncomeSource(
        incomeSourceId: Int,
        incomeSource: IncomeSourceSubmissionView
    ): IncomeSourceDto {
        return incomeSourceRepository.update(incomeSourceId, incomeSource)
            ?: fail("Cannot find income source $incomeSourceId")
    }
}