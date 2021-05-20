package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.ExpenseSourceDto
import com.marks0mmers.budgetcreator.models.persistent.Budget.Budgets
import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory.ExpenseCategories
import com.marks0mmers.budgetcreator.models.persistent.ExpenseSubCategory.ExpenseSubCategories
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Entity that represents the expense source
 *
 * @author Mark Sommers
 * @constructor
 * Internal constructor used by Exposed
 *
 * @param id The primary key for the Expense Source
 */
class ExpenseSource(id: EntityID<Int>) : IntEntity(id), DtoConvertible<ExpenseSourceDto> {

    /** The object that represents the Database table expense_sources */
    object ExpenseSources : IntIdTable("expense_sources") {
        /** The name of the expense source */
        val name = varchar("name", 100)
        /** The amount of the expense source */
        val amount = double("amount")
        /** The budget this expense source is attached to */
        val budgetId = reference("budget", Budgets)
        /** The expense category of this expense source */
        val categoryId = reference("expense_category", ExpenseCategories)
        /** The expense sub-category of this expense source */
        val subCategoryId = reference("expense_sub_category", ExpenseSubCategories).nullable()
    }

    /** The companion object that allows for the query DSL */
    companion object : IntEntityClass<ExpenseSource>(ExpenseSources)

    /** The name of the expense source */
    var name by ExpenseSources.name
    /** The amount of the expense source */
    var amount by ExpenseSources.amount
    /** The budget this expense source is attached to */
    var budget by Budget referencedOn ExpenseSources.budgetId
    /** The expense category of this expense source */
    var category by ExpenseCategory referencedOn ExpenseSources.categoryId
    /** The expense sub-category of this expense source */
    var subCategory by ExpenseSubCategory optionalReferencedOn ExpenseSources.subCategoryId

    /**
     * Converts this Entity to a DTO
     *
     * @return The DTO
     * @see DtoConvertible.toDto
     */
    override fun toDto(): ExpenseSourceDto = ExpenseSourceDto(this)
}