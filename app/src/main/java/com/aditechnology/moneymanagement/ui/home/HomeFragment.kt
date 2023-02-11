package com.aditechnology.moneymanagement.ui.home

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.FragmentHomeBinding
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.utils.Utils
import com.aditechnology.moneymanagement.utils.Utils.Companion.IS_PIN_CHECK
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.google.android.gms.ads.AdRequest
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


        navigateToLock()

        homeFragmentAdapter = HomeFragmentAdapter(this,mAccountList)
        _binding?.pager?.adapter = homeFragmentAdapter

        accountViewModel.mAllDetails.observe(viewLifecycleOwner) { account ->
            if (account.isEmpty()){
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

    private fun navigateToLock() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        if (!IS_PIN_CHECK &&!TextUtils.isEmpty(sharedPref?.getString(Utils.SET_PIN, ""))) {
            val bundle = Bundle()
            bundle.putBoolean(Utils.PIN_FOR_UNLOCK, true)
            findNavController().navigate(R.id.action_setting_to_pin, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}