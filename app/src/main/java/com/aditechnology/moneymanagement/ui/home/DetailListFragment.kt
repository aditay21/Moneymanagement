package com.aditechnology.moneymanagement.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.BottomSheetAddDetailBinding
import com.aditechnology.moneymanagement.databinding.FragmentDetailsListBinding
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.WordViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog

class DetailListFragment : Fragment() , DetailListAdapter.OnClickListener {
    private  lateinit var mAccountListAdapter:DetailListAdapter
    private  val ARG_OBJECT = "accountid"
    private var _binding: FragmentDetailsListBinding? = null
    private val wordViewModel: ExpenseIncomeViewModel by viewModels {
        WordViewModelFactory((requireActivity().application as MainApplication).repository)
    }
    private var accountId =0;
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
            accountId  = requireArguments().getInt(ARG_OBJECT, 0);
         mAccountListAdapter = DetailListAdapter(accountId,this@DetailListFragment)
              val linearLayoutManager1 = LinearLayoutManager(context)
        linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
        binding.recycleView.layoutManager =linearLayoutManager1
        binding.recycleView.adapter= mAccountListAdapter
            wordViewModel.getByAccountId(accountId)?.let {
                mAccountListAdapter.updateList(it)
            }
        }
        wordViewModel.mAllDetails.observe(requireActivity()) { words ->
            wordViewModel.getByAccountId(accountId)?.let {
                mAccountListAdapter.updateList(it)
            }
        }
    }

    override fun openBottomSheet(accountId: Int) {
        //val dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)
        val dialog = BottomSheetDialog(requireContext(),R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(requireContext())
        val binding= BottomSheetAddDetailBinding.inflate(inflater,null,false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)

        binding.textViewExpense.setBackgroundResource(R.drawable.button_bg_black)
        binding.textViewExpense.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.textViewExpense.setOnClickListener {
            binding.textViewExpense.setBackgroundResource(R.drawable.button_bg_black)
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
            binding.textViewIncome.setBackgroundResource(R.drawable.button_bg_black)
            binding.textViewIncome.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
        }




        dialog.show()


    }

}


