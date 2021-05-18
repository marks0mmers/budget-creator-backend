package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.ExpenseSourceDto
import com.marks0mmers.budgetcreator.models.views.ExpenseSourceSubmissionView
import com.marks0mmers.budgetcreator.repositories.ExpenseSourceRepository
import com.marks0mmers.budgetcreator.util.fail
import org.springframework.http.HttpStatus.NOT_FOUND

class ExpenseSourceService(
    private val expenseSourceRepository: ExpenseSourceRepository
) {
    suspend fun addExpenseSourceToBudget(budgetId: Int, expenseSource: ExpenseSourceSubmissionView): ExpenseSourceDto {
        return expenseSourceRepository.create(budgetId, expenseSource)
            ?: fail("Cannot find budget $budgetId", NOT_FOUND)
    }

    suspend fun removeExpenseSourceFromBudget(expenseSourceId: Int): ExpenseSourceDto {
        return expenseSourceRepository.delete(expenseSourceId)
            ?: fail("Cannot find Expense Source $expenseSourceId", NOT_FOUND)
    }

    suspend fun updateExpenseSource(
        expenseSourceId: Int,
        expenseSource: ExpenseSourceSubmissionView
    ): ExpenseSourceDto {
        return expenseSourceRepository.update(expenseSourceId, expenseSource)
            ?: fail("Cannot fid Expense Source $expenseSourceId", NOT_FOUND)
    }
}