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
import com.aditechnology.moneymanagement.databinding.FragmentExpenseIncomeFragmentBinding
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.ui.adapter.ExpenseIncomeDetailListAdapter
import com.aditechnology.moneymanagement.utils.DateTimeUtils
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseViewModelFactory


class ExpenseHomePagerFragment :Fragment(),ExpenseIncomeDetailListAdapter.OnClickListener{
    var mSearchFilter =0
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
        setObservers(DateTimeUtils.getTimeStampFromDate(DateTimeUtils.getDate()).toString())

        binding.imageViewPrevious.setOnClickListener {
            val currentDateSearch =  binding.textViewCurrentDateYearSelection.text.toString()
            var   searchDateTime =""
            var searchDate=""
            if (mSearchFilter ==0) {
                searchDate = DateTimeUtils.getCalculatedPreviousDayDateTimeStamp(
                    DateTimeUtils.getTimeStampFromDate(currentDateSearch).toString())
                 searchDateTime = DateTimeUtils.getDateFromTimeStamp(searchDate)
                binding.textViewCurrentDateYearSelection.text=searchDateTime
                if (searchDate != null) {
                    setObservers(searchDate)
                }
            }else if (mSearchFilter==1){
                searchDate = DateTimeUtils.getPreviousMonth(binding.textViewCurrentDateYearSelection.text.toString())
                binding.textViewCurrentDateYearSelection.text=searchDate
            }
            else if (mSearchFilter==2){
                searchDate = DateTimeUtils.getPreviousYear(binding.textViewCurrentDateYearSelection.text.toString())
                binding.textViewCurrentDateYearSelection.text=searchDate
            }


        }
        binding.imageViewFiltter.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.imageViewFiltter)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
         /*   Toast.makeText(
                requireContext(),
                "You Clicked " + menuItem.title,
                Toast.LENGTH_SHORT
            ).show()*/
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.title){
                    "clear"->{
                        mSearchFilter =0
                        setObservers(DateTimeUtils.getDate())
                    }
                    "Monthly"->{
                        mSearchFilter=1
                        binding.textViewCurrentDateYearSelection.text = DateTimeUtils.getCurrentMonth()
                        setObservers(DateTimeUtils.getDate())
                    }

                    "yearly"->{
                        mSearchFilter=2
                        binding.textViewCurrentDateYearSelection.text = DateTimeUtils.getCurrentYear()
                        setObservers(DateTimeUtils.getDate())

                    }
                    "custom"->{
                        mSearchFilter=3

                    }
                }

                true



            }
        }
        binding.imageViewNext.setOnClickListener {
            val currentDateSearch =  binding.textViewCurrentDateYearSelection.text.toString()
            var searchDate=""
            if (mSearchFilter ==0) {
                 searchDate = DateTimeUtils.getCalculatedNextDayDateTimeStamp(
                    DateTimeUtils.getTimeStampFromDate(currentDateSearch).toString()
                )
                val searchDateTime = DateTimeUtils.getDateFromTimeStamp(searchDate.toString())
                binding.textViewCurrentDateYearSelection.text = searchDateTime
                if (searchDate != null) {
                    setObservers(searchDate)
                }
            }else if (mSearchFilter==1){
              //  val getDateFromText = DateTimeUtils.getCurrentMonthFromMonthSelecetd(currentDateSearch)
                searchDate = DateTimeUtils.getNextMonth(currentDateSearch)
                binding.textViewCurrentDateYearSelection.text=searchDate
            }
            else if (mSearchFilter==2){
                //  val getDateFromText = DateTimeUtils.getCurrentMonthFromMonthSelecetd(currentDateSearch)
                searchDate = DateTimeUtils.getNextYear(currentDateSearch)
                binding.textViewCurrentDateYearSelection.text=searchDate
            }

        }

        mAccountListAdapter = ExpenseIncomeDetailListAdapter( this)
        var linearLayoutManager1 = LinearLayoutManager(context)
        linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
        binding.recycleView.layoutManager = linearLayoutManager1
        binding.recycleView.adapter = mAccountListAdapter
    }

    private fun setObservers(searchDate: String?) {
        if (searchDate != null) {
            expenseIncomeViewModel.getByAccountIdAndDate(accountId, searchDate)
                ?.observe(requireActivity()) { all ->
                    mAccountListAdapter.updateList(all.reversed())

                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun openActionOnTransactionBottomSheet(item: DetailsFileTable) {

    }
}