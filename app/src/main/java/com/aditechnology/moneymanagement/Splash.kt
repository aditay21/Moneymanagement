package com.aditechnology.moneymanagement

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.aditechnology.moneymanagement.utils.Utils

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash) // this will bind your MainActivity.class file with activity_main.
        Handler().postDelayed({
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            if (!TextUtils.isEmpty(sharedPref?.getString(Utils.SET_PIN, ""))) {
                var intent=  Intent(this,LockScreenActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent);
                finish()
            }

        }, 2000)

    }



}