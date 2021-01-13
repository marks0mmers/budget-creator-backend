package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.config.PBKDF2Encoder
import com.marks0mmers.budgetcreator.models.dto.CreateUserDto
import com.marks0mmers.budgetcreator.models.persistent.Role
import com.marks0mmers.budgetcreator.models.persistent.User
import com.marks0mmers.budgetcreator.repositories.UserRepository
import com.marks0mmers.budgetcreator.util.fail
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono

class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PBKDF2Encoder
) {
    suspend fun login(username: String, password: String): User {
        val user = getUserByUsername(username)
        return if (passwordEncoder.matches(password, user.password)) {
            user
        } else {
            fail("Passwords don't match", HttpStatus.UNAUTHORIZED)
        }
    }

    suspend fun getUserByUsername(username: String): User {
        return userRepository
            .findByUsername(username)
            .awaitFirstOrElse { fail("Cannot find user $username", HttpStatus.NOT_FOUND) }
    }

    suspend fun createUser(user: CreateUserDto): User {
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
    }

    suspend fun getUserById(userId: String): User {
        return userRepository
            .findById(userId)
            .awaitFirstOrElse { fail("User with id: $userId doesn't exist", HttpStatus.NOT_FOUND) }
    }
}