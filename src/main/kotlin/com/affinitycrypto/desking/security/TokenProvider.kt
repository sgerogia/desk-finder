package com.affinitycrypto.desking.security

import com.affinitycrypto.desking.config.AppProperties
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.security.SignatureException
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class TokenProvider(
        val appProperties: AppProperties
) {

    private val logger = LoggerFactory.getLogger(TokenProvider::class.java)

    fun createToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserPrincipal

        val now = ZonedDateTime.now()
        val expiryDate = now.plus(appProperties.auth.tokenExpirationMillis, ChronoUnit.MILLIS)

        return Jwts.builder()
                .setSubject(userPrincipal.id)
                .setIssuedAt(Date())
                .setExpiration(Date(expiryDate.toInstant().toEpochMilli()))
                .signWith(SignatureAlgorithm.HS512, appProperties.auth.tokenSecret)
                .compact()
    }

    fun getUserIdFromToken(token: String): Long? {
        val claims = Jwts.parser()
                .setSigningKey(appProperties.auth.tokenSecret)
                .parseClaimsJws(token)
                .getBody()

        return java.lang.Long.parseLong(claims.getSubject())
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(appProperties.auth.tokenSecret).parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }

        return false
    }
}