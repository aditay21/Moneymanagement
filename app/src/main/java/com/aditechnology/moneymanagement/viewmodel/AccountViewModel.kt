package com.aditechnology.moneymanagement.viewmodel

import androidx.lifecycle.*
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.models.MoneyManagementRepository
import kotlinx.coroutines.launch

class AccountViewModel(private val managementRepository: MoneyManagementRepository) : ViewModel(){

    val mAllDetails : LiveData<List<AccountTable>> = managementRepository.allAccountTable.asLiveData()

    fun insertAccountDetail(account: String,balance :Long) = viewModelScope.launch {
        var value = AccountTable(account,balance,System.currentTimeMillis());
        managementRepository.insertItem(value)
    }

    fun updateAccountBalance(amount: String,accountId: Int) = viewModelScope.launch {
        managementRepository.updateAmountOfAccountById(amount,accountId.toString())
    }

    fun getAccountDetailBy(accountId :Int) :LiveData<List<AccountTable>> {
       return managementRepository.getAccountDetailsByAccountId(accountId).asLiveData()
    }

    fun updateAccountName(accountId: Int,accountName:String) {
        viewModelScope.launch {
            managementRepository.updateAccountName(accountName, accountId)
        }
    }

    fun removeAccountById(accountId: Int) {
        viewModelScope.launch {
            managementRepository.removeAccountById(accountId)
        }
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