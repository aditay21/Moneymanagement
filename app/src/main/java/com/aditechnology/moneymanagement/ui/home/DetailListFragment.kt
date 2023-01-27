package com.aditechnology.moneymanagement.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.aditechnology.moneymanagement.utils.TimeView
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

class DetailListFragment : Fragment() , DetailListAdapter.OnClickListener {
    private lateinit var mAccountListAdapter: DetailListAdapter
    private val ARG_OBJECT = "accountid"
    private var _binding: FragmentDetailsListBinding? = null
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
            val linearLayoutManager1 = LinearLayoutManager(context)
            linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
            binding.recycleView.layoutManager = linearLayoutManager1
            binding.recycleView.adapter = mAccountListAdapter
          /*  expenseIncomeViewModel.getByAccountId(accountId)?.let {
                mAccountListAdapter.updateList(it)
            }*/
        }
        expenseIncomeViewModel.mAllDetails.observe(requireActivity()) { all ->
            Log.e("TAG","All Size  ${all.size}")
            expenseIncomeViewModel.getByAccountId(accountId)?.let {
              Log.e("TAG","Size  ${it.size}")
                mAccountListAdapter.updateList(it)

            }
        }
    }

    override fun openBottomSheet(accountId: Int) {
        val type: Type = Type.EXPENSE
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
            if (TextUtils.isEmpty(binding.edittextAmount.text.toString())) {
                Toast.makeText(requireContext(),"Please enter the amount",Toast.LENGTH_SHORT).show()
            } else {
           expenseIncomeViewModel.insert(binding.edittextAmount.text.toString().toInt(),
               type,accountId,binding.edittextToPay.text.toString(),binding.textViewDate.text.toString(),binding.textViewTime.text.toString()
                   ,binding.editTextPaidFor.toString())
            }
        }
        dialog.show()
        binding.textViewDate.text = getDate()
        binding.textViewTime.text = getTime()
    }

    private fun getDate(): String {
        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE MMM dd yyyy")
        return   dateFormat.format(cal.time)
    }

    private fun getTime(): String {
        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("hh:mm a")
        return   dateFormat.format(cal.time)
    }

}


