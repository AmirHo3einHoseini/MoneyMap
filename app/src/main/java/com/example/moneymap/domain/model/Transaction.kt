package com.example.moneymap.domain.model

data class Transaction(
    val id: Long = 0,
    val userId: Long,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Long,
    val categoryName: String,
    val note: String = "",
    val date: Long = System.currentTimeMillis()
)
