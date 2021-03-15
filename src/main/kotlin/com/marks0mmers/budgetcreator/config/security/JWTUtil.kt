package com.marks0mmers.budgetcreator.config.security

import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.persistent.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.boot.context.properties.ConfigurationProperties
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap
import java.time.Instant

@ConfigurationProperties("json-web-token.jjwt")
class JWTUtil : Serializable {
    var secret: String = ""
    var expiration: Long = 0

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

    fun generateToken(user: User): String {
        val claims = HashMap<String, Any>()
        claims["role"] = user.roles
        return doGenerateToken(claims, user.username)
    }

    fun generateToken(user: UserDto): String {
        val claims = HashMap<String, Any>()
        claims["role"] = user.roles
        return doGenerateToken(claims, user.username)
    }

    fun validateToken(token: String): Boolean {
        return !isTokenExpired(token)
    }

    private fun doGenerateToken(claims: Map<String, Any>, username: String): String {
        val createdDate = Instant.now()
        val expirationDate = createdDate.plusMillis(expiration * 1000)
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
        return getExpirationDateFromToken(token)?.isBefore(Instant.now()) ?: true
    }
}