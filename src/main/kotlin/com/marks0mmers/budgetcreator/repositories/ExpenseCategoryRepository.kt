package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ExpenseCategoryRepository : ReactiveMongoRepository<ExpenseCategory, String>