package com.aditechnology.moneymanagement.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.BottomSheetAccountActionBinding
import com.aditechnology.moneymanagement.databinding.BottomsheetRemoveWarningBinding
import com.aditechnology.moneymanagement.databinding.BottomsheetUpdateAccountBinding
import com.aditechnology.moneymanagement.databinding.FragmentDetailsListBinding
import com.aditechnology.moneymanagement.ui.adapter.AllAccountListAdapter
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class AllListFragment : Fragment(), AllAccountListAdapter.OnClickListener {
    private  lateinit var mAccountListAdapter:AllAccountListAdapter
    private var _binding: FragmentDetailsListBinding? = null
    private val accountViewModel : AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((requireActivity().application as MainApplication).repository)
    }
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

        mAccountListAdapter = AllAccountListAdapter(this@AllListFragment)
        val linearLayoutManager1 = LinearLayoutManager(context)
        linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
        binding.recycleView.layoutManager =linearLayoutManager1
        binding.recycleView.adapter= mAccountListAdapter

        accountViewModel.mAllDetails.observe(requireActivity()) { account ->
            account?.let {
                var accountBalance = 0L
                for (item in account) {
                    accountBalance += item.accountBalance

                }
                mAccountListAdapter.updateList(account, accountBalance)
            }
        }
    }

    override fun openBottomSheet(accountId: Int):Boolean {

        val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomSheetAccountActionBinding.inflate(inflater, null, false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)
        dialog.show()
        binding.textViewUpdateAccount.setOnClickListener {
            updateTheAccountName(accountId)
            dialog.dismiss()
        }
        binding.textViewRemoveAccount.setOnClickListener {
             alertWarningBottomSheet(accountId)
            dialog.dismiss()
        }
         dialog.show()
        return true
    }

    private fun alertWarningBottomSheet(accountId: Int) {
        val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomsheetRemoveWarningBinding.inflate(inflater, null, false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)
        binding.buttonCancel.setOnClickListener{
            dialog.dismiss()
        }
        binding.buttonRemove.setOnClickListener{
             accountViewModel.removeAccountById(accountId)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun updateTheAccountName(accountId: Int) {
        val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomsheetUpdateAccountBinding.inflate(inflater, null, false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)

        binding.buttonCreate.setOnClickListener {
            if (binding.edittextUpdateName.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please enter account name", Toast.LENGTH_SHORT)
                    .show()
            }else{
                accountViewModel.updateAccountName(accountId,binding.edittextUpdateName.text.toString())
                Toast.makeText(requireContext(),"Account Name Updated",Toast.LENGTH_SHORT).show()
              dialog.dismiss()
            }
        }
        dialog.show()
    }
}


