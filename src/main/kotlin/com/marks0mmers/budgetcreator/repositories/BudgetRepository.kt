package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.persistent.Budget
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface BudgetRepository : ReactiveMongoRepository<Budget, String>