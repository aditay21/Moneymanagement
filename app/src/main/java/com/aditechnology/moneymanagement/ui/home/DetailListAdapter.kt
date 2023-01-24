package com.aditechnology.moneymanagement.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.ViewHolderDetailSummeryBinding
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.models.DetailsFileTable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailListAdapter(val accountId: Int,val onClickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
     private val HEADER = 0
     private val NORMAL = 1
    private var  adapterList: ArrayList<DetailsFileTable> = ArrayList()

    class DetailListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class HeaderViewHolder(val binding: ViewHolderDetailSummeryBinding) : RecyclerView.ViewHolder(binding.root) {
     //   val accountCount :TextView= itemView.findViewById(R.id.textview_total_count)
       // val createAccount :ImageView= itemView.findViewById(R.id.imageview_create)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 0) {
            val binding = ViewHolderDetailSummeryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return HeaderViewHolder(binding)
        }
        val layoutInflater = LayoutInflater.from(parent.context)
        return DetailListViewHolder(
            layoutInflater.inflate(
                R.layout.view_holder_account_view,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == HEADER){
            (holder as HeaderViewHolder).binding.buttonAdd.setOnClickListener{
                onClickListener.openBottomSheet(accountId)

            }
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
    interface OnClickListener{
        fun openBottomSheet(accountId : Int)
    }
}