package com.example.moneymap.domain.usecase

import com.example.moneymap.domain.model.Transaction
import com.example.moneymap.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(
        userId: Long,
        month: Int,
        year: Int
    ): Flow<List<Transaction>>{
        return transactionRepository.getTransactionByMonth(userId,month,year)
    }
}