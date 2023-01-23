package com.aditechnology.moneymanagement.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.Type
import com.aditechnology.moneymanagement.databinding.FragmentDetailsListBinding
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.WordViewModelFactory

class DetailListFragment : Fragment(){
    private  lateinit var mAccountListAdapter:DetailListAdapter
    private  val ARG_OBJECT = "accountid"
    private var _binding: FragmentDetailsListBinding? = null
    private val wordViewModel: ExpenseIncomeViewModel by viewModels {
        WordViewModelFactory((requireActivity().application as MainApplication).repository)
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
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {


         mAccountListAdapter = DetailListAdapter()
        val linearLayoutManager1 = LinearLayoutManager(context)
        linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
        binding.recycleView.layoutManager =linearLayoutManager1
        binding.recycleView.adapter= mAccountListAdapter
            wordViewModel.getByAccountId(id)?.let {
                mAccountListAdapter.updateList(it)
            }
        }

       // wordViewModel.insert(134,Type.EXPENSE,id)

        wordViewModel.mAllDetails.observe(requireActivity()) { words ->
            Log.e("TAG", "Details Size " + words.size)
            wordViewModel.getByAccountId(id)?.let {
                mAccountListAdapter.updateList(it)
            }
        }
    }

}


