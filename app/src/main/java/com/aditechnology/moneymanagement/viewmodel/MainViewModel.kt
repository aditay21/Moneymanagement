package com.aditechnology.moneymanagement.viewmodel

import androidx.lifecycle.*
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.models.MoneyManagementRepository
import kotlinx.coroutines.launch

class MainViewModel(private val managementRepository: MoneyManagementRepository) : ViewModel(){

    val mAllDetails : LiveData<List<AccountTable>> = managementRepository.allAccountTable.asLiveData()


    fun insertAccountDetail(id: Int, account: String) = viewModelScope.launch {
        var value = AccountTable(id,account)
        managementRepository.insertItem(value);
    }

    class MainViewModelFactory(private val repository: MoneyManagementRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}