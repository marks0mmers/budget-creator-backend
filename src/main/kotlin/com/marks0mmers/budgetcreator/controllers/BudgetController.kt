package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.services.BudgetService
import org.springframework.web.reactive.function.server.*
import com.marks0mmers.budgetcreator.util.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.UNAUTHORIZED

@Configuration
class BudgetController(val budgetService: BudgetService) {
    @Bean
    fun budgetRouter() = coRouter {
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
                val budgetId = req.pathVariable("budgetId")
                val updatedBudget = budgetService.updateBudget(budgetId, body)
                ok().json()
                    .bodyValueAndAwait(updatedBudget)
            }

            DELETE("/{budgetId}") { req ->
                val budgetId = req.pathVariable("budgetId")
                val deletedBudget = budgetService.deleteBudget(budgetId)
                ok().json()
                    .bodyValueAndAwait(deletedBudget)
            }
        }
    }
}
