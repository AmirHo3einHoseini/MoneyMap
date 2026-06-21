package com.example.moneymap.presentation.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.moneymap.databinding.FragmentSetBudgetBinding
import com.example.moneymap.presentation.auth.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class SetBudgetFragment : Fragment() {

    private var _binding: FragmentSetBudgetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BudgetViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private var selectedCategoryId: Long = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSetBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadExpenseCategories()
        observeCategories()
        setupClickListeners()
        observeUiState()
    }

    private fun observeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collect { categories ->
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line, categories.map { { it.name } }
                    )
                    binding.autCategory.setAdapter(adapter)
                    binding.autCategory.setOnItemClickListener { _, _, position, _ ->
                        selectedCategoryId = categories[position].id
                    }
                }
            }
        }
    }


    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener { saveBudget() }
        binding.btnCancel.setOnClickListener { findNavController().popBackStack() }
    }


    private fun saveBudget() {
        val limitText = binding.edtLimit.text.toString()
        if (limitText.isBlank()) {
            binding.edtLimit.error = ""
            return
        }
        if (selectedCategoryId == -1L) {
            Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG).show()
            return
        }
        val calendar = Calendar.getInstance()
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.savedUserId.first().let { userId ->
                viewModel.setBudget(
                    userId = userId,
                    categoryId = selectedCategoryId,
                    limitAmount = limitText.toDouble(),
                    month = calendar.get(Calendar.MONTH) + 1,
                    year = calendar.get(Calendar.YEAR)
                )
            }
        }
    }


    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is BudgetViewModel.UiState.Loading -> {
                            binding.progressbar.visibility = View.VISIBLE
                            binding.btnSave.isEnabled = false
                        }

                        is BudgetViewModel.UiState.BudgetSaved -> {
                            binding.progressbar.visibility = View.GONE
                            findNavController().popBackStack()
                        }

                        is BudgetViewModel.UiState.Error -> {
                            binding.progressbar.visibility = View.GONE
                            binding.btnSave.isEnabled = true
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                            viewModel.resetState()
                        }

                        else -> {
                            binding.progressbar.visibility = View.GONE
                            binding.btnSave.isEnabled = true
                        }
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