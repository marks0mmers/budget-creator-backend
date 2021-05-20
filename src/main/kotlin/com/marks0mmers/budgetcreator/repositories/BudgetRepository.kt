package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.persistent.Budget
import com.marks0mmers.budgetcreator.models.persistent.Budget.Budgets
import com.marks0mmers.budgetcreator.models.persistent.User
import com.marks0mmers.budgetcreator.models.persistent.User.Users
import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.util.fail
import kotlinx.coroutines.flow.asFlow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * The singleton responsible for interacting with the Postgres budget table
 *
 * @author Mark Sommers
 */
object BudgetRepository {

    /**
     * Find all budgets for a user
     *
     * @param username The user to get budgets for
     * @return The list of budgets
     */
    suspend fun findAllByUsername(username: String) = newSuspendedTransaction {
        val user = User.find { Users.username eq username }.firstOrNull() ?: fail("Cannot find user $username")
        Budget.find { Budgets.primaryUserId eq user.id }.map { it.toDto() }.asFlow()
    }

    /**
     * Create
     *
     * @param budget
     * @param username
     */
    suspend fun create(budget: BudgetSubmissionView, username: String) = newSuspendedTransaction {
        val user = User.find { Users.username eq username }.firstOrNull() ?: fail("Cannot find user $username")
        Budget.new {
            title = budget.title
            primaryUser = user
        }.toDto()
    }

    suspend fun update(budgetId: Int, budget: BudgetSubmissionView) = newSuspendedTransaction {
        Budget.findById(budgetId)?.apply {
            title = budget.title
        }?.toDto()
    }

    suspend fun delete(budgetId: Int) = newSuspendedTransaction {
        Budget.findById(budgetId)?.apply { delete() }?.toDto()
    }
}