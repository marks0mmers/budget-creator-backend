package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.constants.Role
import com.marks0mmers.budgetcreator.models.persistent.User
import com.marks0mmers.budgetcreator.models.persistent.User.Users
import com.marks0mmers.budgetcreator.models.persistent.UserRole
import com.marks0mmers.budgetcreator.models.views.CreateUserView
import com.marks0mmers.budgetcreator.util.fail
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder

object UserRepository {
    suspend fun findById(id: Int) = newSuspendedTransaction {
        User.findById(id)?.toDto()
    }

    suspend fun findByUsername(username: String) = newSuspendedTransaction {
        User.find { Users.username eq username }.firstOrNull()?.toDto()
    }

    suspend fun login(username: String, password: String, passwordEncoder: PasswordEncoder) = newSuspendedTransaction {
        val user = User.find { Users.username eq username }.firstOrNull()
        when {
            passwordEncoder.matches(password, user?.password) -> user?.toDto()
            else -> fail("Username doesn't exist or password is incorrect", HttpStatus.UNAUTHORIZED)
        }
    }

    suspend fun createUser(user: CreateUserView) = newSuspendedTransaction {
        User.new {
            userName = user.username
            hashedPassword = user.password
            firstName = user.firstName
            lastName = user.lastName
            enabled = true
        }.also { saved ->
            UserRole.new {
                userId = saved.id
                role = Role.ROLE_USER
            }
        }.toDto()

    }
}