package com.marks0mmers.budgetcreator.handlers

import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.services.BudgetService
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*
import com.marks0mmers.budgetcreator.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.UNAUTHORIZED

class BudgetHandler {
    @Autowired
    lateinit var budgetService: BudgetService

    suspend fun getBudgetsForUser(req: ServerRequest) = try {
        val principal = req.awaitPrincipal() ?: fail("Cannot get user from request", UNAUTHORIZED)
        val budgets = budgetService.getAllBudgetItemsForUser(principal.name)
        ok().json()
            .bodyAndAwait(budgets)
    } catch (e: Exception) { handleException(e) }

    suspend fun createBudget(req: ServerRequest) = try {
        val principal = req.awaitPrincipal() ?: fail("Cannot get user from request", UNAUTHORIZED)
        val body = req.awaitBody<BudgetSubmissionView>()
        val createdBudget = budgetService.createBudgetForUser(body, principal.name)
        ok().json()
            .bodyValueAndAwait(createdBudget)
    } catch (e: Exception) { handleException(e) }

    suspend fun updateBudget(req: ServerRequest) = try {
        val body = req.awaitBody<BudgetSubmissionView>()
        val updatedBudget = budgetService.updateBudget(req.pathVariable("budgetId"), body)
        ok().json()
            .bodyValueAndAwait(updatedBudget)
    } catch (e: Exception) { handleException(e) }

    suspend fun deleteBudget(req: ServerRequest) = try {
        val deletedBudget = budgetService.deleteBudget(req.pathVariable("budgetId"))
        ok().json()
            .bodyValueAndAwait(deletedBudget)
    } catch (e: Exception) { handleException(e) }
}
