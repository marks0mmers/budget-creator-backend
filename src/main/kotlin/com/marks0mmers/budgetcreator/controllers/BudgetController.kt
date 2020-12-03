package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.models.views.DeletedObjectView
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.services.BudgetService
import com.marks0mmers.budgetcreator.services.IncomeSourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/budget")
class BudgetController @Autowired constructor(
        private val budgetService: BudgetService,
        private val incomeSourceService: IncomeSourceService
) {
    @GetMapping
    fun getBudgetsForUser(p: Principal) = budgetService
            .getAllBudgetItemsForUser(p.name)

    @PostMapping
    fun createBudget(@RequestBody budget: BudgetSubmissionView, p: Principal) = budgetService
            .createBudgetForUser(budget, p.name)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PutMapping("/{budgetId}")
    fun updateBudget(@PathVariable budgetId: String, @RequestBody budget: BudgetSubmissionView) = budgetService
            .updateBudget(budgetId, budget)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("/{budgetId}")
    fun deleteBudget(@PathVariable budgetId: String) = budgetService
            .deleteBudget(budgetId)
            .map { ResponseEntity.ok(DeletedObjectView(it)) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PostMapping("/{budgetId}/incomeSource")
    fun addIncomeSourceToBudget(@PathVariable budgetId: String, @RequestBody incomeSource: IncomeSourceSubmissionView) = incomeSourceService
            .addIncomeSourceToBudget(budgetId, incomeSource)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PutMapping("/{budgetId}/incomeSource/{incomeSourceId}")
    fun updateIncomeSourceOnBudget(
            @PathVariable budgetId: String,
            @PathVariable incomeSourceId: String,
            @RequestBody incomeSource: IncomeSourceSubmissionView
    ) = incomeSourceService
            .updateIncomeSource(budgetId, incomeSourceId, incomeSource)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("/{budgetId}/incomeSource/{incomeSourceId}")
    fun deleteIncomeSourceFromBudget(@PathVariable budgetId: String, @PathVariable incomeSourceId: String) = incomeSourceService
            .removeIncomeSourceFromBudget(budgetId, incomeSourceId)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

}