package com.example.moneymap.domain.repository

import com.example.moneymap.domain.model.Transaction
import com.example.moneymap.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

import com.example.moneymap.util.Result

interface TransactionRepository {
    fun getTransactionsByUser(userId: Long): Flow<List<Transaction>>
    fun getTransactionByMonth(userId: Long,month: Int,year:Int): Flow<List<Transaction>>
    suspend fun addTransaction(transaction: Transaction): Result<Long>
    suspend fun deleteTransaction(id: Long): Result<Unit>
    suspend fun getTotalByType(userId:Long,type: TransactionType,month:Int,year: Int): Double
}