package com.aditechnology.moneymanagement.ui.account

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.databinding.FragmentAccountsBinding
import com.aditechnology.moneymanagement.databinding.FragmentCreateAccountBinding
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel

class CreateAccountFragment : Fragment() {

    private var _binding: FragmentCreateAccountBinding? = null
    private val accountViewModel : AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((requireActivity().application as MainApplication).repository)
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
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewModel.mAllDetails.observe(requireActivity(), Observer { account ->

                Log.e("TAG",""+account.size)

        })
        binding.buttonCreate.setOnClickListener {

            when {
                TextUtils.isEmpty(binding.edittextAccountName.text) -> {
                    binding.edittextAccountName.error = "Please fill account name"
                }
                TextUtils.isEmpty(binding.edittextStartingBalance.text) -> {
                    binding.edittextStartingBalance.error = "Please fill account name"
                }
                else -> {
                    accountViewModel.insertAccountDetail(
                        binding.edittextAccountName.text.toString(),
                        binding.edittextStartingBalance.text.toString().toLong()
                    )
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