package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.repositories.BudgetRepository
import com.marks0mmers.budgetcreator.util.fail
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.http.HttpStatus.NOT_FOUND

class IncomeSourceService(private val budgetRepository: BudgetRepository) {
    suspend fun addIncomeSourceToBudget(budgetId: String, incomeSource: IncomeSourceSubmissionView): BudgetDto {
        val budget = budgetRepository
            .findById(budgetId)
            .awaitFirstOrElse { fail("Cannot find budget $budgetId", NOT_FOUND) }
        return budgetRepository
            .save(budget.addIncomeSource(incomeSource))
            .awaitFirstOrElse { fail("Failed to add income source") }
            .toDto()
    }

    suspend fun removeIncomeSourceFromBudget(budgetId: String, incomeSourceId: String): BudgetDto {
        val budget = budgetRepository
            .findById(budgetId)
            .awaitFirstOrElse { fail("Cannot find budget $budgetId", NOT_FOUND) }
        return budgetRepository
            .save(budget.removeIncomeSource(incomeSourceId))
            .awaitFirstOrElse { fail("Failed to remove income source") }
            .toDto()
    }

    suspend fun updateIncomeSource(
        budgetId: String,
        incomeSourceId: String,
        incomeSource: IncomeSourceSubmissionView
    ): BudgetDto {
        val budget = budgetRepository
            .findById(budgetId)
            .awaitFirstOrElse { fail("Cannot find budget $budgetId", NOT_FOUND) }
        val incomeSourceDto = budget.incomeSources
            .first { i -> i.id == incomeSourceId }
            .toDto()
        return budgetRepository
            .save(
                budget.updateIncomeSource(
                    incomeSourceDto.copy(
                        name = incomeSource.name,
                        amount = incomeSource.amount
                    )
                )
            )
            .awaitFirstOrElse { fail("Failed to update income source $incomeSourceId") }
            .toDto()
    }
}