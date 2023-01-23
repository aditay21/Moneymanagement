package com.aditechnology.moneymanagement.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.models.DetailsFileTable

class DetailListAdapter  : RecyclerView.Adapter<DetailListAdapter.DetailListViewHolder>() {
    private val NORMAL = 1
    private var  adapterList: ArrayList<DetailsFileTable> = ArrayList()

    class DetailListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accountName : TextView = itemView.findViewById(R.id.text_view_account_name_value)
        val accountBalance : TextView = itemView.findViewById(R.id.text_view_account_balance_value)
        val date : TextView = itemView.findViewById(R.id.text_view_account_created_date_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DetailListViewHolder(layoutInflater.inflate(R.layout.view_holder_account_view,parent,false))
    }


    override fun onBindViewHolder(holder:DetailListViewHolder, position: Int) {
       /* holder.accountName.text = adapterList?.get(position-1)?.accountName
            holder.accountBalance.text = adapterList?.get(position-1)?.accountBalance.toString()*/
    }

    override fun getItemViewType(position: Int): Int {
        return  NORMAL
    }
    override fun getItemCount(): Int {
        return adapterList.size +1
    }
    fun updateList(list: List<DetailsFileTable>){
        adapterList.clear()
        adapterList.addAll(list)
        notifyDataSetChanged()
    }
}