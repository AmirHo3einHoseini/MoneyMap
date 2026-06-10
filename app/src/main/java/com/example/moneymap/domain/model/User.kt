package com.example.moneymap.domain.model

data class User(
    val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val currency: String = "IRR",
    val createdAt: Long = System.currentTimeMillis()
)
