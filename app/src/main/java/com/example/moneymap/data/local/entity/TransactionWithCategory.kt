package com.example.moneymap.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.moneymap.domain.model.Transaction
import com.example.moneymap.domain.model.TransactionType

data class TransactionWithCategory(
    @Embedded val transaction: TransactionEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity
)
fun TransactionWithCategory.toDomain() = Transaction(
    id = transaction.id,
    userId = transaction.userId,
    amount = transaction.amount,
    type = TransactionType.valueOf(transaction.type),
    categoryId = transaction.categoryId,
    categoryName = category.name,
    note = transaction.note,
    date = transaction.date
)