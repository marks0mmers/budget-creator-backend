package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.ExpenseCategoryDto
import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory
import com.marks0mmers.budgetcreator.models.views.DeletedObjectView
import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import com.marks0mmers.budgetcreator.repositories.ExpenseCategoryRepository
import com.marks0mmers.budgetcreator.util.fail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.stereotype.Service

@Service
class ExpenseCategoryService(val expenseCategoryRepository: ExpenseCategoryRepository) {

    fun getExpenseCategories(): Flow<ExpenseCategoryDto> {
        return expenseCategoryRepository
            .findAll()
            .asFlow()
            .map { ExpenseCategoryDto(it) }
    }

    suspend fun getExpenseCategoryById(expenseCategoryId: String): ExpenseCategoryDto {
        return expenseCategoryRepository
            .findById(expenseCategoryId)
            .awaitFirstOrElse { fail("Cannot find expense category with ID: $expenseCategoryId") }
            .let { ExpenseCategoryDto(it) }
    }

    suspend fun createExpenseCategory(expenseCategory: ExpenseCategorySubmissionView): ExpenseCategoryDto {
        return expenseCategoryRepository
            .insert(ExpenseCategory(expenseCategory))
            .awaitFirstOrElse { fail("Failed to create Expense Category") }
            .let { ExpenseCategoryDto(it) }
    }

    suspend fun updateExpenseCategory(
        expenseCategoryId: String,
        expenseCategorySubmission: ExpenseCategorySubmissionView
    ): ExpenseCategoryDto {
        val expenseCategory = getExpenseCategoryById(expenseCategoryId)
        return expenseCategoryRepository
            .save(
                ExpenseCategory(
                    expenseCategory.copy(
                        name = expenseCategorySubmission.name,
                        description = expenseCategorySubmission.description
                    )
                )
            )
            .awaitFirstOrElse { fail("Failed to update Expense Category") }
            .let { ExpenseCategoryDto(it) }
    }

    suspend fun deleteExpenseCategory(expenseCategoryId: String): DeletedObjectView {
        expenseCategoryRepository
            .deleteById(expenseCategoryId)
            .awaitFirstOrElse { fail("Failed to delete Expense Category") }
        return DeletedObjectView(expenseCategoryId)
    }
}