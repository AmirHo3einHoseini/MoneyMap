package com.example.moneymap.domain.usecase

import androidx.work.impl.model.Preference
import com.example.moneymap.data.datastore.UserPreferencesDataStore
import com.example.moneymap.data.repository.UserRepositoryImpl
import com.example.moneymap.domain.model.User
import com.example.moneymap.domain.repository.UserRepository
import com.example.moneymap.util.Result
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val preference: UserPreferencesDataStore,
    private val userRepositoryImpl: UserRepositoryImpl

) {


    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Result<User> {
        if (name.isBlank())
            return Result.Error("نام نمیتواند خالی باشد")
        if (email.isBlank())
            return Result.Error("ایمیل معتبر نیست")
        if (password.length < 6)
            return Result.Error("رمز عبور باید حداقل ۶ کاراکتر باشد")
        val user = User(
            name = name.trim(),
            email = email.trim().lowercase(),
            passwordHash = userRepositoryImpl.hashPassword(password)
        )
        return when (val result = userRepository.register(user)) {
            is Result.Success -> {
                preference.saveUserId(result.data.id)
                result
            }

            is Result.Error -> result
            else -> result
        }
    }
}