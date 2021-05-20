package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import com.marks0mmers.budgetcreator.models.persistent.ExpenseSource.ExpenseSources
import com.marks0mmers.budgetcreator.models.persistent.IncomeSource.IncomeSources
import com.marks0mmers.budgetcreator.models.persistent.User.Users
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Entity that represents the budget
 *
 * @author Mark Sommers
 * @constructor
 * Internal constructor used by Exposed
 *
 * @param id The primary key for the Budget
 */
class Budget(id: EntityID<Int>) : IntEntity(id), DtoConvertible<BudgetDto> {

    /** The object that represents the Database table budgets */
    object Budgets : IntIdTable("budgets") {
        /** The title of the budget */
        val title = varchar("title", 100)
        /** The primary user of the budget */
        val primaryUserId = reference("primary_user_id", Users)
    }

    /** The companion object that allows for the query DSL */
    companion object : IntEntityClass<Budget>(Budgets)

    /** The title of the budget */
    var title by Budgets.title
    /** The primary user of the budget */
    var primaryUser by User referencedOn Budgets.primaryUserId
    /** The list of income sources of the budget */
    val incomeSources by IncomeSource referrersOn IncomeSources.budgetId
    /** The list of expense sources of the budget */
    val expenseSources by ExpenseSource referrersOn ExpenseSources.budgetId

    /**
     * Converts this Entity to a DTO
     *
     * @return The DTO
     * @see DtoConvertible.toDto
     */
    override fun toDto() = BudgetDto(this)
}