package com.aditechnology.moneymanagement.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.BottomSheetAccountActionBinding
import com.aditechnology.moneymanagement.databinding.BottomsheetCustomRangeBinding
import com.aditechnology.moneymanagement.databinding.FragmentExpenseIncomeFragmentBinding
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.ui.adapter.ExpenseIncomeDetailListAdapter
import com.aditechnology.moneymanagement.utils.CalenderView
import com.aditechnology.moneymanagement.utils.DateTimeUtils
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog


class ExpenseHomePagerFragment :Fragment(),ExpenseIncomeDetailListAdapter.OnClickListener{
    var mTotalBalance = 0
    var mTotalExpense = 0
    var mTotalIncome = 0
    var mSearchFilter =0
    private lateinit var mAccountListAdapter: ExpenseIncomeDetailListAdapter
    private val expenseIncomeViewModel: ExpenseIncomeViewModel by viewModels {
        ExpenseViewModelFactory((requireActivity().application as MainApplication).repository)
    }

    private val accountViewModel : AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((requireActivity().application as MainApplication).repository)
    }

    private var _binding: FragmentExpenseIncomeFragmentBinding? = null
    private val binding get() = _binding!!
    private var accountId =0
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
            accountId = requireArguments().getInt("accountid", 0)
        }
        binding.imageViewFilterApplied.visibility   = View.GONE
        accountViewModel.getAccountDetailBy(accountId).observe(viewLifecycleOwner){ list->
            if (list.isNotEmpty()) {
                mTotalBalance = list[0].accountBalance.toInt()
                mTotalExpense = list[0].accountExpense.toInt()
                mTotalIncome = list[0].accountIncome.toInt()
                 setHeader()
                /*if (binding != null) {
                    binding.textViewTotalBalance.text = "Account balance : $mTotalBalance"
                    binding.textViewTotalExpense.text = "Account Expense : $mTotalExpense"
                    binding.textViewTotalIncome.text = "Account Income   : $mTotalIncome"
                }*/

            }
        }
        binding.textViewCurrentDateYearSelection.text = DateTimeUtils.getDate()
        setObservers(DateTimeUtils.getTimeStampFromDate(DateTimeUtils.getDate()).toString())

        binding.imageViewPrevious?.setOnClickListener {
            if (mSearchFilter != 3) {
                val currentDateSearch = binding.textViewCurrentDateYearSelection.text.toString()
                var searchDateTime = ""
                var searchDate = ""
                if (mSearchFilter == 0) {
                    searchDate = DateTimeUtils.getCalculatedPreviousDayDateTimeStamp(
                        DateTimeUtils.getTimeStampFromDate(currentDateSearch).toString()
                    )
                    searchDateTime = DateTimeUtils.getDateFromTimeStamp(searchDate)
                    binding.textViewCurrentDateYearSelection.text = searchDateTime
                    if (searchDate != null) {
                        setObservers(searchDate)
                    }
                } else if (mSearchFilter == 1) {
                    searchDate =
                        DateTimeUtils.getPreviousMonth(binding.textViewCurrentDateYearSelection.text.toString())
                    binding.textViewCurrentDateYearSelection.text = searchDate
                    val monthStartDate: String =
                        DateTimeUtils.getTimestampOfStartDateOfMonth(searchDate)
                    val monthEndDate: String =
                        DateTimeUtils.getTimestampOfEndDateOfMonth(searchDate)
                    setObserversMonthlyWise(monthStartDate, monthEndDate)

                } else if (mSearchFilter == 2) {
                    searchDate =
                        DateTimeUtils.getPreviousYear(binding.textViewCurrentDateYearSelection.text.toString())
                    binding.textViewCurrentDateYearSelection.text = searchDate
                    val monthStartDate: String =
                        DateTimeUtils.getTimestampOfStartDateOfYear(searchDate)
                    val monthEndDate: String = DateTimeUtils.getTimestampOfEndDateOfYear(searchDate)
                    setObserversMonthlyWise(monthStartDate, monthEndDate)

                }

            }else{
                Toast.makeText(requireContext(),
                    "Button Disabled for Custom Search",
                    Toast.LENGTH_SHORT).show()
            }
        }
        binding.imageViewFiltter.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.imageViewFiltter)
            popupMenu.menuInflater.inflate(R.menu.popup_menu_custom_search, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.title){
                    "Clear Filter"->{
                        binding.imageViewFilterApplied.visibility = View.GONE
                        mSearchFilter =0
                        binding.textViewCurrentDateYearSelection.text =DateTimeUtils.getDate()
                        setObservers(DateTimeUtils.getTimeStampFromDate(DateTimeUtils.getDate()).toString())
                    }
                    "Monthly"->{
                        mSearchFilter=1
                        binding.imageViewFilterApplied.visibility = View.VISIBLE
                        binding.textViewCurrentDateYearSelection.text = DateTimeUtils.getCurrentMonth()
                        val monthStartDate :String = DateTimeUtils.getTimestampOfStartDateOfMonth(DateTimeUtils.getCurrentMonth())
                        val monthEndDate :String = DateTimeUtils.getTimestampOfEndDateOfMonth(DateTimeUtils.getCurrentMonth())
                        setObserversMonthlyWise(monthStartDate,monthEndDate)
                    }

                    "Yearly"->{
                        binding.imageViewFilterApplied.visibility = View.VISIBLE
                        mSearchFilter=2
                        binding.textViewCurrentDateYearSelection.text = DateTimeUtils.getCurrentYear()
                        val monthStartDate :String = DateTimeUtils.getTimestampOfStartDateOfYear(DateTimeUtils.getCurrentYear())
                        val monthEndDate :String = DateTimeUtils.getTimestampOfEndDateOfYear(DateTimeUtils.getCurrentYear())
                        setObserversMonthlyWise(monthStartDate,monthEndDate)

                    }
                    "Custom"->{
                        mSearchFilter=3
                        openCustomRangeBottomSheet()
                    }
                }
                true
            }
        }
        binding.imageViewNext.setOnClickListener {
            if (mSearchFilter != 3) {
                val currentDateSearch = binding.textViewCurrentDateYearSelection.text.toString()
                val searchDate: String
                if (mSearchFilter == 0) {
                    searchDate = DateTimeUtils.getCalculatedNextDayDateTimeStamp(
                        DateTimeUtils.getTimeStampFromDate(currentDateSearch).toString()
                    )
                    val searchDateTime = DateTimeUtils.getDateFromTimeStamp(searchDate)
                    binding.textViewCurrentDateYearSelection.text = searchDateTime
                    setObservers(searchDate)
                } else if (mSearchFilter == 1) {
                    searchDate = DateTimeUtils.getNextMonth(currentDateSearch)
                    binding.textViewCurrentDateYearSelection.text = searchDate
                    val monthStartDate: String =
                        DateTimeUtils.getTimestampOfStartDateOfMonth(searchDate)
                    val monthEndDate: String =
                        DateTimeUtils.getTimestampOfEndDateOfMonth(searchDate)
                    setObserversMonthlyWise(monthStartDate, monthEndDate)
                } else if (mSearchFilter == 2) {
                    searchDate = DateTimeUtils.getNextYear(currentDateSearch)
                    binding.textViewCurrentDateYearSelection.text = searchDate
                    val monthStartDate: String =
                        DateTimeUtils.getTimestampOfStartDateOfYear(searchDate)
                    val monthEndDate: String = DateTimeUtils.getTimestampOfEndDateOfYear(searchDate)
                    setObserversMonthlyWise(monthStartDate, monthEndDate)

                }
            }else{
                Toast.makeText(requireContext(),
                    "Button Disabled for Custom Search",
                    Toast.LENGTH_SHORT).show()
            }
        }
        mAccountListAdapter = ExpenseIncomeDetailListAdapter( this)
        val linearLayoutManager1 = LinearLayoutManager(context)
        linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
        binding.recycleView.layoutManager = linearLayoutManager1
        binding.recycleView.adapter = mAccountListAdapter
    }

    private fun setHeader() {

        binding.textViewTotalBalance?.text = "Account balance : $mTotalBalance"
        binding.textViewTotalExpense?.text = "Account Expense : $mTotalExpense"
        binding.textViewTotalIncome?.text = "Account Income   : $mTotalIncome"
    }

    private fun setObserversMonthlyWise(monthStartDate: String, monthEndDate: String) {
        expenseIncomeViewModel.getDetailsByAccountIdAndBYRANGE(accountId,monthStartDate, monthEndDate)
                ?.observe(viewLifecycleOwner) { all ->
                    mAccountListAdapter.updateList(all.reversed())

                }

    }

    override fun onStop() {
        super.onStop()
        //accountViewModel.th().removeObservers(this);
    }
    private fun setObservers(searchDate: String?) {
        if (searchDate != null) {
            expenseIncomeViewModel.getByAccountIdAndDate(accountId, searchDate)
                ?.observe(viewLifecycleOwner) { all ->
                 if (mSearchFilter ==0) {
                     mAccountListAdapter.updateList(all.reversed())
                 }
                }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun openActionOnTransactionBottomSheet(item: DetailsFileTable) {

        val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomSheetAccountActionBinding.inflate(inflater, null, false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)
        dialog.show()

        binding.textViewRemoveAccount.text = "Remove Transaction"
        binding.imageViewUpdateAccount.visibility =View.GONE
        binding.textViewUpdateAccount.visibility = View.GONE
        binding.textViewRemoveAccount.setOnClickListener {
            expenseIncomeViewModel.removeTransaction(item.id)
            if (item.type==1){
                // expense
                mTotalBalance += item.money
                mTotalExpense -= item.money
            }else{
                //income
                mTotalBalance -= item.money
                mTotalIncome  -=item.money
            }

            accountViewModel.updateAccountBalance(
                mTotalBalance.toString(), accountId,mTotalExpense,mTotalIncome)
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

private fun  openCustomRangeBottomSheet(){
    val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)
    val inflater = LayoutInflater.from(requireContext())
    val binding = BottomsheetCustomRangeBinding.inflate(inflater, null, false)
    binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
    dialog.setCancelable(true)

    binding.buttonSearch.setOnClickListener {
       val startDate =  DateTimeUtils.getTimeStampFromDate(binding.edittextStartDate.text.toString()).toString()
       val endDate =  DateTimeUtils.getTimeStampFromDate(binding.edittextEndDate.text.toString()).toString()
        setObserversMonthlyWise(startDate,endDate)
        _binding?.imageViewFilterApplied?.visibility   = View.VISIBLE
        dialog.dismiss()
    }
    binding.edittextStartDate.setOnClickListener {
        val timepicker = CalenderView(binding.edittextStartDate)
        timepicker.show(parentFragmentManager, "showDate")
    }

    binding.edittextEndDate.setOnClickListener {
        val timepicker = CalenderView(binding.edittextEndDate)
        timepicker.show(parentFragmentManager, "showDate")
    }



    dialog.setContentView(binding.root)
    dialog.show()

}



}