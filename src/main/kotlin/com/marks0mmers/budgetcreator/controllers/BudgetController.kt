package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import com.marks0mmers.budgetcreator.models.dto.DeletedObjectDto
import com.marks0mmers.budgetcreator.services.BudgetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/budget")
class BudgetController @Autowired constructor(
        private val budgetService: BudgetService
) {
    @GetMapping
    fun getBudgetsForUser(p: Principal) = budgetService
            .getAllBudgetItemsForUser(p.name)

    @PostMapping
    fun createBudget(@RequestBody budget: BudgetDto, p: Principal) = budgetService
            .createBudgetForUser(budget, p.name)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PutMapping("/{budgetId}")
    fun updateBudget(@PathVariable budgetId: String, @RequestBody budget: BudgetDto) = budgetService
            .updateBudget(budgetId, budget)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("/{budgetId}")
    fun deleteBudget(@PathVariable budgetId: String) = budgetService
            .deleteBudget(budgetId)
            .map { ResponseEntity.ok(DeletedObjectDto(it)) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
}