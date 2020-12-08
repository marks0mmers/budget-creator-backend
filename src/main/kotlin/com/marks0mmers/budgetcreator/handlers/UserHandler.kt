package com.marks0mmers.budgetcreator.handlers

import com.marks0mmers.budgetcreator.models.dto.AuthRequest
import com.marks0mmers.budgetcreator.models.dto.CreateUserDto
import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.persistent.User
import com.marks0mmers.budgetcreator.services.UserService
import com.marks0mmers.budgetcreator.util.JWTUtil
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*

class UserHandler(
        private val userService: UserService,
        private val jwtUtil: JWTUtil
) {
    fun login(req: ServerRequest) = req
            .bodyToMono<AuthRequest>()
            .map { userService
                    .login(it.username, it.password)
                    .map(this::addToken) }
            .flatMap { ok().body(it) }
            .switchIfEmpty( status(HttpStatus.UNAUTHORIZED).build() )

    fun createUser(req: ServerRequest) = req
            .bodyToMono<CreateUserDto>()
            .map { userService.createUser(it) }
            .flatMap { ok().body(it) }

    fun getCurrentUser(req: ServerRequest) = req
            .principal()
            .map { userService
                    .getUserByUsername(it.name)
                    .map(this::addToken)}
            .flatMap { ok().body(it) }
            .switchIfEmpty(notFound().build())

    fun getUserById(req: ServerRequest) = ok()
            .body(userService.getUserById(req.pathVariable("userId")).map(::UserDto))
            .switchIfEmpty(notFound().build())

    private fun addToken(user: User): UserDto {
        val userDto = UserDto(user)
        userDto.token = jwtUtil.generateToken(user)
        return userDto
    }
}