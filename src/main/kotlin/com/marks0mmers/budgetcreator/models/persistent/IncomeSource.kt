package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.IncomeSourceDto
import com.marks0mmers.budgetcreator.models.persistent.Budget.Budgets
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Entity that represents the income sources
 *
 * @author Mark Sommers
 * @constructor
 * Internal constructor used by Exposed
 *
 * @param id The primary key for the IncomeSource
 */
class IncomeSource(id: EntityID<Int>) : IntEntity(id), DtoConvertible<IncomeSourceDto> {

    /** The object that represents the Database table income_sources */
    object IncomeSources : IntIdTable("income_sources") {
        /** The name of the income source */
        val name = varchar("name", 50)
        /** The amount of the income source */
        val amount = double("amount")
        /** The foreign key to the budget table */
        val budgetId = reference("budget_id", Budgets)
    }

    /** The companion object that allows for the query DSL */
    companion object : IntEntityClass<IncomeSource>(IncomeSources)

    /** The name of the income source */
    var name by IncomeSources.name
    /** The amount of the income source */
    var amount by IncomeSources.amount
    /** The budget object that this income source is linked to */
    var budget by Budget referencedOn IncomeSources.budgetId

    /**
     * Converts this Entity to a DTO
     *
     * @return The DTO
     * @see DtoConvertible.toDto
     */
    override fun toDto() = IncomeSourceDto(this)
}