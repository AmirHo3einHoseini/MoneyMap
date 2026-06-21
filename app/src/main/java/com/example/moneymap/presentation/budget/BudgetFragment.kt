package com.example.moneymap.presentation.budget

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymap.R
import com.example.moneymap.databinding.FragmentBudgetBinding
import com.example.moneymap.presentation.auth.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BudgetFragment : Fragment() {
    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BudgetViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var adapter: BudgetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadData()
        observeUiState()

        binding.fabAddBudget.setOnClickListener {
            findNavController().navigate(R.id.action_budget_to_set_budget)
        }
    }

    private fun setupRecyclerView() {
        adapter = BudgetAdapter(onDeleteClick ={ budget -> viewModel.deleteBudget(budget.id) } )

        binding.recyclerBudget.apply {
            this.adapter = this@BudgetFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadData() {
        val calendar = Calendar.getInstance()
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.savedUserId.first().let { userid ->
                if (userid != -1L) {
                    viewModel.loadBudgets(
                        userid, month = calendar.get(Calendar.MONTH) + 1,
                        year = calendar.get(Calendar.YEAR)
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
                        is BudgetViewModel.UiState.BudgetList -> {
                            if (state.budgets.isEmpty()) {
                                binding.txtEmpty.visibility = View.VISIBLE
                                binding.recyclerBudget.visibility = View.GONE
                            } else {
                                binding.txtEmpty.visibility = View.GONE
                                binding.recyclerBudget.visibility = View.VISIBLE
                                adapter.submitList(state.budgets)
                            }
                        }
                        is BudgetViewModel.UiState.Error -> {
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}