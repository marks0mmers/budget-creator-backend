package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.persistent.User.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Entity that represents the one-to-many relation from user to roles
 *
 * @author Mark Sommers
 * @constructor
 * Internal constructor used by Expose
 *
 * @param id The Primary Key for the User Role
 */
class UserRole(id: EntityID<Int>) : IntEntity(id) {

    /** The object that represents the Database table user_roles */
    object UserRoles : IntIdTable("user_roles") {
        /** The foreign key to the users table */
        val userId = reference("user_id", Users)
        /** The role that this object contains */
        val role = text("role")
    }

    /** The companion object that allows for the query DSL */
    companion object : IntEntityClass<UserRole>(UserRoles)

    /** The user that the foreign key refers to */
    var user by User referencedOn UserRoles.userId
    /** The role that the table contains. Transforms between a string and [Role] */
    var role by UserRoles.role.transform(
        toColumn = Role::name,
        toReal = { Role.valueOf(it) }
    )
}