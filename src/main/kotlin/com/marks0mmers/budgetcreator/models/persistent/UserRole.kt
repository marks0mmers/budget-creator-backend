package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.constants.Role
import com.marks0mmers.budgetcreator.models.persistent.User.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class UserRole(id: EntityID<Int>) : IntEntity(id) {
    object UserRoles : IntIdTable("user_roles") {
        val userId = reference("user_id", Users)
        val role = text("role")
    }
    companion object : IntEntityClass<UserRole>(UserRoles)

    var userId by UserRoles.userId
    var role by UserRoles.role.transform(
        toColumn = Role::name,
        toReal = { Role.valueOf(it) }
    )
}