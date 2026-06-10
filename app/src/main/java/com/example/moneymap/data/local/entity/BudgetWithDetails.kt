package com.example.moneymap.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.moneymap.domain.model.Budget

data class BudgetWithDetails(
    @Embedded val budget: BudgetEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity,
    val spentAmount: Double = 0.0
)

fun BudgetWithDetails.toDomain()= Budget(
    id = budget.id,
    userId = budget.userId,
    categoryId = budget.categoryId,
    categoryName = category.name,
    limitAmount = budget.limitAmount,
    spentAmount = spentAmount,
    month = budget.month,
    year = budget.year
)
