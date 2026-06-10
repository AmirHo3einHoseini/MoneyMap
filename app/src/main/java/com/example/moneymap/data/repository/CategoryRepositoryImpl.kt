package com.example.moneymap.data.repository

import com.example.moneymap.data.local.dao.CategoryDao
import com.example.moneymap.data.local.database.DefaultCategories
import com.example.moneymap.data.local.entity.toDomain
import com.example.moneymap.domain.model.Category
import com.example.moneymap.domain.model.TransactionType
import com.example.moneymap.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
            .map { list -> list.map { it.toDomain() } }

    }

    override fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(type.name)
            .map { list ->
                list.map {
                    it.toDomain()
                }
            }
    }

    override suspend fun insertDefaultCategories() {
        withContext(Dispatchers.IO) {
            val count = categoryDao.getCount()
            if (count == 0) {
                categoryDao.insertAll(DefaultCategories.get())
            }
        }
    }

}