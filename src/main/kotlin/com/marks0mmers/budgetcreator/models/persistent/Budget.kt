package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.views.BudgetSubmissionView
import com.marks0mmers.budgetcreator.models.views.IncomeSourceSubmissionView
import com.marks0mmers.budgetcreator.util.fail
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Budget(
        @Id val id: String? = null,
        val title: String,
        val primaryUserId: String,
        val incomeSources: List<IncomeSource>
) {
        constructor(budget: BudgetSubmissionView, primaryUserId: String): this(
                null,
                budget.title,
                primaryUserId,
                listOf()
        )

        fun addIncomeSource(incomeSource: IncomeSourceSubmissionView) = Budget(
                id, title, primaryUserId,
                listOf(*incomeSources.toTypedArray(), IncomeSource(incomeSource))
        )

        fun updateIncomeSource(incomeSourceId: String, incomeSource: IncomeSourceSubmissionView) = Budget(
                id, title, primaryUserId,
                listOf(
                        *incomeSources.filter { it.id != incomeSourceId }.toTypedArray(),
                        incomeSources.find { it.id == incomeSourceId }?.let { IncomeSource(it.id, incomeSource.name, incomeSource.amount) } ?: fail("Income Source not found")
                )
        )

        fun removeIncomeSource(incomeSourceId: String) = Budget(
                id, title, primaryUserId,
                incomeSources.filter { it.id != incomeSourceId }
        )
}