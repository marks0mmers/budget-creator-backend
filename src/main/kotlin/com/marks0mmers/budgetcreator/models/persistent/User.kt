package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.persistent.UserRole.UserRoles
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.springframework.security.core.userdetails.UserDetails

class User(id: EntityID<Int>) : IntEntity(id), UserDetails, DtoConvertible<UserDto> {
    object Users : IntIdTable("users") {
        val username = varchar("name", 50).index(isUnique = true)
        val password = varchar("password", 300)
        val firstName = varchar("first_name", 50)
        val lastName = varchar("last_name", 50)
        val enabled = bool("enabled")
    }
    companion object : IntEntityClass<User>(Users)

    var userName by Users.username
    var hashedPassword by Users.password
    var firstName by Users.firstName
    var lastName by Users.lastName
    var enabled by Users.enabled
    val roles by UserRole referrersOn UserRoles.userId

    override fun isEnabled() = enabled
    override fun getUsername() = userName
    override fun isCredentialsNonExpired() = false
    override fun getPassword() = hashedPassword
    override fun isAccountNonExpired() = false
    override fun isAccountNonLocked() = false
    override fun getAuthorities() = roles.map { it.role }
    override fun toDto() = UserDto(this)
}