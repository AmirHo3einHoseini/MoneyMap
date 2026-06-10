package com.example.moneymap.data.repository

import com.example.moneymap.data.local.dao.UserDao
import com.example.moneymap.data.local.entity.toDomain
import com.example.moneymap.data.local.entity.toEntity
import com.example.moneymap.domain.model.User
import com.example.moneymap.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import com.example.moneymap.util.Result.Success
import com.example.moneymap.util.Result.Error
import com.example.moneymap.util.Result.Loading
import com.example.moneymap.util.Result
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun register(user: User): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val entity = user.toEntity()
                val id = userDao.insert(entity)
                Success(user.copy(id = id))
            } catch (e: Exception) {
                Error("")
            }
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val entity = userDao.findByEmail(email)
                    ?: return@withContext Error("کاربری با این ایمیل یافت نشد")
                val hashedPassword = hashPassword(password)
                if (entity.passwordHash != hashedPassword) {
                    return@withContext Error("رمز عبور اشتباه است")
                }
                Success(entity.toDomain())

            } catch (e: Exception) {
                Error("خطا در ورود به سیستم", e)
            }
        }
    }

    override suspend fun getUserById(id: Long): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val entity = userDao.findById(id) ?: return@withContext Error("کاربر یافت نشد")

                Success(entity.toDomain())

            } catch (e: Exception) {
                Error("خطا در دریافت اطلاعات کاربر", e)
            }
        }
    }

    override suspend fun updateCurrency(
        userId: Long,
        currency: String
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                userDao.updateCurrency(currency, userId)
                Success(Unit)
            } catch (e: Exception) {
                Error("",e)
            }
        }
    }

    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}