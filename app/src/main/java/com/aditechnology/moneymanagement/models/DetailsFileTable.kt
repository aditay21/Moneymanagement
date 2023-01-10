package com.aditechnology.moneymanagement.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_income_details")
data class DetailsFileTable(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "type") val type :Int,
    @ColumnInfo(name="money") val money: Int,
    @ColumnInfo(name="account_id") val account_id: Int
    )
