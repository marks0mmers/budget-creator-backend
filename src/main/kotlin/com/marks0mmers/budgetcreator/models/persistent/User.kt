package com.marks0mmers.budgetcreator.models.persistent

import com.marks0mmers.budgetcreator.models.constants.Role
import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.types.DtoConvertible
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
data class User(
        private var username: String,
        private var password: String,
        var firstName: String,
        var lastName: String,
        var enabled: Boolean,
        var roles: List<Role>
) : UserDetails, DtoConvertible<UserDto> {
    @Id var id: String? = null
    
    override fun isEnabled() = enabled
    override fun getUsername() = username
    override fun isCredentialsNonExpired() = false
    override fun getPassword() = password
    override fun isAccountNonExpired() = false
    override fun isAccountNonLocked() = false
    override fun getAuthorities() = roles.map { SimpleGrantedAuthority(it.name) }.toMutableList()
    override fun toDto() = UserDto(this)
}