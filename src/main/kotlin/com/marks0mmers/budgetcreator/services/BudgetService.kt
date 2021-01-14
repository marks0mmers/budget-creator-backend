package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.repositories.BudgetRepository
import com.marks0mmers.budgetcreator.util.fail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

class BudgetService {
    @Autowired
    lateinit var budgetRepository: BudgetRepository

    @Autowired
    lateinit var userService: UserService

    suspend fun getAllBudgetItemsForUser(username: String): Flow<BudgetDto> {
        val user = userService.getUserByUsername(username)
        return budgetRepository
            .findByPrimaryUserId(user.id)
            .asFlow()
            .map { BudgetDto(it) }
    }

    suspend fun createBudgetForUser(budget: BudgetSubmissionView, username: String): BudgetDto {
        val user = userService.getUserByUsername(username)
        val createdBudget = budgetRepository
            .insert(Budget(budget, user.id ?: fail("User $username's ID is null")))
            .awaitFirstOrElse { fail("Failed to create user") }
        return BudgetDto(createdBudget)
    }

    suspend fun updateBudget(budgetId: String, budgetSubmission: BudgetSubmissionView): BudgetDto {
        val budget = budgetRepository
            .findById(budgetId)
            .awaitFirstOrElse { fail("Cannot find budget", HttpStatus.NOT_FOUND) }
        val updatedBudget = budgetRepository.save(
            Budget(
                budget.id,
                budgetSubmission.title,
                budget.primaryUserId,
                budget.incomeSources
            )
        ).awaitFirstOrElse { fail("Failed to update budget") }
        return BudgetDto(updatedBudget)
    }

    suspend fun deleteBudget(budgetId: String): String {
        budgetRepository
            .deleteById(budgetId)
            .awaitFirstOrElse { fail("Failed to delete budget") }
        return budgetId
    }
}