package com.example.moneymap.domain.usecase

import com.example.moneymap.domain.model.TransactionType
import com.example.moneymap.domain.repository.TransactionRepository
import javax.inject.Inject


data class DashboardSummary(
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double
)

class GetDashboardSummaryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(
        userId: Long,
        month: Int,
        year: Int
    ): DashboardSummary {
        val income = transactionRepository.getTotalByType(
            userId, TransactionType.Income, month, year
        )
        val expense = transactionRepository.getTotalByType(
            userId, TransactionType.EXPENSE, month, year
        )
        return DashboardSummary(
            totalIncome = income,
            totalExpense = expense,
            balance = income - expense
        )
    }
}