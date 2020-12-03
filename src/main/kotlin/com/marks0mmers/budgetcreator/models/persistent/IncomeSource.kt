package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class IncomeSource(
        @Id val id: String? = null,
        val name: String,
        val amount: Double
) {
    constructor(incomeSourceDto: IncomeSourceSubmissionView): this(
            null,
            incomeSourceDto.name,
            incomeSourceDto.amount
    )
}