package com.aditechnology.moneymanagement

import android.app.Application
import com.aditechnology.moneymanagement.models.MoneyManagementRepository
import com.aditechnology.moneymanagement.models.MoneymakingDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.security.AccessController.getContext

class MainApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { MoneymakingDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { MoneyManagementRepository(database.expenseDao()) }




}