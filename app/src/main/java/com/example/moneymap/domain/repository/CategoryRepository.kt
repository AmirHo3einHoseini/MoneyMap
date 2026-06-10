package com.example.moneymap.domain.repository

import com.example.moneymap.domain.model.Category
import com.example.moneymap.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>
    suspend fun insertDefaultCategories()
}