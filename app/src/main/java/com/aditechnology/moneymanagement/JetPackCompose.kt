package com.aditechnology.moneymanagement

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

class JetPackCompose : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            previewTextView()
        }
    }
    // Preview annotation will work only if there will no any argument passed

    data class TextViewData(val id: Int,val name: String)

    @Composable
    fun makeTextView(textViewData: TextViewData){
      Row {
          Image(painter = painterResource(R.drawable.ic_dashboard_black_24dp), contentDescription ="ad" )
          
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