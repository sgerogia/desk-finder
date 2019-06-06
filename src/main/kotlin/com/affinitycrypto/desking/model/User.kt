package com.affinitycrypto.desking.model

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

enum class AuthProvider {
    google,
    github
}

@Entity
@Table(
        name = "users",
        uniqueConstraints = [
            UniqueConstraint(columnNames = ["email"])
        ]
)
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(nullable = false)
        val name: String,

        @Email
        @Column(nullable = false)
        val email: String,

        val imageUrl: String,

        @NotNull
        @Enumerated(EnumType.STRING)
        val authProvider: AuthProvider
)