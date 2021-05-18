package com.marks0mmers.budgetcreator.models.constants

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;

    override fun getAuthority() = name
}
