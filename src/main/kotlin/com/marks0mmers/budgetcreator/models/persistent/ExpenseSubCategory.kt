package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.ExpenseSubCategoryDto
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import org.bson.types.ObjectId

data class ExpenseSubCategory(
    val name: String,
    val description: String
): DtoConvertible<ExpenseSubCategoryDto> {
    var id: String = ObjectId().toHexString()

    constructor(expenseCategorySubmission: ExpenseCategorySubmissionView) : this(
        expenseCategorySubmission.name,
        expenseCategorySubmission.description
    )

    constructor(expenseSubCategoryDto: ExpenseSubCategoryDto) : this(
        expenseSubCategoryDto.name,
        expenseSubCategoryDto.description
    ) {
        id = expenseSubCategoryDto.id
    }

    override fun toDto() = ExpenseSubCategoryDto(this)
}