package com.marks0mmers.budgetcreator.models.types

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface DtoConvertible<T> {
    fun toDto(): T
}

fun <T> Flow<DtoConvertible<T>>.toDtos() = map { it.toDto() }