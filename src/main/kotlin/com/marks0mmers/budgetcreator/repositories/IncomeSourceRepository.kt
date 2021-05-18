package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.models.persistent.IncomeSource
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object IncomeSourceRepository {
    suspend fun create(budgetId: Int, incomeSource: IncomeSourceSubmissionView) = newSuspendedTransaction {
        Budget.findById(budgetId)?.let { budget ->
            IncomeSource.new {
                name = incomeSource.name
                amount = incomeSource.amount
                this.budget = budget
            }
        }?.toDto()
    }

    suspend fun update(incomeSourceId: Int, incomeSource: IncomeSourceSubmissionView) = newSuspendedTransaction {
        IncomeSource.findById(incomeSourceId)?.apply {
            name = incomeSource.name
            amount = incomeSource.amount
        }?.toDto()
    }

    suspend fun delete(incomeSourceId: Int) = newSuspendedTransaction {
        IncomeSource.findById(incomeSourceId)?.apply { delete() }?.toDto()
    }
}