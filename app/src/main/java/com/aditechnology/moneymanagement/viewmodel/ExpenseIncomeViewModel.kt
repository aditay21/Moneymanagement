package com.aditechnology.moneymanagement.viewmodel

import androidx.lifecycle.*
import com.aditechnology.moneymanagement.Type
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.models.MoneyManagementRepository
import kotlinx.coroutines.launch

class ExpenseIncomeViewModel(private val managementRepository: MoneyManagementRepository) : ViewModel(){

    val mAllDetails : LiveData<List<DetailsFileTable>> = managementRepository.allDetailsFileTable.asLiveData()

    fun insert(money: Int, type: Type) = viewModelScope.launch {
        var value :DetailsFileTable
        when (type) {
            Type.EXPENSE -> {
                 value=  DetailsFileTable(0, 1, money,1)
            }
            else -> {
                 value=  DetailsFileTable(0, 0, money,1)
            }
        }
        managementRepository.insertItem(value);
    }



}

class WordViewModelFactory(private val repository: MoneyManagementRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseIncomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseIncomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}