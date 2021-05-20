package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.services.BudgetService
import org.springframework.web.reactive.function.server.*
import com.marks0mmers.budgetcreator.util.*
import org.springframework.http.HttpStatus.UNAUTHORIZED

/**
 * The API Controller for Budget related functionality
 *
 * @see RouterFunction
 * @see CoRouterFunctionDsl
 * @constructor
 * Injects the parameters as dependencies
 *
 * @param budgetService The budget service containing business logic
 */
class BudgetController(
    budgetService: BudgetService
): RouterFunction<ServerResponse> by coRouter({
    "/api/budgets".nest {
        GET { req ->
            val principal = req.awaitPrincipal() ?: fail("Cannot get user from request", UNAUTHORIZED)
            val budgets = budgetService.getAllBudgetItemsForUser(principal.name)
            ok().json()
                .bodyAndAwait(budgets)
        }

        POST { req ->
            val principal = req.awaitPrincipal() ?: fail("Cannot get user from request", UNAUTHORIZED)
            val body = req.awaitBody<BudgetSubmissionView>()
            val createdBudget = budgetService.createBudgetForUser(body, principal.name)
            ok().json()
                .bodyValueAndAwait(createdBudget)
        }

        PUT("/{budgetId}") { req ->
            val body = req.awaitBody<BudgetSubmissionView>()
            val budgetId = req.pathVariable("budgetId").toInt()
            val updatedBudget = budgetService.updateBudget(budgetId, body)
            ok().json()
                .bodyValueAndAwait(updatedBudget)
        }

        DELETE("/{budgetId}") { req ->
            val budgetId = req.pathVariable("budgetId").toInt()
            val deletedBudget = budgetService.deleteBudget(budgetId)
            ok().json()
                .bodyValueAndAwait(deletedBudget)
        }
    }
})
