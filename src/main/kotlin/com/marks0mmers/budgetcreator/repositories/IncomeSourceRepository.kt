package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.models.persistent.IncomeSource
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * The singleton responsible for interacting with the Postgres income_source table
 *
 * @author Mark Sommers
 */
object IncomeSourceRepository {

    /**
     * Creates an income source on a budget
     *
     * @param budgetId The budget to create the income source on
     * @param incomeSource The income source to create
     * @return The created income source
     */
    suspend fun create(budgetId: Int, incomeSource: IncomeSourceSubmissionView) = newSuspendedTransaction {
        Budget.findById(budgetId)?.let { budget ->
            IncomeSource.new {
                name = incomeSource.name
                amount = incomeSource.amount
                this.budget = budget
            }
        }?.toDto()
    }

    /**
     * Update an income source
     *
     * @param incomeSourceId The income source to update
     * @param incomeSource The new values for the updated income source
     * @return The updated income source
     */
    suspend fun update(incomeSourceId: Int, incomeSource: IncomeSourceSubmissionView) = newSuspendedTransaction {
        IncomeSource.findById(incomeSourceId)?.apply {
            name = incomeSource.name
            amount = incomeSource.amount
        }?.toDto()
    }

    /**
     * Delete an income source
     *
     * @param incomeSourceId The income source to delete
     * @return The deleted income source
     */
    suspend fun delete(incomeSourceId: Int) = newSuspendedTransaction {
        IncomeSource.findById(incomeSourceId)?.apply { delete() }?.toDto()
    }
}