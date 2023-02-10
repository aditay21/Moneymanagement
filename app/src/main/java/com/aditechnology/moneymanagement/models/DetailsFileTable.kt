package com.aditechnology.moneymanagement.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "expense_income_details",indices  = [Index(value = ["time"], unique = true)])
data class DetailsFileTable(
    @ColumnInfo(name = "type")     val type :Int,
    @ColumnInfo(name="money")      val money: Int,
    @ColumnInfo(name="account_id") val account_id: Int,
    @ColumnInfo(name="pay_to")     val pay_to: String,
    @ColumnInfo(name="date")       val date: String,
    @ColumnInfo(name="time")       val time: String,
    @ColumnInfo(name="paid_for")   val paid_for: String,
    @ColumnInfo(name="account_name")   val account_name: String){

    @PrimaryKey(autoGenerate = true) var id : Int=0
}
