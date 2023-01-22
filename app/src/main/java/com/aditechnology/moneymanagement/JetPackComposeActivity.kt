package com.aditechnology.moneymanagement

import android.os.Bundle
import android.os.PersistableBundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aditechnology.test.ui.theme.MoneyManagementTheme

class JetPackComposeActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            MoneyManagementTheme() {
                previewTextView()
            }

        }
    }
    // Preview annotation will work only if there will no any argument passed

    data class TextViewData(val id: Int,val name: String)

    @Composable
    fun makeTextView(textViewData: TextViewData){
      Row(modifier =Modifier.padding(all = 30.dp)) {
          Image(painter = painterResource(R.drawable.ic_dashboard_black_24dp)
              , contentDescription ="ad"
              , modifier = Modifier.size(40.dp)
          )
          Spacer(modifier = Modifier.width(22.dp))
          Column {
              Text(text = textViewData.id.toString())
              Text(text = textViewData.name)
           }
      }
    }

    @Preview
    @Composable
    fun previewTextView(){
        makeTextView(
            TextViewData(1,"Aditay")
        )
    }
}