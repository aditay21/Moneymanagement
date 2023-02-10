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
    fun allDetailsByAccountIdAndDate(id: Int,timeStamp:String) : Flow<List<DetailsFileTable>>{
        return expenseIncomeDao.getAllExpenseOrIncomeDetailsByAccountIdAndDate(id.toString(),timeStamp)

    }

     fun allDetailsByAccountIdAndByTwoDates(id: Int,startTimeStamp:String,endTimeStamp:String) : Flow<List<DetailsFileTable>>{
        return expenseIncomeDao.getAllExpenseOrIncomeDetailsByAccountIdAndTwoDates(id.toString(),startTimeStamp,endTimeStamp)

    }

    fun allExpenseOrIncomeByAccountId(id: Int,type:Int) : Flow<List<DetailsFileTable>>{
        return expenseIncomeDao.getAllExpenseOrIncomeDetailsByAccountId(id.toString(),type)

    }

    fun getAccountDetailsByAccountId(id: Int) : Flow<List<AccountTable>>{
        return expenseIncomeDao.getAccountDetailsByAccountId(id.toString())
    }
    fun getAccountDetailsByAccountName(accountName: String) : Flow<List<AccountTable>>{
        return expenseIncomeDao.getAccountDetailsByAccountName(accountName)
    }

    suspend  fun updateAmountOfAccountById(amount: String, accountId:String,expense: String,income: String) {
      expenseIncomeDao.updateAmountOfAccountById(amount,accountId,expense,income)
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
    suspend fun removeAllAccounts() {
        expenseIncomeDao.removeAllAccounts()

    }
    suspend fun removeAllTransaction(){
        expenseIncomeDao.removeAllTransactionDetail()
    }

}