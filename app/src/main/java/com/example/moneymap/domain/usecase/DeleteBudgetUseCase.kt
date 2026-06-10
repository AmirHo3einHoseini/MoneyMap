package com.example.moneymap.domain.usecase

import com.example.moneymap.domain.repository.BudgetRepository
import com.example.moneymap.util.Result
import javax.inject.Inject

class DeleteBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {

    suspend operator fun invoke(id: Long): Result<Unit> {
        return budgetRepository.deleteBudget(id)
    }
}