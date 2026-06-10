package com.example.moneymap.domain.model


enum class TransactionType { Income, EXPENSE }

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String,
    val type: TransactionType,
    val color: String
)
