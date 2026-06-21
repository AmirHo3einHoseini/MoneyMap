package com.example.moneymap.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.moneymap.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        checkAlreadyLoggedIn()
        setupClickListener()
        observeUiState()
    }

//    private fun checkAlreadyLoggedIn() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.savedUserId.first().let { userId ->
//                if (userId != -1L) {
//                    navigateToDashboard()
//                }
//            }
//        }
//    }

    private fun setupClickListener() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(com.example.moneymap.R.id.action_login_to_register)
        }
    }


    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is AuthViewModel.UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.btnLogin.isEnabled = false
                        }

                        is AuthViewModel.UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            navigateToDashboard()
                        }

                        is AuthViewModel.UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnLogin.isEnabled = true
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG)
                            viewModel.resetState()
                        }

                        else -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnLogin.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    private fun navigateToDashboard() {
        findNavController().navigate(com.example.moneymap.R.id.action_login_to_dashboard)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}