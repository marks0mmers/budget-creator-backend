package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.IncomeSourceDto
import com.marks0mmers.budgetcreator.models.persistent.Budget.Budgets
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class IncomeSource(id: EntityID<Int>) : IntEntity(id), DtoConvertible<IncomeSourceDto> {
    object IncomeSources : IntIdTable("income_sources") {
        val name = varchar("name", 50)
        val amount = double("amount")
        val budgetId = reference("budget_id", Budgets)
    }
    companion object : IntEntityClass<IncomeSource>(IncomeSources)

    var name by IncomeSources.name
    var amount by IncomeSources.amount
    var budget by Budget referencedOn IncomeSources.budgetId

    override fun toDto(): IncomeSourceDto = IncomeSourceDto(this)
}