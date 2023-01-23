package com.aditechnology.moneymanagement.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.databinding.FragmentHomeBinding
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private lateinit var  homeFragmentAdapter: HomeFragmentAdapter
    private var _binding: FragmentHomeBinding? = null
    private val accountViewModel : AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((requireActivity().application as MainApplication).repository)
    }
    private val mAccountList:ArrayList<AccountTable> = ArrayList()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragmentAdapter = HomeFragmentAdapter(this,mAccountList)
        _binding?.pager?.adapter = homeFragmentAdapter
        accountViewModel.mAllDetails.observe(requireActivity()) { account ->
            if (account.isEmpty()){
                accountViewModel.insertAccountDetail("Personal",0)
            }else {
                mAccountList.clear()
                mAccountList.addAll(account)
                _binding?.let {
                    TabLayoutMediator(it.tabLayout, it.pager) { tab, position ->
                        tab.text = mAccountList[position].accountName
                    }.attach()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}