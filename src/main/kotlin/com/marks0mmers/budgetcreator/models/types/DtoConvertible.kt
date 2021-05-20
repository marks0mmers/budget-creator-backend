package com.marks0mmers.budgetcreator.models.types

/**
 * An interface allowing for an entity to be convertible to a DTO
 *
 * @param T The DTO to convert to
 */
interface DtoConvertible<T> {

    /**
     * The function that allows conversion to a DTO
     *
     * @return The converted DTO object
     */
    fun toDto(): T
}