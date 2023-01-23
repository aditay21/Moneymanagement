package com.aditechnology.moneymanagement.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.models.AccountTable

class HomeFragmentAdapter(fragment: Fragment, private val mVisibleList: ArrayList<AccountTable>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = mVisibleList.size


    fun updateList(visibleList: ArrayList<AccountTable>){
        mVisibleList.clear()
        mVisibleList.addAll(visibleList)
    }

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
       if (position==0) {
           val fragment = AllListFragment()
           fragment.arguments = Bundle().apply {
               // Our object is just an integer :-P
               putInt(ARG_OBJECT, mVisibleList[position].accountId)
           }
           return fragment
       }else{
           val fragment = DetailListFragment()
           fragment.arguments = Bundle().apply {
               // Our object is just an integer :-P
               putInt(ARG_OBJECT, mVisibleList[position].accountId)

           }
           return fragment
       }
    }

}

private const val ARG_OBJECT = "accountid"

