package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.models.persistent.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface BudgetRepository : ReactiveMongoRepository<Budget, String> {
    fun findByPrimaryUserId(primaryUserId: String?): Flux<Budget>
}