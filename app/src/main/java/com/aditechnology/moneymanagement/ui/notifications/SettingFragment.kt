package com.aditechnology.moneymanagement.ui.notifications

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aditechnology.moneymanagement.BuildConfig
import com.aditechnology.moneymanagement.MainActivity
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.WebViewActivity
import com.aditechnology.moneymanagement.databinding.BottomsheetShareBackupBinding
import com.aditechnology.moneymanagement.databinding.FragmentSettingBinding
import com.aditechnology.moneymanagement.utils.Utils
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetDialog


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private var mInterstitialAd: InterstitialAd? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        loadAds()
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.textExportCsv.setOnClickListener {
            openDialog(0);

        }
        binding.textPrivacyPolicy.setOnClickListener {
            val intent = Intent(requireActivity(), WebViewActivity::class.java)
            startActivity(intent)
        }
        binding.textRateUs.setOnClickListener {
            rateUs()
        }
        binding.textSetPin.setOnClickListener {
            openDialog(1);


        }
        binding.textShareApp.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Money management")
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
          ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
          
          
          """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                e.toString()
            }
        }


        binding.textCreateBackup.setOnClickListener {
            openDialog(2);

        }
        binding.textRestore.setOnClickListener {
            (requireActivity() as MainActivity).selectBackUpFile()

        }
        binding.textReset.setOnClickListener {
            openDialog(4);


        }


        return root
    }
    private fun rateUs(){

        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + requireContext().getPackageName())
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + requireContext().packageName)
                )
            )
        }
    }

    private fun resetAlertWarning() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomsheetShareBackupBinding.inflate(inflater, null, false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        binding.textviewShareInfo.text = "All data will be reset!! We suggest you to make a backup first so that it can be recovered later "
        binding.buttonOk.text = "Cancel"
        binding.buttonShareInfo.text = "Reset"
        binding.buttonOk.setOnClickListener {
            dialog.dismiss()
        }

        binding.buttonShareInfo.setOnClickListener {
            (requireActivity() as MainActivity).resetDb(true)
          //  (requireActivity() as MainActivity).triggerRebirth()
            //dialog.dismiss()


        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun openDialog(index :Int) {
        val builder = AlertDialog.Builder(requireContext())


        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage("Please watch ads to continue the add").setTitle("Watch Ads")

        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to watch ?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog: DialogInterface, id: Int ->
                if (mInterstitialAd != null) {
                    mInterstitialAd!!.show(requireActivity())
                    mInterstitialAd!!.setFullScreenContentCallback(object :
                        FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            Log.d("TAG", "The ad was dismissed.")
                            takeActions(index)


                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Called when fullscreen content failed to show.
                            Log.d("TAG", "The ad failed to show.")

                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null

                            loadAds()
                            Log.d("TAG", "The ad was shown.")
                        }
                    })
                } else {

                    takeActions(index)
                    Toast.makeText(
                        requireContext(),
                        "Ads not prepared yet!!Enjoy our free service",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
                dialog.cancel()
            }
            .setNegativeButton("No") { dialog, id -> //  Action for 'NO' Button
                dialog.cancel()
            }
        //Creating dialog box
        val alert = builder.create()
        //Setting the title manually
        alert.setTitle("Watch Ads to use this feature")
        alert.show()
    }

    private fun takeActions(index: Int) {
        if (index == 0) {
            (requireActivity() as MainActivity).exportDatabaseToCSVFile()
        }else if (index==1){
            val bundle = Bundle()
            bundle.putBoolean(Utils.PIN_FOR_UNLOCK, false)
            findNavController().navigate(R.id.action_setting_to_pin,bundle)
        }else if(index==2){
            (requireActivity()as MainActivity).createBackup()
        }else if(index==3){
            (requireActivity() as MainActivity).selectBackUpFile()
        }else if(index==4){
            resetAlertWarning()
        }

    }

    private fun loadAds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),"ca-app-pub-3940256099942544/1033173712",adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    Log.i("TAG", "onAdLoaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.i("TAG", loadAdError.message)
                    mInterstitialAd = null
                }
            })
    }
}