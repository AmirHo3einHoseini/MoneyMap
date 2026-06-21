package com.example.moneymap.presentation.transaction

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
import com.example.moneymap.R
import com.example.moneymap.databinding.FragmentAddTransactionBinding
import com.example.moneymap.domain.model.TransactionType
import com.example.moneymap.presentation.auth.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
@AndroidEntryPoint
class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private var selectedCategoryId: Long = -1L
    private var selectedType = TransactionType.EXPENSE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTypeToggle()
        setupClickListeners()
        observeCategories()
        observeUiState()

        viewModel.loadCategories(TransactionType.EXPENSE)

    }

    private fun setupTypeToggle() {
        binding.toggleTransactionType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                selectedType = when (checkedId) {
                    R.id.btn_income -> TransactionType.Income
                    else -> TransactionType.EXPENSE
                }
                selectedCategoryId = -1L
                binding.autCategory.setText("")
                viewModel.loadCategories(selectedType)
            }
        }
    }


    private fun observeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collect { categories ->
                    val names = categories.map { it.name }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        names
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
        binding.btnSave.setOnClickListener { saveTransaction() }
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun saveTransaction() {
        val amountText = binding.edtAmount.text.toString()
        if (amountText.isBlank()) {
            binding.edtAmount.error = ""
            return
        }
        if (selectedCategoryId == -1L) {
            Snackbar.make(binding.root, "انتخاب دسته بندی الزامی است", Snackbar.LENGTH_LONG).show()
            return
        }
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.savedUserId.first().let { userId ->
                viewModel.addTransaction(
                    userId,
                    amountText.toDouble(),
                    selectedType,
                    selectedCategoryId,
                    binding.autCategory.text.toString(),
                    binding.edtNote.text.toString(),
                    System.currentTimeMillis()
                )
            }
        }

    }


    private fun observeUiState(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect { state->

                    when(state){
                        is TransactionViewModel.UiState.Loading -> {
                            binding.progressbar.visibility = View.VISIBLE
                            binding.btnSave.isEnabled = false
                        }
                        is TransactionViewModel.UiState.TransactionAdded -> {
                            binding.progressbar.visibility = View.GONE
                            findNavController().popBackStack()
                        }
                        is TransactionViewModel.UiState.Error -> {
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