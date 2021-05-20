package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory
import com.marks0mmers.budgetcreator.models.persistent.ExpenseSubCategory
import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * The singleton responsible for interacting with the Postgres expense_sub_category table
 *
 * @author Mark Sommers
 */
object ExpenseSubCategoryRepository {

    /**
     * Creates an expense sub-category on an expense category
     *
     * @param expenseCategoryId The expense category to create a sub-category on
     * @param expenseSubCategory The expense sub-category to create
     * @return The created expense sub-category
     */
    suspend fun create(expenseCategoryId: Int, expenseSubCategory: ExpenseCategorySubmissionView) = newSuspendedTransaction {
        ExpenseCategory.findById(expenseCategoryId)?.let { expenseCategory ->
            ExpenseSubCategory.new {
                this.expenseCategory = expenseCategory
                name = expenseSubCategory.name
                description = expenseSubCategory.description
            }
        }?.toDto()
    }

    /**
     * Update an expense sub-category
     *
     * @param expenseSubCategoryId The expense sub-category to update
     * @param expenseSubCategory The new values to update the sub-category with
     * @return The updated expense sub-category
     */
    suspend fun update(expenseSubCategoryId: Int, expenseSubCategory: ExpenseCategorySubmissionView) = newSuspendedTransaction {
        ExpenseSubCategory.findById(expenseSubCategoryId)?.apply {
            name = expenseSubCategory.name
            description = expenseSubCategory.description
        }?.toDto()
    }

    /**
     * Delete an expense sub-category
     *
     * @param expenseSubCategoryId The expense sub-category to delete
     * @return The deleted expense sub-category
     */
    suspend fun delete(expenseSubCategoryId: Int) = newSuspendedTransaction {
        ExpenseSubCategory.findById(expenseSubCategoryId)?.apply { delete() }?.toDto()
    }
}