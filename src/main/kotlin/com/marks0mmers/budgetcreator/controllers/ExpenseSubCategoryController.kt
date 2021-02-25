package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import com.marks0mmers.budgetcreator.services.ExpenseSubCategoryService
import com.marks0mmers.budgetcreator.util.POST
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.json

@Configuration
class ExpenseSubCategoryController(val expenseSubCategoryService: ExpenseSubCategoryService) {
    @Bean
    fun expenseSubCategoryRouter() = coRouter {
        "/api/expenseCategories/{expenseCategoryId}/expenseSubCategories".nest {
            POST { req ->
                val body = req.awaitBody<ExpenseCategorySubmissionView>()
                val expenseCategoryId = req.pathVariable("expenseCategoryId")
                val createdExpenseCategory = expenseSubCategoryService.addExpenseSubCategoryToExpenseCategory(
                    expenseCategoryId,
                    body
                )
                ok().json()
                    .bodyValueAndAwait(createdExpenseCategory)
            }

            PUT("/{expenseSubCategoryId}") { req ->
                val body = req.awaitBody<ExpenseCategorySubmissionView>()
                val expenseCategoryId = req.pathVariable("expenseCategoryId")
                val expenseSubCategoryId  = req.pathVariable("expenseSubCategoryId")
                val updatedExpenseSubCategory = expenseSubCategoryService.updateExpenseSubCategory(
                    expenseCategoryId,
                    expenseSubCategoryId,
                    body
                )
                ok().json()
                    .bodyValueAndAwait(updatedExpenseSubCategory)
            }

            DELETE("/{expenseSubCategoryId}") { req ->
                val expenseCategoryId = req.pathVariable("expenseCategoryId")
                val expenseSubCategoryId  = req.pathVariable("expenseSubCategoryId")
                val deletedExpenseSubCategory = expenseSubCategoryService.removeExpenseSubCategoryFromExpenseCategory(
                    expenseCategoryId,
                    expenseSubCategoryId
                )
                ok().json()
                    .bodyValueAndAwait(deletedExpenseSubCategory)
            }
        }
    }
}