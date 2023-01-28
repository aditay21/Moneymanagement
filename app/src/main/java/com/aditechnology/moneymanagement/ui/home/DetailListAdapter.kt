package com.aditechnology.moneymanagement.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.RowTransactionBinding
import com.aditechnology.moneymanagement.databinding.ViewHolderAccountViewBinding
import com.aditechnology.moneymanagement.databinding.ViewHolderDetailSummeryBinding
import com.aditechnology.moneymanagement.databinding.ViewHolderDetailViewBinding
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.utils.DateTimeUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailListAdapter(val accountId: Int,val onClickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
     private val HEADER = 0
     private val NORMAL = 1
    private var  adapterList: ArrayList<DetailsFileTable> = ArrayList()
    private var  headerItem: ArrayList<AccountTable> = ArrayList()

    class RowListViewHolder(val binding: RowTransactionBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    class HeaderViewHolder(val binding: ViewHolderDetailSummeryBinding) : RecyclerView.ViewHolder(binding.root) {
      // val totalAmount :TextView= itemView.findViewById(R.id.text_view_total_amount)
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
        val  binding = RowTransactionBinding.inflate(LayoutInflater.from(parent.context))
        return RowListViewHolder(binding)

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == HEADER){
                if (headerItem.size>0) {
                    (holder as HeaderViewHolder).binding.textViewTotalAmount.text =
                        headerItem[0]?.accountBalance.toString()
                }
            (holder as HeaderViewHolder).binding.buttonAdd.setOnClickListener {
                if (headerItem.isEmpty()){
                    onClickListener.openBottomSheet(accountId,0)
                }
                headerItem[0]?.accountBalance?.let { it1 -> onClickListener.openBottomSheet(accountId, it1.toInt()) }
            }
        }else {
             val item =  adapterList[position-1]
             val date = DateTimeUtils.getDateFromTimeStamp(item.date)
             val time = DateTimeUtils.getTimeFromTimeStamp(item.date)

            (holder as RowListViewHolder).binding.textViewDate.text = "$date  ${time}"


            holder.binding.textViewPaidFrom.text = item.pay_to
            if (item.type ==1){//expense
                holder.binding.textViewTransactionAmount.text = "-"+item.money.toString()
                holder.binding.textViewTransactionAmount.setTextColor(
                    Color.RED
                )
              if (item.paid_for.isEmpty()){
                  holder.binding.textViewPaidForValue.text = "Paid For :-Record not added"
              }else{
                  holder.binding.textViewPaidForValue.text = "Paid For "+item.paid_for
              }

                if (item.pay_to.isEmpty()){
                    holder.binding.textViewPaidForValue.text = "Paid To :-Record not added"
                }else{
                    holder.binding.textViewPaidForValue.text = "Paid to "+item.pay_to
                }


            }else{
                holder.binding.textViewTransactionAmount.text = "+"+item.money.toString()
                holder.binding.textViewTransactionAmount.setTextColor(
                    Color.GREEN
                )
                if (item.paid_for.isEmpty()){
                    holder.binding.textViewPaidForValue.text = "Get For :-Record not added"
                }else{
                    holder.binding.textViewPaidForValue.text = "Get For "+item.paid_for
                }

                if (item.pay_to.isEmpty()){
                    holder.binding.textViewPaidForValue.text = "Get from :-Record not added"
                }else{
                    holder.binding.textViewPaidForValue.text = "Get Frm  "+item.pay_to
                }

            }
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
    fun updateHeader(item:List<AccountTable>){
        headerItem.clear()
        headerItem.addAll(item)
        notifyDataSetChanged()
    }

    interface OnClickListener{
        fun openBottomSheet(accountId:Int,accountBalance:Int)
    }
}