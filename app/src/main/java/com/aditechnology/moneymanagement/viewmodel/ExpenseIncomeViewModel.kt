package com.aditechnology.moneymanagement.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.aditechnology.moneymanagement.Type
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.models.MoneyManagementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpenseIncomeViewModel(private val managementRepository: MoneyManagementRepository) : ViewModel(){

    val mAllDetails : LiveData<List<DetailsFileTable>> = managementRepository.allDetailsFileTable.asLiveData()

    val mAllDetailsById : LiveData<List<DetailsFileTable>> = managementRepository.allDetailsByAccountId(1).asLiveData()

    fun  insert(money: Int, type: Type,accountId : Int,payTo:String,date:String,time:String,paidFor:String) =

        viewModelScope.launch {

        var value :DetailsFileTable
        when (type) {
            Type.EXPENSE -> {
                 value=  DetailsFileTable( 1, money,accountId,payTo,date,time,paidFor)
            }
            else -> {
                 value=  DetailsFileTable( 0, money,accountId,payTo,date,time,paidFor)
            }
        }
          Log.e("TAG", "Added "+ managementRepository.insertItem(value))

        }
    fun  getByAccountId(id : Int):LiveData<List<DetailsFileTable>>? {
        return managementRepository.allDetailsByAccountId(id).asLiveData()

    }
}

class ExpenseViewModelFactory(private val repository: MoneyManagementRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseIncomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseIncomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}