package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory
import com.marks0mmers.budgetcreator.models.persistent.ExpenseSource
import com.marks0mmers.budgetcreator.models.views.ExpenseSourceSubmissionView
import com.marks0mmers.budgetcreator.util.fail
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * The singleton responsible for interacting with the Postgres expense_source table
 * 
 * @author Mark Sommers
 */
object ExpenseSourceRepository {

    /**
     * Creates an expense source on a budget
     *
     * @param budgetId The budget to create the expense source on
     * @param expenseSource The expense source to create
     * @return The created expense source
     */
    suspend fun create(budgetId: Int, expenseSource: ExpenseSourceSubmissionView) = newSuspendedTransaction {
        val expenseCategory = ExpenseCategory.findById(expenseSource.categoryId)?.load(ExpenseCategory::subCategories)
            ?: fail("Cannot find expense category ${expenseSource.categoryId}")

        Budget.findById(budgetId)?.let { budget ->
            ExpenseSource.new {
                name = expenseSource.name
                amount = expenseSource.amount
                this.budget = budget
                category = expenseCategory
                subCategory = expenseCategory.subCategories.find { it.id.value == expenseSource.subCategoryId }
            }
        }?.toDto()
    }

    /**
     * Update an expense source
     *
     * @param expenseSourceId The expense source to update
     * @param expenseSource The new values for the updated expense source
     * @return The updated expense source
     */
    suspend fun update(expenseSourceId: Int, expenseSource: ExpenseSourceSubmissionView) = newSuspendedTransaction {
        val expenseCategory = ExpenseCategory.findById(expenseSource.categoryId)?.load(ExpenseCategory::subCategories)
            ?: fail("Cannot find expense category ${expenseSource.categoryId}")

        ExpenseSource.findById(expenseSourceId)?.apply {
            name = expenseSource.name
            amount = expenseSource.amount
            category = expenseCategory
            subCategory = expenseCategory.subCategories.find { it.id.value == expenseSource.subCategoryId }
        }?.toDto()
    }

    /**
     * Delete an expense source
     *
     * @param expenseSourceId The expense source to delete
     * @return The deleted expense source
     */
    suspend fun delete(expenseSourceId: Int) = newSuspendedTransaction {
        ExpenseSource.findById(expenseSourceId)?.apply { delete() }?.toDto()
    }
}