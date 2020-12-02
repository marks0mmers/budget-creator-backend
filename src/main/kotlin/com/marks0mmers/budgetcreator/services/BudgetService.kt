package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.repositories.BudgetRepository
import com.marks0mmers.budgetcreator.util.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BudgetService @Autowired constructor(
        private val budgetRepository: BudgetRepository,
        private val userService: UserService
) {
    fun getAllBudgetItemsForUser(username: String) = userService
            .getUserByUsername(username)
            .flatMapMany {
                budgetRepository.findByPrimaryUserId(it.id)
            }
            .map(::BudgetDto)

    fun createBudgetForUser(budget: BudgetDto, username: String) = userService
            .getUserByUsername(username)
            .flatMap {
                budgetRepository.insert(Budget(budget, it.id ?: fail("User $username's ID is null")))
            }
            .map(::BudgetDto)

    fun updateBudget(budgetId: String, budget: BudgetDto) = budgetRepository
            .findById(budgetId)
            .flatMap {
                budgetRepository.save(Budget(
                        it.id,
                        budget.title,
                        it.primaryUserId
                ))
            }
            .map(::BudgetDto)

    fun deleteBudget(budgetId: String) = budgetRepository
            .deleteById(budgetId)
            .then(Mono.just(budgetId))
}