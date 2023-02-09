package com.aditechnology.moneymanagement.viewmodel

import androidx.lifecycle.*
import com.aditechnology.moneymanagement.Type
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.models.MoneyManagementRepository
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
            managementRepository.insertItem(value)


        }

    fun  getExpenseIncomeByAccountId(id : Int,type :Int):LiveData<List<DetailsFileTable>>? {
        return managementRepository.allExpenseOrIncomeByAccountId(id,type).asLiveData()
    }

    fun  getAllDetailsByAccountId(id : Int):LiveData<List<DetailsFileTable>>? {
        return managementRepository.allDetailsByAccountId(id).asLiveData()

    }

    fun  getByAccountIdAndDate(id : Int,timeStamp:String):LiveData<List<DetailsFileTable>>? {
        return managementRepository.allDetailsByAccountIdAndDate(id,timeStamp).asLiveData()

    }
    fun  getDetailsByAccountIdAndBYRANGE(id : Int,startDate:String,endDate:String):LiveData<List<DetailsFileTable>>? {
        return managementRepository.allDetailsByAccountIdAndByTwoDates(id,startDate,endDate).asLiveData()

    }

    fun removeTransaction(id: Int) {
       viewModelScope.launch {
             managementRepository.removeTransactionById(id)

       }
    }

    fun removeAllTransaction() {
        viewModelScope.launch {
            managementRepository.removeAllTransaction()

        }
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