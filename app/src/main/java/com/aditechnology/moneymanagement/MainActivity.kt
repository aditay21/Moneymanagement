package com.aditechnology.moneymanagement

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aditechnology.moneymanagement.databinding.ActivityMain2Binding
import com.aditechnology.moneymanagement.databinding.BottomsheetShareBackupBinding
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.utils.DateTimeUtils
import com.aditechnology.moneymanagement.utils.StorageUtils
import com.aditechnology.moneymanagement.utils.Utils
import com.aditechnology.moneymanagement.utils.Utils.Companion.ACCOUNT_DETAIL_JSON
import com.aditechnology.moneymanagement.utils.Utils.Companion.ACCOUNT_NAME
import com.aditechnology.moneymanagement.utils.Utils.Companion.ALL
import com.aditechnology.moneymanagement.utils.Utils.Companion.CREATE_BACKUP_FILE_EXTENSION
import com.aditechnology.moneymanagement.utils.Utils.Companion.CREATE_BACKUP_FILE_MONEY_MANAGEMENT_FILE_NAME
import com.aditechnology.moneymanagement.utils.Utils.Companion.EXPORT_FILE_EXTENSION
import com.aditechnology.moneymanagement.utils.Utils.Companion.EXPORT_FILE_MONEY_MANAGEMENT_FILE_NAME
import com.aditechnology.moneymanagement.utils.Utils.Companion.Personal
import com.aditechnology.moneymanagement.utils.Utils.Companion.TRANSACTION_JSON_OBJECT
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseViewModelFactory
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONArray
import org.json.JSONObject
import java.io.File


