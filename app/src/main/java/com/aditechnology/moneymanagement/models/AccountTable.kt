package com.aditechnology.moneymanagement.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="account_detail")
class AccountTable(
    @ColumnInfo(name="account_name") val accountName :String,
    @ColumnInfo(name="account_balance") val accountBalance :Long,
    @ColumnInfo(name="date") val date :Long,
    @ColumnInfo(name="account_expense") val accountExpense :Long,
    @ColumnInfo(name="account_Income") val accountIncome :Long
    ) {

    @PrimaryKey(autoGenerate = true) var accountId: Int=0

}