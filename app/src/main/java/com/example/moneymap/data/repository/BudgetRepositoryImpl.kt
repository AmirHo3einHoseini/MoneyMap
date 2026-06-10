package com.example.moneymap.data.repository

import com.example.moneymap.data.local.dao.BudgetDao
import com.example.moneymap.data.local.entity.BudgetEntity
import com.example.moneymap.data.local.entity.toDomain
import com.example.moneymap.domain.model.Budget
import com.example.moneymap.domain.repository.BudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.example.moneymap.util.Result.Success
import com.example.moneymap.util.Result.Error
import com.example.moneymap.util.Result

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {


    override fun getBudgetsByMonth(
        userId: Long,
        month: Int,
        year: Int
    ): Flow<List<Budget>> {
        return budgetDao.getBudgetsWithDetails(userId, month, year.toString(), year)
            .map { list -> list.map { it.toDomain() } }

    }

    override suspend fun setBudget(budget: Budget): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val entity = BudgetEntity(
                    id = budget.id,
                    userId = budget.userId,
                    categoryId = budget.categoryId,
                    limitAmount = budget.limitAmount,
                    month = budget.month,
                    year = budget.year
                )
                budgetDao.insert(entity)
                Success(Unit)
            }catch (e: Exception){
                Error("خطا در تنظیم بودجه",e)
            }
        }
    }

    override suspend fun deleteBudget(id: Long): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                budgetDao.deleteById(id)
                Success(Unit)
            }catch (e: Exception){
                Error("خطا در حذف بودجه",e)
            }
        }
    }
}