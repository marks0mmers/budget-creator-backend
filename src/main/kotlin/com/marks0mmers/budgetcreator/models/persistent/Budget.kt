package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Budgets : IntIdTable("budgets") {
    val title = varchar("title", 100)
    val primaryUserId = reference("primary_user_id", Users)
}

class Budget(id: EntityID<Int>) : IntEntity(id), DtoConvertible<BudgetDto> {
    companion object : IntEntityClass<Budget>(Budgets)

    var title by Budgets.title
    var primaryUser by User referencedOn Budgets.primaryUserId
    val incomeSources by IncomeSource referrersOn IncomeSources.budgetId
    val expenseSources by ExpenseSource referrersOn ExpenseSources.budgetId

    override fun toDto() = BudgetDto(this)
}