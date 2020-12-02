package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.dto.AuthRequest
import com.marks0mmers.budgetcreator.models.dto.CreateUserDto
import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.persistent.User
import com.marks0mmers.budgetcreator.services.UserService
import com.marks0mmers.budgetcreator.util.JWTUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(
        val userService: UserService,
        val jwtUtil: JWTUtil
) {
    @PostMapping("/login")
    fun login(@RequestBody ar: AuthRequest) = userService
            .login(ar.username, ar.password)
            .map { ResponseEntity.ok(addToken(it)) }
            .defaultIfEmpty( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() )

    @PostMapping
    fun createUser(@RequestBody user: CreateUserDto) = userService
            .createUser(user)
            .map(::UserDto)

    @GetMapping("/me")
    fun getCurrentUser(principal: Principal) = userService
            .getUserByUsername(principal.name)
            .map(this::addToken)

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    fun getUserById(@PathVariable userId: String) = userService
            .getUserById(userId)
            .map(::UserDto)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    private fun addToken(user: User): UserDto {
        val userDto = UserDto(user)
        userDto.token = jwtUtil.generateToken(user)
        return userDto
    }
}