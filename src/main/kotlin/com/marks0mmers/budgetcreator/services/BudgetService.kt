package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.models.views.DeletedObjectView
import com.marks0mmers.budgetcreator.repositories.BudgetRepository
import com.marks0mmers.budgetcreator.util.fail
import com.marks0mmers.budgetcreator.util.toDtos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.reactive.*
import org.springframework.stereotype.Service

@Service
class BudgetService(private val budgetRepository: BudgetRepository, private val userService: UserService) {
    fun getAllBudgetItemsForUser(username: String): Flow<BudgetDto> {
        return budgetRepository
            .findAll()
            .asFlow()
            .filter { b -> userService.getUserByUsername(username).id == b.primaryUserId }
            .toDtos()
    }

    private suspend fun getBudgetById(budgetId: String): BudgetDto {
        return budgetRepository
            .findById(budgetId)
            .awaitFirstOrElse { fail("Cannot find budget with ID: $budgetId") }
            .toDto()
    }

    suspend fun createBudgetForUser(budget: BudgetSubmissionView, username: String): BudgetDto {
        val user = userService.getUserByUsername(username)
        return budgetRepository
            .save(Budget(budget, user.id))
            .awaitFirstOrElse { fail("Failed to create budget") }
            .toDto()
    }

    suspend fun updateBudget(budgetId: String, budgetSubmission: BudgetSubmissionView): BudgetDto {
        val budget = getBudgetById(budgetId)
        return budgetRepository
            .save(
                Budget(
                    budget.copy(
                        title = budgetSubmission.title
                    )
                )
            )
            .awaitFirstOrElse { fail("Failed to update budget") }
            .toDto()
    }

    suspend fun deleteBudget(budgetId: String): DeletedObjectView {
        budgetRepository
            .deleteById(budgetId)
            .awaitFirstOrNull()
        return DeletedObjectView(budgetId)
    }
}