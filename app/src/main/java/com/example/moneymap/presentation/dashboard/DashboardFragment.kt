package com.example.moneymap.presentation.dashboard

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymap.databinding.FragmentDashboardBinding
import com.example.moneymap.domain.usecase.DashboardSummary
import com.example.moneymap.presentation.auth.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var transactionAdapter: RecentTransactionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        loadData()
        observeUiState()
    }

    private fun setRecyclerView() {
        transactionAdapter = RecentTransactionAdapter()
        binding.recyclerTransactions.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle((Lifecycle.State.STARTED)) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is DashboardViewModel.UiState.Loading -> {
                            binding.progressbar.visibility = View.VISIBLE
                        }

                        is DashboardViewModel.UiState.Success -> {
                            binding.progressbar.visibility = View.GONE
                            updateSummary(state.summary)
                            transactionAdapter.submitList(state.recentTransactions)
                        }

                        is DashboardViewModel.UiState.Error -> {
                            binding.progressbar.visibility = View.GONE
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is DashboardViewModel.UiState.Loading -> {
                            binding.progressbar.visibility = View.VISIBLE
                            delay(10000.milliseconds)
                            binding.progressbar.visibility = View.GONE
                            binding.txtEmpty.visibility= View.VISIBLE
                            binding.txtTitle.visibility= View.GONE
                        }

                        is DashboardViewModel.UiState.Success -> {
                            binding.progressbar.visibility = View.GONE
                            updateSummary(state.summary)
                            transactionAdapter.submitList(state.recentTransactions)
                        }

                        is DashboardViewModel.UiState.Error -> {
                            binding.progressbar.visibility = View.GONE
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun updateSummary(summary: DashboardSummary) {
        binding.txtBalance.text = formatAmount(summary.balance)
        binding.txtIncome.text = formatAmount(summary.totalIncome)
        binding.txtExpense.text = formatAmount(summary.totalExpense)
    }

    private fun formatAmount(amount: Double): String {

        return "%,.0fریال ".format(amount)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}