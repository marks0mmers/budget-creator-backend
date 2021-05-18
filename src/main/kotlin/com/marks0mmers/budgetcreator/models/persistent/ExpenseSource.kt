package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.ExpenseSourceDto
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ExpenseSources : IntIdTable("expense_sources") {
    val name = varchar("name", 100)
    val amount = double("amount")
    val budgetId = reference("budget", Budgets)
    val categoryId = reference("expense_category", ExpenseCategories)
    val subCategoryId = reference("expense_sub_category", ExpenseSubCategories).nullable()
}

class ExpenseSource(id: EntityID<Int>) : IntEntity(id), DtoConvertible<ExpenseSourceDto> {
    companion object : IntEntityClass<ExpenseSource>(ExpenseSources)

    var name by ExpenseSources.name
    var amount by ExpenseSources.amount
    var budget by Budget referencedOn ExpenseSources.budgetId
    var category by ExpenseCategory referencedOn ExpenseSources.categoryId
    var subCategory by ExpenseSubCategory optionalReferencedOn ExpenseSources.subCategoryId

    override fun toDto(): ExpenseSourceDto = ExpenseSourceDto(this)
}