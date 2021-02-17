package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.config.PasswordEncoder
import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.persistent.Role
import com.marks0mmers.budgetcreator.models.persistent.User
import com.marks0mmers.budgetcreator.models.views.CreateUserView
import com.marks0mmers.budgetcreator.repositories.UserRepository
import com.marks0mmers.budgetcreator.util.fail
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var passwordEncoder: PasswordEncoder

    suspend fun login(username: String, password: String): User {
        val user = userRepository
            .findByUsername(username)
            .awaitFirstOrElse { fail("Cannot find user with username: $username") }
        return if (passwordEncoder.matches(password, user.password)) user else fail(
            "Passwords don't match",
            HttpStatus.UNAUTHORIZED
        )
    }

    suspend fun getUserByUsername(username: String): UserDto {
        return userRepository
            .findByUsername(username)
            .awaitFirstOrElse { fail("Cannot find user $username", HttpStatus.NOT_FOUND) }
            .let { UserDto(it) }
    }

    suspend fun createUser(user: CreateUserView): UserDto {
        return userRepository
            .save(
                User(
                    username = user.username,
                    password = passwordEncoder.encode(user.password),
                    firstName = user.firstName,
                    lastName = user.lastName,
                    enabled = true,
                    roles = listOf(Role.ROLE_USER)
                )
            )
            .awaitFirstOrElse { fail("Failed to save user") }
            .let { UserDto(it) }
    }

    suspend fun getUserById(userId: String): UserDto {
        return userRepository
            .findById(userId)
            .awaitFirstOrElse { fail("User with id: $userId doesn't exist", HttpStatus.NOT_FOUND) }
            .let { UserDto(it) }
    }
}