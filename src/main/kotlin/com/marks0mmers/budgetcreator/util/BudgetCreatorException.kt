package com.marks0mmers.budgetcreator.util

class BudgetCreatorException(message: String?) : Exception(message)

fun fail(message: String?): Nothing {
    throw BudgetCreatorException(message)
}