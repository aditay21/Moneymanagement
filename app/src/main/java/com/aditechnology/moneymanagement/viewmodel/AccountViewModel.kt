package com.aditechnology.moneymanagement.viewmodel

import androidx.lifecycle.*
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.models.MoneyManagementRepository
import kotlinx.coroutines.launch

class AccountViewModel(private val managementRepository: MoneyManagementRepository) : ViewModel(){

    public val mAllDetails : LiveData<List<AccountTable>> = managementRepository.allAccountTable.asLiveData()


    fun insertAccountDetail(account: String,balance :Long) = viewModelScope.launch {
        var value = AccountTable(account,balance,System.currentTimeMillis());
        managementRepository.insertItem(value)
    }

    class AccountViewModelFactory(private val repository: MoneyManagementRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AccountViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}