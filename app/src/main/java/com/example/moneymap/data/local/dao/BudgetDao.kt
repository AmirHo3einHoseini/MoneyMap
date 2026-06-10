package com.example.moneymap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.moneymap.data.local.entity.BudgetEntity
import com.example.moneymap.data.local.entity.BudgetWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity): Long

    @Query("DELETE FROM budget WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Transaction
    @Query(
        """
        SELECT b.*, COALESCE(SUM(t.amount), 0.0) as spentAmount
        FROM budget b
        LEFT JOIN transactions t 
            ON t.categoryId = b.categoryId 
            AND t.userId = b.userId
            AND t.type = 'EXPENSE'
            AND strftime('%m', t.date/1000, 'unixepoch') = printf('%02d', :month)
            AND strftime('%Y', t.date/1000, 'unixepoch') = :year
        WHERE b.userId = :userId AND b.month = :month AND b.year = :yearInt
        GROUP BY b.id
    """
    )
    fun getBudgetsWithDetails(
        userId: Long,
        month: Int,
        year: String,
        yearInt: Int
    ): Flow<List<BudgetWithDetails>>
}