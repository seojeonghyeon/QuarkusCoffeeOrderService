package me.ronan.infra.auth

import io.smallrye.jwt.algorithm.SignatureAlgorithm
import io.smallrye.jwt.build.Jwt
import io.smallrye.jwt.util.KeyUtils
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import me.ronan.modules.member.Member
import mu.KotlinLogging
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.Claims
import java.util.*


@ApplicationScoped
class JwtTokenProvider{
    companion object {
        private val log = KotlinLogging.logger { }
    }

    @Inject
    @ConfigProperty(name = "quarkus.security.jwt.token_expiration_time", defaultValue = "864000")
    private lateinit var tokenExpirationTime: String

    @Inject
    @ConfigProperty(name = "quarkus.security.jwt.privateKey")
    private lateinit var privateKeyLocation: String

    fun createToken(member: Member): String {
        val currentTime = Date(System.currentTimeMillis()).time
        val expirationTime = currentTime + Date(tokenExpirationTime.toLong()).time

        val authorities = HashSet<String>()
        member.getAuthorities().forEach { authorities.add(it.authorityName) }

        val privateKey = KeyUtils.readPrivateKey(privateKeyLocation, SignatureAlgorithm.RS256)

        val claimsBuilder = Jwt.claims()
        claimsBuilder.issuedAt(currentTime)
        claimsBuilder.expiresAt(expirationTime)
        claimsBuilder.groups(authorities)
        claimsBuilder.claim(Claims.upn,member.memberId)
        claimsBuilder.issuer("http://ronan.seo")

        log.info("[${member.memberId}][${member.memberName}] Completed creating token")

        return claimsBuilder
            .jws()
            .sign(privateKey)
    }
}