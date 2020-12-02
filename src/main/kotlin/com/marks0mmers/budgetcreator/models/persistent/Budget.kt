package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Budget(
        @Id val id: String? = null,
        val title: String,
        val primaryUserId: String
) {
        constructor(budget: BudgetDto, primaryUserId: String): this(
                budget.id,
                budget.title,
                primaryUserId
        )
}