package com.marks0mmers.budgetcreator.config.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@Component
@ConfigurationProperties(prefix = "json-web-token.password.encoder")
class PasswordEncoder : PasswordEncoder {
    private var secret: String = ""
    private var iteration: Int = 0
    private var keylength: Int = 0

    override fun matches(cs: CharSequence?, pw: String?): Boolean = encode(cs) == pw

    override fun encode(cs: CharSequence?): String {
        val result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
            .generateSecret(
                PBEKeySpec(
                    cs.toString().toCharArray(),
                    secret.toByteArray(),
                    iteration,
                    keylength
                )
            )
            .encoded
        return Base64.getEncoder().encodeToString(result)
    }

}