package com.example.moneymap.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.moneymap.data.local.entity.TransactionEntity
import com.example.moneymap.data.local.entity.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transactionEntity: TransactionEntity): Long

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): TransactionEntity?

    @Transaction
    @Query(
        """
       SELECT * FROM transactions
        WHERE userId= :userId
        ORDER BY date DESC
    """
    )
    fun getTransactionsWithCategory(userId: Long): Flow<List<TransactionWithCategory>>


    @Transaction
    @Query("""
        SELECT * FROM transactions 
        WHERE userId = :userId
        AND strftime('%m', date/1000, 'unixepoch') = printf('%02d', :month)
        AND strftime('%Y', date/1000, 'unixepoch') = :year
        ORDER BY date DESC
    """)
    fun getTransactionsByMonth(
        userId: Long,
        month: Int,
        year: String
    ): Flow<List<TransactionWithCategory>>

    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions 
        WHERE userId = :userId 
        AND type = :type
        AND strftime('%m', date/1000, 'unixepoch') = printf('%02d', :month)
        AND strftime('%Y', date/1000, 'unixepoch') = :year
    """)
    suspend fun getTotalByType(
        userId: Long,
        type: String,
        month: Int,
        year: String
    ): Double
}