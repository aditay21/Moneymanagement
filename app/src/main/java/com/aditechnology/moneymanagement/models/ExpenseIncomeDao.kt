package com.aditechnology.moneymanagement.models

import androidx.room.*
import com.aditechnology.moneymanagement.Type
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseIncomeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(value: DetailsFileTable) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(value: AccountTable)

    @Query("SELECT * FROM expense_income_details")
     fun getAllDetails(): Flow<List<DetailsFileTable>>

     @Query("SELECT * FROM expense_income_details WHERE account_id LIKE :account_id")
    fun getAllDetailsByAccountId(account_id :String): Flow<List<DetailsFileTable>>

    @Query("SELECT * FROM expense_income_details WHERE account_id LIKE :account_id AND type LIKE :type")
    fun getAllExpenseOrIncomeDetailsByAccountId(account_id :String,type:Int): Flow<List<DetailsFileTable>>

    @Query("SELECT * FROM expense_income_details WHERE account_id LIKE :account_id AND date LIKE :timeStamp")
    fun getAllExpenseOrIncomeDetailsByAccountIdAndDate(account_id :String,timeStamp:String): Flow<List<DetailsFileTable>>


    @Query("SELECT * FROM account_detail")
    fun getAllAccountDetail(): Flow<List<AccountTable>>

    @Query("SELECT * FROM account_detail WHERE accountId LIKE :account_id")
    fun getAccountDetailsByAccountId(account_id :String): Flow<List<AccountTable>>

    @Query("SELECT * FROM account_detail WHERE account_name LIKE :account_name")
    fun getAccountDetailsByAccountName(account_name :String): Flow<List<AccountTable>>


    @Query("UPDATE account_detail SET account_balance =:amount,account_expense=:expense,account_income =:income WHERE accountId LIKE :account_id")
    suspend fun updateAmountOfAccountById(amount: String,account_id: String,expense: String,income: String)

    @Query("UPDATE account_detail SET account_name =:accountName WHERE accountId LIKE :accountId")
    suspend fun updateAccountNameById(accountName:String,accountId:String)


    @Query("DELETE FROM account_detail WHERE accountId LIKE :accountId")
    suspend fun removeAccountById(accountId: String)

    @Query("DELETE FROM expense_income_details WHERE account_id LIKE :accountId")
    suspend fun removeAccountDetailsById(accountId: String)

    @Query("UPDATE expense_income_details set money =:money,type =:type,pay_to =:payTo,date=:date,time=:time,paid_for=:paidFor WHERE id LIKE :id")
    suspend  fun updateTransactionById(
        money: Int,
        type: Type,
        payTo: String,
        date: String,
        time: String,
        paidFor: String,
        id: Int
    )
    @Query("DELETE FROM expense_income_details WHERE id LIKE :transactionId")
    suspend fun removeTransactionById(transactionId: String)

    @Query("SELECT * FROM expense_income_details WHERE account_id LIKE :account_id AND date between :startTimeStamp AND :endTimeStamp ")
    fun getAllExpenseOrIncomeDetailsByAccountIdAndTwoDates(
        account_id: String,
        startTimeStamp: String,
        endTimeStamp: String
    ): Flow<List<DetailsFileTable>>


    @Query("DELETE FROM account_detail")
     suspend  fun removeAllAccounts()

    @Query("DELETE FROM expense_income_details")
    suspend  fun removeAllTransactionDetail()
}