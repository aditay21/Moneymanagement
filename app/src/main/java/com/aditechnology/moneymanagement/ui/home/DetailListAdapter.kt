package com.aditechnology.moneymanagement.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.models.DetailsFileTable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
     private val HEADER = 0
     private val NORMAL = 1
    private var  adapterList: ArrayList<DetailsFileTable> = ArrayList()

    class DetailListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     //   val accountCount :TextView= itemView.findViewById(R.id.textview_total_count)
       // val createAccount :ImageView= itemView.findViewById(R.id.imageview_create)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
      if (viewType==0){
           return HeaderViewHolder(layoutInflater.inflate(R.layout.view_holder_detail_summery,parent,false))
      }
        return DetailListViewHolder(layoutInflater.inflate(R.layout.view_holder_account_view,parent,false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == HEADER){
           /* (holder as HeaderViewHolder).accountCount.text= (adapterList.size).toString();
            holder.createAccount.setOnClickListener{
                    view ->
                view.findNavController().navigate(R.id.action_account_to_createfragment)
            }*/
        }else {
            //(holder as DetailListViewHolder).accountName.text = adapterList?.get(position-1)?.accountName
         //   holder.accountBalance.text = adapterList?.get(position-1)?.accountBalance.toString()
           // holder.date.text = adapterList?.get(position-1)?.let { getDateTime(it.date) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position==0){
        return HEADER
        }
       return  NORMAL
    }
    override fun getItemCount(): Int {
       return adapterList?.size!!+1
    }
    fun updateList(list: List<DetailsFileTable>){
        adapterList.clear()
        adapterList.addAll(list)
        notifyDataSetChanged()
    }
}