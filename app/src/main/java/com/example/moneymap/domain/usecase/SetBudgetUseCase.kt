package com.example.moneymap.domain.usecase

import com.example.moneymap.domain.model.Budget
import com.example.moneymap.domain.repository.BudgetRepository
import com.example.moneymap.util.Result
import javax.inject.Inject

class SetBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget): Result<Unit> {
        if (budget.limitAmount <= 0)
            return Result.Error("")
        if (budget.categoryId <= 0)
            return Result.Error("")
        return budgetRepository.setBudget(budget)
    }
}