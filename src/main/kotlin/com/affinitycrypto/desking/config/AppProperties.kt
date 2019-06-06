package com.affinitycrypto.desking.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppProperties(
        val auth: Auth,
        val oauth2: OAuth2
) {

    data class Auth(
        val tokenSecret: String,
        val tokenExpirationMillis: Long
    )

    data class OAuth2(
        val authorizedRedirectUris: List<String>
    )
}