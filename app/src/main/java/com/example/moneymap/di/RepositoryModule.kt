package com.example.moneymap.di

import com.example.moneymap.data.repository.BudgetRepositoryImpl
import com.example.moneymap.data.repository.CategoryRepositoryImpl
import com.example.moneymap.data.repository.TransactionRepositoryImpl
import com.example.moneymap.data.repository.UserRepositoryImpl
import com.example.moneymap.domain.repository.BudgetRepository
import com.example.moneymap.domain.repository.CategoryRepository
import com.example.moneymap.domain.repository.TransactionRepository
import com.example.moneymap.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

}