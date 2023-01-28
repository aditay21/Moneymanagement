package com.aditechnology.moneymanagement.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.Type
import com.aditechnology.moneymanagement.databinding.BottomSheetAddDetailBinding
import com.aditechnology.moneymanagement.databinding.FragmentDetailsListBinding
import com.aditechnology.moneymanagement.utils.CalenderView
import com.aditechnology.moneymanagement.utils.DateTimeUtils
import com.aditechnology.moneymanagement.utils.TimeView
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

class DetailListFragment : Fragment() , DetailListAdapter.OnClickListener {
    private lateinit var mAccountListAdapter: DetailListAdapter
    private val ARG_OBJECT = "accountid"
    var mTotalBalance = 0
    private var _binding: FragmentDetailsListBinding? = null
    private val accountViewModel : AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((requireActivity().application as MainApplication).repository)
    }
    private val expenseIncomeViewModel: ExpenseIncomeViewModel by viewModels {
        ExpenseViewModelFactory((requireActivity().application as MainApplication).repository)
    }
    private var accountId = 0;
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
            accountId = requireArguments().getInt(ARG_OBJECT, 0);
            mAccountListAdapter = DetailListAdapter(accountId, this@DetailListFragment)
            var  linearLayoutManager1 = LinearLayoutManager(context)
            linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
            binding.recycleView.layoutManager =linearLayoutManager1
            binding.recycleView.adapter = mAccountListAdapter
        }
        accountViewModel.getAccountDetailBy(accountId)?.observe(requireActivity()){
            list->
            if (list.isNotEmpty()){
                mTotalBalance = list[0].accountBalance.toInt()
            }
            mAccountListAdapter.updateHeader(list)
        }
        expenseIncomeViewModel.getByAccountId(accountId)?.observe(requireActivity()){
                all->
               mAccountListAdapter.updateList(all)
            }
    }

    override fun openBottomSheet(accountId: Int, accountBalance: Int) {
        var type: Type = Type.EXPENSE
        val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomSheetAddDetailBinding.inflate(inflater, null, false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)

        binding.textViewExpense.setBackgroundResource(R.drawable.toggel_bg_black)
        binding.textViewExpense.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.textViewExpense.setOnClickListener {
            binding.textViewWhoToPayTitle.text = "Pay to"
            binding.textViewPaidForTitle.text = "Pay For"
            type = Type.EXPENSE
            binding.textViewExpense.setBackgroundResource(R.drawable.toggel_bg_black)
            binding.textViewIncome.setBackgroundResource(R.drawable.button_bg_white)

            binding.textViewExpense.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.textViewIncome.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
        }
        binding.textViewIncome.setOnClickListener {
            type = Type.INCOME
            binding.textViewWhoToPayTitle.text = "Get From"
            binding.textViewPaidForTitle.text = "Get For"
            binding.textViewExpense.setBackgroundResource(R.drawable.button_bg_white)
            binding.textViewExpense.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            binding.textViewIncome.setBackgroundResource(R.drawable.toggel_bg_black)
            binding.textViewIncome.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
        }
        binding.imageCalender.setOnClickListener {
            val timepicker = CalenderView(binding.textViewDate)
            timepicker.show(parentFragmentManager, "showDate")
        }
        binding.imageCalenderTime.setOnClickListener {
            val timepicker = TimeView(binding.textViewTime)
            timepicker.show(parentFragmentManager, "showDate")
        }
        binding.buttonCreate.setOnClickListener {
            if (type== Type.EXPENSE && accountBalance <= 0) {
                Toast.makeText(
                    requireContext(),
                    "You don't have money in account",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (TextUtils.isEmpty(binding.edittextAmount.text.toString())) {
                    Toast.makeText(requireContext(), "Please enter the amount", Toast.LENGTH_SHORT)
                        .show()
                } else {


                    var totalBalance = 0
                    if (type == Type.EXPENSE) {
                        totalBalance =
                            accountBalance - binding.edittextAmount.text.toString().toInt()
                    } else {
                        totalBalance =
                            accountBalance + binding.edittextAmount.text.toString().toInt()
                    }

                    if (type == Type.EXPENSE && totalBalance < 0) {
                        Toast.makeText(
                            requireContext(),
                            "Your transaction is more then you have balance in this account ",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val date =
                            DateTimeUtils.getTimeStampFromDate(binding.textViewDate.text.toString())
                        val time =
                            DateTimeUtils.getTimeStampFromTime(binding.textViewTime.text.toString())

                        expenseIncomeViewModel.insert(
                            binding.edittextAmount.text.toString().toInt(),
                            type,
                            accountId,
                            binding.editTextPaidFor.text.toString(),
                            date.toString(),
                            time.toString(),
                            binding.edittextToPay.text.toString()
                        )
                        accountViewModel.updateAccountBalance(
                            totalBalance.toString()
                        )
                    }

                    Toast.makeText(requireContext(), "Transaction added", Toast.LENGTH_SHORT).show()

                    dialog.dismiss()
            }}
        }
        dialog.show()
        binding.textViewDate.text = DateTimeUtils.getDate()
        binding.textViewTime.text = DateTimeUtils.getTime()
    }

}


