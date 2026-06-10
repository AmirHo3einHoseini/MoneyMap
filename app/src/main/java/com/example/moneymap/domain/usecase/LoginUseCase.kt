package com.example.moneymap.domain.usecase

import com.example.moneymap.data.datastore.UserPreferencesDataStore
import com.example.moneymap.domain.model.User
import com.example.moneymap.domain.repository.UserRepository
import com.example.moneymap.util.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesDataStore: UserPreferencesDataStore
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<User> {
        if (email.isBlank() || !email.contains("@"))
            return Result.Error("ایمیل معتبر نیست.")
        if (password.isBlank())
            return Result.Error("رمز عبور را وارد کنید.")
        return when (val result = userRepository.login(
            email.trim().lowercase(),
            password
        )) {
            is Result.Success -> {
                preferencesDataStore.saveUserId(result.data.id)
                result
            }

            is Result.Error -> result
            else -> result
        }

    }
}