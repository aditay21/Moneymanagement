package com.aditechnology.moneymanagement.ui.notifications

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.aditechnology.moneymanagement.BuildConfig
import com.aditechnology.moneymanagement.MainActivity
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.WebViewActivity
import com.aditechnology.moneymanagement.databinding.BottomsheetShareBackupBinding
import com.aditechnology.moneymanagement.databinding.FragmentSettingBinding
import com.aditechnology.moneymanagement.utils.Utils
import com.google.android.gms.ads.AdRequest
import com.google.android.material.bottomsheet.BottomSheetDialog


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.textExportCsv.setOnClickListener {
            (requireActivity()as MainActivity).exportDatabaseToCSVFile()
        }
        binding.textPrivacyPolicy.setOnClickListener {
            val intent = Intent(requireActivity(), WebViewActivity::class.java)
            startActivity(intent)
        }
        binding.textRateUs.setOnClickListener {
            rateUs()
        }
        binding.textSetPin.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(Utils.PIN_FOR_UNLOCK, false)
            findNavController().navigate(R.id.action_setting_to_pin,bundle)
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

            (requireActivity()as MainActivity).createBackup()
        }
        binding.textRestore.setOnClickListener {
            (requireActivity() as MainActivity).selectBackUpFile()
        }
        binding.textReset.setOnClickListener {
             resetAlertWarning()

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
}