package com.aditechnology.moneymanagement.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.Type
import com.aditechnology.moneymanagement.databinding.BottomSheetAccountActionBinding
import com.aditechnology.moneymanagement.databinding.BottomSheetAddDetailBinding
import com.aditechnology.moneymanagement.databinding.FragmentDetailsListBinding
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.ui.adapter.DetailListAdapter
import com.aditechnology.moneymanagement.utils.CalenderView
import com.aditechnology.moneymanagement.utils.DateTimeUtils
import com.aditechnology.moneymanagement.utils.TimeView
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog


class DetailListFragment : Fragment() , DetailListAdapter.OnClickListener {
    private lateinit var mAccountListAdapter: DetailListAdapter
    private val ARG_OBJECT = "account_id"
    private var mTotalBalance = 0
    private var mTotalIncome = 0
    private var mTotalExpense = 0
    private var _binding: FragmentDetailsListBinding? = null
    private val accountViewModel : AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((requireActivity().application as MainApplication).repository)
    }
    private val expenseIncomeViewModel: ExpenseIncomeViewModel by viewModels {
        ExpenseViewModelFactory((requireActivity().application as MainApplication).repository)
    }
    private var accountId = 0
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
            accountId = requireArguments().getInt(ARG_OBJECT, 0)
            mAccountListAdapter = DetailListAdapter(accountId, this@DetailListFragment)
            val linearLayoutManager1 = LinearLayoutManager(context)
            linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
            binding.recycleView.layoutManager =linearLayoutManager1
            binding.recycleView.adapter = mAccountListAdapter
        }
        accountViewModel.getAccountDetailBy(accountId).observe(viewLifecycleOwner){
                list->
            if (list.isNotEmpty()){
                mTotalBalance = list[0].accountBalance.toInt()
                mTotalExpense = list[0].accountExpense.toInt()
                mTotalIncome = list[0].accountIncome.toInt()
            }
            mAccountListAdapter.updateHeader(list)
        }
        expenseIncomeViewModel.getAllDetailsByAccountId(accountId)?.observe(viewLifecycleOwner){
                all->
            mAccountListAdapter.updateList(all.reversed())
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
            binding.textViewWhoToPayTitle.text = requireContext().getString(R.string.pay_to)
            binding.textViewPaidForTitle.text =  requireContext().getString(R.string.pay_for)
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
            binding.textViewWhoToPayTitle.text = requireContext().getString(R.string.get_from)
            binding.textViewPaidForTitle.text = requireContext().getString(R.string.get_for)
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
        binding.editTextPaidFor.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveTransaction(binding, type, accountBalance, accountId)
                dialog.dismiss()
                return@OnEditorActionListener true
            }
            false
        })

        binding.buttonCreate.setOnClickListener {

                saveTransaction(binding, type, accountBalance, accountId)

                    dialog.dismiss()


            }

        dialog.show()
        binding.textViewDate.text = DateTimeUtils.getDate()
        binding.textViewTime.text = DateTimeUtils.getTime()
    }

    private fun saveTransaction(
        binding: BottomSheetAddDetailBinding,
        type: Type,
        accountBalance: Int,
        accountId: Int
    ) {
        if (TextUtils.isEmpty(binding.edittextAmount.text.toString())) {
            Toast.makeText(requireContext(), requireContext().getString(R.string.required_amount), Toast.LENGTH_SHORT)
                .show()
        } else {
            if (type == Type.EXPENSE) {
                mTotalBalance -= binding.edittextAmount.text.toString().toInt()
                mTotalExpense += binding.edittextAmount.text.toString().toInt()
            } else {
                mTotalBalance += binding.edittextAmount.text.toString().toInt()
                mTotalIncome += binding.edittextAmount.text.toString().toInt()
            }
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
                mTotalBalance.toString(), accountId,mTotalExpense,mTotalIncome
            )
            Toast.makeText(requireContext(), "Transaction added", Toast.LENGTH_SHORT)
                .show()
        }
    }
    override fun openActionOnTransactionBottomSheet(accountBalance: Int,item: DetailsFileTable) {
        var mAccountBalance = accountBalance
        val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomSheetAccountActionBinding.inflate(inflater, null, false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)
        dialog.show()

        binding.textViewRemoveAccount.text = requireContext().getString(R.string.remove_transaction)
        binding.imageViewUpdateAccount.visibility =View.GONE
        binding.textViewUpdateAccount.visibility = View.GONE
        binding.textViewRemoveAccount.setOnClickListener {
            expenseIncomeViewModel.removeTransaction(item.id)
             if (item.type==1){
                // expense
                 mAccountBalance += item.money
                 mTotalExpense -= item.money
             }else{
                 //income
                 mAccountBalance -= item.money
                 mTotalIncome -= item.money
             }

            accountViewModel.updateAccountBalance(
                mAccountBalance.toString(), accountId,mTotalExpense,mTotalIncome)
            dialog.dismiss()
        } 
        dialog.show()
    }

}


