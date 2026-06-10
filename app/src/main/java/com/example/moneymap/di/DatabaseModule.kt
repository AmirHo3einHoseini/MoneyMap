package com.example.moneymap.di

import android.content.Context
import androidx.room.Room
import com.example.moneymap.data.local.dao.BudgetDao
import com.example.moneymap.data.local.dao.CategoryDao
import com.example.moneymap.data.local.dao.TransactionDao
import com.example.moneymap.data.local.dao.UserDao
import com.example.moneymap.data.local.database.MoneyMapDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MoneyMapDatabase {
        return Room.databaseBuilder(
            context, MoneyMapDatabase::class.java,
            "moneymap.dp"
        ).build()
    }

    @Provides
    fun provideUserDao(db: MoneyMapDatabase): UserDao = db.userDao()

    @Provides
    fun provideCategoryDao(db: MoneyMapDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideTransactionDao(db: MoneyMapDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideBudgetDao(db: MoneyMapDatabase): BudgetDao=db.budgetDao()



}