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

class Budget(id: EntityID<Int>) : IntEntity(id), DtoConvertible<BudgetDto> {
    object Budgets : IntIdTable("budgets") {
        val title = varchar("title", 100)
        val primaryUserId = reference("primary_user_id", Users)
    }
    companion object : IntEntityClass<Budget>(Budgets)

    var title by Budgets.title
    var primaryUser by User referencedOn Budgets.primaryUserId
    val incomeSources by IncomeSource referrersOn IncomeSources.budgetId
    val expenseSources by ExpenseSource referrersOn ExpenseSources.budgetId

    override fun toDto() = BudgetDto(this)
}