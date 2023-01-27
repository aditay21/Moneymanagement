package com.aditechnology.moneymanagement.models

import kotlinx.coroutines.flow.Flow

class MoneyManagementRepository(private val expenseIncomeDao: ExpenseIncomeDao) {

    val allDetailsFileTable: Flow<List<DetailsFileTable>> = expenseIncomeDao.getAllDetails()



    val allAccountTable: Flow<List<AccountTable>> = expenseIncomeDao.getAllAccountDetail()

    public suspend fun insertItem(value: DetailsFileTable){
        expenseIncomeDao.insertData(value)
    }

    public suspend fun insertItem(value: AccountTable){
        expenseIncomeDao.insertData(value)
    }

    fun allDetailsByAccountId(id: Int) : Flow<List<DetailsFileTable>>{
        return  expenseIncomeDao.getAllDetailsByAccountId(id.toString())
    }


}