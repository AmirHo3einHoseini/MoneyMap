package com.example.moneymap.di

import com.example.moneymap.domain.model.Category
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
object RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoriImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: TransactionRepositoriImpl): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(impl: BudgetRepositoriImpl): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoriImpl): CategoryRepository

}