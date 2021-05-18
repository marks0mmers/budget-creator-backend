package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory
import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import kotlinx.coroutines.flow.asFlow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object ExpenseCategoryRepository {
    suspend fun findAll() = newSuspendedTransaction {
        ExpenseCategory.all().map { it.toDto() }.asFlow()
    }

    suspend fun create(ec: ExpenseCategorySubmissionView) = newSuspendedTransaction {
        ExpenseCategory.new {
            name = ec.name
            description = ec.description
        }.toDto()
    }

    suspend fun update(expenseCategoryId: Int, expenseCategorySubmission: ExpenseCategorySubmissionView) = newSuspendedTransaction {
        ExpenseCategory.findById(expenseCategoryId)?.apply {
            name = expenseCategorySubmission.name
            description = expenseCategorySubmission.description
        }?.toDto()
    }

    suspend fun delete(expenseCategoryId: Int) = newSuspendedTransaction {
        ExpenseCategory.findById(expenseCategoryId)?.apply { delete() }?.toDto()
    }
}