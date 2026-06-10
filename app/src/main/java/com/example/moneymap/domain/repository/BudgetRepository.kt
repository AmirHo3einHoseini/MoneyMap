package com.example.moneymap.domain.repository

import com.example.moneymap.domain.model.Budget
import kotlinx.coroutines.flow.Flow
import com.example.moneymap.util.Result
interface BudgetRepository {
    fun getBudgetsByMonth(userId:Long,month:Int,year:Int): Flow<List<Budget>>
    suspend fun setBudget(budget: Budget): Result<Unit>
    suspend fun deleteBudget(id: Long): Result<Unit>
}