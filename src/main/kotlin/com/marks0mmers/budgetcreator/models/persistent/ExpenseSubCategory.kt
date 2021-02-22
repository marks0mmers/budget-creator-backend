package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.ExpenseSubCategoryDto
import com.marks0mmers.budgetcreator.models.views.ExpenseCategorySubmissionView
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ExpenseSubCategory(
    val name: String,
    val description: String
): DtoConvertible<ExpenseSubCategoryDto> {
    @Id var id: String? = null

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