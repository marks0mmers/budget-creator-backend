package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import com.marks0mmers.budgetcreator.services.ExpenseCategoryService
import com.marks0mmers.budgetcreator.util.*
import org.springframework.web.reactive.function.server.*

fun expenseCategoryRouter(expenseCategoryService: ExpenseCategoryService) = coRouter {
    "/api/expenseCategories".nest {
        GET {
            val expenseCategories = expenseCategoryService.getExpenseCategories()
            ok().json()
                .bodyAndAwait(expenseCategories)
        }

        POST { req ->
            val body = req.awaitBody<ExpenseCategorySubmissionView>()
            val createdExpenseCategory = expenseCategoryService.createExpenseCategory(body)
            ok().json()
                .bodyValueAndAwait(createdExpenseCategory)
        }

        PUT("/{expenseCategoryId}") { req ->
            val expenseCategoryId = req.pathVariable("expenseCategoryId")
            val body = req.awaitBody<ExpenseCategorySubmissionView>()
            val updatedExpenseCategory = expenseCategoryService.updateExpenseCategory(expenseCategoryId, body)
            ok().json()
                .bodyValueAndAwait(updatedExpenseCategory)
        }

        DELETE("/{expenseCategoryId}") { req ->
            val expenseCategoryId = req.pathVariable("expenseCategoryId")
            val deletedExpenseCategory = expenseCategoryService.deleteExpenseCategory(expenseCategoryId)
            ok().json()
                .bodyValueAndAwait(deletedExpenseCategory)
        }
    }
}