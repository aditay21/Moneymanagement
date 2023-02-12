package com.aditechnology.moneymanagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.aditechnology.moneymanagement.databinding.ContentLockScreenBinding
import com.aditechnology.moneymanagement.utils.Utils
import com.google.android.gms.ads.AdRequest

class LockScreenActivity : AppCompatActivity() {


    private lateinit var binding: ContentLockScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ContentLockScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        setSupportActionBar(binding.toolbar)

        binding.buttonEnter.setOnClickListener {
            if (TextUtils.isEmpty(binding.edittextSetPin.text.toString())) {
                binding.edittextSetPin.error = "Please enter  pin"
            } else {
                val sharedPref = getSharedPreferences("My",0)
                if (sharedPref?.getString(Utils.SET_PIN, "")
                        .equals(binding.edittextSetPin.text.toString())
                ) {
                    Utils.IS_PIN_CHECK = true
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    binding.edittextSetPin.error = "Wrong Pin,Try Again"
                }
            }
        }
    }

}