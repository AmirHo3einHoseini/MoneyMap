package com.example.moneymap.domain.usecase

import com.example.moneymap.data.datastore.UserPreferencesDataStore
import javax.inject.Inject


class LogoutUseCase @Inject constructor(
    private val preferencesDataStore: UserPreferencesDataStore
) {
    suspend operator fun invoke() {
        preferencesDataStore.clear()
    }
}