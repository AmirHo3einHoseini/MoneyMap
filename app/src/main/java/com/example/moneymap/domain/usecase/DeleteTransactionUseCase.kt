package com.example.moneymap.domain.usecase

import com.example.moneymap.domain.repository.TransactionRepository
import com.example.moneymap.util.Result
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(id: Long): Result<Unit> {
        return transactionRepository.deleteTransaction(id)
    }
}