package com.example.moneymap.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moneymap.data.local.dao.BudgetDao
import com.example.moneymap.data.local.dao.CategoryDao
import com.example.moneymap.data.local.dao.TransactionDao
import com.example.moneymap.data.local.dao.UserDao
import com.example.moneymap.data.local.entity.BudgetEntity
import com.example.moneymap.data.local.entity.CategoryEntity
import com.example.moneymap.data.local.entity.TransactionEntity
import com.example.moneymap.data.local.entity.UserEntity
import com.example.moneymap.domain.model.Budget

@Database(
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class MoneyMapDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao


}