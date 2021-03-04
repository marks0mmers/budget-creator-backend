package com.marks0mmers.budgetcreator.models.types

interface DtoConvertible<T> {
    fun toDto(): T
}