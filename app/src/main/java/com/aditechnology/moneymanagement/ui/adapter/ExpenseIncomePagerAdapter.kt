package com.aditechnology.moneymanagement.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aditechnology.moneymanagement.ui.home.ExpenseIncomeDetailFragment

class ExpenseIncomePager(fragment: Fragment,val accountId :Int ) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = ExpenseIncomeDetailFragment()
           fragment.arguments = Bundle().apply {
               putInt(ARG_OBJECT, position)
               putInt(ARG_OBJECT2, accountId)
           }
           return fragment
    }
}
private const val ARG_OBJECT = "position"
private const val ARG_OBJECT2 = "accountid"

