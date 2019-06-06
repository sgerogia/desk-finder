package com.affinitycrypto.desking.controller

import com.affinitycrypto.desking.exception.ResourceNotFoundException
import com.affinitycrypto.desking.model.User
import org.springframework.web.bind.annotation.RestController
import com.affinitycrypto.desking.security.CurrentUser
import org.springframework.security.access.prepost.PreAuthorize
import com.affinitycrypto.desking.repository.UserRepository
import com.affinitycrypto.desking.security.UserPrincipal
import org.springframework.web.bind.annotation.GetMapping


@RestController
class UserController(
    private val userRepository: UserRepository
) {

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    fun getCurrentUser(@CurrentUser userPrincipal: UserPrincipal): User {
        return userRepository!!.findById(userPrincipal.id.toLong())
                .orElseThrow<RuntimeException> { ResourceNotFoundException("User", "id", userPrincipal.id) }
    }
}