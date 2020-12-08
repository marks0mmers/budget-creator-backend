package com.marks0mmers.budgetcreator.handlers

import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.services.BudgetService
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*
import com.marks0mmers.budgetcreator.util.*

class BudgetHandler (
        private val budgetService: BudgetService
) {
    fun getBudgetsForUser(req: ServerRequest) = req
            .principal()
            .map { budgetService.getAllBudgetItemsForUser(it.name) }
            .flatMap { ok().body(it) }

    fun createBudget(req: ServerRequest) = req
            .bodyToMono<BudgetSubmissionView>()
            .zipWith(req.principal())
            .map { (body, p) -> budgetService.createBudgetForUser(body, p.name) }
            .flatMap { ok().body(it) }

    fun updateBudget(req: ServerRequest) = req
            .bodyToMono<BudgetSubmissionView>()
            .map { budgetService.updateBudget(req.pathVariable("budgetId"), it) }
            .flatMap { ok().body(it) }
            .switchIfEmpty(notFound().build())

    fun deleteBudget(req: ServerRequest) = ok()
            .body(budgetService.deleteBudget(req.pathVariable("budgetId")))
            .switchIfEmpty(notFound().build())
}
