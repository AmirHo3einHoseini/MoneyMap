package com.example.moneymap.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.moneymap.R
import com.example.moneymap.databinding.FragmentSplashBinding
import com.example.moneymap.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            // کمی صبر کن تا splash نشون داده بشه
            delay(1000)
            // userId رو از DataStore بخون
            authViewModel.savedUserId.first().let { userId ->
                if (userId != -1L) {
                    // قبلاً login کرده — مستقیم بره Dashboard
                    findNavController().navigate(R.id.action_splash_to_dashboard)
                } else {
                    // login نکرده — بره Login
                    findNavController().navigate(R.id.action_splash_to_login)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}