package me.ronan.infra.config

import jakarta.enterprise.context.ApplicationScoped
import me.ronan.modules.member.PasswordDto
import java.security.SecureRandom
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@ApplicationScoped
class PasswordEncoder {
    companion object {
        private const val SALT_BYTE = 16
        private const val ITERATIONS = 10_000
        private const val KEY_LENGTH = 256
        private const val ALGORITHM = "PBKDF2WithHmacSHA1"
    }

    fun encode(password: String): PasswordDto {
        //salt
        val salt = ByteArray(SALT_BYTE)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(salt)
        val encodedSalt = Base64.getEncoder().encodeToString(salt)

        //password hashing
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        val skf = SecretKeyFactory.getInstance(ALGORITHM)
        val hash = skf.generateSecret(spec).encoded
        val encodedHash = Base64.getEncoder().encodeToString(hash)

        return PasswordDto(
            password = encodedHash,
            passwordSalt = encodedSalt,
        )
    }

    fun matches(password: String, storedPasswordDto: PasswordDto): Boolean {
        val salt = Base64.getDecoder().decode(storedPasswordDto.passwordSalt)
        return verify(password, salt, storedPasswordDto.password)
    }

    private fun verify(password: String, salt: ByteArray, storedHash: String): Boolean {
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(ALGORITHM)
        val hash = factory.generateSecret(spec).encoded
        val encodedHash = Base64.getEncoder().encodeToString(hash)
        return encodedHash == storedHash
    }
}