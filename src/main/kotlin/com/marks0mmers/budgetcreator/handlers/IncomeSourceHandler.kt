package com.marks0mmers.budgetcreator.handlers

import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.services.IncomeSourceService
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*


class IncomeSourceHandler (
        private val incomeSourceService: IncomeSourceService
) {
    fun addIncomeSourceToBudget(req: ServerRequest) = req
            .bodyToMono<IncomeSourceSubmissionView>()
            .map { incomeSourceService.addIncomeSourceToBudget(req.pathVariable("budgetId"), it) }
            .flatMap { ok().body(it) }
            .switchIfEmpty(notFound().build())

    fun updateIncomeSourceOnBudget(req: ServerRequest) = req
            .bodyToMono<IncomeSourceSubmissionView>()
            .map { incomeSourceService.updateIncomeSource(
                    req.pathVariable("budgetId"),
                    req.pathVariable("incomeSourceId"),
                    it
            ) }
            .flatMap { ok().body(it) }
            .switchIfEmpty(notFound().build())

    fun deleteIncomeSourceFromBudget(req: ServerRequest) = ok()
            .body(incomeSourceService.removeIncomeSourceFromBudget(
                    req.pathVariable("budgetId"),
                    req.pathVariable("incomeSourceId")
            ))
            .switchIfEmpty(notFound().build())
}