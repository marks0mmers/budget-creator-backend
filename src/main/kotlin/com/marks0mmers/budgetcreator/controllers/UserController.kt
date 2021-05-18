package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.config.security.JWTUtil
import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.views.AuthRequestView
import com.marks0mmers.budgetcreator.models.views.CreateUserView
import com.marks0mmers.budgetcreator.services.UserService
import com.marks0mmers.budgetcreator.util.POST
import com.marks0mmers.budgetcreator.util.fail
import org.springframework.web.reactive.function.server.*

class UserController(
    userService: UserService,
    jwtUtil: JWTUtil
) : RouterFunction<ServerResponse> by coRouter({
    fun addToken(user: UserDto): UserDto {
        user.token = jwtUtil.generateToken(user.username, user.roles)
        return user
    }

    "/api/users".nest {
        this.
        GET("/current") { req ->
            val principal = req.awaitPrincipal() ?: fail("Cannot get Principal")
            val user = userService.getUserByUsername(principal.name)
            ok().json()
                .bodyValueAndAwait(addToken(user))
        }

        POST { req ->
            val body = req.awaitBody<CreateUserView>()
            val createdUser = userService.createUser(body)
            ok().json()
                .bodyValueAndAwait(createdUser)
        }

        POST("/login") { req ->
            val body = req.awaitBody<AuthRequestView>()
            val user = userService.login(body.username, body.password)
            ok().json()
                .bodyValueAndAwait(addToken(user))
        }

        GET("/{userId}") { req ->
            val userId = req.pathVariable("userId").toInt()
            val user = userService.getUserById(userId)
            ok().json()
                .bodyValueAndAwait(user)
        }
    }
})