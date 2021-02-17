package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.IncomeSourceDto
import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.repositories.BudgetRepository
import com.marks0mmers.budgetcreator.util.fail
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service

@Service
class IncomeSourceService {
    @Autowired lateinit var budgetRepository: BudgetRepository

    suspend fun addIncomeSourceToBudget(budgetId: String, incomeSource: IncomeSourceSubmissionView): Budget {
        val budget = budgetRepository
            .findById(budgetId)
            .awaitFirstOrElse { fail("Cannot find budget $budgetId", NOT_FOUND) }
        return budgetRepository
            .save(budget.addIncomeSource(incomeSource))
            .awaitFirstOrElse { fail("Failed to add income source") }
    }

    suspend fun removeIncomeSourceFromBudget(budgetId: String, incomeSourceId: String): Budget {
        val budget = budgetRepository
            .findById(budgetId)
            .awaitFirstOrElse { fail("Cannot find budget $budgetId", NOT_FOUND) }
        return budgetRepository
            .save(budget.removeIncomeSource(incomeSourceId))
            .awaitFirstOrElse { fail("Failed to remove income source") }
    }

    suspend fun updateIncomeSource(
        budgetId: String,
        incomeSourceId: String,
        incomeSource: IncomeSourceSubmissionView
    ): Budget {
        val budget = budgetRepository
            .findById(budgetId)
            .awaitFirstOrElse { fail("Cannot find budget $budgetId", NOT_FOUND) }
        val incomeSourceDto = budget.incomeSources.first { it.id == incomeSourceId }.let { IncomeSourceDto(it) }
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
    }
}