package com.aditechnology.moneymanagement.models

import kotlinx.coroutines.flow.Flow

class MoneyManagementRepository(private val expenseIncomeDao: ExpenseIncomeDao) {

    val allDetailsFileTable: Flow<List<DetailsFileTable>> = expenseIncomeDao.getAllDetails()

    val allAccountTable: Flow<List<AccountTable>> = expenseIncomeDao.getAllAccountDetail()

    public suspend fun insertItem(value: DetailsFileTable) :Long{
      return expenseIncomeDao.insertData(value)
    }

    public suspend fun insertItem(value: AccountTable){
        expenseIncomeDao.insertData(value)
    }

    fun allDetailsByAccountId(id: Int) : Flow<List<DetailsFileTable>>{
       return expenseIncomeDao.getAllDetailsByAccountId(id.toString())

    }

    fun getAccountDetailsByAccountId(id: Int) : Flow<List<AccountTable>>{
        return expenseIncomeDao.getAccountDetailsByAccountId(id.toString())
    }

    suspend  fun updateItem(amount: String,accountId:String) {
      expenseIncomeDao.updateAmountOfAccountById(amount,accountId)
    }

    suspend fun updateAccountName(accountName: String, accountId: Int) {
        expenseIncomeDao.updateAccountNameById(accountName,accountId.toString())

    }

   suspend fun removeAccountById(accountId: Int) {
        expenseIncomeDao.removeAccountById(accountId.toString())
        expenseIncomeDao.removeAccountDetailsById(accountId.toString())
    }


}