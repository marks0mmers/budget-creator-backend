package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.BudgetDto
import com.marks0mmers.budgetcreator.models.dto.IncomeSourceDto
import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.util.fail
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Budget(
    val title: String,
    val primaryUserId: String,
    val incomeSources: List<IncomeSource>
): DtoConvertible<BudgetDto> {
    @Id var id: String? = null

    constructor(budget: BudgetSubmissionView, primaryUserId: String) : this(
        budget.title,
        primaryUserId,
        emptyList()
    )

    constructor(budget: BudgetDto) : this(
        budget.title,
        budget.primaryUserId,
        budget.incomeSources.map { IncomeSource(it) }
    ) {
        id = budget.id
    }

    fun addIncomeSource(incomeSource: IncomeSourceSubmissionView): Budget {
        return copy(
            incomeSources = listOf(*incomeSources.toTypedArray(), IncomeSource(incomeSource))
        )
    }

    fun updateIncomeSource(incomeSource: IncomeSourceDto): Budget {
        return copy(
            incomeSources = listOf(
                *incomeSources.filter { it.id != incomeSource.id }.toTypedArray(),
                incomeSources.find { it.id == incomeSource.id }
                    ?.let { IncomeSource(incomeSource) } ?: fail("Income Source not found")
            )
        )
    }

    fun removeIncomeSource(incomeSourceId: String): Budget {
        return copy(
            incomeSources = incomeSources.filter { it.id != incomeSourceId }
        )
    }

    override fun toDto() = BudgetDto(this)
}