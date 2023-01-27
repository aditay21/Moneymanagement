package com.aditechnology.moneymanagement.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aditechnology.moneymanagement.Type
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseIncomeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(value: DetailsFileTable)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(value: AccountTable)

    @Query("SELECT * FROM expense_income_details")
     fun getAllDetails(): Flow<List<DetailsFileTable>>


    @Query("SELECT * FROM expense_income_details WHERE account_id LIKE :account_id")
    fun getAllDetailsByAccountId(account_id :String): Flow<List<DetailsFileTable>>

    @Query("SELECT * FROM account_detail")
    fun getAllAccountDetail(): Flow<List<AccountTable>>

    @Query("SELECT * FROM account_detail WHERE accountId LIKE :account_id")
    fun getAccountDetailsByAccountId(account_id :String): Flow<List<AccountTable>>

   /* @Query("UPDATE account_detail SET
            account_balance = :value1,
        field2 = :value2,
        ...
    //some more fields to update
    ...
    field_N= :value_N
    WHERE id = :id)*/
    @Query("UPDATE account_detail SET account_balance =:amount")
    fun updateAmountOfAccountById(amount: String)


}