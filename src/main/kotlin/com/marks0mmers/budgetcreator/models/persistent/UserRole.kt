package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.constants.Role
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UserRoles : IntIdTable("user_roles") {
    val userId = reference("user_id", Users)
    val role = text("role")
}

class UserRole(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserRole>(UserRoles)

    var userId by UserRoles.userId
    var role by UserRoles.role.transform(
        toColumn = Role::name,
        toReal = { Role.valueOf(it) }
    )
}