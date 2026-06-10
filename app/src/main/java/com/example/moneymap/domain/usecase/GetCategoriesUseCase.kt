package com.example.moneymap.domain.usecase

import com.example.moneymap.domain.model.Category
import com.example.moneymap.domain.model.TransactionType
import com.example.moneymap.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
){

    operator fun invoke(type: TransactionType): Flow<List<Category>> {
        return categoryRepository.getCategoriesByType(type)
    }
}