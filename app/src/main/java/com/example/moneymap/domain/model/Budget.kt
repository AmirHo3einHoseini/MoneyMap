package com.example.moneymap.domain.model

import java.time.Month

data class Budget(
    val id: Long,
    val userId: Long,
    val categoryId: Long,
    val categoryName: String = "",
    val limitAmount: Double,
    val spentAmount: Double = 0.0,
    val month: Int,
    val year: Int
) {
    val remainingAmount: Double get() = limitAmount - spentAmount
    val progressPercent: Int get() = ((spentAmount / limitAmount) * 100).toInt().coerceIn(0, 100)
    val isOverBudget: Boolean get() = spentAmount > limitAmount
}
