package com.aditechnology.moneymanagement.ui.account

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.Type
import com.aditechnology.moneymanagement.databinding.FragmentCreateAccountBinding
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseViewModelFactory
import com.google.android.gms.ads.AdRequest

class CreateAccountFragment : Fragment() {

    private var _binding: FragmentCreateAccountBinding? = null
    private val accountViewModel : AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((requireActivity().application as MainApplication).repository)
    }
    private val expenseIncomeViewModel: ExpenseIncomeViewModel by viewModels {
        ExpenseViewModelFactory((requireActivity().application as MainApplication).repository)
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewModel.mAllDetails.observe(viewLifecycleOwner, Observer { account ->
        })
        binding.buttonCreate.setOnClickListener {

            when {
                TextUtils.isEmpty(binding.edittextAccountName.text) -> {
                    binding.edittextAccountName.error = "Please fill account name"
                }
                else -> {

                    var balance =0
                   if (TextUtils.isEmpty(binding.edittextStartingBalance.text)){
                       balance =0
                    }else{
                       balance = binding.edittextStartingBalance.text.toString().toInt()
                   }
                    accountViewModel.insertAccountDetail(
                        binding.edittextAccountName.text.toString(),
                        balance.toLong()
                    )
                  /*  expenseIncomeViewModel.insert(
                        binding.edittextStartingBalance.text.toString().toInt(),
                        Type.INCOME,
                        accountId = 0,
                        binding.editTextPaidFor.text.toString(),
                        date.toString(),
                        time.toString(),
                        binding.edittextToPay.text.toString(),
                        mAccountName
                    )*/
                    binding.edittextAccountName.setText("")
                    binding.edittextStartingBalance.setText("")
                    Toast.makeText(requireContext(),"Account Createtd",Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}