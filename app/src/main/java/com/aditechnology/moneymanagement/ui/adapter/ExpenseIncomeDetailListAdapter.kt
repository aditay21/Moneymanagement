package com.aditechnology.moneymanagement.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aditechnology.moneymanagement.databinding.RowTransactionBinding
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.utils.DateTimeUtils


class ExpenseIncomeDetailListAdapter(val onClickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var  adapterList: ArrayList<DetailsFileTable> = ArrayList()


    class RowListViewHolder(val binding: RowTransactionBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val  binding = RowTransactionBinding.inflate(LayoutInflater.from(parent.context))
        return RowListViewHolder(binding)

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item =  adapterList[position]
        val date = DateTimeUtils.getDateFromTimeStamp(item.date)
        val time = DateTimeUtils.getTimeFromTimeStamp(item.time)

        (holder as RowListViewHolder).binding.textViewDate.text = "$date  ${time}"
        (holder as RowListViewHolder).itemView.setOnClickListener{
            onClickListener.openActionOnTransactionBottomSheet(item)

        }


        holder.binding.textViewPaidFrom.text = item.pay_to
            if (item.type ==1){//expense
                holder.binding.textViewTransactionAmount.text = "-"+item.money.toString()
                holder.binding.textViewTransactionAmount.setTextColor(
                    Color.RED
                )
              if (item.paid_for.isEmpty()){
                  holder.binding.textViewPaidFrom.text = "Paid For :-"
              }else{
                  holder.binding.textViewPaidFrom.text = "Paid For "+item.paid_for
              }

                if (item.pay_to.isEmpty()){
                    holder.binding.textViewPaidForValue.text = "Paid To :-"
                }else{
                    holder.binding.textViewPaidForValue.text = "Paid to "+item.pay_to
                }
            }else{
                holder.binding.textViewTransactionAmount.text = "+"+item.money.toString()
                holder.binding.textViewTransactionAmount.setTextColor(
                    Color.GREEN
                )
                if (item.pay_to.isEmpty()){
                    holder.binding.textViewPaidFrom.text = "Get For :-"
                }else{
                    holder.binding.textViewPaidFrom.text = "Get For "+item.pay_to
                }

                if (item.paid_for.isEmpty()){
                    holder.binding.textViewPaidForValue.text = "Get From :-"
                }else{
                    holder.binding.textViewPaidForValue.text = "Get From  "+item.paid_for
                }

            }
    }

    override fun getItemCount(): Int {
       return adapterList!!.size
    }

    fun updateList(list: List<DetailsFileTable>){
        adapterList.clear()
        adapterList.addAll(list)
        notifyDataSetChanged()
    }


    interface OnClickListener{
        fun openActionOnTransactionBottomSheet(item:DetailsFileTable)
    }
}