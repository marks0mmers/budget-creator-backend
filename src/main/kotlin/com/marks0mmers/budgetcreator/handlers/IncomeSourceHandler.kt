package com.marks0mmers.budgetcreator.handlers

import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.services.IncomeSourceService
import com.marks0mmers.budgetcreator.util.handleException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*

class IncomeSourceHandler {
    @Autowired
    lateinit var incomeSourceService: IncomeSourceService

    suspend fun addIncomeSourceToBudget(req: ServerRequest) = try {
        val body = req.awaitBody<IncomeSourceSubmissionView>()
        val addedIncomeSource = incomeSourceService.addIncomeSourceToBudget(req.pathVariable("budgetId"), body)
        ok().json()
            .bodyValueAndAwait(addedIncomeSource)
    } catch (e: Exception) { handleException(e) }

    suspend fun updateIncomeSourceOnBudget(req: ServerRequest) = try {
        val body = req.awaitBody<IncomeSourceSubmissionView>()
        val updatedIncomeSource = incomeSourceService.updateIncomeSource(
            req.pathVariable("budgetId"),
            req.pathVariable("incomeSourceId"),
            body
        )
        ok().json()
            .bodyValueAndAwait(updatedIncomeSource)
    } catch (e: Exception) { handleException(e) }

    suspend fun deleteIncomeSourceFromBudget(req: ServerRequest) = try {
        val removedIncomeSource = incomeSourceService.removeIncomeSourceFromBudget(
            req.pathVariable("budgetId"),
            req.pathVariable("incomeSourceId")
        )
        ok().json()
            .bodyValueAndAwait(removedIncomeSource)
    } catch (e: Exception) { handleException(e) }
}