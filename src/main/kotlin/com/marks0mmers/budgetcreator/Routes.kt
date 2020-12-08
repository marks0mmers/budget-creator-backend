package com.marks0mmers.budgetcreator

import com.marks0mmers.budgetcreator.handlers.BudgetHandler
import com.marks0mmers.budgetcreator.handlers.IncomeSourceHandler
import com.marks0mmers.budgetcreator.handlers.UserHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

class Routes(
        private val userHandler: UserHandler,
        private val budgetHandler: BudgetHandler,
        private val incomeSourceHandler: IncomeSourceHandler
) {
    fun routes() = router {
        accept(MediaType.APPLICATION_JSON).nest {
            "/api".nest {
                "/users".nest {
                    GET(userHandler::getCurrentUser)
                    GET("/{userId}", userHandler::getUserById)
                    POST(userHandler::createUser)
                    POST("/login", userHandler::login)
                }
                "/budget".nest {
                    GET(budgetHandler::getBudgetsForUser)
                    POST(budgetHandler::createBudget)

                    "/{budgetId}".nest {
                        PUT(budgetHandler::updateBudget)
                        DELETE(budgetHandler::deleteBudget)

                        "/incomeSource".nest {
                            POST(incomeSourceHandler::addIncomeSourceToBudget)

                            "/{incomeSourceId}".nest {
                                PUT(incomeSourceHandler::updateIncomeSourceOnBudget)
                                DELETE(incomeSourceHandler::deleteIncomeSourceFromBudget)
                            }
                        }
                    }
                }
            }
        }
    }
}