package com.aditechnology.moneymanagement.ui.home

import android.R
import android.R.attr.button
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.databinding.FragmentExpenseIncomeFragmentBinding
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.ui.adapter.ExpenseIncomeDetailListAdapter
import com.aditechnology.moneymanagement.utils.DateTimeUtils
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseViewModelFactory


class ExpenseHomePagerFragment :Fragment(),ExpenseIncomeDetailListAdapter.OnClickListener{

    private lateinit var mAccountListAdapter: ExpenseIncomeDetailListAdapter
    private val expenseIncomeViewModel: ExpenseIncomeViewModel by viewModels {
        ExpenseViewModelFactory((requireActivity().application as MainApplication).repository)
    }

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

        binding.textViewCurrentDateYearSelection.text = DateTimeUtils.getDate()
        expenseIncomeViewModel.getByAccountIdAndDate(accountId,DateTimeUtils.getTimeStampFromDate(DateTimeUtils.getDate()).toString())?.observe(requireActivity()){
                all->
            mAccountListAdapter.updateList(all.reversed())
        }

        binding.imageViewPrevious.setOnClickListener {
            val currentDateSearch =  binding.textViewCurrentDateYearSelection.text.toString()
            val searchDate = DateTimeUtils.getCalculatedPreviousDayDateTimeStamp(DateTimeUtils.getTimeStampFromDate(currentDateSearch).toString())
            val searchDateTime = DateTimeUtils.getDateFromTimeStamp(searchDate.toString())
            binding.textViewCurrentDateYearSelection.text=searchDateTime
            if (searchDate != null) {
                expenseIncomeViewModel.getByAccountIdAndDate(accountId,searchDate)?.observe(requireActivity()){all->
                    mAccountListAdapter.updateList(all.reversed())

                }
            }
        }
        binding.imageViewFiltter.setOnClickListener({
            val popupMenu = PopupMenu(requireContext(), binding.imageViewFiltter)
            popupMenu.getMenuInflater().inflate(R.menu., popupMenu.getMenu())
        })
        binding.imageViewNext.setOnClickListener {
            val currentDateSearch =  binding.textViewCurrentDateYearSelection.text.toString()
            val searchDate = DateTimeUtils.getCalculatedNextDayDateTimeStamp(DateTimeUtils.
            getTimeStampFromDate(currentDateSearch).toString())
            val searchDateTime = DateTimeUtils.getDateFromTimeStamp(searchDate.toString())
            binding.textViewCurrentDateYearSelection.text=searchDateTime
            if (searchDate != null) {
                expenseIncomeViewModel.getByAccountIdAndDate(accountId,searchDate)?.observe(requireActivity()){all->
                    mAccountListAdapter.updateList(all.reversed())

                }
            }

        }

        mAccountListAdapter = ExpenseIncomeDetailListAdapter( this)
        var linearLayoutManager1 = LinearLayoutManager(context)
        linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
        binding.recycleView.layoutManager = linearLayoutManager1
        binding.recycleView.adapter = mAccountListAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun openActionOnTransactionBottomSheet(item: DetailsFileTable) {

    }
}