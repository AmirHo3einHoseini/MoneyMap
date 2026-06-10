package com.example.moneymap.domain.repository

import com.example.moneymap.domain.model.User
import com.example.moneymap.util.Result

interface UserRepository {
    suspend fun register(user: User): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun getUserById(id: Long): Result<User>
    suspend fun updateCurrency(userId:Long,currency: String): Result<Unit>
}