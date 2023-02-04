package com.aditechnology.moneymanagement.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aditechnology.moneymanagement.databinding.FragmentExpenseIncomeFragmentBinding
import com.aditechnology.moneymanagement.ui.adapter.ExpenseIncomePager
import com.google.android.material.tabs.TabLayoutMediator

class ExpenseHomePagerFragment :Fragment(){


    private lateinit var  expenseIncomePager: ExpenseIncomePager
    private var _binding: FragmentExpenseIncomeFragmentBinding? = null
    private val binding get() = _binding!!
    private var accountId =0;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseIncomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey("accountid") }?.apply {
            accountId = requireArguments().getInt("accountid", 0);
        }


        expenseIncomePager = ExpenseIncomePager(this,accountId)
        _binding?.pager?.adapter = expenseIncomePager

        _binding?.let {
            TabLayoutMediator(it.tabLayout, it.pager) { tab, position ->
               if (position==0) {
                   tab.text = "Income"
               }else{
                   tab.text = "Expense"
               }
            }.attach()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}