package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.persistent.User
import com.marks0mmers.budgetcreator.models.views.CreateUserView
import com.marks0mmers.budgetcreator.repositories.UserRepository
import com.marks0mmers.budgetcreator.util.fail
import com.marks0mmers.budgetcreator.util.BudgetCreatorException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * The Service layer responsible for handling [User] functionality
 *
 * @property userRepository The user MongoDB Repository
 * @property passwordEncoder The encoder used to encode passwords
 * @author Mark Sommers
 */
class UserService(private val passwordEncoder: PasswordEncoder) {

    private val userRepository = UserRepository

    /**
     * Log into the application
     *
     * @param username The username to use to login
     * @param password The plaintext password to login
     * @return The user that matches the username/password
     * @throws BudgetCreatorException if the user cannot login
     */
    suspend fun login(username: String, password: String): UserDto {
        return userRepository
            .login(username, password, passwordEncoder)
            ?: fail("Cannot find user with username: $username")
    }

    /**
     * Get user by their username
     *
     * @param username The username to search by
     * @return The user that matches the username
     * @throws BudgetCreatorException if the user doesn't exist
     */
    suspend fun getUserByUsername(username: String): UserDto {
        return userRepository
            .findByUsername(username)
            ?: fail("Cannot find user $username", HttpStatus.NOT_FOUND)
    }

    /**
     * Creates a new User
     *
     * @param user The user to create
     * @return The created user
     * @throws BudgetCreatorException If the user doesn't save
     */
    suspend fun createUser(user: CreateUserView): UserDto {
        return userRepository
            .createUser(user.copy(password = passwordEncoder.encode(user.password)))
    }

    /**
     * Get a user by their ID
     *
     * @param userId The user ID to search by
     * @return The user that matches the ID
     * @throws BudgetCreatorException If the user cannot be found
     */
    suspend fun getUserById(userId: Int): UserDto {
        return userRepository
            .findById(userId)
            ?: fail("User with id: $userId doesn't exist", HttpStatus.NOT_FOUND)
    }
}