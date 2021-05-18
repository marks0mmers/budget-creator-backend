package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.models.views.DeletedObjectView
import com.marks0mmers.budgetcreator.repositories.BudgetRepository
import com.marks0mmers.budgetcreator.util.fail
import kotlinx.coroutines.flow.Flow

class BudgetService {

    private val budgetRepository = BudgetRepository

    suspend fun getAllBudgetItemsForUser(username: String): Flow<BudgetDto> {
        return budgetRepository
            .findAllByUsername(username)
    }

    suspend fun createBudgetForUser(budget: BudgetSubmissionView, username: String): BudgetDto {
        return budgetRepository
            .create(budget, username)
    }

    suspend fun updateBudget(budgetId: Int, budgetSubmission: BudgetSubmissionView): BudgetDto {
        return budgetRepository
            .update(budgetId, budgetSubmission)
            ?: fail("Failed to find Budget $budgetId")
    }

    suspend fun deleteBudget(budgetId: Int): DeletedObjectView {
        budgetRepository.delete(budgetId)
        return DeletedObjectView(budgetId)
    }
}