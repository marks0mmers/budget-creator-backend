package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.ExpenseCategoryDto
import com.marks0mmers.budgetcreator.models.persistent.ExpenseSubCategory.ExpenseSubCategories
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Entity that represents the Expense Category
 *
 * @author Mark Sommers
 * @constructor
 * Internal constructor used by Exposed
 *
 * @param id The primary key for the Expense Category
 */
class ExpenseCategory(id: EntityID<Int>) : IntEntity(id), DtoConvertible<ExpenseCategoryDto> {

    /** The object that represents the Database table expense_categories */
    object ExpenseCategories : IntIdTable("expense_categories") {
        /** The name of the expense category */
        val name = varchar("name", 50)
        /** The description of the expense category */
        val description = varchar("description", 4000)
    }

    /** The companion object that allows for the query DSL */
    companion object : IntEntityClass<ExpenseCategory>(ExpenseCategories)

    /** The name of the expense category */
    var name by ExpenseCategories.name
    /** The description of the expense category */
    var description by ExpenseCategories.description
    /** The list of sub-categories that belong to this category */
    val subCategories by ExpenseSubCategory referrersOn ExpenseSubCategories.expenseCategoryId

    /**
     * Converts this Entity to a DTO
     *
     * @return The DTO
     * @see DtoConvertible.toDto
     */
    override fun toDto() = ExpenseCategoryDto(this)
}