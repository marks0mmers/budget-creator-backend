package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.IncomeSourceDto
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.repositories.IncomeSourceRepository
import com.marks0mmers.budgetcreator.util.fail
import org.springframework.http.HttpStatus.NOT_FOUND

class IncomeSourceService {

    private val incomeSourceRepository = IncomeSourceRepository

    suspend fun addIncomeSourceToBudget(budgetId: Int, incomeSource: IncomeSourceSubmissionView): IncomeSourceDto {
        return incomeSourceRepository.create(budgetId, incomeSource)
            ?: fail("Cannot find budget $budgetId", NOT_FOUND)
    }

    suspend fun removeIncomeSourceFromBudget(incomeSourceId: Int): IncomeSourceDto {
        return incomeSourceRepository.delete(incomeSourceId)
            ?: fail("Cannot find income source $incomeSourceId")
    }

    suspend fun updateIncomeSource(
        incomeSourceId: Int,
        incomeSource: IncomeSourceSubmissionView
    ): IncomeSourceDto {
        return incomeSourceRepository.update(incomeSourceId, incomeSource)
            ?: fail("Cannot find income source $incomeSourceId")
    }
}