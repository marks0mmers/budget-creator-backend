package com.marks0mmers.budgetcreator.config.security

import com.marks0mmers.budgetcreator.models.constants.Role
import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.persistent.User
import com.marks0mmers.budgetcreator.util.PropertyValue
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*
import kotlin.collections.HashMap
import java.time.Instant

class JWTUtil {
    private val secret: String by PropertyValue("json-web-token.jjwt") { it ?: "" }
    private val expiration: Long by PropertyValue("json-web-token.jjwt") { it?.toLong() ?: 0 }

    fun getAllClaimsFromToken(token: String): Claims? {
        val encodedString = Base64.getEncoder().encodeToString(secret.toByteArray())
        return try {
            Jwts
                .parser()
                .setSigningKey(encodedString)
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            null
        }
    }

    fun getUsernameFromToken(token: String): String? {
        return getAllClaimsFromToken(token)?.subject
    }

    fun validateToken(token: String): Boolean {
        return !isTokenExpired(token)
    }

    fun generateToken(user: User) = doGenerateToken(user.roles, user.username)

    fun generateToken(user: UserDto) = doGenerateToken(user.roles, user.username)

    private fun doGenerateToken(roles: List<Role>, username: String): String {
        val createdDate = Instant.now()
        val expirationDate = createdDate.plusMillis(expiration * 1000)
        val claims = mutableMapOf<String, Any>("role" to roles)
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(Date.from(createdDate))
            .setExpiration(Date.from(expirationDate))
            .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.toByteArray()))
            .compact()
    }

    private fun getExpirationDateFromToken(token: String): Instant? {
        return getAllClaimsFromToken(token)?.expiration?.toInstant()
    }

    private fun isTokenExpired(token: String): Boolean {
        val expirationDate = getExpirationDateFromToken(token) ?: return true
        return expirationDate < Instant.now()
    }
}