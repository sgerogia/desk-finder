package com.affinitycrypto.desking.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
data class ResourceNotFoundException(
        val resourceName: String,
        val fieldName: String,
        val fieldValue: Any
): RuntimeException("%s not found with %s : '%s'".format(resourceName, fieldName, fieldValue))