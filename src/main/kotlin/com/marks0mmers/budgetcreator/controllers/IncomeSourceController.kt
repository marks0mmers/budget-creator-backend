package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.services.IncomeSourceService
import com.marks0mmers.budgetcreator.util.POST
import org.springframework.web.reactive.function.server.*

/**
 * The API Controller for Income Source related functionality
 *
 * @see RouterFunction
 * @see CoRouterFunctionDsl
 * @constructor
 * Injects the parameters as dependencies
 *
 * @param incomeSourceService The income source service containing business logic
 */
class IncomeSourceController(
    incomeSourceService: IncomeSourceService
) : RouterFunction<ServerResponse> by coRouter({
    "/api/budgets/{budgetId}/incomeSource".nest {
        POST { req ->
            val body = req.awaitBody<IncomeSourceSubmissionView>()
            val budgetId = req.pathVariable("budgetId").toInt()
            val addedIncomeSource = incomeSourceService.addIncomeSourceToBudget(budgetId, body)
            ok().json()
                .bodyValueAndAwait(addedIncomeSource)
        }

        PUT("/{incomeSourceId}") { req ->
            val body = req.awaitBody<IncomeSourceSubmissionView>()
            val incomeSourceId = req.pathVariable("incomeSourceId").toInt()
            val updatedIncomeSource = incomeSourceService.updateIncomeSource(incomeSourceId, body)
            ok().json()
                .bodyValueAndAwait(updatedIncomeSource)
        }

        DELETE("/{incomeSourceId}") { req ->
            val incomeSourceId = req.pathVariable("incomeSourceId").toInt()
            val removedIncomeSource = incomeSourceService.removeIncomeSourceFromBudget(incomeSourceId)
            ok().json()
                .bodyValueAndAwait(removedIncomeSource)
        }
    }
})