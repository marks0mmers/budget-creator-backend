package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.ExpenseSubCategoryDto
import com.marks0mmers.budgetcreator.models.persistent.ExpenseCategory.ExpenseCategories
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class ExpenseSubCategory(id: EntityID<Int>) : IntEntity(id), DtoConvertible<ExpenseSubCategoryDto> {
    object ExpenseSubCategories : IntIdTable("expense_sub_categories") {
        val name = varchar("name", 50)
        val description = varchar("description", 4000)
        val expenseCategoryId = reference("expense_category_id", ExpenseCategories)
    }
    companion object : IntEntityClass<ExpenseSubCategory>(ExpenseSubCategories)

    var name by ExpenseSubCategories.name
    var description by ExpenseSubCategories.description
    var expenseCategory by ExpenseCategory referencedOn ExpenseSubCategories.expenseCategoryId

    override fun toDto() = ExpenseSubCategoryDto(this)
}