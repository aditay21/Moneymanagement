package com.aditechnology.moneymanagement

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aditechnology.moneymanagement.databinding.ActivityMain2Binding
import com.aditechnology.moneymanagement.databinding.BottomSheetAccountActionBinding
import com.aditechnology.moneymanagement.databinding.BottomsheetShareBackupBinding
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.models.DetailsFileTable
import com.aditechnology.moneymanagement.utils.DateTimeUtils
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseIncomeViewModel
import com.aditechnology.moneymanagement.viewmodel.ExpenseViewModelFactory
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
    }
    private val mAccountList: ArrayList<AccountTable> = ArrayList()
    private lateinit var binding: ActivityMain2Binding
    private val accountViewModel: AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((application as MainApplication).repository)
    }


    private val expenseIncomeViewModel: ExpenseIncomeViewModel by viewModels {
        ExpenseViewModelFactory((application as MainApplication).repository)
    }
    private var  detailListAdapterList: java.util.ArrayList<DetailsFileTable> = java.util.ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        accountViewModel.mAllDetails.observe(this) { account ->
            mAccountList.clear()
            mAccountList.addAll(account)

        }
        expenseIncomeViewModel.mAllDetails.observe(this){
                all->
            detailListAdapterList.addAll(all.reversed())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun exportDatabaseToCSVFile() {
        checkPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            STORAGE_PERMISSION_CODE)
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
        val file = File(folder, DateTimeUtils.getDateWithTime()+"_moneymanagment.csv")
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
                ,"[${"Date"}]","[${"time"}]","[${"Paid For"}]","[${"Pay to For"}]"))

            detailListAdapterList.forEachIndexed { index, item ->
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
                 savePublicly()
            } else {
                Toast.makeText(this@MainActivity, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
     fun openBackInfoShare(path:String) {
         val dialog = BottomSheetDialog(this, R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(this)
        val binding = BottomsheetShareBackupBinding.inflate(inflater, null, false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
         binding.textviewShareInfo.text = "Backup is saved successfully on following path $path"
         dialog.show()
         binding.buttonOk.setOnClickListener {
             dialog.dismiss()
         }
         binding.buttonShareInfo.setOnClickListener {
             var shareIntent = Intent(Intent.ACTION_SEND)
             shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
             shareIntent.type = "application/csv"
             shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(File(path).toString()))
             startActivity(Intent.createChooser(shareIntent, "Share to"))
             dialog.dismiss()
         }

     }

}
