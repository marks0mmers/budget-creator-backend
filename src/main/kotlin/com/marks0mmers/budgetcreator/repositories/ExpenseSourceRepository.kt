package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory
import com.marks0mmers.budgetcreator.models.persistent.ExpenseSource
import com.marks0mmers.budgetcreator.models.views.ExpenseSourceSubmissionView
import com.marks0mmers.budgetcreator.util.fail
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object ExpenseSourceRepository {
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

    suspend fun delete(expenseSourceId: Int) = newSuspendedTransaction {
        ExpenseSource.findById(expenseSourceId)?.apply { delete() }?.toDto()
    }
}