class MainActivity : AppCompatActivity() {
    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
        private const val READ_STORAGE_PERMISSION_CODE = 102

    }
    var mPersonalAccountId = 0
    private  var FROM_CREATE_BACKUP = false

    private val mAccountList: ArrayList<AccountTable> = ArrayList()
    private lateinit var binding: ActivityMain2Binding
    private val accountViewModel: AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((application as MainApplication).repository)
    }

    var resultLauncher: ActivityResultLauncher<Intent>? = null
    private val expenseIncomeViewModel: ExpenseIncomeViewModel by viewModels {
        ExpenseViewModelFactory((application as MainApplication).repository)
    }
    private var  transactionDetailList: java.util.ArrayList<DetailsFileTable> = java.util.ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { result ->
                // Initialize result data
                val data: Intent? = result.getData()
                // check condition
                if (data != null) {
                    // When data is not equal to empty
                    // Get PDf uri
                    val sUri = data.data
                    try {
                        resetDb()
                        val raw =   StorageUtils.getdata(sUri!!,this)
                         val backup = JSONObject(raw)
                        val accountsJsonArray =backup.getJSONArray(ACCOUNT_DETAIL_JSON)
                        for (i in 0 until accountsJsonArray.length()) {
                            val accountObject = accountsJsonArray.getJSONObject(i)
                            if (!accountObject.getString(ACCOUNT_NAME)
                                    .equals(ALL) && !accountObject.getString(ACCOUNT_NAME)
                                    .equals(Personal)
                            ) {
                                accountViewModel.insertAccountDetail(
                                    accountObject.getString(ACCOUNT_NAME),
                                    accountObject.getLong(Utils.ACCOUNT_BALANCE),
                                    accountObject.getLong(Utils.DATE),
                                    accountObject.getLong(Utils.ACCOUNT_EXPENSE),
                                    accountObject.getLong(Utils.ACCOUNT_INCOME)
                                )
                            }
                        }
                        val transactionJsonArray =backup.getJSONArray(TRANSACTION_JSON_OBJECT)
                        for (i in 0 until transactionJsonArray.length()) {
                            val accountObject = transactionJsonArray.getJSONObject(i)
                            var type = Type.INCOME
                            if (accountObject.get(Utils.TYPE) == 1) {
                                type = Type.EXPENSE
                            } else {
                                type = Type.INCOME
                            }
                           /* accountViewModel.getAccountDetailName(accountObject.getString(Utils.ACCOUNT_NAME))
                                .observe(this) { list ->*/
                                    var id = accountObject.getInt(Utils.ACCOUNT_ID)
                                    Log.e("TAG", "$mPersonalAccountId Personal Account id " +accountObject.getString(ACCOUNT_NAME))
                                    if (accountObject.getString(ACCOUNT_NAME) == Personal  ) {
                                        id = mPersonalAccountId
                                    }
                                    expenseIncomeViewModel.insert(
                                        accountObject.getInt(Utils.MONEY),
                                        type,
                                        id,
                                        accountObject.getString(Utils.PAY_TO),
                                        accountObject.getString(Utils.DATE),
                                        accountObject.getString(Utils.TIME),
                                        accountObject.getString(Utils.PAID_FOR),
                                        accountObject.getString(ACCOUNT_NAME)

                                    )
                                }

                     //   }

                       // triggerRebirth()
                   }catch (e:Exception){
                       e.printStackTrace()
                        Toast.makeText(this,"Invalid backup file",Toast.LENGTH_SHORT).show()
                   }


                }
            })

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        accountViewModel.mAllDetails.observe(this) { account ->
            if (!account.isEmpty()) {


                if (account.size>1 && account[1].accountName== Personal) {
                    Log.e("TAG","153"+account[1].accountName)
                    Log.e("TAG","153"+account[1].accountId)
                    mPersonalAccountId = account[1].accountId
                }
                mAccountList.clear()
                mAccountList.addAll(account)
            }
        }
        expenseIncomeViewModel.mAllDetails.observe(this){
                all->

            transactionDetailList.addAll(all.reversed())
        }
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main2)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

     fun resetDb() {
        accountViewModel.removeAllAccount()
        expenseIncomeViewModel.removeAllTransaction()
        accountViewModel.insertAccountDetail(ALL, 0)
        accountViewModel.insertAccountDetail(Personal, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun exportDatabaseToCSVFile() {
        checkPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            STORAGE_PERMISSION_CODE)
        FROM_CREATE_BACKUP= false
    }
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@MainActivity, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
        } else {
            savePublicly()
           // Toast.makeText(this@MainActivity, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePublicly() {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(folder, EXPORT_FILE_MONEY_MANAGEMENT_FILE_NAME+DateTimeUtils.getDateWithTime()+ EXPORT_FILE_EXTENSION)
        exportMoviesWithDirectorsToCSVFile(file)
    }

    private fun exportMoviesWithDirectorsToCSVFile(csvFile: File) {
        csvWriter().open(csvFile, append = false) {
            writeRow(listOf("[accountID]", "[${"account_name"}]", "[${"Account Balance"}]","[${"Account Income"}]","[${"Account Expense"}]"))
            mAccountList.forEachIndexed { index, item ->
                if (item.accountName != "All"){
                    writeRow(
                        listOf(
                            item.accountId, item.accountName, item.accountBalance,
                            item.accountIncome, item.accountExpense
                        )
                    )
            }
        }
            writeRow(listOf("[transaction id]", "[${"money"}]", "[${"account name"}]","[${"ExpenseOrIncome"}]"
                ,"[${"Date"}]","[${"time"}]","[${"Paid For"}]","[${"Pay to"}]"))

            transactionDetailList.forEachIndexed { index, item ->
               var type =""
                if (item.type==1){
                    type ="Expense"
                }else{
                    type ="Income"
                }
                var accountName = ""
                mAccountList.forEachIndexed { index1, item1 ->
                    if (item1.accountName != "All" && item.account_id == item1.accountId) {
                        accountName  =  item1.accountName

                    }
                }
               val date = DateTimeUtils.getDateFromTimeStamp(item.date)
               val time = DateTimeUtils.getTimeFromTimeStamp(item.date)

                var paidFor =""
                var paidTo =""
                if (item.type==1) {
                    if (item.paid_for.isEmpty()) {
                        paidFor = "Paid For :---"
                    } else {
                        paidFor = "Paid For " + item.paid_for
                    }

                    if (item.pay_to.isEmpty()) {
                        paidTo = "Paid To :---"
                    } else {
                        paidTo = "Paid to " + item.pay_to
                    }
                }else{
                    if (item.pay_to.isEmpty()){
                       paidTo = "Get For :----"
                    }else{
                       paidTo = "Get For "+item.pay_to
                    }

                    if (item.paid_for.isEmpty()){
                       paidFor = "Get From :---"
                    }else{
                       paidTo = "Get From  "+item.paid_for
                    }
                }

                writeRow(
                    listOf(
                        item.id, item.money, accountName,
                        type, date,time,paidFor,paidTo
                    )
                )
            }
        }
        openBackInfoShare(csvFile.absolutePath)
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
                if (FROM_CREATE_BACKUP){
                    createBackup()
                }else {
                    savePublicly()
                }
            } else {
                Toast.makeText(this@MainActivity, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }else if (requestCode == READ_STORAGE_PERMISSION_CODE){
              launchPicker()
        }
    }
     private fun openBackInfoShare(path:String) {
         val dialog = BottomSheetDialog(this, R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(this)
        val binding = BottomsheetShareBackupBinding.inflate(inflater, null, false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
         if (FROM_CREATE_BACKUP) {
             binding.textviewShareInfo.text = "Backup is saved successfully on following path  $path"
         }else{
             binding.textviewShareInfo.text = "CSV file is saved successfully on following path  $path"
         }
         dialog.show()
         binding.buttonOk.setOnClickListener {
             dialog.dismiss()
         }
         binding.buttonShareInfo.setOnClickListener {
             var shareIntent = Intent(Intent.ACTION_SEND)
             shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
               if (FROM_CREATE_BACKUP) {
                   shareIntent.type = "application/json"
               }else{
                   shareIntent.type = "application/csv"
               }
             shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(File(path).toString()))
             startActivity(Intent.createChooser(shareIntent, "Share to"))
             dialog.dismiss()
         }

     }
     fun createBackup() {
         FROM_CREATE_BACKUP = true
         if (ContextCompat.checkSelfPermission(this@MainActivity,  Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
             // Requesting the permission
             ActivityCompat.requestPermissions(this@MainActivity, arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
         } else {
             createJsonbackup()
         }

    }

    private fun createJsonbackup() {
        var jsonObject = JSONObject()
        var accountDetailJsonArray = JSONArray()

        mAccountList.forEachIndexed { index, item ->
            var accountDetailJsonObject = JSONObject()
            if (item.accountName != "All") {
                accountDetailJsonObject.put(Utils.ACCOUNT_ID, item.accountId)
                accountDetailJsonObject.put(ACCOUNT_NAME, item.accountName)
                accountDetailJsonObject.put(Utils.ACCOUNT_BALANCE, item.accountBalance)
                accountDetailJsonObject.put(Utils.ACCOUNT_EXPENSE, item.accountExpense)
                accountDetailJsonObject.put(Utils.ACCOUNT_INCOME, item.accountIncome)
                accountDetailJsonObject.put(Utils.DATE, item.date)
                accountDetailJsonArray.put(accountDetailJsonObject)
            }
        }
        jsonObject.put(Utils.ACCOUNT_DETAIL_JSON ,accountDetailJsonArray)

        var transactionDetailJsonArray = JSONArray()

        transactionDetailList.forEachIndexed { _, item ->
            var transactionDetailJsonObject = JSONObject()
            transactionDetailJsonObject.put(Utils.ACCOUNT_ID, item.account_id)
            transactionDetailJsonObject.put(Utils.MONEY, item.money)
            transactionDetailJsonObject.put(Utils.TRANSACTION_ID, item.id)
            transactionDetailJsonObject.put(Utils.DATE, item.date)
            transactionDetailJsonObject.put(Utils.TIME, item.time)
            transactionDetailJsonObject.put(Utils.PAID_FOR, item.paid_for)
            transactionDetailJsonObject.put(Utils.PAY_TO, item.pay_to)
            transactionDetailJsonObject.put(Utils.TYPE, item.type)
            transactionDetailJsonObject.put(ACCOUNT_NAME, item.account_name)
            transactionDetailJsonArray.put(transactionDetailJsonObject)
        }
        jsonObject.put(Utils.TRANSACTION_JSON_OBJECT,transactionDetailJsonArray)
        createJsonFile(jsonObject)
    }

    private fun createJsonFile(jsonArray:JSONObject) {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(folder, CREATE_BACKUP_FILE_MONEY_MANAGEMENT_FILE_NAME+DateTimeUtils.getDateWithTime()+ CREATE_BACKUP_FILE_EXTENSION)

       StorageUtils.writeTextData(file, jsonArray)
       openBackInfoShare(file.absolutePath)
    }

   fun selectBackUpFile() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_CODE
            )
        } else {
            launchPicker()
        }
    }

    private fun launchPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/json"
        resultLauncher?.launch(intent)
    }

    fun triggerRebirth() {
        val packageManager = packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }

}


