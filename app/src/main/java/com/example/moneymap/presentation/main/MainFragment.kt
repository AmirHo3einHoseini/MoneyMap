package com.example.moneymap.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.moneymap.R
import com.example.moneymap.databinding.FragmentMainBinding
import com.example.moneymap.presentation.auth.AuthViewModel
import com.example.moneymap.presentation.budget.BudgetFragment
import com.example.moneymap.presentation.dashboard.DashboardFragment
import com.example.moneymap.presentation.report.ReportFragment
import com.example.moneymap.presentation.transaction.TransactionListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupBottomNavigation()
        setupFab()
    }


    private fun setupViewPager() {
        val fragments = listOf(
            DashboardFragment(),
            TransactionListFragment(),
            BudgetFragment(),
            ReportFragment()
        )
        binding.viewPager.adapter = MainPagerAdapter(this, fragments)
        binding.viewPager.isUserInputEnabled = false
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> binding.viewPager.currentItem = 0
                R.id.nav_transactions -> binding.viewPager.currentItem = 1
                R.id.nav_budget -> binding.viewPager.currentItem = 2
                R.id.nav_report -> binding.viewPager.currentItem = 3
            }
            true
        }
    }

    private fun setupFab() {
        binding.fabAddTransaction.setOnClickListener {
            findNavController().navigate(
                R.id.action_main_to_add_transaction
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}


