package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.ExpenseCategoryDto
import com.marks0mmers.budgetcreator.models.persistent.ExpenseSubCategory.ExpenseSubCategories
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class ExpenseCategory(id: EntityID<Int>) : IntEntity(id), DtoConvertible<ExpenseCategoryDto> {
    object ExpenseCategories : IntIdTable("expense_categories") {
        val name = varchar("name", 50)
        val description = varchar("description", 4000)
    }
    companion object : IntEntityClass<ExpenseCategory>(ExpenseCategories)

    var name by ExpenseCategories.name
    var description by ExpenseCategories.description
    val subCategories by ExpenseSubCategory referrersOn ExpenseSubCategories.expenseCategoryId

    override fun toDto() = ExpenseCategoryDto(this)
}