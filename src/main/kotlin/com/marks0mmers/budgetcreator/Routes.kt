package com.marks0mmers.budgetcreator

import com.marks0mmers.budgetcreator.handlers.BudgetHandler
import com.marks0mmers.budgetcreator.handlers.IncomeSourceHandler
import com.marks0mmers.budgetcreator.handlers.UserHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

class Routes(
    private val userHandler: UserHandler,
    private val budgetHandler: BudgetHandler,
    private val incomeSourceHandler: IncomeSourceHandler
) {
    fun routes() = coRouter {
        accept(MediaType.APPLICATION_JSON).nest {
            "/api".nest {
                GET("/users", userHandler::getCurrentUser)
                POST("/users", userHandler::createUser)
                "/users".nest {
                    GET("/{userId}", userHandler::getUserById)
                    POST("/login", userHandler::login)
                }
                GET("/budgets", budgetHandler::getBudgetsForUser)
                POST("/budgets", budgetHandler::createBudget)
                "/budgets".nest {
                    PUT("/{budgetId}", budgetHandler::updateBudget)
                    DELETE("/{budgetId}", budgetHandler::deleteBudget)
                    "/{budgetId}".nest {
                        POST("/incomeSource", incomeSourceHandler::addIncomeSourceToBudget)
                        "/incomeSource".nest {
                            PUT("/{incomeSourceId}", incomeSourceHandler::updateIncomeSourceOnBudget)
                            DELETE("/{incomeSourceId}", incomeSourceHandler::deleteIncomeSourceFromBudget)
                        }
                    }
                }
            }
        }
    }
}