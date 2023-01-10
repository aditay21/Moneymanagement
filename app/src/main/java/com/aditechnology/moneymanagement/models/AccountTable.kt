package com.aditechnology.moneymanagement.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="account_detail")
class AccountTable(
    @PrimaryKey(autoGenerate = true) val accountId: Int,
    @ColumnInfo(name="account_name") val accountName :String
  ) {
}