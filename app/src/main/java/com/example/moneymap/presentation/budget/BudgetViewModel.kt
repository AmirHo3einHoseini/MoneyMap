package com.example.moneymap.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymap.data.datastore.UserPreferencesDataStore
import com.example.moneymap.domain.model.Budget
import com.example.moneymap.domain.model.Category
import com.example.moneymap.domain.model.TransactionType
import com.example.moneymap.domain.usecase.DeleteBudgetUseCase
import com.example.moneymap.domain.usecase.GetBudgetUseCase
import com.example.moneymap.domain.usecase.GetCategoriesUseCase
import com.example.moneymap.domain.usecase.SetBudgetUseCase
import com.example.moneymap.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getBudgetsUseCase: GetBudgetUseCase,
    private val setBudgetUseCase: SetBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val preferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    sealed class UiState {
        data object Idle : UiState()
        data object Loading : UiState()
        data object BudgetSaved : UiState()
        data class BudgetList(val budgets: List<Budget>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    fun loadBudgets(userId: Long, month: Int, year: Int) {
        viewModelScope.launch {
            getBudgetsUseCase(userId, month, year)
                .collect { budgets ->
                    _uiState.value = UiState.BudgetList(budgets)
                }
        }
    }

    fun loadExpenseCategories() {
        viewModelScope.launch {
            getCategoriesUseCase(TransactionType.EXPENSE).collect { list ->
                _categories.value = list
            }
        }
    }

    fun setBudget(
        userId: Long,
        categoryId: Long,
        limitAmount: Double,
        month: Int,
        year: Int
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val budget = Budget(
                userId = userId,
                categoryId = categoryId,
                limitAmount = limitAmount,
                month = month,
                year = year, id = 0
            )
            when (val result = setBudgetUseCase(budget)) {
                is Result.Success -> _uiState.value = UiState.BudgetSaved
                is Result.Error -> _uiState.value = UiState.Error(result.message)
                else -> Unit
            }
        }
    }

    fun deleteBudget(id: Long) {
        viewModelScope.launch {
            when (val result = deleteBudgetUseCase(id)) {
                is Error -> _uiState.value = UiState.Error(result.message.toString())
                else -> Unit
            }
        }
    }


    fun resetState() {
        _uiState.value = UiState.Idle
    }
}