package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.services.IncomeSourceService
import com.marks0mmers.budgetcreator.util.POST
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class IncomeSourceController(val incomeSourceService: IncomeSourceService) {
    @Bean
    fun incomeSourceRouter() = coRouter {
        "/api/budgets/{budgetId}/incomeSource".nest {
            POST { req ->
                val body = req.awaitBody<IncomeSourceSubmissionView>()
                val budgetId = req.pathVariable("budgetId")
                val addedIncomeSource = incomeSourceService.addIncomeSourceToBudget(budgetId, body)
                ok().json()
                    .bodyValueAndAwait(addedIncomeSource)
            }

            PUT("/{incomeSourceId}") { req ->
                val body = req.awaitBody<IncomeSourceSubmissionView>()
                val budgetId = req.pathVariable("budgetId")
                val incomeSourceId = req.pathVariable("incomeSourceId")
                val updatedIncomeSource = incomeSourceService.updateIncomeSource(budgetId, incomeSourceId, body)
                ok().json()
                    .bodyValueAndAwait(updatedIncomeSource)
            }

            DELETE("/{incomeSourceId}") { req ->
                val budgetId = req.pathVariable("budgetId")
                val incomeSourceId = req.pathVariable("incomeSourceId")
                val removedIncomeSource = incomeSourceService.removeIncomeSourceFromBudget(budgetId, incomeSourceId)
                ok().json()
                    .bodyValueAndAwait(removedIncomeSource)
            }
        }
    }
}