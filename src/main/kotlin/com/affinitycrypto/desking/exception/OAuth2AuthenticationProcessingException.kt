package com.affinitycrypto.desking.exception

import org.springframework.security.core.AuthenticationException

class OAuth2AuthenticationProcessingException(
        msg: String,
        t: Throwable? = null
): AuthenticationException(msg, t)