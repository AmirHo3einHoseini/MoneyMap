package com.example.moneymap.domain.usecase

import com.example.moneymap.domain.model.Budget
import com.example.moneymap.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(
        userId: Long,
        month: Int,
        year: Int
    ): Flow<List<Budget>> {
        return budgetRepository.getBudgetsByMonth(userId, month, year)
    }
}