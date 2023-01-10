package com.aditechnology.moneymanagement.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(DetailsFileTable::class,AccountTable::class), version = 1, exportSchema = false)
abstract class MoneymakingDatabase : RoomDatabase() {

abstract fun expenseDao():ExpenseIncomeDao


    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {


                }
            }
        }
    }


companion object{
    private  var INSTANCE  :  MoneymakingDatabase?=null


    fun getDatabase(context: Context,scope: CoroutineScope): MoneymakingDatabase {
        // if the INSTANCE is not null, then return it,
        // if it is, then create the database
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MoneymakingDatabase::class.java,
                "moneymaking_database"
            ).addCallback(WordDatabaseCallback(scope))
                .build()
            INSTANCE = instance
            // return instance
            instance
        }
    }
}
}