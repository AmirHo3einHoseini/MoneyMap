package com.example.moneymap.domain.usecase

import com.example.moneymap.domain.model.Transaction
import com.example.moneymap.domain.repository.TransactionRepository
import com.example.moneymap.util.Result
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend  operator fun invoke(transaction: Transaction) : Result<Long>{
        if (transaction.amount <=0)
            return Result.Error("")
        if (transaction.categoryId <=0 )
           return Result.Error("")

        return transactionRepository.addTransaction(transaction)
    }
}