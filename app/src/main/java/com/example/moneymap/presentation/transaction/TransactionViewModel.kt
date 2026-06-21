package com.example.moneymap.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymap.data.datastore.UserPreferencesDataStore
import com.example.moneymap.domain.model.Category
import com.example.moneymap.domain.model.Transaction
import com.example.moneymap.domain.model.TransactionType
import com.example.moneymap.domain.usecase.AddTransactionUseCase
import com.example.moneymap.domain.usecase.DeleteTransactionUseCase
import com.example.moneymap.domain.usecase.GetCategoriesUseCase
import com.example.moneymap.domain.usecase.GetTransactionUseCase
import com.example.moneymap.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getTransactionsUseCase: GetTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val preferencesDataStore: UserPreferencesDataStore
) : ViewModel() {


    sealed class UiState {
        data object Idle : UiState()
        data object Loading : UiState()
        data object TransactionAdded : UiState()
        data class TransactionList(val transactions: List<Transaction>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    private val _snackBar = MutableSharedFlow<String>()
    val snackBar: SharedFlow<String> = _snackBar

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val calendar = Calendar.getInstance()

    fun loadTransactions(userId: Long, month: Int, year: Int) {
        viewModelScope.launch {
            getTransactionsUseCase(userId, month, year)
                .collect { transactions ->
                    _uiState.value = UiState.TransactionList(transactions)
                }
        }
    }

    fun loadCategories(type: TransactionType) {
        viewModelScope.launch {
            getCategoriesUseCase(type)
                .collect { list ->
                    _categories.value = list
                }
        }
    }

    fun addTransaction(
        userId: Long,
        amount: Double,
        type: TransactionType,
        categoryId: Long,
        categoryName: String,
        note: String,
        date: Long
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val transaction = Transaction(
                userId = userId,
                amount = amount,
                type = type,
                categoryId = categoryId,
                categoryName = categoryName,
                note = note,
                date = date
            )
            when (val result = addTransactionUseCase(transaction)) {
                is Result.Success -> _uiState.value = UiState.TransactionAdded
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.message)
                    _snackBar.emit(result.message)
                }

                else -> Unit
            }
        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            when (val result = deleteTransactionUseCase(id)) {
                is Result.Error -> _uiState.value = UiState.Error(result.message)
                else -> Unit
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }

}