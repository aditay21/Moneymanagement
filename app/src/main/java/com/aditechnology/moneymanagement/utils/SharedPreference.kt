package com.aditechnology.moneymanagement.utils

import android.app.Activity
import android.content.Context

class SharedPreference {


     fun setPin(activity: Activity,pin:String){
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(Utils.SET_PIN,pin)
            apply()
        }
    }
}