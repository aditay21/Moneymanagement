package com.aditechnology.moneymanagement.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.FragmentHomeBinding
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.ui.home.HomeFragment.*
import com.aditechnology.moneymanagement.utils.Utils
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment :Fragment(){

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

        val adRequest = AdRequest.Builder().build()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.adView.loadAd(adRequest)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragmentAdapter = HomeFragmentAdapter(this,mAccountList)
        _binding?.pager?.adapter = homeFragmentAdapter

        accountViewModel.mAllDetails.observe(viewLifecycleOwner) { account ->
            if (account.isEmpty()){
                Log.e("TAG","Insert Home")
                accountViewModel.insertAccountDetail(Utils.ALL, 0)
                accountViewModel.insertAccountDetail(Utils.Personal, 0)

            }else {
                mAccountList.clear()
                mAccountList.addAll(account)
                homeFragmentAdapter.notifyDataSetChanged()
                _binding?.let {
                    TabLayoutMediator(it.tabLayout, it.pager) { tab, position ->
                        tab.text = mAccountList[position].accountName
                    }.attach()
                }
            }
        }
    }

    private fun createDummyAccounts(name :String) {
        Log.e("TAG","65 $name")
        accountViewModel.getAccountDetailName(name)
                    .observe(viewLifecycleOwner) { list ->
                        if (list.isEmpty()) {
                            Log.e("TAG","list is empty $name")
                            accountViewModel.insertAccountDetail(name, 0)
                        }
                    }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}