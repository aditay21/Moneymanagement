package com.aditechnology.moneymanagement.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.databinding.FragmentDetailsListBinding
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.ui.adapter.ExpenseIncomeDetailListAdapter
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseViewModelFactory

class ExpenseIncomeDetailFragment : Fragment(), ExpenseIncomeDetailListAdapter.OnClickListener {
    private lateinit var mAccountListAdapter: ExpenseIncomeDetailListAdapter
    private val ARG_OBJECT = "position"
    private val ARG_OBJECT2 = "accountid"
    var mTotalBalance = 0
    private var _binding: FragmentDetailsListBinding? = null
    private val accountViewModel : AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((requireActivity().application as MainApplication).repository)
    }
    private val expenseIncomeViewModel: ExpenseIncomeViewModel by viewModels {
        ExpenseViewModelFactory((requireActivity().application as MainApplication).repository)
    }
    private var accountId = 0
    private var position = 0
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            position = requireArguments().getInt(ARG_OBJECT, 0);

            arguments?.takeIf { it.containsKey(ARG_OBJECT2) }?.apply {
                accountId = requireArguments().getInt(ARG_OBJECT2, 0);

                mAccountListAdapter = ExpenseIncomeDetailListAdapter( this@ExpenseIncomeDetailFragment)
                var linearLayoutManager1 = LinearLayoutManager(context)
                linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
                binding.recycleView.layoutManager = linearLayoutManager1
                binding.recycleView.adapter = mAccountListAdapter
            }
            expenseIncomeViewModel.getExpenseIncomeByAccountId(accountId,position)?.observe(requireActivity()) { all ->
                mAccountListAdapter.updateList(all.reversed())
            }
        }
    }

    override fun openActionOnTransactionBottomSheet(item: DetailsFileTable) {

    }
}


