package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.persistent.UserRole.UserRoles
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.springframework.security.core.userdetails.UserDetails

/**
 * Entity that represents the User
 *
 * @author Mark Sommers
 * @constructor
 * Internal constructor used by Expose
 *
 * @param id The Primary Key for the User
 */
class User(id: EntityID<Int>) : IntEntity(id), UserDetails, DtoConvertible<UserDto> {

    /** The object that represents the Database table users */
    object Users : IntIdTable("users") {
        /** The username of the user */
        val username = varchar("name", 50).index(isUnique = true)
        /** The password of the user */
        val password = varchar("password", 300)
        /** The first name of the user */
        val firstName = varchar("first_name", 50)
        /** The last name of the user */
        val lastName = varchar("last_name", 50)
        /** Whether the user is enabled */
        val enabled = bool("enabled")
    }

    /** The companion object that allows for the query DSL */
    companion object : IntEntityClass<User>(Users)

    /** The username of the user */
    var userName by Users.username
    /** The password of the user */
    var hashedPassword by Users.password
    /** The first name of the user */
    var firstName by Users.firstName
    /** The last name of the user */
    var lastName by Users.lastName
    /** Whether the user is enabled */
    var enabled by Users.enabled
    /** The roles of the user */
    val roles by UserRole referrersOn UserRoles.userId

    /**
     * Gets whether the user is enabled
     * @return Whether the user is enabled
     * @see UserDetails.isEnabled
     */
    override fun isEnabled() = enabled

    /**
     * Gets the user's username
     * @return The username
     * @see UserDetails.getUsername
     */
    override fun getUsername() = userName

    /**
     * Gets whether the credentials are expired
     * @return Whether the credentials are expired
     * @see UserDetails.isCredentialsNonExpired
     */
    override fun isCredentialsNonExpired() = false

    /**
     * Gets the user's password
     * @return The password
     * @see UserDetails.getPassword
     */
    override fun getPassword() = hashedPassword

    /**
     * Gets whether the account is expired or not
     * @return Whether the account is expired
     * @see UserDetails.isAccountNonExpired
     */
    override fun isAccountNonExpired() = false

    /**
     * Gets whether the account is locked or not
     * @return Whether the account is locked
     * @see UserDetails.isAccountNonLocked
     */
    override fun isAccountNonLocked() = false

    /**
     * Gets the authorities of the user
     * @return The authorities
     * @see UserDetails.getAuthorities
     */
    override fun getAuthorities() = roles.map { it.role }

    /**
     * Converts this entity to a DTO
     * @return The DTO
     * @see DtoConvertible.toDto
     */
    override fun toDto() = UserDto(this)
}