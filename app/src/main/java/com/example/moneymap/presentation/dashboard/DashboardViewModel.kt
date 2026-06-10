package com.example.moneymap.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymap.data.datastore.UserPreferencesDataStore
import com.example.moneymap.domain.model.Transaction
import com.example.moneymap.domain.usecase.DashboardSummary
import com.example.moneymap.domain.usecase.GetDashboardSummaryUseCase
import com.example.moneymap.domain.usecase.GetTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionUseCase,
    private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    private val preferencesDataStore: UserPreferencesDataStore
): ViewModel() {

    sealed class UiState {
        data object Loading : UiState()
        data class Success(
            val summary: DashboardSummary,
            val recentTransactions: List<Transaction>
        ) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private val calendar = Calendar.getInstance()
    private val currentMonth = calendar.get(Calendar.MONTH) + 1
    private val currentYear = calendar.get(Calendar.YEAR)
    fun loadDashboard(userId: Long) {
        viewModelScope.launch {
            // تراکنش‌های ماه جاری را به صورت Flow دریافت می‌کنیم
            getTransactionsUseCase(userId, currentMonth, currentYear)
                .collect { transactions ->
                    try {
                        val summary = getDashboardSummaryUseCase(
                            userId, currentMonth, currentYear
                        )
                        _uiState.value = UiState.Success(
                            summary = summary,
                            // فقط ۵ تراکنش آخر را نشان می‌دهیم
                            recentTransactions = transactions.take(5)
                        )
                    } catch (e: Exception) {
                        _uiState.value = UiState.Error("خطا در دریافت اطلاعات")
                    }
                }
        }
    }
}