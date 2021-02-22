package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.models.views.DeletedObjectView
import com.marks0mmers.budgetcreator.repositories.BudgetRepository
import com.marks0mmers.budgetcreator.util.fail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.stereotype.Service

@Service
class BudgetService(private val budgetRepository: BudgetRepository, private val userService: UserService) {
    fun getAllBudgetItemsForUser(username: String): Flow<BudgetDto> {
        return budgetRepository
            .findAll()
            .asFlow()
            .filter { b -> userService.getUserByUsername(username).id == b.primaryUserId }
            .map { BudgetDto(it) }
    }

    private suspend fun getBudgetById(budgetId: String): BudgetDto {
        return budgetRepository
            .findById(budgetId)
            .awaitFirstOrElse { fail("Cannot find budget with ID: $budgetId") }
            .let { BudgetDto(it) }
    }

    suspend fun createBudgetForUser(budget: BudgetSubmissionView, username: String): BudgetDto {
        val user = userService.getUserByUsername(username)
        return budgetRepository
            .insert(Budget(budget, user.id))
            .awaitFirstOrElse { fail("Failed to create user") }
            .let { BudgetDto(it) }
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
            .let { BudgetDto(it) }
    }

    suspend fun deleteBudget(budgetId: String): DeletedObjectView {
        budgetRepository
            .deleteById(budgetId)
            .awaitFirstOrElse { fail("Failed to delete budget") }
        return DeletedObjectView(budgetId)
    }
}