package com.example.moneymap.application

import android.app.Application
import com.example.moneymap.domain.repository.CategoryRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MoneyMapApp : Application() {
    @Inject
    lateinit var categoryRepository: CategoryRepository
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            categoryRepository.insertDefaultCategories()
        }
    }
}