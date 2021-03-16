package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.IncomeSourceDto
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import org.bson.types.ObjectId

data class IncomeSource(
    val name: String,
    val amount: Double
): DtoConvertible<IncomeSourceDto> {
    var id: String = ObjectId().toHexString()

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