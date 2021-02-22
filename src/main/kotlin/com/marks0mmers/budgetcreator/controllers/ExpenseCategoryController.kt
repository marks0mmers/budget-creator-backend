package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import com.marks0mmers.budgetcreator.services.ExpenseCategoryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class ExpenseCategoryController(val expenseCategoryService: ExpenseCategoryService) {
    @Bean
    fun expenseCategoryRouter() = coRouter {
        "/api".nest {
            GET("/expenseCategories") {
                val expenseCategories = expenseCategoryService.getExpenseCategories()
                ok().json()
                    .bodyAndAwait(expenseCategories)
            }

            POST("/expenseCategories") { req ->
                val body = req.awaitBody<ExpenseCategorySubmissionView>()
                val createdExpenseCategory = expenseCategoryService.createExpenseCategory(body)
                ok().json()
                    .bodyValueAndAwait(createdExpenseCategory)
            }

            PUT("/expenseCategories/{expenseCategoryId}") { req ->
                val expenseCategoryId = req.pathVariable("expenseCategoryId")
                val body = req.awaitBody<ExpenseCategorySubmissionView>()
                val updatedExpenseCategory = expenseCategoryService.updateExpenseCategory(expenseCategoryId, body)
                ok().json()
                    .bodyValueAndAwait(updatedExpenseCategory)
            }

            DELETE("/expenseCategories/{expenseCategoryId}") { req ->
                val expenseCategoryId = req.pathVariable("expenseCategoryId")
                val deletedExpenseCategory = expenseCategoryService.deleteExpenseCategory(expenseCategoryId)
                ok().json()
                    .bodyValueAndAwait(deletedExpenseCategory)
            }
        }
    }
}