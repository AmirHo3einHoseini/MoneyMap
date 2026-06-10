package com.example.moneymap.data.repository

import com.example.moneymap.data.local.dao.TransactionDao
import com.example.moneymap.data.local.entity.TransactionEntity
import com.example.moneymap.data.local.entity.toDomain
import com.example.moneymap.domain.model.Transaction
import com.example.moneymap.domain.model.TransactionType
import com.example.moneymap.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import com.example.moneymap.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {


    override fun getTransactionsByUser(userId: Long): Flow<List<Transaction>> {

        return transactionDao.getTransactionsWithCategory(userId)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getTransactionByMonth(
        userId: Long,
        month: Int,
        year: Int
    ): Flow<List<Transaction>> {

        return transactionDao
            .getTransactionsByMonth(userId, month, year.toString())
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addTransaction(transaction: Transaction): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {

                val entity = TransactionEntity(
                    userId = transaction.userId,
                    amount = transaction.amount,
                    type = transaction.type.name,
                    categoryId = transaction.categoryId,
                    id = transaction.id,
                    note = transaction.note,
                    date = transaction.date
                )
                val id = transactionDao.insert(entity)
                Result.Success(id)
            } catch (e: Exception) {
                Result.Error("خطا در ثبت تراکنش", e)
            }
        }
    }

    override suspend fun deleteTransaction(id: Long): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val entity = transactionDao.findById(id)
                    ?: return@withContext Result.Error("تراکنش یافت نشد")
                transactionDao.delete(entity)
                Result.Success(Unit)

            } catch (e: Exception) {
                Result.Error("خطا در حذف تراکنش", e)

            }
        }
    }

    override suspend fun getTotalByType(
        userId: Long,
        type: TransactionType,
        month: Int,
        year: Int
    ): Double {
        return withContext(Dispatchers.IO) {
            transactionDao.getTotalByType(userId, type.name, month, year.toString())
        }
    }

}