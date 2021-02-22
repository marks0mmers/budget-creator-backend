package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.IncomeSourceDto
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class IncomeSource(
    val name: String,
    val amount: Double
): DtoConvertible<IncomeSourceDto> {
    @Id var id: String? = null

    constructor(incomeSourceSubmission: IncomeSourceSubmissionView) : this(
        incomeSourceSubmission.name,
        incomeSourceSubmission.amount
    )

    constructor(incomeSourceDto: IncomeSourceDto) : this(
        incomeSourceDto.name,
        incomeSourceDto.amount
    ) {
        id = incomeSourceDto.id
    }

    override fun toDto(): IncomeSourceDto = IncomeSourceDto(this)
}