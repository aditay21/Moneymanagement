package com.aditechnology.moneymanagement.ui.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.FragmentAccountsBinding
import com.aditechnology.moneymanagement.ui.adapter.AccountListAdapter
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel


class AccountFragment : Fragment() {
    lateinit var mAccountListAdapter : AccountListAdapter
    private var _binding: FragmentAccountsBinding? = null
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
        _binding = FragmentAccountsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.lifecycleOwner = this
        binding.account = accountViewModel

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       /* binding.imageviewCreate.setOnClickListener {
            findNavController().navigate(R.id.action_account_to_createfragment)
        }*/

        mAccountListAdapter = AccountListAdapter()
        var  linearLayoutManager1 = LinearLayoutManager(context)
        linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
        binding.recycleView.layoutManager =linearLayoutManager1
        binding.recycleView.adapter= mAccountListAdapter

        accountViewModel.mAllDetails.observe(requireActivity(), Observer { account ->
            if (account.isEmpty()){
              //  Log.e("TAG","Account is empty")
             //   accountViewModel.insertAccountDetail("Personal",0)
            }else{
                account?.let {
                    mAccountListAdapter.updateList(account)
                }
                Log.e("TAG",""+account.size)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}