package com.aditechnology.moneymanagement.utils

import android.app.Activity
import android.content.Context
import com.aditechnology.moneymanagement.MainApplication

class SharedPreference {


     fun setPin(activity: Activity,pin:String){
        val sharedPref = activity.getSharedPreferences("My",0)
        with (sharedPref.edit()) {
            putString(Utils.SET_PIN,pin)
            apply()
        }
    }
}