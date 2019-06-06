package com.affinitycrypto.desking.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(
        message: String,
        cause: Throwable? = null
): RuntimeException(message, cause)