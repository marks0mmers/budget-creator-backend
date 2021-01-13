package com.marks0mmers.budgetcreator.handlers

import com.marks0mmers.budgetcreator.models.dto.AuthRequest
import com.marks0mmers.budgetcreator.models.dto.CreateUserDto
import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.persistent.User
import com.marks0mmers.budgetcreator.services.UserService
import com.marks0mmers.budgetcreator.util.BudgetCreatorException
import com.marks0mmers.budgetcreator.util.JWTUtil
import com.marks0mmers.budgetcreator.util.fail
import com.marks0mmers.budgetcreator.util.handleException
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*

class UserHandler(
        private val userService: UserService,
        private val jwtUtil: JWTUtil
) {
    suspend fun login(req: ServerRequest) = try {
        val body = req.awaitBody<AuthRequest>()
        val user = userService.login(body.username, body.password)
        ok().json()
            .bodyValueAndAwait(addToken(user))
    } catch (e: Exception) { handleException(e) }

    suspend fun createUser(req: ServerRequest) = try {
        val body = req.awaitBody<CreateUserDto>()
        val createdUser = userService.createUser(body)
        ok().json()
            .bodyValueAndAwait(createdUser)
    } catch (e: Exception) { handleException(e) }

    suspend fun getCurrentUser(req: ServerRequest) = try {
        val principal = req.awaitPrincipal() ?: fail("Cannot get Principal")
        val user = userService.getUserByUsername(principal.name)
        ok().json()
            .bodyValueAndAwait(addToken(user))
    } catch (e: Exception) { handleException(e) }

    suspend fun getUserById(req: ServerRequest) = try {
        val user = userService.getUserById(req.pathVariable("userId"))
        ok()
            .bodyValueAndAwait(UserDto(user))
    } catch (e: Exception) { handleException(e) }

    private fun addToken(user: User): UserDto {
        val userDto = UserDto(user)
        userDto.token = jwtUtil.generateToken(user)
        return userDto
    }
}