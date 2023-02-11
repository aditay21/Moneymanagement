package com.aditechnology.moneymanagement.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.ViewHolderDetailSummeryBinding
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AllAccountListAdapter(val onclickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
     private val HEADER = 0
     private val NORMAL = 1
    private var mAccountBalance:Long =0L
    private var  adapterList: ArrayList<AccountTable> = ArrayList()

    class AccountListViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
          val accountName :TextView= itemView.findViewById(R.id.text_view_account_name_value)
          val accountBalance :TextView= itemView.findViewById(R.id.text_view_account_balance_value)
          val date :TextView= itemView.findViewById(R.id.text_view_account_created_date_value)



    }

    class HeaderViewHolder(val binding: ViewHolderDetailSummeryBinding) : RecyclerView.ViewHolder(binding.root) {
         val buttonAdd :TextView= itemView.findViewById(R.id.button_add)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
      if (viewType==0){
          val binding = ViewHolderDetailSummeryBinding.inflate(
              LayoutInflater.from(parent.context),
              parent,
              false
          )
          return HeaderViewHolder(binding)
      }
        return AccountListViewHolder(layoutInflater.inflate(R.layout.view_holder_account_view,parent,false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (getItemViewType(position) == HEADER) {
            (holder as HeaderViewHolder).binding.buttonAdd.setOnClickListener { view ->
                view.findNavController().navigate(R.id.action_account_to_createfragment)
            }
            holder.binding.textViewTitle.text = "All Account \n Balance"
            holder.buttonAdd.text ="Add New Account"
            holder.binding.textViewTotalAmount.text = mAccountBalance.toString()
            holder.binding.buttonDetail.visibility = View.INVISIBLE
        }else {
            (holder as AccountListViewHolder).accountName.text = adapterList?.get(position-1)?.accountName
            holder.accountBalance.text = adapterList?.get(position-1)?.accountBalance.toString()
            holder.accountBalance.setTextColor(Color.RED)
            holder.date.text = adapterList?.get(position-1)?.let { getDateTime(it.date) }
            (holder as AccountListViewHolder).itemView.setOnClickListener() {view ->
                var id  = adapterList?.get(position-1)!!.accountId
                    if (adapterList?.get(position-1)!!.accountName != "Personal"){
                        onclickListener.openBottomSheet(id)
                    }
                 true
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
    fun updateList(list: List<AccountTable>,accountBalance:Long){
        adapterList.clear()
        adapterList.addAll(list)
        if(adapterList[0].accountName == Utils.ALL) {
            adapterList.removeAt(0)
        }
        mAccountBalance = accountBalance
        notifyDataSetChanged()
    }
    private fun getDateTime(timeStamp: Long): String? {
        return try {
            val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            simpleDate.format(Date(timeStamp))
        } catch (e: Exception) {
            e.toString()
        }
    }
    interface OnClickListener{
        fun openBottomSheet(accountId:Int):Boolean
    }

}