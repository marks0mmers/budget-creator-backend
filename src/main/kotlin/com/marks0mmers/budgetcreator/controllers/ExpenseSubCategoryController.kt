package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import com.marks0mmers.budgetcreator.services.ExpenseSubCategoryService
import com.marks0mmers.budgetcreator.util.POST
import org.springframework.web.reactive.function.server.*

/**
 * The API Controller for Expense Sub-category functionality
 *
 * @see RouterFunction
 * @see CoRouterFunctionDsl
 * @constructor
 * Injects the parameters as dependencies
 *
 * @param expenseSubCategoryService The expense sub-category service containing business logic
 */
class ExpenseSubCategoryController(
    expenseSubCategoryService: ExpenseSubCategoryService
) : RouterFunction<ServerResponse> by coRouter({
    "/api/expenseCategories/{expenseCategoryId}/expenseSubCategories".nest {
        POST { req ->
            val body = req.awaitBody<ExpenseCategorySubmissionView>()
            val expenseCategoryId = req.pathVariable("expenseCategoryId").toInt()
            val createdExpenseCategory = expenseSubCategoryService.addExpenseSubCategoryToExpenseCategory(
                expenseCategoryId,
                body
            )
            ok().json()
                .bodyValueAndAwait(createdExpenseCategory)
        }

        PUT("/{expenseSubCategoryId}") { req ->
            val body = req.awaitBody<ExpenseCategorySubmissionView>()
            val expenseSubCategoryId = req.pathVariable("expenseSubCategoryId").toInt()
            val updatedExpenseSubCategory = expenseSubCategoryService.updateExpenseSubCategory(
                expenseSubCategoryId,
                body
            )
            ok().json()
                .bodyValueAndAwait(updatedExpenseSubCategory)
        }

        DELETE("/{expenseSubCategoryId}") { req ->
            val expenseSubCategoryId = req.pathVariable("expenseSubCategoryId").toInt()
            val deletedExpenseSubCategory = expenseSubCategoryService.removeExpenseSubCategoryFromExpenseCategory(
                expenseSubCategoryId
            )
            ok().json()
                .bodyValueAndAwait(deletedExpenseSubCategory)
        }
    }
})