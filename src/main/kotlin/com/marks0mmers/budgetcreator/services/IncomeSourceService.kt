package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.repositories.BudgetRepository

class IncomeSourceService (
        private val budgetRepository: BudgetRepository
) {
    fun addIncomeSourceToBudget(budgetId: String, incomeSource: IncomeSourceSubmissionView) = budgetRepository
            .findById(budgetId)
            .flatMap {
                budgetRepository.save(it.addIncomeSource(incomeSource))
            }

    fun removeIncomeSourceFromBudget(budgetId: String, incomeSourceId: String) = budgetRepository
            .findById(budgetId)
            .flatMap {
                budgetRepository.save(it.removeIncomeSource(incomeSourceId))
            }

    fun updateIncomeSource(budgetId: String, incomeSourceId: String, incomeSource: IncomeSourceSubmissionView) = budgetRepository
            .findById(budgetId)
            .flatMap {
                budgetRepository.save(it.updateIncomeSource(incomeSourceId, incomeSource))
            }
}