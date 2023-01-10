package com.aditechnology.moneymanagement.ui.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.databinding.FragmentDashboardBinding
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel

class AccountFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
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

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel.mAllDetails.observe(requireActivity(), Observer { account ->
            if (account.isEmpty()){
                Log.e("TAG","Account is empty")
                accountViewModel.insertAccountDetail(12,"Personal")
            }else{
                Log.e("TAG",""+account.size)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}