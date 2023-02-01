package com.aditechnology.moneymanagement.models

import com.aditechnology.moneymanagement.Type
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

    suspend  fun updateAmountOfAccountById(amount: String, accountId:String) {
      expenseIncomeDao.updateAmountOfAccountById(amount,accountId)
    }

    suspend fun updateAccountName(accountName: String, accountId: Int) {
        expenseIncomeDao.updateAccountNameById(accountName,accountId.toString())

    }

   suspend fun removeAccountById(accountId: Int) {
        expenseIncomeDao.removeAccountById(accountId.toString())
        expenseIncomeDao.removeAccountDetailsById(accountId.toString())
    }
    suspend fun updateTransactionById(money: Int, type: Type, accountId : Int, payTo:String, date:String,
                 time:String, paidFor:String, id: Int){
        expenseIncomeDao.updateTransactionById(money,type,payTo,date,time,paidFor,id)
    }

   suspend fun removeTransactionById(id: Int) {
       expenseIncomeDao.removeTransactionById(id.toString())
    }

}