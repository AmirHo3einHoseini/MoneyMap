package com.example.moneymap.data.local.entity

import android.icu.util.Currency
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneymap.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val currency: String = "IRR",
    val createdAt: Long = System.currentTimeMillis()
)

fun UserEntity.toDomain() = User(
    id = id,
    name = name,
    email = email,
    passwordHash = passwordHash,
    currency = currency,
    createdAt = createdAt
)

fun User.toEntity() = UserEntity(
    id = id,
    name = name,
    email = email,
    passwordHash = passwordHash,
    currency = currency,
    createdAt = createdAt
)