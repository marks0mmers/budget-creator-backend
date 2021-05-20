package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.ExpenseSubCategoryDto
import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory.ExpenseCategories
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Entity that represents the expense sub-categories
 *
 * @author Mark Sommers
 * @constructor
 * Internal constructor used by Exposed
 *
 * @param id The primary key for the ExpenseSubCategory
 */
class ExpenseSubCategory(id: EntityID<Int>) : IntEntity(id), DtoConvertible<ExpenseSubCategoryDto> {

    /** The object that represents the Database table expense_sub_categories */
    object ExpenseSubCategories : IntIdTable("expense_sub_categories") {
        /** The name of the expense sub-category */
        val name = varchar("name", 50)
        /** The description of the expense sub-category */
        val description = varchar("description", 4000)
        /** The category that this sub-category is attached to */
        val expenseCategoryId = reference("expense_category_id", ExpenseCategories)
    }

    /** The companion object that allows for the query DSL */
    companion object : IntEntityClass<ExpenseSubCategory>(ExpenseSubCategories)

    /** The name of the expense sub-category */
    var name by ExpenseSubCategories.name
    /** The description of the expense sub-category */
    var description by ExpenseSubCategories.description
    /** The category that this sub-category is attached to */
    var expenseCategory by ExpenseCategory referencedOn ExpenseSubCategories.expenseCategoryId

    /**
     * Converts this Entity to a DTO
     *
     * @return The DTO
     * @see DtoConvertible.toDto
     */
    override fun toDto() = ExpenseSubCategoryDto(this)
}