package com.aditechnology.moneymanagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aditechnology.moneymanagement.databinding.ActivityMain2Binding
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File

class MainActivity : AppCompatActivity() {
    private val mAccountList:ArrayList<AccountTable> = ArrayList()
    private lateinit var binding: ActivityMain2Binding
    private val accountViewModel : AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((application as MainApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        accountViewModel.mAllDetails.observe(this) { account ->
            mAccountList.clear()
            mAccountList.addAll(account)

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
    public fun exportDatabaseToCSVFile() {
        val csvFile = generateFile(this, "MoneyManagement.csv")
        if (csvFile != null) {
            exportMoviesWithDirectorsToCSVFile(csvFile)
            Toast.makeText(this, getString(R.string.csv_file_generated_text), Toast.LENGTH_LONG).show()
            val intent = goToFileIntent(this, csvFile)
            try {
                startActivity(intent)
            }catch (e:Exception){
                Log.e("TAG",e.printStackTrace().toString())
            }

        } else {
            Toast.makeText(this, getString(R.string.csv_file_not_generated_text), Toast.LENGTH_LONG).show()
        }
    }
    private fun exportMoviesWithDirectorsToCSVFile(csvFile: File) {
        csvWriter().open(csvFile, append = false) {

            writeRow(listOf("[id]", "[${"account_detail"}]", "[${"expense_income_details"}]"))
            mAccountList.forEachIndexed { index, item ->
                writeRow(listOf(index, item.accountId, item.accountName,item.accountBalance,
                    item.accountIncome,item.accountExpense))
            }
        }
    }

    private fun generateFile(context: Context, fileName: String): File? {
        val csvFile = File(context.filesDir, fileName)
        csvFile.createNewFile()

        return if (csvFile.exists()) {
            csvFile
        } else {
            null
        }
    }

    private fun goToFileIntent(context: Context, file: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        val contentUri = FileProvider.getUriForFile(this, "${context.packageName}.utils.GenricFileProvider", file)
        val mimeType = contentResolver.getType(contentUri)
        intent.setDataAndType(contentUri, mimeType)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        return intent
    }

}