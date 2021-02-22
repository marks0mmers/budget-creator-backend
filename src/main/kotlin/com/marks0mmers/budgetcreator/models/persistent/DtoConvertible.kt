package com.marks0mmers.budgetcreator.models.persistent

interface DtoConvertible<T> {
    fun toDto(): T
}