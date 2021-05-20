package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.views.ExpenseSourceSubmissionView
import com.marks0mmers.budgetcreator.services.ExpenseSourceService
import com.marks0mmers.budgetcreator.util.POST
import org.springframework.web.reactive.function.server.*

/**
 * The API Controller for Expense Source functionality
 *
 * @see RouterFunction
 * @see CoRouterFunctionDsl
 * @constructor
 * Injects the parameters as dependencies
 *
 * @param expenseSourceService The expense source service containing business logic
 */
class ExpenseSourceController(
    expenseSourceService: ExpenseSourceService
) : RouterFunction<ServerResponse> by coRouter({
    "/api/budgets/{budgetId}/expenseSource".nest {
        POST { req ->
            val body = req.awaitBody<ExpenseSourceSubmissionView>()
            val budgetId = req.pathVariable("budgetId").toInt()
            val addedExpenseSource = expenseSourceService.addExpenseSourceToBudget(budgetId, body)
            ok().json()
                .bodyValueAndAwait(addedExpenseSource)
        }
        PUT("/{expenseSourceId}") { req ->
            val body = req.awaitBody<ExpenseSourceSubmissionView>()
            val expenseSourceId = req.pathVariable("expenseSourceId").toInt()
            val updatedExpenseSource = expenseSourceService.updateExpenseSource(expenseSourceId, body)
            ok().json()
                .bodyValueAndAwait(updatedExpenseSource)
        }
        DELETE("/{expenseSourceId}") { req ->
            val expenseSourceId = req.pathVariable("expenseSourceId").toInt()
            val removedExpenseSource = expenseSourceService.removeExpenseSourceFromBudget(expenseSourceId)
            ok().json()
                .bodyValueAndAwait(removedExpenseSource)
        }
    }
})
