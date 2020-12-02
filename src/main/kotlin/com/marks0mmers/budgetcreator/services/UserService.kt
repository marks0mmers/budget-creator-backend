package com.marks0mmers.budgetcreator.services

import com.marks0mmers.budgetcreator.config.PBKDF2Encoder
import com.marks0mmers.budgetcreator.models.dto.CreateUserDto
import com.marks0mmers.budgetcreator.models.persistent.Role
import com.marks0mmers.budgetcreator.models.persistent.User
import com.marks0mmers.budgetcreator.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository,
        private val passwordEncoder: PBKDF2Encoder
) {
    fun login(username: String, password: String) = getUserByUsername(username)
            .filter { it != null && passwordEncoder.matches(password, it.password) }

    fun getUserByUsername(username: String) = userRepository
            .findByUsername(username)

    fun createUser(user: CreateUserDto) = userRepository
            .save(User(
                    username = user.username,
                    password = passwordEncoder.encode(user.password),
                    firstName = user.firstName,
                    lastName = user.lastName,
                    enabled = true,
                    roles = listOf(Role.ROLE_USER)
            ))

    fun getUserById(userId: String) = userRepository
            .findById(userId)
}