package com.example.moneymap.presentation.report

import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.moneymap.databinding.FragmentReportBinding
import com.example.moneymap.domain.model.Transaction
import com.example.moneymap.domain.model.TransactionType
import com.example.moneymap.presentation.auth.AuthViewModel
import com.example.moneymap.presentation.transaction.TransactionViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCharts()
        loadData()
        observeUiState()
    }

    private fun setupCharts() {
        binding.pieChart.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            holeRadius = 40f
            legend.isEnabled = true
            setEntryLabelTextSize(11f)
        }

        binding.barChart.apply {
            description.isEnabled = false
            legend.isEnabled = true
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)
            axisRight.isEnabled = false
        }

    }


    private fun loadData() {
        val calendar = Calendar.getInstance()
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.savedUserId.first().let { userId ->
                if (userId != -1L) {
                    viewModel.loadTransactions(
                        userId, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)
                    )
                }
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is TransactionViewModel.UiState.Loading -> {
                            binding.progresbar.visibility = View.VISIBLE
                        }

                        is TransactionViewModel.UiState.TransactionList -> {
                            binding.progresbar.visibility = View.GONE
                            updatePieChart(state.transactions)
                            updateBarChart(state.transactions)
                        }

                        is TransactionViewModel.UiState.Error -> {
                            binding.progresbar.visibility = View.GONE
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }


    private fun updatePieChart(transaction: List<Transaction>) {
        val expenseMap = transaction
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.categoryName }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
        if (expenseMap.isEmpty()) {
            binding.pieChart.clear()
            binding.pieChart.centerText = "داده ای نیست"
            return
        }
        val entries = expenseMap.map { (name, amount) ->
            PieEntry(amount.toFloat(), name)
        }
        val dataset = PieDataSet(entries, "هزینه ها").apply {
            colors = listOf(
                Color.parseColor("#E24B4A"),
                Color.parseColor("#534AB7"),
                Color.parseColor("#1D9E75"),
                Color.parseColor("#BA7517"),
                Color.parseColor("#378ADD"),
                Color.parseColor("#D4537E")
            )
            valueTextSize = 11f
        }

        binding.pieChart.data = PieData(dataset)
        binding.pieChart.invalidate()
    }

    private fun updateBarChart(transaction: List<Transaction>) {
        val totalIncome = transaction
            .filter { it.type == TransactionType.Income }
            .sumOf { it.amount }

        val totalExpense = transaction
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        val incomeEntry = BarEntry(0f, totalIncome.toFloat())
        val expenseEntry = BarEntry(1f, totalExpense.toFloat())

        val incomeDataSet = BarDataSet(listOf(incomeEntry), "").apply {
            color = Color.parseColor("#1D9E75")
            valueTextSize = 11f
        }

        val expenseDataSet = BarDataSet(listOf(expenseEntry), "").apply {
            color = Color.parseColor("#E24B4A")
            valueTextSize = 11f
        }


        binding.barChart.data = BarData(incomeDataSet, expenseDataSet).apply {
            barWidth = 0.4f
        }

        binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(
            listOf("", "")
        )
        binding.barChart.invalidate()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}