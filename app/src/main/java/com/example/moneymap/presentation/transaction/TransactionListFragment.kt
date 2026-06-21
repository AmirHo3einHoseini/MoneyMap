package com.example.moneymap.presentation.transaction

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
import com.example.moneymap.databinding.FragmentTransactionListBinding
import com.example.moneymap.presentation.auth.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class TransactionListFragment : Fragment() {
    private var _binding: FragmentTransactionListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransactionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadDate()
        observeUiState()
    }


    private fun loadDate() {
        val calendar = Calendar.getInstance()
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.savedUserId.first().let { userId ->
                if (userId != -1L) {
                    viewModel.loadTransactions(
                        userId,
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.YEAR)
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
                            binding.progresbar.visibility = View.VISIBLE
                            if (state.transactions.isEmpty()) {
                                binding.txtEmpty.visibility = View.VISIBLE
                                binding.recyclerList.visibility = View.GONE
                            } else {
                                binding.txtEmpty.visibility = View.GONE
                                binding.recyclerList.visibility = View.VISIBLE
                                adapter.submitList(state.transactions)
                            }

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

    private fun setupRecyclerView() {
        adapter = TransactionAdapter(
            onDeleteClick = { transaction ->
                viewModel.deleteTransaction(transaction.id)
            }
        )
        binding.recyclerList.apply {
            this.adapter = this@TransactionListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}